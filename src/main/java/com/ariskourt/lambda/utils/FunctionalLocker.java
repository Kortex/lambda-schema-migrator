package com.ariskourt.lambda.utils;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FunctionalLocker {

    private final Lock lock;

    private FunctionalLocker(Lock lock) {
        this.lock = lock;
    }

    public void doLocked(Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
	} finally {
            lock.unlock();
	}
    }

    public static FunctionalLocker create() {
        return new FunctionalLocker(new ReentrantLock());
    }

}
