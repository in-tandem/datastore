package com.org.somak.datastore.engine;

import com.org.somak.datastore.exception.ExceptionCode;
import com.org.somak.datastore.exception.InvalidRequestException;
import com.org.somak.datastore.exception.WriteFailureException;
import com.org.somak.datastore.exception.WriteTimeoutException;

import java.io.Serializable;


public interface IDataStoreEngine<K extends Serializable & Comparable<K>,V extends  Serializable>  {

    String KEY_CANNOT_BE_NULL = "Attempting to insert data with null key";
    String VALUE_CANNOT_BE_NULL = "Attempting to insert data with null value";

    void acquirePermit() throws InterruptedException;
    void releasePermit();
    default void save(String tableName, K key, V value) throws WriteTimeoutException, WriteFailureException, InvalidRequestException{

        validateInput(key, value);
        try{
            acquirePermit();
            addToCache(tableName, key, value);
            while(isMaximumSizeBreached(tableName)) // cannt be done multiple threads may execute this part
                flushToDisk(tableName);
        } catch (InterruptedException exc) {
            throw new WriteFailureException(ExceptionCode.WRITE_FAILURE,
                    String.format("Failed to generate journal entry for table %s", tableName),
                    exc);
        } finally {
            releasePermit();
        }
//        System.out.println(String.format("1. table name %s, and size breached %s", tableName, isMaximumSizeBreached(tableName)));

//        while(!getIsSizeBreached().compareAndSet(isMaximumSizeBreached(tableName), false)){
//            flushToDisk(tableName);
//        }
//        while(isMaximumSizeBreached(tableName)) // cannt be done multiple threads may execute this part
//            flushToDisk(tableName);

//        CountDownLatch flushOngoing = new CountDownLatch(1);
//        while(isFlushOngoing())
//            flushOngoing.await();
//        flushOngoing.countDown();
//        addToCache(tableName, key, value);
//        AtomicBoolean flushOngoing = new AtomicBoolean(isFlushOngoing());
//        while(!flushOngoing.compareAndSet(false, true))
//            addToCache(tableName, key, value);
//        System.out.println(String.format("2. table name %s, and size breached %s", tableName, isMaximumSizeBreached(tableName)));

    };

//    public boolean isFlushOngoing();
//    public AtomicBoolean getIsSizeBreached();
    void flushToDisk(String tableName) throws WriteTimeoutException, WriteFailureException;

    boolean addToCache(String tableName, K key, V value) throws WriteFailureException;

    boolean isMaximumSizeBreached(String tableName);

    default void validateInput(K key, V value) throws InvalidRequestException{
        requireNonNull(key, KEY_CANNOT_BE_NULL);
        requireNonNull(value, VALUE_CANNOT_BE_NULL);
    }

    private void requireNonNull(final Object object, final String message) throws InvalidRequestException{

        if(object==null)
            throw new InvalidRequestException(ExceptionCode.INVALID_REQUEST, message);
    }

}
