// For week 1

// gradle -PmainClass=exercises01.TestLongCounterExperiments run
package exercises01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestLongCounterExperiments {

    LongCounter lc = new LongCounter();
    int counts = 10_000_000;

    // Comment out ReentrantLock for demo
    Lock l = new ReentrantLock();

    public TestLongCounterExperiments() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < counts; i++)
                lc.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < counts; i++)
                lc.increment();
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException exn) {
            System.out.println("Some thread was interrupted");
        }
        System.out.println("Count is " + lc.get() + " and should be " + 2 * counts);
    }

    public static void main(String[] args) {
        new TestLongCounterExperiments();
    }

    class LongCounter {
        private long count = 0;

        public void increment() {
            /*
             * We identify the critical section to be "count = count + 1;" because it is
             * subject to data race: Multiple threads may access and update the same value
             * at the same time.
             */
            l.lock();
            count = count + 1;
            l.unlock();
        }

        public long get() {
            return count;
        }
    }
}
