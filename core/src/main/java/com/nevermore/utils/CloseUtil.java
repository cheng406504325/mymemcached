package com.nevermore.utils;

/**
 * @author suncheng
 * @since 15/11/25
 */
public abstract class CloseUtil {

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {

            }
        }
    }
}
