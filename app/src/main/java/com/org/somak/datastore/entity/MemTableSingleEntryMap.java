package com.org.somak.datastore.entity;

import com.org.somak.datastore.configuration.engine.EngineMetadata;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class MemTableSingleEntryMap<K extends Serializable & Comparable<K>, V extends Serializable> implements  IMemTable<K,V>{

    private ConcurrentHashMap<String, MemTable<K,V>> internal;

    private static MemTableSingleEntryMap uniqueInstance;

    private MemTableSingleEntryMap(){
        this.internal = new ConcurrentHashMap<>();
    }

    @Override
    public int getSize(String tableName) {
        MemTable<K,V> memTable = this.internal.get(tableName);
        return memTable!=null?memTable.getSize():0;
    }

    public static MemTableSingleEntryMap getUniqueInstance(){

        if(uniqueInstance==null){
            synchronized (MemTableMultipleEntryMap.class){
                if(uniqueInstance==null){
                    uniqueInstance = new MemTableSingleEntryMap();
                }
            }
        }
        return  uniqueInstance;
    }

    /***
     * Will always return a copy of the internal structure to prevent
     * state changes/loss
     *
     * @return
     */
    public ConcurrentHashMap<String,MemTable<K,V>> getInternal(){
        return new ConcurrentHashMap<>(this.internal);
    }

    @Override
    public boolean put(String tableName, K key, V value) {
        boolean response = false;

        if(this.internal.get(tableName)==null){
            MemTable<K,V> memTable = new MemTable<>();
            memTable.put(key, value);
            this.internal.put(tableName, memTable);
        }else{

            MemTable<K,V> memTable = this.internal.get(tableName);
            memTable.put(key, value);
            response = checkIfMaxSizeBreached(memTable);
        }
        return response;
    }

    private boolean checkIfMaxSizeBreached(MemTable<K, V> memTable) {
        int maximumBufferSize = Integer.parseInt(EngineMetadata.getEngineMetaData("MAX_BUFFER_SIZE"));

        if(maximumBufferSize== memTable.getSize()){

            return true;
        }
        return false;
    }

}
