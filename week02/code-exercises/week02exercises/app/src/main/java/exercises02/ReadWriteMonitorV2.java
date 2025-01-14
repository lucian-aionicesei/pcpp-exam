// For week 2
package exercises02;

public class ReadWriteMonitorV2 {
    private int readsAcquires = 0;
    private int readsReleases = 0;
    private boolean writer = false;

    //////////////////////////
    // Read lock operations //
    //////////////////////////

    public synchronized void readLock() {
        try {
            while (writer)
                this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readsAcquires++;
    }

    public synchronized void readUnlock() {
        readsReleases++;
        if (readsAcquires == readsReleases)
            this.notifyAll();
    }

    ///////////////////////////
    // Write lock operations //
    ///////////////////////////

    public synchronized void writeLock() {
        try {
            while (writer)
                this.wait();
            writer = true;
            while (readsAcquires != readsReleases)
                this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeUnlock() {
        writer = false;
        this.notifyAll();
    }

}
