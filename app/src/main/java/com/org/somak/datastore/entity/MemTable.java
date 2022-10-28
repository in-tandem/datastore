package com.org.somak.datastore.entity;

import com.org.somak.datastore.configuration.engine.EngineMetadata;
import com.org.somak.datastore.engine.FlushPermit;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class MemTable<K extends Serializable & Comparable<K>, V extends Serializable> {

    private final int MAX_SIZE;
    private Map<K,V> map;
    private String uniqueId = UUID.randomUUID().toString();

    public String getSegmentId(){
        return  this.uniqueId;
    }

    public MemTable(){

        this.MAX_SIZE = Integer.parseInt(EngineMetadata.getEngineMetaData("MAX_BUFFER_SIZE"));
        this.map = new TreeMap<>();

    }

    public final boolean put(K key, V value){

//        int permit = FlushPermit.getUniqueInstance().getAvailableFlushPermit();
        System.out.println(String.format("%s size of table is %d " +
                " at time %d", Thread.currentThread().getName(), this.map.size(), System.nanoTime()));
        boolean response = false;
        this.map.put(key, value);
        if(this.MAX_SIZE == this.map.size()){
            response = true;
        }
        return response;
    }

    public final void clearAll(){
        this.map.clear();
    }

    public final int getSize(){
        return this.map.size();
    }
}
