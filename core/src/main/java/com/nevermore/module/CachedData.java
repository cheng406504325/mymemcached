package com.nevermore.module;

import com.nevermore.core.storage.impl.StorageCoreImpl;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/26
 */
public class CachedData {
    private StorageCoreImpl.LRUReleaseNode node;


    public StorageCoreImpl.LRUReleaseNode getNode() {
        return node;
    }

    public void setNode(StorageCoreImpl.LRUReleaseNode node) {
        this.node = node;
    }
}
