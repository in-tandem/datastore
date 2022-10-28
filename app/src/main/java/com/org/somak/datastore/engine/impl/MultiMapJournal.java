package com.org.somak.datastore.engine.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.org.somak.datastore.engine.FlushPermit;
import com.org.somak.datastore.engine.IJournal;
import com.org.somak.datastore.entity.*;
import com.org.somak.datastore.exception.ExceptionCode;
import com.org.somak.datastore.exception.WriteFailureException;
import com.org.somak.datastore.exception.WriteTimeoutException;
import com.org.somak.datastore.util.Utility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class MultiMapJournal<K extends Serializable & Comparable<K>, V extends Serializable> implements IJournal<K,V> {

    private final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);//No need to pre-register the class
        return kryo;
    });

    @Override
    public void flush(IMemTable<K, V> item,String tableName) throws WriteTimeoutException, WriteFailureException {
        if (item != null) {

            MemTableMultipleEntryMap<K, V> multipleEntryMap = (MemTableMultipleEntryMap<K, V>) item;
            CurrentAwareData<TreeMap<K,V>> memTable = multipleEntryMap.getInternal().get(tableName);
            try {
                if(memTable!=null && memTable.getPast().size()!=0){
                    Map<K,V> map = new TreeMap<>();
                    for(TreeMap<K,V> tree: memTable.getPast())
                        map.putAll(tree);
                    memTable.getPast().clear();
                    serializeAndSaveObjectMap(map, tableName);

                }
            } catch (IOException exc) {
                throw new WriteFailureException(ExceptionCode.WRITE_FAILURE,
                        String.format("Failed to generate journal entry for table %s", tableName),
                        exc);
            }

        } else
            throw new WriteFailureException(ExceptionCode.WRITE_FAILURE,
                    String.format("Failed to get proper instance of memtable for table %s", tableName));
    }

    private void serializeAndSaveObjectMap(Map<K, V> memTable, String tableName) throws FileNotFoundException, IOException {

        String fileName = Utility.getSerializedOutputFileName(tableName);
        if(!Path.of(fileName).toFile().exists())
            Files.createFile(Path.of(fileName));
        Output output = null;
        try {
            System.out.println("size of object being flushed = "+memTable.size());
            output = new Output(new FileOutputStream(fileName));
            kryoLocal.get().writeObject(output, memTable);
        } finally {
            output.close();
        }
    }

}
