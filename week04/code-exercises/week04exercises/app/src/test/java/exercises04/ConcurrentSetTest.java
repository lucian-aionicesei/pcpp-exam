package exercises04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;
// TODO: Very likely you need to expand the list of imports

public class ConcurrentSetTest {

    // Variable with set under test
    private ConcurrentIntegerSet set;

    // TODO: Very likely you should add more variables here

    private CyclicBarrier barrier;

    // Uncomment the appropriate line below to choose the class to
    // test
    // Remember that @BeforeEach is executed before each test
    @BeforeEach
    public void initialize() {
        // init set
        set = new ConcurrentIntegerSetBuggy();
        // set = new ConcurrentIntegerSetSync();
        // set = new ConcurrentIntegerSetLibrary();
    }

    // TODO: Define your tests below

    // Buggy test add

    @RepeatedTest(500)
    public void AddTester() {
        int threadCount = 16;
        barrier = new CyclicBarrier(threadCount);

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    SetAddOne();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        assertTrue(set.size() == 1, "The set should contain exactly one element.");
    }

    public void SetAddOne() {
        for (int i = 0; i < 10000; i++) {
            set.add(1);
        }
    }

    // test remove

    @RepeatedTest(500)
    public void RemoveTester() {
        int threadCount = 16;
        barrier = new CyclicBarrier(threadCount);

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    barrier.await();
                    SetRemoveOne();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        assertTrue(set.size() >= 0, "The set should not contain less than 0.");
    }

    public void SetRemoveOne() {
        for (int i = 0; i < 1000; i++) {
            if (set.size() == 0) {
                set.add(1);
            }

            set.remove(1);
        }
    }
}
