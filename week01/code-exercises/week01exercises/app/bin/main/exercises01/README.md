# Week 1 - answers
## Exercise 1.1

1. Count is variates between 10_000_000 and  20_000_000, but it should be 20_000_000. We did not get the expected output.
   
2. It is not guaranteed that the output is always 200. A data race occures, due to two concurrent threads accessing a shared memory location.
	* The statement ```count = count + 1;``` is not atomic
	* Some interleavings result in threads reading state (outdated) data
	* Consequently, the program has race conditions that result in incorrect outputs
	  
3. Changing the notation would not make a difference, the statement is not atomic.
   
4. We identify the critical section to be "count = count + 1;" because it is subject to data race: Multiple threads may access and update the same value at the same time. By wrapping the critical section with the .lock() and .unlock() methods we can ensure mutual exclusion, that is, critical sections of different threads do not overlap.

```java	
public void increment() {
	l.lock();
	count = count + 1;
	l.unlock(); 
}
```
   
5. Yes, "Critical sections should cover the parts of the code handling shared memory". In our case it is the statement ```count = count + 1;``` namely count the shared value.

## Exercise 1.2

2. A race condition occurs: "*A race condition occurs when the correctness of a computation depends on the relative timing or interleaving of multiple threads by the runtime; in other words, when getting the right answer relies on lucky timing*" (pg.41). The critical section related to printing the values is currently accesed by multiple threads at the same time. We need to ensure mutual exclusion.

3. By introducing the ReentrantLock object and wrapping the critical section in the .lock() and .unlock() methods we can ensure mutual exclusion, that is, multiple threads cannot access the same critical section at the same time. By using this approach we can also ensure absence of deadlock.

```java 
class Printer {

	public void print() {
		l.lock();
		try {
			System.out.print("-"); // start of critical section
			try {
				Thread.sleep(50);
			} catch (InterruptedException exn) {
		}
		System.out.print("|"); // end of critical section
	} finally {
		l.unlock();
	}
	}
}
```

## Exercise 1.3

1. 
```java 
public class Turnstile extends Thread {
	public void run() {
		for (int i = 0; i < PEOPLE; i++) {
			l.lock();
			if (counter < MAX_PEOPLE_COVID) // start of critical section
				counter++;// end of critical section
			l.unlock();
		}
	}
}
```

2. The `counter` variable is shared by multiple threads. The identified critical section is where the `counter` variable is being read and updated. It is prone to data racing, therefore we must ensure **mutual exclusion**. We do this by wrapping the critical section in the Lock methods, so that there won't be an unexpected behaviour in the execution of the code. Additionaly, a counter won't exceed 15 000 due to upper bound, which is adjusted by **MAX_PEOPLE_COVID** within a **if (counter < MAX_PEOPLE_COVID)**.

## Exercise 1.4

1. Nygaards and Goetz motivations for concurrency, we understand to be interlinked. Each motivation highlights aspects of concurrency in different ways but agree on why the concept is useful and important. 

	E.g. Nygaards definition of "inherent" being dealing with interfaces and I/O relates to Goetz "Resource utilization" where he mentions waiting for external operations etc. 

	Every modern application running concurrency would fit within the bounds of both of these descriptions. 
	
2. **Inherent:** A web application can execute multiple tasks at the same time, for instance loading data, and listening to events such as the button for opening a "burger menu" , displaying a loading animation while data is processing or waiting for a response.

	**Exploitation:** Having multiple applications running at the same time on our operating system.

	**Hidden** An example of such behavior is when the frontend receives user input while the backend handles database interactions. Instead of having a single program, we distribute the functionality of the system across multiple layers, thereby fulfilling the concept of convenience.


# Challenging section

## Exercise 1.1.6 - challenging

The javap output shows that all three versions of increment are composed of multiple instructions, confirming that they are not atomic. This directly supports the explanation in part 3,that these operations are not thread-safe and require proper synchronization to prevent race conditions
## Exercise 1.1.7 - challenging

The expected output is not predictable due to the race condition caused by the decrement() method's lack of synchronization. The value will likely be around zero but can deviate significantly in either direction depending on how the threads interleave.

```java

public void decrement() {

lock.lock();

	try {
		count = count - 1;
	} finally {
		lock.unlock();
	}
}

```

By adding a ReentrantLock to the decrement() method, I ensure that both the increment() and decrement() methods are thread-safe. This synchronization prevents race conditions, leading to a consistent and predictable result.

## Exercise 1.1.8 - challenging

Using ReentrantLock ensures mutual exclusion and establishes a proper happens-before relationship between threads. This guarantees that each increment operation is executed correctly, leading to an output of exactly 20 million. No other output is possible due to the memory visibility and ordering guarantees provided by ReentrantLock.

## Exercise 1.1.9 - challenging

Minimum Value for counts = 3: The minimum value printed can be as low as 3 due to race conditions.The reason I was consistently getting 6 with counts = 3 is likely due to the small number of operations, which reduces the chances of an interleaving that results in lost updates. Here's a breakdown: 

Effect of Larger counts: The minimum value can be quite close to 2 * counts for large numbers, but due to race conditions, it will never be exactly 2 * counts.

Interleaving Leading to Minimum Value: An interleaving where threads read the same value of count, increment it, and write back the same incremented value can lead to lost updates and the minimum value being printed.
## Exercise 2.1.3 - challenging

With ReentrantLock (Fair Mode): It is possible to ensure the absence of starvation by using ReentrantLock with the fairness policy enabled (new ReentrantLock(true)). This way, the longest-waiting thread gets the lock next, preventing starvation.