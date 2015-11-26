package com.nevermore.core.storage.impl;

import com.nevermore.context.Context;
import com.nevermore.core.storage.StorageCore;
import com.nevermore.exceptions.StorageException;
import com.nevermore.lifecycle.Configurable;
import com.nevermore.lifecycle.LifecycleState;
import com.nevermore.module.CachedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.nevermore.lifecycle.LifecycleState.ERROR;
import static com.nevermore.lifecycle.LifecycleState.START;
import static com.nevermore.lifecycle.LifecycleState.STOP;

/**
 * TODO:性能优化,内存回收的速度与写入速度相差太远
 *
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/25
 */
public class StorageCoreImpl implements StorageCore, Configurable {
    private static final Logger logger = LoggerFactory.getLogger(StorageCore.class);
    private ExecutorService pool = Executors.newFixedThreadPool(2);

    private volatile LifecycleState lifecycleState = STOP;

    private LinkedBlockingQueue<LRUReleaseNode> commandQueue;

    private ConcurrentHashMap<String, CachedData> nodes;

    private AtomicInteger currentSize;
    private int batchReleaseSize = 3;
    private int maxSize = 10000;

    private LRUReleaseNode head;
    private LRUReleaseNode last;

    public StorageCoreImpl() {
        nodes = new ConcurrentHashMap<>();
        head = new LRUReleaseNode("head");
        last = new LRUReleaseNode("last");
        head.next = last;
        head.prev = last;
        last.next = head;
        last.prev = head;
        currentSize = new AtomicInteger(0);
        commandQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void configure(Context context) {
        //TODO:配置缓存
    }

    @Override
    public CachedData get(String key) {
        CachedData data = nodes.get(key);
        moveToHead(data.getNode());
        return data;
    }

    @Override
    public void set(String key, CachedData cachedData) {
        checkArgument(key != null && key.trim().length() != 0,
                "key must not be blank");
        if (nodes.get(key) == null) {
            LRUReleaseNode node = new LRUReleaseNode(key);
            cachedData.setNode(node);
            nodes.put(key, cachedData);
            moveToHead(node);

            currentSize.getAndIncrement();
        } else {
            nodes.put(key, cachedData);
        }
    }

    @Override
    public void delete(String key) {
        checkArgument(key != null, "Argument key was %s but expected not null", key);
        checkArgument(nodes.get(key) == null, "there is no value for key %s", key);
        nodes.remove(key);
    }

    @Override
    public void release() {
        while (true) {
            if (currentSize.get() >= maxSize) {
                logger.debug("the size of cache is [{}]", currentSize);
                logger.debug("begin release cache space");
                nodes.remove(last.prev.key);
                last.prev.prev.next = last;
                last.prev = last.prev.prev;
                currentSize.getAndDecrement();
                logger.debug("end of release");
            }
        }
    }

    @Override
    public void start() {
        lifecycleState = START;

        //start sorter thread
        pool.execute(this::keepNodeSorted);

        //start releaser thread
        pool.execute(this::release);
    }

    @Override
    public void stop() {
        lifecycleState = STOP;
        pool.shutdown();
        nodes = null;
    }

    @Override
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    private void doMoveToHead(LRUReleaseNode node) {
        node.next = head.next;
        node.prev = head;
        head.next = node;
        head.next.prev = node;
    }

    private void moveToHead(LRUReleaseNode node) {
        try {
            commandQueue.put(node);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            this.lifecycleState = ERROR;
            throw new StorageException(e);
        }
    }


    private void keepNodeSorted() {
        try {
            LRUReleaseNode node = commandQueue.take();
            doMoveToHead(node);
        } catch (InterruptedException e) {
            logger.error("sort LRU list is interrupted", e);
            this.lifecycleState = ERROR;
        }
    }

    public class LRUReleaseNode {
        LRUReleaseNode prev;
        LRUReleaseNode next;
        String key;

        LRUReleaseNode(LRUReleaseNode prev,
                       LRUReleaseNode next, String key) {
            this.key = key;
            this.next = next;
            this.prev = prev;
        }

        LRUReleaseNode() {

        }

        LRUReleaseNode(String key) {
            this.key = key;
        }
    }
}
