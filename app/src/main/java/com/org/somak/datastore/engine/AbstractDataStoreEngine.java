package com.org.somak.datastore.engine;

import com.org.somak.datastore.configuration.engine.EngineMetadata;
import com.org.somak.datastore.engine.impl.MultiMapJournal;
import com.org.somak.datastore.engine.impl.SingleMapJournal;
import com.org.somak.datastore.entity.IMemTable;
import com.org.somak.datastore.entity.MemTableMultipleEntryMap;
import com.org.somak.datastore.entity.MemTableSingleEntryMap;
import com.org.somak.datastore.exception.ExceptionCode;
import com.org.somak.datastore.exception.WriteFailureException;
import com.org.somak.datastore.exception.WriteTimeoutException;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public abstract class AbstractDataStoreEngine<K extends Serializable & Comparable<K>, V extends Serializable> implements IDataStoreEngine<K,V>{

    private IJournal journal;
    protected IMemTable<K,V> memTable;
    private static final String DATA_STORE_NOT_PROVIDED = "Internal data structure not provided/initialized";
    private AtomicBoolean isSizeBreached = new AtomicBoolean(false);
    private final FlushPermit flushPermit;

    @Override
    public void acquirePermit() throws InterruptedException {
        this.flushPermit.acquireFlushPermit();
    }

    @Override
    public void releasePermit() {
        this.flushPermit.releaseFlushPermit();
    }

    private Function<IMemTable<K,V>, Integer> sizeCheckFunction =(t)->{
        Objects.requireNonNull(t);
        if(t instanceof MemTableSingleEntryMap){
            return Integer.parseInt(EngineMetadata.getEngineMetaData("MAX_BUFFER_SIZE"));
        }
        else if(t instanceof  MemTableMultipleEntryMap){
            return Integer.parseInt(EngineMetadata.getEngineMetaData("MAX_POOL_SIZE"));
        }
      return -1;
    };

    private Function<IMemTable<K,V>, IJournal<K,V>> journalFunction = (t)->{
       if(t instanceof MemTableMultipleEntryMap)
           return new MultiMapJournal<K,V>();
       else
           return new SingleMapJournal<K,V>();
    } ;

    protected AbstractDataStoreEngine(IMemTable<K,V> table){

        this.memTable = table;
        this.journal =  journalFunction.apply(table);
        this.flushPermit = FlushPermit.getUniqueInstance();
    }

    @Override
    public void flushToDisk(String tableName) throws WriteTimeoutException, WriteFailureException {
        System.out.println("before flush "+ this.memTable.getSize(tableName));
        this.journal.flush(this.memTable, tableName);
    }

    protected void validateInternalCache() {
        if(memTable == null){
            throw new WriteFailureException(ExceptionCode.INTERNAL_CACHE_NOT_PROVIDED, AbstractDataStoreEngine.DATA_STORE_NOT_PROVIDED);
        }
    }

    @Override
    public boolean isMaximumSizeBreached(String tableName) {
        return this.memTable.getSize(tableName)>=sizeCheckFunction.apply(this.memTable);
    }

}
