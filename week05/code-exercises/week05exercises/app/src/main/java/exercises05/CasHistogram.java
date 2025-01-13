package exercises05;

import java.util.concurrent.atomic.AtomicInteger;

public class CasHistogram implements Histogram {
    private final AtomicInteger[] counts;

    public CasHistogram(int span) {
        this.counts = new AtomicInteger[span];
        for (int i = 0; i < span; i++) {
            counts[i] = new AtomicInteger(0); // Initialize each AtomicInteger
        }
    }

    public void increment(int bin) {
        int oldValue, newValue;
        do {
            oldValue = counts[bin].get();
            newValue = oldValue + 1;
        } while (!counts[bin].compareAndSet(oldValue, newValue));
    }

    public int getCount(int bin) {
        return counts[bin].get();
    }

    public int getSpan() {
        return counts.length;
    }

    public int getAndClear(int bin) {
        int oldValue;
        do {
            oldValue = counts[bin].get();
        } while (!counts[bin].compareAndSet(oldValue, 0));
        return oldValue;
    }
}
