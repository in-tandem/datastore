package com.org.somak.datastore.entity;

import com.org.somak.datastore.configuration.engine.EngineMetadata;

import java.io.Serializable;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class MemTableMultipleEntryMap<K extends Serializable & Comparable<K>, V extends Serializable> implements IMemTable<K,V>{

    private ConcurrentHashMap<String, CurrentAwareData<TreeMap<K,V>>> internal;

    private static MemTableMultipleEntryMap uniqueInstance;

    private MemTableMultipleEntryMap(){
        this.internal = new ConcurrentHashMap<>();
    }

    @Override
    public int getSize(String tableName) {
        CurrentAwareData<TreeMap<K,V>> memTable = this.internal.get(tableName);
        return memTable!=null?memTable.getPast().size():0;
    }

    public static MemTableMultipleEntryMap getUniqueInstance(){

        if(uniqueInstance==null){
            synchronized (MemTableMultipleEntryMap.class){
                if(uniqueInstance==null){
                    uniqueInstance = new MemTableMultipleEntryMap();
                }
            }
        }
        return  uniqueInstance;

    }

    /**
     * Will always return a copy of the internal structure to prevent
     * state loss or change
     * @return
     */
    public ConcurrentHashMap<String, CurrentAwareData<TreeMap<K,V>>> getInternal(){
        return new ConcurrentHashMap<>(this.internal);
    }

    public boolean put(String tableName, K key, V value){

        boolean response = false;

        if(this.internal.get(tableName)==null){
            TreeMap<K,V> map = new TreeMap<>();
            map.put(key, value);
            CurrentAwareData<TreeMap<K,V>> current = new CurrentAwareData<>(map);
            this.internal.put(tableName,current);

        }else{

            CurrentAwareData<TreeMap<K,V>> currentValue = this.internal.get(tableName);
            TreeMap<K,V> currentTable = Optional.ofNullable(currentValue.getCurrent()).orElseGet(()->new TreeMap<>());
            currentTable.put(key, value);
            currentValue.setCurrent(currentTable);
            response = checkIfMaxSizeBreached(currentTable);
            if(checkIfMaxSizeBreached(currentTable)){
                currentValue.moveToPast();
                response = checkIfMaxPoolSizeBreached(currentValue);
            }

        }
        return response;
    }

    private boolean checkIfMaxPoolSizeBreached(CurrentAwareData<TreeMap<K,V>> currentValue){
        int maximumPoolSize = Integer.parseInt(EngineMetadata.getEngineMetaData("MAX_POOL_SIZE"));
        if(currentValue!=null && currentValue.getPast()!=null && currentValue.getPast().size()==maximumPoolSize)
            return true;
        return false;
    }

    private boolean checkIfMaxSizeBreached(TreeMap<K, V> currentTable) {
        int maximumBufferSize = Integer.parseInt(EngineMetadata.getEngineMetaData("MAX_BUFFER_SIZE"));
        if(maximumBufferSize== currentTable.size()){
            return true;
        }
        return false;
    }

}
