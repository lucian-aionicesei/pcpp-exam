package exercises08;
// Microbenchmarks for small object creation, Thread creation, thread

// start, thread execution and join, taking an uncontended lock.
// sestoft@itu.dk * 2014-09-10, 2015-09-15
// jst@itu.dk * 2024-09-09

// gradle -PmainClass=exercises08.TestTimeThreads run

import java.util.concurrent.atomic.AtomicInteger;

import benchmarking.Benchmark;
import benchmarking.Benchmarkable;

public class TestTimeThreadsMark7 {

  public static void main(String[] args) {
    new TestTimeThreadsMark7();
  }

  public TestTimeThreadsMark7() {
    Benchmark.SystemInfo();
    System.out.println("Mark 7 measurements");
    final Point myPoint = new Point(42, 39);
    Benchmark.Mark7("Point creation",
        i -> {
          Point p = new Point(i, i);
          return p.hashCode();
        });
    final AtomicInteger ai = new AtomicInteger();
    Benchmark.Mark7("Thread's work",
        i -> {
          for (int j = 0; j < 1000; j++)
            ai.getAndIncrement();
          return ai.doubleValue();
        });
    Benchmark.Mark7("Thread create",
        i -> {
          Thread t = new Thread(() -> {
            for (int j = 0; j < 1000; j++)
              ai.getAndIncrement();
          });
          return t.hashCode();
        });
    Benchmark.Mark7("Thread create start",
        i -> {
          Thread t = new Thread(() -> {
            for (int j = 0; j < 1000; j++)
              ai.getAndIncrement();
          });
          t.start();
          return t.hashCode();
        });
    Benchmark.Mark7("Thread create start join",
        i -> {
          Thread t = new Thread(() -> {
            for (int j = 0; j < 1000; j++)
              ai.getAndIncrement();
          });
          t.start();
          try {
            t.join();
          } catch (InterruptedException exn) {
          }
          return t.hashCode();
        });
    System.out.printf("ai value = %d%n", ai.intValue());
    final Object obj = new Object();
    Benchmark.Mark7("Uncontended lock",
        i -> {
          synchronized (obj) {
            return i;
          }
        });
  }
}
