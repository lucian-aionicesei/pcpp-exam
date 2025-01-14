package exercises01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// gradle -PmainClass=exercises01.Exercise2 run

public class Exercise2 {
    Lock l = new ReentrantLock();
    Printer p = new Printer();

    public Exercise2() {
        Thread t1 = new Thread(() -> {
            while (true) {
                p.print("A");
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                p.print("B");
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException exn) {
            System.out.println("Some thread was interrupted");
        }
    }

    public static void main(String[] args) {
        new Exercise2();
    }

    class Printer {
        public void print(String s) {
            l.lock();
            System.out.print(s + "-"); // step (1)
            try {
                Thread.sleep(50); // step (2)
            } catch (InterruptedException exn) {
            }
            System.out.print(s + "|"); // step (3)
            l.unlock();
        }
    }
}

/*
 * 2. A race condition occurs - A race condition occurs when the correctness of
 * a computation depends on the relative timing or interleaving of multiple
 * threads by the runtime; in other words, when getting the right answer relies
 * on lucky timing (pg.41). The critical section related to printing the values
 * is currently accesed by multiple threads at the same time. We need to ensure
 * mutual exclusion
 */

// Example 1 (v2)
// t1(1), print "-"
// t1(2), wait
// t2(1), print "-"
// t2(2), wait
// t1(3), print "|"
// t1(1), print "-"
// t1(2), wait
// t2(3), print "|"
// t2(1), print "-"
// t2(1), wait
// t1(3), print "|"