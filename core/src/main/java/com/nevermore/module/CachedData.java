package com.nevermore.module;

import com.nevermore.core.storage.impl.StorageCoreImpl;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/26
 */
public class CachedData {
    private byte[] bytes;
    private StorageCoreImpl.LRUReleaseNode node;
    private int bodyLength;
    private boolean deleted;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    private int flag;

    public CachedData() {

    }
    public CachedData(byte[] bytes, int flag, int bodyLength) {
        this.bytes = bytes;
        this.flag = flag;
        this.bodyLength = bodyLength;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public StorageCoreImpl.LRUReleaseNode getNode() {
        return node;
    }

    public void setNode(StorageCoreImpl.LRUReleaseNode node) {
        this.node = node;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
