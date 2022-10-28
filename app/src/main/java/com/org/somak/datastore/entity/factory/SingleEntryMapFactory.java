package com.org.somak.datastore.entity.factory;

import com.org.somak.datastore.entity.IMemTable;
import com.org.somak.datastore.entity.MemTableSingleEntryMap;

import java.io.Serializable;

public class SingleEntryMapFactory<K extends Serializable & Comparable<K>, V extends  Serializable> implements  IMemTableFactory<K,V>{

    @Override
    public IMemTable<K, V> createMemTable() {
        return MemTableSingleEntryMap.getUniqueInstance();
    }
}
