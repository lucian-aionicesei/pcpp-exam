// Week 3
// raup@itu.dk * 2021/09/16
package exercises03;

import java.util.List;
import java.util.ArrayList;

class ArrayListEscape {

    public ArrayListEscape() {
        IntArrayList array = new IntArrayList();
        new Thread(() -> {
            array.set(0, 1); // access shared state with lock
        }).start();
        new Thread(() -> {
            array.get().set(0, 42); // access shared state without locks
        }).start();

        System.out.println(array.get());
    }

    public static void main(String[] args) {
        new ArrayListEscape();
    }
}

class IntArrayList {
    private final List<Integer> a = new ArrayList<Integer>();

    public IntArrayList() {
        a.add(1);
    }

    public synchronized void set(Integer index, Integer elem) {
        a.set(index, elem);
    }
    // returns the reference to the a object and not a copy
    // public synchronized List<Integer> get() { return a ;}

    // Returns a copy of the list a
    public synchronized List<Integer> get() {
        return new ArrayList<>(a);
    }
}