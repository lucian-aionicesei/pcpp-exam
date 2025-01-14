// For week 2

package exercises02;

public class ReadWriteMonitorV1 {
    private int readsAcquires = 0;
    private int readsReleases = 0;
    private boolean writer = false;

    Object o = new Object();

    //////////////////////////
    // Read lock operations //
    //////////////////////////

    public void readLock() {
        synchronized (o) {
            try {
                while (writer)
                    o.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readsAcquires++;
        }
    }

    public void readUnlock() {
        synchronized (o) {
            readsReleases++;
            if (readsAcquires == readsReleases)
                o.notifyAll();
        }
    }

    ///////////////////////////
    // Write lock operations //
    ///////////////////////////

    public void writeLock() {
        synchronized (o) {
            try {
                while (writer)
                    o.wait();
                writer = true;
                while (readsAcquires != readsReleases)
                    o.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeUnlock() {
        synchronized (o) {
            writer = false;
            o.notifyAll();
        }
    }

}
