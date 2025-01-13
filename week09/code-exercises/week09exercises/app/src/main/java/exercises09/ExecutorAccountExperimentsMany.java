// jst@itu.dk * 04/10/2024
package exercises09;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ExecutorAccountExperimentsMany {

  static final int N = 10;
  static final int NO_TRANSACTION = 5;
  static final int NO_THREADS = 10;
  static final Account[] accounts = new Account[N];
  static final Thread[] threads = new Thread[NO_THREADS];
  static Random rnd = new Random();

  private static ForkJoinPool pool;

  public static void main(String[] args) {
    new ExecutorAccountExperimentsMany();
  }

  public ExecutorAccountExperimentsMany() {
    for (int i = 0; i < N; i++) {
      accounts[i] = new Account(i);
    }
    // for (int i = 0; i < NO_THREADS; i++) {
    // try {
    // (threads[i] = new Thread(() -> doNTransactions(NO_TRANSACTION))).start();
    // } catch (Error ex) {
    // System.out.println("At i = " + i + " I got error: " + ex);
    // System.exit(0);
    // }
    // }
    // for (int i = 0; i < NO_THREADS; i++) {
    // try {
    // threads[i].join();
    // } catch (Exception dummy) {
    // }
    // ;
    // }

    // while (!exec.isShutdown()) {
    // exec.execute(new Runnable() {
    // public void run() {
    // doNTransactions(NO_TRANSACTION);
    // }
    // });
    // }

    // Runnable task = () -> doNTransactions(NO_TRANSACTION);
    // while (!exec.isShutdown()) {
    // exec.execute(task);
    // }

    pool = new ForkJoinPool(NO_THREADS);

    // List to keep track of Futures for each task
    List<Future<?>> futures = new ArrayList<>();

    // Submit transactions as tasks to the ForkJoinPool and collect Futures
    for (int i = 0; i < NO_THREADS; i++) {
      Future<?> future = pool.submit(() -> doNTransactions(NO_TRANSACTION));
      futures.add(future);
    }

    // Wait for all tasks to complete by checking each Future
    try {
      for (Future<?> future : futures) {
        try {
          future.get(); // This will block until the task is complete
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace(); // Log the exception, but continue to the next task
        }
      }
    } finally {
      // Ensure pool is always shut down
      pool.shutdown();
      try {
        if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
          System.out.println("Forcing shutdown as tasks didn't finish in time");
          pool.shutdownNow(); // Force shutdown if tasks didn't complete in time
        }
      } catch (InterruptedException e) {
        pool.shutdownNow();
      }
    }

  }

  private static void doNTransactions(int noTransactions) {
    for (int i = 0; i < noTransactions; i++) {
      long amount = rnd.nextInt(5000) + 100;
      int source = rnd.nextInt(N);
      int target = (source + rnd.nextInt(N - 2) + 1) % N; // make sure target <> source
      doTransaction(new Transaction(amount, accounts[source], accounts[target]));
    }
  }

  private static void doTransaction(Transaction t) {
    System.out.println(t);
    t.transfer();
  }

  static class Transaction {
    final Account source, target;
    final long amount;

    Transaction(long amount, Account source, Account target) {
      this.amount = amount;
      this.source = source;
      this.target = target;
    }

    public void transfer() {
      Account min = accounts[Math.min(source.id, target.id)];
      Account max = accounts[Math.max(source.id, target.id)];
      synchronized (min) {
        synchronized (max) {
          source.withdraw(amount);
          try {
            Thread.sleep(50);
          } catch (Exception e) {
          }
          ; // Simulate transaction time
          target.deposit(amount);
        }
      }
    }

    public String toString() {
      return "Transfer " + amount + " from " + source.id + " to " + target.id;
    }
  }

  static class Account {
    // should have transaction history, owners, account-type, and 100 other real
    // things
    public final int id;
    private long balance = 0;

    Account(int id) {
      this.id = id;
    }

    public void deposit(long sum) {
      balance += sum;
    }

    public void withdraw(long sum) {
      balance -= sum;
    }

    public long getBalance() {
      return balance;
    }
  }

}
