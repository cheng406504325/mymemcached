package com.nevermore.coretest;

import com.nevermore.core.storage.StorageCore;
import com.nevermore.core.storage.impl.StorageCoreImpl;
import com.nevermore.module.CachedData;
import org.junit.Test;

/**
 * @author suncheng
 * @version 0.0.1
 * @since 15/11/26
 */
public class LRUTest {

    @Test
    public void testConstructor() {
        StorageCore storageCore = new StorageCoreImpl();
    }

    @Test
    public void testSet() {
        StorageCore storageCore = new StorageCoreImpl();
        storageCore.set("1", new CachedData());
        System.out.println(storageCore.get("1"));
    }

    @Test
    public void testRelease() {
        StorageCore storageCore = new StorageCoreImpl();
        storageCore.start();
        for (int i=0;i<10000;i++){
            storageCore.set(String.valueOf(i), new CachedData());
        }
    }

    public static void main(String[] args) {
        StorageCore storageCore = new StorageCoreImpl();
        storageCore.start();
        for (int i=0;i<100000;i++){
            storageCore.set(String.valueOf(i), new CachedData());
        }
        System.out.println("done");
    }
}
