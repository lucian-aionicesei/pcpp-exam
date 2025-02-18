// NOTE: In this file, you should only modify the class ConcurrentIntegerSetSync
package exercises04;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentSkipListSet;

public interface ConcurrentIntegerSet {
    public boolean add(Integer element);

    public boolean remove(Integer element);

    public int size();
}

class ConcurrentIntegerSetBuggy implements ConcurrentIntegerSet {
    final private Set<Integer> set;

    public ConcurrentIntegerSetBuggy() {
        this.set = new HashSet<Integer>();
    }

    public boolean add(Integer element) {
        return set.add(element);
    }

    public boolean remove(Integer element) {
        return set.remove(element);
    }

    public int size() {
        return set.size();
    }
}

// TODO: Fix this class to pass your tests
class ConcurrentIntegerSetSync implements ConcurrentIntegerSet {
    final private Set<Integer> set;
    private static Object lockObject = new Object();
    // you can also use standard locks here
    // or implement it with a concurrencydesigned complex set object??

    public ConcurrentIntegerSetSync() {
        this.set = new HashSet<Integer>();
    }

    public boolean add(Integer element) {
        synchronized (lockObject) {
            return set.add(element);
        }
    }

    public boolean remove(Integer element) {
        synchronized (lockObject) {
            return set.remove(element);
        }
    }

    public int size() {
        return set.size();
    }
}

class ConcurrentIntegerSetLibrary implements ConcurrentIntegerSet {
    final private Set<Integer> set;

    public ConcurrentIntegerSetLibrary() {
        this.set = new ConcurrentSkipListSet<Integer>();
    }

    public boolean add(Integer element) {
        return set.add(element);
    }

    public boolean remove(Integer element) {
        return set.remove(element);
    }

    public int size() {
        return set.size();
    }
}
