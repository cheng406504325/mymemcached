package com.nevermore.context.impl;

import com.google.common.base.Optional;
import com.nevermore.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用程序上下文
 *
 * @author suncheng
 * @since 15/11/25
 */
public class AppContext implements Context {
    private static final Logger logger = LoggerFactory.getLogger(Context.class);
    private static AppContext context;

    private Map<String, Optional<Object>> map;

    public static AppContext getAppContext() {
        if (context == null) {
            synchronized (AppContext.class) {
                if (context == null) {
                    context = new AppContext();
                }
            }
        }
        return context;
    }


    private AppContext() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<Object> get(String key) {
        return map.get(key);
    }

    @Override
    public void set(String key, Optional<Object> value) {
        map.put(key, value);
    }

    @Override
    public void initialize() throws IOException {
    }

    @Override
    public void close() throws Exception {
    }
}
