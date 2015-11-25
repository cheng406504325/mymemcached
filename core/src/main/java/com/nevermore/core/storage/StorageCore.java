package com.nevermore.core.storage;

import com.nevermore.lifecycle.LifecycleAware;
import com.nevermore.module.CachedData;

/**
 * @author suncheng
 * @since 15/11/25
 */
public interface StorageCore extends LifecycleAware {

    CachedData get(String key);

    void set(String key, CachedData cachedData);

    void delete(String key);

    void release();
}
