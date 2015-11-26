package com.nevermore.core.storage.impl;

import com.nevermore.context.Context;
import com.nevermore.core.storage.StorageCore;
import com.nevermore.lifecycle.Configurable;
import com.nevermore.lifecycle.LifecycleState;
import com.nevermore.module.CachedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 *TODO:利用双链表加ConcurrentHashMap实现LRU算法
 *
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/25
 */
public class StorageCoreImpl implements StorageCore, Configurable {
    private static final Logger logger = LoggerFactory.getLogger(StorageCore.class);

    private ConcurrentHashMap<String, CachedData> nodes;

    private LRUReleaseNode head;
    private LRUReleaseNode last;

    public StorageCoreImpl() {
        nodes = new ConcurrentHashMap<>();
        head = new LRUReleaseNode();
        last = new LRUReleaseNode();
        head.next = last;
        head.prev = last;
        last.next = head;
        last.prev = head;
    }

    @Override
    public void configure(Context context) {
        //TODO:
    }

    @Override
    public CachedData get(String key) {
        CachedData data = nodes.get(key);
        addToHead(data.getNode());
        return data;
    }

    @Override
    public void set(String key, CachedData cachedData) {
        if (nodes.get(key) == null) {
            LRUReleaseNode node = new LRUReleaseNode(key);
            cachedData.setNode(node);
            nodes.put(key, cachedData);
            addToHead(node);
        } else {
            nodes.put(key, cachedData);
        }
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void release() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public LifecycleState getLifecycleState() {
        return null;
    }

    private void addToHead(LRUReleaseNode node) {
        node.next = head.next;
        node.prev = head;
        head.next = node;
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
