package exercises03;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BoundedBuffer<T> implements BoundedBufferInteface<T> {

    // private final int capacity;
    private final Queue<T> buffer;
    private final Semaphore items; // Tracks available items (for take)
    private final Semaphore spaces; // Tracks available spaces (for insert)
    private final Semaphore mutex; // Ensures mutual exclusion - mutex

    public BoundedBuffer(int capacity) {
        // this.capacity = capacity;
        this.buffer = new LinkedList<>();
        this.items = new Semaphore(0); // Initially no items in the buffer
        this.spaces = new Semaphore(capacity); // Initially all buffer spaces are available
        this.mutex = new Semaphore(1); // Mutual exclusion semaphore
    }

    public T take() throws Exception {
        items.acquire(); // Wait for an available item
        mutex.acquire(); // Enter critical section (mutual exclusion)

        T elem = buffer.poll();
        System.out.println("Consumer: " + elem);
        Thread.sleep(250);

        mutex.release(); // Exit critical section
        spaces.release(); // Signal that there is now an additional space

        return elem;
    };

    public void insert(T elem) throws Exception {
        spaces.acquire(); // Wait for an available space
        mutex.acquire(); // Enter critical section (mutual exclusion)

        buffer.add(elem);
        System.out.println("Producer: " + elem);
        Thread.sleep(250);

        mutex.release(); // Exit critical section
        items.release(); // Signal that there is now an additional item
    };

    public static void main(String[] args) {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(10);
        new Thread(() -> {
            for (int i = 0; i < i + 1; i++) {
                try {
                    // Random random = new Random();
                    buffer.insert(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // while (true)
        }).start();
        new Thread(() -> {
            for (int i = 0; i < i + 1; i++) {
                try {
                    // Random random = new Random();
                    buffer.take();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
