// raup@itu.dk * 2023-10-20
package exercises06;

// gradle cleanTest test --tests exercises06.TestLockFreeStack

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

class TestLockFreeStack {

    // The imports above are just for convenience, feel free add or remove imports
    private LockFreeStack<Integer> myStack;
    private CyclicBarrier barrier;

    @BeforeEach
    void setUp() {
        myStack = new LockFreeStack<>();
    }

    // TODO: 6.2.2 - Test push
    @Test
    void testLockFreeStack_Push() {
        int nrThreads = 10;
        myStack = new LockFreeStack<>();
        barrier = new CyclicBarrier(nrThreads + 1); // +1 for the main thread

        for (int i = 0; i < nrThreads; i++) {
            final int currentIndex = i;
            new Thread(() -> {
                try {
                    barrier.await(); // Wait until all threads are ready to start
                    myStack.push(currentIndex); // Each thread pushes its index
                    barrier.await(); // Wait for all threads to finish pushing
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Ensure all threads start together
        try {
            barrier.await(); // First barrier await ensures all threads start simultaneously
            barrier.await(); // Second barrier await ensures all threads finish before checking results
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // Validate the stack after all threads have pushed
        int stackSum = 0;
        int expectedSum = IntStream.range(0, nrThreads).sum(); // Sum of 0 to nrThreads - 1

        for (int i = 0; i < nrThreads; i++) {
            stackSum += myStack.pop();
        }

        // Check if the total sum matches the expected sum
        assertEquals(expectedSum, stackSum, "All pushed values should sum correctly");
    }

    // TODO: 6.2.3 - Test pop
    @Test
    void testLockFreeStack_Pop() {
        int nrThreads = 10;
        myStack = new LockFreeStack<>();
        barrier = new CyclicBarrier(nrThreads + 1); // +1 for the main thread
        AtomicInteger stackSum = new AtomicInteger(0); // Use AtomicInteger for thread-safe updates

        // Push values to the stack before starting threads to pop them
        for (int i = 0; i < nrThreads; i++) {
            myStack.push(i);
        }

        // Start threads that pop values from the stack
        for (int i = 0; i < nrThreads; i++) {
            new Thread(() -> {
                try {
                    barrier.await(); // Wait until all threads are ready to start
                    Integer poppedValue = myStack.pop();
                    if (poppedValue != null) {
                        stackSum.addAndGet(poppedValue); // Thread-safe addition
                    }
                    barrier.await(); // Wait for all threads to finish
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Ensure all threads start together
        try {
            barrier.await(); // First barrier await ensures all threads start simultaneously
            barrier.await(); // Second barrier await ensures all threads finish before checking results
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        int expectedSum = IntStream.range(0, nrThreads).sum(); // Sum of 0 to nrThreads - 1

        // Check if the total sum matches the expected sum
        assertEquals(expectedSum, stackSum.get(), "All popped values should sum correctly");
    }

    // Test pop on an empty stack;
    @Test
    void LockFreeStack_Pop_returnsNull() {
        myStack = new LockFreeStack<>();

        assertEquals(null, myStack.pop(), "Popped value is null");
    }

    @Test
    void testLockFreeStack_PushThenPop() {
        int nrThreads = 2;
        myStack = new LockFreeStack<>();
        barrier = new CyclicBarrier(nrThreads + 1); // +1 for the main thread
        AtomicInteger stackSum = new AtomicInteger(0); // Use AtomicInteger for thread-safe updates

        // Push values to the stack before starting threads to pop them
        myStack.push(10);

        // Start threads that pop values from the stack
        for (int i = 0; i < nrThreads; i++) {
            new Thread(() -> {
                try {
                    barrier.await(); // Wait until all threads are ready to start
                    Integer poppedValue = myStack.pop();
                    if (poppedValue != null) {
                        stackSum.addAndGet(poppedValue); // Thread-safe addition
                    }
                    barrier.await(); // Wait for all threads to finish
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        // Ensure all threads start together
        try {
            barrier.await(); // First barrier await ensures all threads start simultaneously
            barrier.await(); // Second barrier await ensures all threads finish before checking results
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        // Check if the total sum matches the expected sum
        assertEquals(10, stackSum.get(), "All popped values should sum correctly");
    }
}
