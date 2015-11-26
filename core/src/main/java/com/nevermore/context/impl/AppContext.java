package com.nevermore.context.impl;

import com.google.common.base.Optional;
import com.nevermore.context.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.nevermore.utils.CloseUtil.closeQuietly;
import static com.nevermore.utils.IOUtils.getReader;
import static com.nevermore.utils.IOUtils.readToMap;

/**
 * 应用程序上下文
 *
 * @author suncheng
 * @since 15/11/25
 */
public class AppContext implements Context {
    private static final String PROPERTIES_FILE_PATH = "";

    private Map<String, Optional<Object>> map;

    private BufferedReader reader;

    public AppContext() throws FileNotFoundException {
        reader = getReader(PROPERTIES_FILE_PATH);
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
        readToMap(reader, map);
    }

    @Override
    public void close() throws Exception {
        closeQuietly(reader);
    }
}
