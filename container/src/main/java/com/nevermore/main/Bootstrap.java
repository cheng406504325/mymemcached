package com.nevermore.main;

import com.google.common.base.Optional;
import com.nevermore.context.impl.AppContext;
import com.nevermore.core.storage.impl.StorageCoreImpl;
import com.nevermore.lifecycle.LifecycleAware;
import com.nevermore.protocol.MemcachedMessageDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.nevermore.lifecycle.LifecycleState.ERROR;
import static com.nevermore.lifecycle.LifecycleState.STOP;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/25
 */
public class Bootstrap {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    /** 生命周期元素 */
    private List<LifecycleAware> components;


    public Bootstrap() throws FileNotFoundException {
        components = new ArrayList<>();
    }

    /**
     * 初始化缓存及上下文
     */
    private void init() {
        AppContext.getAppContext().set("core", Optional.of(new StorageCoreImpl()));
        components.add((LifecycleAware) AppContext.getAppContext().get("core").get());
    }

    /**
     * 启动辅助方法
     * @throws InterruptedException
     */
    private void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler((new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new MemcachedMessageDecoder());
                        }
                    }));

            ChannelFuture channelFuture = bootstrap.bind(12121).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 生命周期控制
     */
    private void lifecycleControl() {
        boolean flag = false;
        while (true) {
            if (!flag) {
                components.forEach(LifecycleAware::start);
                flag = true;
            }
            components.stream().filter(
                    (component) -> component.getLifecycleState() == STOP
                            || component.getLifecycleState() == ERROR)
                    .forEach(LifecycleAware::start);
        }
    }

    /**
     * 执行入口
     * @param args 无
     */
    public static void main(String args[]) {
        Bootstrap bootstrap;
        try {
            bootstrap = new Bootstrap();
            new Thread(bootstrap::lifecycleControl).start();
            bootstrap.init();
            bootstrap.start();
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
