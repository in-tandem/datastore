package com.org.somak.datastore.engine;

import com.org.somak.datastore.entity.IMemTable;
import com.org.somak.datastore.exception.WriteFailureException;
import com.org.somak.datastore.exception.WriteTimeoutException;

import java.io.Serializable;

public interface IJournal<K extends Serializable & Comparable<K>, V extends Serializable> {

    public void flush(IMemTable<K,V> item, String tableName) throws WriteTimeoutException, WriteFailureException;
}
