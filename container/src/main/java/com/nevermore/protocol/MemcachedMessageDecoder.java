package com.nevermore.protocol;

import com.nevermore.context.impl.AppContext;
import com.nevermore.core.storage.StorageCore;
import com.nevermore.exceptions.NotFoundException;
import com.nevermore.module.CachedData;
import exceptions.ClientException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import static com.nevermore.protocol.Operation.*;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/27
 */
public class MemcachedMessageDecoder extends ChannelHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MemcachedMessageDecoder.class);
    private boolean hasData = false;
    private Charset charset = Charset.defaultCharset();
    private byte[] body;

    /** 缓存 */
    private StorageCore core;

    /** 需要缓存的key */
    private String key;

    /** 需要缓存的Body的长度 */
    private int bodyLength;

    /** flag */
    private int flag;

    /** 正在执行的操作 */
    private Operation operation;

    public MemcachedMessageDecoder() {
       core = (StorageCore) AppContext.getAppContext().get("core").get();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (!hasData) {
            handlLine(ctx, buf.toString(charset));
        } else {
            if (buf.readableBytes() < bodyLength) {
                return;
            }
            byte[] body = new byte[bodyLength];
            buf.readBytes(body, 0, bodyLength);
            this.body = body;
            doSet();
            ctx.write(Unpooled.copiedBuffer("STORED\r\n".getBytes()));
            logger.info(body.length + "");
        }
    }


    private void handlLine(ChannelHandlerContext ctx, String line) throws FileNotFoundException {
        logger.info(line);
        if (line.startsWith("set")) {
            String[] words = line.split(" ");
            this.bodyLength = Integer.parseInt(words[4]);
            this.key = words[1];
            this.flag = Integer.parseInt(words[3]);
            this.hasData = true;
        } else if (line.startsWith("get")) {
            doGet(ctx, line);
        } else if (line.startsWith("delete")) {
            doDelete(line);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ClientException) {
            ctx.write(Unpooled.copiedBuffer("CLIENT_ERROR 命令不能被识别\r\n".getBytes()));
        } else if (operation == DELETE && cause instanceof NotFoundException) {
            ctx.write(Unpooled.copiedBuffer(("NOT_FOUND" + "\r\n").getBytes()));
        } else {
            ctx.write(Unpooled.copiedBuffer("SERVER_ERROR\r\n".getBytes()));
        }
        logger.error("error", cause);
        ctx.flush();
    }


    private void doSet() throws FileNotFoundException {
        operation = SET;
        core.set(key, new CachedData(body, flag, bodyLength));
        hasData = false;
        body = null;
    }

    private void doGet(ChannelHandlerContext ctx, String line) {
        operation = GET;
        String[] words;
        List<String> keys;
        try {
            words = line.split(" ");
            keys = new LinkedList<>();
            for (String word:words) {
                if (word.equals("get")) continue;
                keys.add(word);
            }
        } catch (Exception e) {
            throw new ClientException(e);
        }
        keys.forEach((key) -> wrapValues(key, core.get(key), ctx));
        ctx.write(Unpooled.copiedBuffer("END\r\n".getBytes()));
    }

    private void doDelete(String line) {
        operation = DELETE;
        core.delete(line.split(" ")[1]);
    }

    private void wrapValues(String key,
                            CachedData data, ChannelHandlerContext ctx) {
        if (data.isDeleted()) {
            return;
        }
        ctx.write(Unpooled.copiedBuffer(
                ("VALUE " + key + " " + data.getFlag() + " " + data.getBodyLength() + "\r\n").getBytes()));
        ctx.write(Unpooled.copiedBuffer(data.getBytes()));
        ctx.write(Unpooled.copiedBuffer("\r\n".getBytes()));
    }
}
