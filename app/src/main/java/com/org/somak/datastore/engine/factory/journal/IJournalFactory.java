package com.org.somak.datastore.engine.factory.journal;

import com.org.somak.datastore.engine.IJournal;
import com.org.somak.datastore.entity.IMemTable;

import java.io.Serializable;

public interface IJournalFactory<K extends Serializable & Comparable<K>, V extends Serializable> {

    public IJournal<K,V> createJournalFactory(IMemTable<K,V> memTable);

}
