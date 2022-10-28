package com.org.somak.datastore.engine.impl;

import com.org.somak.datastore.engine.AbstractDataStoreEngine;
import com.org.somak.datastore.engine.IDataStoreEngine;
import com.org.somak.datastore.entity.IMemTable;
import com.org.somak.datastore.exception.WriteFailureException;
import java.io.Serializable;


public class DiskDataStoreEngine<K extends Serializable & Comparable<K>,V extends Serializable> extends AbstractDataStoreEngine<K,V> implements IDataStoreEngine<K,V> {

    public DiskDataStoreEngine(IMemTable<K, V> table) {
        super(table);
    }

    @Override
    public boolean addToCache(String tableName, K key, V value) throws WriteFailureException {
        validateInternalCache();
        return this.memTable.put(tableName, key, value);
    }

}
