package exercises05;

import java.util.concurrent.atomic.AtomicInteger;

public class CasHistogram implements Histogram {
    private final AtomicInteger[] counts;

    public CasHistogram(int span) {
        this.counts = new AtomicInteger[span];
        for (int i = 0; i < span; i++) {
            counts[i] = new AtomicInteger(0); // This must apparently be done with atomicIntegers, else the values are
                                              // null
        }
    }

    public void increment(int bin) {
        int oldValue, newValue;
        AtomicInteger count = counts[bin];
        do {
            oldValue = counts[bin].get();
            newValue = oldValue + 1;
        } while (!count.compareAndSet(oldValue, newValue));
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
            oldValue = getCount(bin);
        } while (!counts[bin].compareAndSet(oldValue, 0));
        return oldValue;
    }
}

/*
 * 
 * 1. In our implementation, the class state is comprised by the array of atomic
 * integers `counts`. We ensure that the class state does not escape:
 * - By making the fields `private` such that the local fields are only
 * available to the instance of a class;
 * - By only accessing our fields for read/write, through the `increment`,
 * `getCount` and `getAndClear` methods, which does not return a reference to
 * the private field, but return a copy of that.
 * We ensure safe publication by making the class state immutable. If an
 * object’s fields are marked as `final`, they are guaranteed to be fully
 * initialised by the time the constructor finishes. Any thread that accesses
 * the object after construction will see the correctly initialised `final`
 * field values.*
 */

/*
 * 2. We change `counts` to be an array of `AtomicInteger` because of their
 * inherited ability to provide lock-free read/write methods such as CAS. CAS
 * can detect interference from other threads and atomically update the old
 * value to the new value `0`. We also use the do-while loop, which checks for
 * the old value and updates to the new value only if the older value hasn't
 * been overwritten in the meanwhile. After the value has been updated
 * successfully only then we return the old value. This way we ensure atomicity.
 */