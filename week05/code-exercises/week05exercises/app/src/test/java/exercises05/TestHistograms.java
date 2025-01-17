package exercises05;

// gradle cleanTest test --tests exercises05.TestHistograms

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

// Concurrency imports
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class TestHistograms {
    // The imports above are just for convenience, feel free to add or remove
    // imports

    private CasHistogram casHistogram;
    private Histogram1 simpleHistogram;
    private CyclicBarrier barrier;

    @BeforeEach
    public void initialize() {
        casHistogram = new CasHistogram(12);
        simpleHistogram = new Histogram1(12);
    }

    // Function to count the number of prime factors of a number `p`
    private static int countFactors(int p) {
        if (p < 2)
            return 0;
        int factorCount = 1, k = 2;
        while (p >= k * k) {
            if (p % k == 0) {
                factorCount++;
                p = p / k;
            } else {
                k = k + 1;
            }
        }
        return factorCount;
    }

    @Test
    void testHistogramFunction() {
        int nrThreads = 2600;
        barrier = new CyclicBarrier(nrThreads + 1);

        // Start incrementing in multiple threads
        for (int i = 0; i < nrThreads; i++) {
            final int currentIndex = i;
            new Thread(() -> {
                try {
                    barrier.await();
                    casHistogram.increment(countFactors(currentIndex));
                    barrier.await();
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

        // Increment the simple histogram in the main thread
        for (int i = 0; i < nrThreads; i++) {
            simpleHistogram.increment(countFactors(i));
        }

        // Test here the values of simpleHistogram vs casHistogram
        for (int bin = 0; bin < simpleHistogram.getSpan(); bin++) {
            assertEquals(simpleHistogram.getCount(bin), casHistogram.getCount(bin),
                    "Counts for bin " + bin + " do not match.");
        }
    }
}
