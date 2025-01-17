package exercises04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class SemaphoreImpTest {

    @Test
    public void testSemaphoreHangsWhenCapacityIsZero() throws InterruptedException {
        final int capacity = 0;
        final SemaphoreImp semaphore = new SemaphoreImp(capacity);

        semaphore.release();

        Thread thread1 = new Thread(() -> {
            try {
                semaphore.acquire();
                fail("Thread was able to acquire the semaphore with capacity 0, which indicates an issue.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();

        thread1.join();
    }
}
