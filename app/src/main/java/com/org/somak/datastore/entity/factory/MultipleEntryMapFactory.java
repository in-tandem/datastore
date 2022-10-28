package com.org.somak.datastore.entity.factory;

import com.org.somak.datastore.entity.IMemTable;
import com.org.somak.datastore.entity.MemTableMultipleEntryMap;

import java.io.Serializable;

public class MultipleEntryMapFactory<K extends Serializable & Comparable<K>, V extends Serializable> implements IMemTableFactory<K,V>{

    @Override
    public IMemTable<K, V> createMemTable() {
        return MemTableMultipleEntryMap.getUniqueInstance();
    }
}
