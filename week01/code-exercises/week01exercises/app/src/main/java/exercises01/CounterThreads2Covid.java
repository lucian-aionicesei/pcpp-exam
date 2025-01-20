// For week 1

// gradle -PmainClass=exercises01.CounterThreads2Covid run
package exercises01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CounterThreads2Covid {
    Lock l = new ReentrantLock();
    long counter = 0;
    final long PEOPLE = 10_000;
    final long MAX_PEOPLE_COVID = 15_000;

    public CounterThreads2Covid() {
        try {
            Turnstile turnstile1 = new Turnstile();
            Turnstile turnstile2 = new Turnstile();

            turnstile1.start();
            turnstile2.start();
            turnstile1.join();
            turnstile2.join();

            System.out.println(counter + " people entered");
        } catch (InterruptedException e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CounterThreads2Covid();
    }

    // Initial implementation
    public class Turnstile extends Thread {
        public void run() {
            for (int i = 0; i < PEOPLE; i++) {
                counter++;
            }
        }
    }

    // t1(temp = 10),t2(temp = 10),t1(counter = 11), t2(counter = 11)

    // Solution
    // public class Turnstile extends Thread {
    // public void run() {
    // for (int i = 0; i < PEOPLE; i++) {
    // l.lock();
    // try {
    // if (counter < MAX_PEOPLE_COVID) // start of critical section
    // counter++;// end of critical section
    // } finally {
    // l.unlock();
    // }
    // }
    // }
    // }
}
