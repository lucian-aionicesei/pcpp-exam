// For week 2

// gradle -PmainClass=lecture02.ReadersWriters run
package exercises02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ReadWriteMonitor {
    private int readers = 0;
    private boolean writer = false;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    //////////////////////////
    // Read lock operations //
    //////////////////////////

    public void readLock() {
        lock.lock();
        try {
            while (writer)
                condition.await();
            readers++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void readUnlock() {
        lock.lock();
        try {
            readers--;
            if (readers == 0)
                condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    ///////////////////////////
    // Write lock operations //
    ///////////////////////////

    public void writeLock() {
        lock.lock();
        try {
            while (readers > 0 || writer)
                condition.await();
            writer = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void writeUnlock() {
        lock.lock();
        try {
            writer = false;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

}
