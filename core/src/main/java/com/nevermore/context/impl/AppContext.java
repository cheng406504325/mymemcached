package com.nevermore.context.impl;

import com.google.common.base.Optional;
import com.nevermore.context.Context;

/**
 * 应用程序上下文
 *
 * @author suncheng
 * @since 15/11/25
 */
public class AppContext implements Context {

    @Override
    public Optional<Object> get(String key) {
        return null;
    }

    @Override
    public void set(String key, Optional<Object> value) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void close() throws Exception {

    }
}
