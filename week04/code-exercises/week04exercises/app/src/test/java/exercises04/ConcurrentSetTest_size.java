package exercises04;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

public class ConcurrentSetTest_size {

    private ConcurrentIntegerSet set;
    private CyclicBarrier barrier;

    @BeforeEach
    public void initialize() {
        // Use the buggy version that is prone to interleaving issues
        set = new ConcurrentIntegerSetBuggy();
    }

    public void addAndRemoveElements() {
        for (int i = 0; i < 1000; i++) {
            set.add(i);
            set.remove(i);
        }
    }

    @RepeatedTest(500)
    void testSizeFunction() {
        int nrThreads = 15;
        barrier = new CyclicBarrier(nrThreads * 2 + 1); // 30 threads + main thread

        // 15 threads modifying the set concurrently
        for (int i = 0; i < nrThreads; i++) {
            new Thread(() -> {
                try {
                    barrier.await();
                    addAndRemoveElements();
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // 15 threads calling size() concurrently
        for (int i = 0; i < nrThreads; i++) {
            new Thread(() -> {
                try {
                    barrier.await(); // Wait for all threads to reach the start point
                    int reportedSize = set.size();
                    barrier.await(); // Wait for all threads to finish
                    if (reportedSize < 0) {
                        throw new AssertionError("Set size is negative: " + reportedSize);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        try {
            barrier.await(); // Ensure all threads start together
            barrier.await(); // Ensure all threads finish before main thread checks the size
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // After all modifications, check that the size is correct
        int finalSize = set.size();
        assertNotEquals(finalSize, -1, "Final size should not be negative.");
    }
}
