// For week 2

// gradle -PmainClass=exercises02.NewMutableInteger run
package exercises02;

public class NewMutableInteger {
    public static void main(String[] args) {
        final SyncMutableInteger mi = new SyncMutableInteger();
        Thread t = new Thread(() -> {
            while (mi.get() == 0) // Loop while zero
            {
                /* Do nothing */ }
            System.out.println("I completed, mi = " + mi.get());
        });
        t.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mi.set(42);
        System.out.println("mi set to 42, waiting for thread ...");
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread t completed, and so does main");
    }
}

class SyncMutableInteger {

    private int value = 0;

    public synchronized void set(int value) {
        this.value = value;
    }

    public synchronized int get() {
        return value;
    }
}
