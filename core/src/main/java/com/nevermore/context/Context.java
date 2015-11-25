package com.nevermore.context;

import com.google.common.base.Optional;

/**
 * 上下文
 *
 * @author suncheng
 * @since 15/11/25
 */
public interface Context extends AutoCloseable {

    /**
     * 获取上下文中的一个值
     *
     * @param key 在上下文保存的key
     * @return 包含value的一个Optional
     */
    Optional<Object> get(String key);

    /**
     * 在上下文中添加一个键值对
     *
     * @param key 键
     * @param value 值
     */
    void set(String key, Optional<Object> value);

    /**
     * 初始化上下文
     */
    void initialize();
}
