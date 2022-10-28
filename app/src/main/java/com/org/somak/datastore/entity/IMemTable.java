package com.org.somak.datastore.entity;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public interface IMemTable<K extends Serializable & Comparable<K>, V extends Serializable> {

    public boolean  put(String tableName, K key, V value);
    public int getSize(String tableName);

}
