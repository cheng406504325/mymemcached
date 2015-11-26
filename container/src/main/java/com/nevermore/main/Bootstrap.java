package com.nevermore.main;

import com.nevermore.core.storage.StorageCore;
import com.nevermore.core.storage.impl.StorageCoreImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/25
 */
public class Bootstrap {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String args[]) {
        StorageCore storageCore = new StorageCoreImpl();
    }
}
