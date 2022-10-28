package com.org.somak.datastore.client;

import com.org.somak.datastore.configuration.engine.EngineMetadata;
import com.org.somak.datastore.engine.IDataStoreEngine;
import com.org.somak.datastore.engine.impl.DiskDataStoreEngine;
import com.org.somak.datastore.entity.IMemTable;
import com.org.somak.datastore.entity.factory.IMemTableFactory;
import com.org.somak.datastore.entity.factory.MultipleEntryMapFactory;
import com.org.somak.datastore.entity.factory.SingleEntryMapFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class LSMClient<K extends Serializable & Comparable<K>, V extends Serializable> {


    private IMemTableFactory<K, V> getFactory() {

        boolean isMultiMapEnable = Boolean.parseBoolean(EngineMetadata.getEngineMetaData("IS_MULTIPLE_MAP"));
        if (isMultiMapEnable)
            return new MultipleEntryMapFactory<K, V>();
        else
            return new SingleEntryMapFactory<K, V>();
    }

    private IMemTable<K, V> createMemTable() {

        return getFactory().createMemTable();

    }

    public void saveData(String tableName, K key, V value) {
        IDataStoreEngine<K, V> engine = new DiskDataStoreEngine<K, V>(createMemTable());
        log.info("Engine found ", engine);
        engine.save(tableName, key, value);

    }

}
