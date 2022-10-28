package com.org.somak.datastore.engine;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class FlushPermit {

    private static FlushPermit uniqueInstance;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    private FlushPermit() {
        writeLock = new ReentrantReadWriteLock().writeLock();

    }

    public static FlushPermit getUniqueInstance() {
        if (uniqueInstance == null) {
            synchronized (FlushPermit.class) {
                if (uniqueInstance == null)
                    uniqueInstance = new FlushPermit();
            }
        }
        return uniqueInstance;
    }

    public void acquireFlushPermit() throws InterruptedException {
        this.writeLock.lock();
    }

    public void releaseFlushPermit() {
        this.writeLock.unlock();
    }


}
