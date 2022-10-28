package com.org.somak.datastore.engine.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FileSequencer {

    private int fileNumber;

    private String leftPad= "000";
    private Map<String,Integer> map = new ConcurrentHashMap<String,Integer>();
    public final static FileSequencer uniqueInstance = new FileSequencer();

    private FileSequencer(){

    }

    public String fileNumber(String tableName){

        if(!this.map.containsKey(tableName)){
            this.map.put(tableName,1);
            return leftPad+"1";
        }else{
            this.map.put(tableName,this.map.get(tableName)+1);
            return leftPad + Integer.toString(this.map.get(tableName));
        }
    }

}
