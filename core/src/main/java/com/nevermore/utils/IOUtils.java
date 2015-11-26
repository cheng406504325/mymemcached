package com.nevermore.utils;

import com.google.common.base.Optional;

import java.io.*;
import java.util.Map;

/**
 * @author suncheng
 * @since 15/11/25
 */
public abstract class IOUtils {

    public static BufferedReader getReader(String filePath)
            throws FileNotFoundException {
        return new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath)));
    }

    public static BufferedReader getReader(InputStreamReader reader) {
        return new BufferedReader(reader);
    }

    public static BufferedReader getReader(InputStream in) {
        return new BufferedReader(new InputStreamReader(in));
    }

    public static void readToMap(BufferedReader reader,
                                 Map<String, Optional<Object>> map)
            throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String key = line.split("=")[0];
            String value = line.split("=")[1];
            map.put(key, Optional.of(value));
        }
    }
}
