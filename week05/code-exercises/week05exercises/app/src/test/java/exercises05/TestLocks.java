package exercises05;

// JUnit testing imports
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;

// Data structures imports
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// Concurrency imports
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestLocks {
    // The imports above are just for convenience, feel free add or remove imports
    private SimpleRWTryLock myLock;
    private CyclicBarrier barrier;

    @BeforeEach
    public void initialize() {
        myLock = new SimpleRWTryLock();
    }

    // Test 1: It is not possible to take a read lock while holding a write lock
    @Test
    void testReadLockWhileHoldingWriteLock() {
        // Take write lock
        assertTrue(myLock.writerTryLock());

        // Try to take read lock (should fail)
        assertFalse(myLock.readerTryLock());

        // Release write lock
        myLock.writerUnlock();
    }

    // Test 2: It is not possible to take a write lock while holding a read lock
    @Test
    void testWriteLockWhileHoldingReadLock() {
        // Take read lock
        assertTrue(myLock.readerTryLock());

        // Try to take write lock (should fail)
        assertFalse(myLock.writerTryLock());

        // Release read lock
        myLock.readerUnlock();
    }

    // Test 3: It is not possible to unlock a lock that you do not hold (read
    // unlock)
    @Test
    void testReaderUnlockWithoutHoldingLock() {
        // Ensure exception is thrown if no lock is held
        assertThrows(IllegalMonitorStateException.class, () -> myLock.readerUnlock());

        // Take read lock
        assertTrue(myLock.readerTryLock());

        // Take another thread's unlock (should throw an exception)
        Thread t = new Thread(() -> assertThrows(IllegalMonitorStateException.class, () -> myLock.readerUnlock()));
        t.start();

        // Clean up: release the lock
        myLock.readerUnlock();
    }

    // Test 4: It is not possible to unlock a lock that you do not hold (write
    // unlock)
    @Test
    void testWriterUnlockWithoutHoldingLock() {
        // Ensure exception is thrown if no lock is held
        assertThrows(IllegalMonitorStateException.class, () -> myLock.writerUnlock());

        // Take write lock
        assertTrue(myLock.writerTryLock());

        // Take another thread's unlock (should throw an exception)
        Thread t = new Thread(() -> assertThrows(IllegalMonitorStateException.class, () -> myLock.writerUnlock()));
        t.start();

        // Clean up: release the lock
        myLock.writerUnlock();
    }

    // TODO: 5.2.6
    @Test
    void multipleWriters_CannotAcquireLock_Simultaneously() {
        int nrThreads = 10; // Number of writer threads
        barrier = new CyclicBarrier(nrThreads + 1); // CyclicBarrier to sync all threads
        AtomicInteger activeWriters = new AtomicInteger(0); // Track number of active writers
        AtomicInteger maxActiveWriters = new AtomicInteger(0); // Track max concurrent writers

        // Start multiple writer threads
        for (int i = 0; i < nrThreads; i++) {
            final int current = i;
            new Thread(() -> {
                try {
                    System.out.println("Initializing thread " + current);
                    barrier.await(); // Wait for all threads to be ready

                    if (myLock.writerTryLock()) {
                        int active = activeWriters.incrementAndGet(); // Increment active writers

                        // Simplified max tracking
                        if (active > maxActiveWriters.get()) {
                            maxActiveWriters.set(active);
                        }

                        Thread.sleep(100); // Simulate some work

                        activeWriters.decrementAndGet(); // Decrement active writers
                        myLock.writerUnlock(); // Release the lock
                        System.out.println("Thread " + current + " released write lock.");
                    }
                    barrier.await(); // Wait for all threads to finish
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Ensure all threads start together and finish together
        try {
            barrier.await(); // Trigger all threads to start simultaneously
            barrier.await(); // Wait for all threads to complete their execution
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // Verify that at no point did more than one writer hold the lock concurrently
        assertTrue(maxActiveWriters.get() <= 1, "Two writers acquired the lock simultaneously!");
    }

}
