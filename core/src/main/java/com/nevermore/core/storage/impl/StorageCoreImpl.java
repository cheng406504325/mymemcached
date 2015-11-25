package com.nevermore.core.storage.impl;

import com.google.common.cache.Cache;
import com.nevermore.context.Context;
import com.nevermore.core.storage.Releaser;
import com.nevermore.core.storage.StorageCore;
import com.nevermore.lifecycle.Configurable;
import com.nevermore.lifecycle.LifecycleState;
import com.nevermore.module.CachedData;

/**
 *
 *
 * @author suncheng
 * @since 15/11/25
 */
public class StorageCoreImpl implements StorageCore, Configurable {
    private Releaser releaser;

    private Cache<String, CachedData> cache;

    @Override
    public void configure(Context context) {

    }

    @Override
    public CachedData get(String key) {
        return null;
    }

    @Override
    public void set(String key, CachedData cachedData) {

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
}
