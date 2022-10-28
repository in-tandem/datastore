package com.org.somak.datastore.entity.factory;

import com.org.somak.datastore.entity.IMemTable;

import java.io.Serializable;

public interface IMemTableFactory<K extends Serializable & Comparable<K>, V extends Serializable> {

    public IMemTable<K,V> createMemTable();
}
