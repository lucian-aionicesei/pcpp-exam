
# 6.1
### 6.1.1.
<em>Is this execution sequentially consistent? If so, provide a sequential execution that satisfies the standard specification of a sequential FIFO queue. Otherwise, explain why it is not sequentially consistent.</em>

A: ---------------|q.enq(x)|--|q.enq(y)|->

B: ---|q.deq(x)|------------------------->

**Answer:**

This is seq consistent, because Program order is preserved (for each thread). Possible sequential execution:

**<q.enq(x),q.enq(y),q.deq(x)>**

### 6.1.2.
<em>Is this execution (same as above) linearizable? If so, provide a linearization that satisfies the standard specification of a sequential FIFO queue. Otherwise, explain why it is not linearizable.</em>

A: ---------------|q.enq(x)|--|q.enq(y)|->

B: ---|q.deq(x)|------------------------->

**Answer:**

Not linearizable, because we have to preserve real time order, the q.deq(x) cannot happen before q.enq(x).

### 6.1.3.
<em>Is this execution linearizable? If so, provide a linearization that satisfies the standard specification of a sequential FIFO queue. Otherwise, explain why it is not linearizable.</em>

A: ---|      q.enq(x)          |-->

B: ------|q.deq(x)|--------------->

**Answer:**

Yes it is linearizable, as the linearization point
of q.eng(x) can be before q.deq(x):

**<q.enq(x),q.deq(x)>**

### 6.1.4.
<em>Is this execution linearizable? If so, provide a linearization that satisfies the standard specification of a sequential FIFO queue. Otherwise, explain why it is not linearizable.</em>

A: ---|q.enq(x)|-----|q.enq(y)|-->

B: --|       q.deq(y)          |->

**Answer:**

It is not linearizable, as we must deq x before y and this never happens. Therefore the program order of the FIFO is not respected.

### 6.1.5.
<em>Is this execution sequentially consistent?  If so, provide a sequential execution that satisfies the standard specification of a sequential FIFO queue. Otherwise, explain why it is not sequentially consistent.</em>

A: -|p.enq(x)|------------|q.enq(x)|------------|p.deq(y)|------------>
B: ------------|q.enq(y)|------------|p.enq(y)|------------|q.deq(x)|->

**Answer**

No, because there is no possible reordering of the executions that satisfies the standard specification of a FIFO queue.

## 6.2

```java
public void push(T value) {
	Node<T> newHead = new Node<T>(value);
	Node<T> oldHead;
	do {
		oldHead = top.get(); // T1
		newHead.next = oldHead; // T2
		} while (!top.compareAndSet(oldHead, newHead)); // T3
}
```

1. **push() has one linearization point:**
	- T3 – if successfully executed, the element has been pushed to the stack
	**Correctness (informal but systematic, tries to cover all branches):**
	- If two threads execute push() concurrently before the oldHead is updated, then only one succeeds in executing T3 (and possibly updating the oldHead). The other fails and repeats the do loop.
	- If a thread executes push() after another thread updated the oldHead, then T3 fails and it repeats the loop.

```java
public T pop() {
	Node<T> newHead;
	Node<T> oldHead;
	do {
		oldHead = top.get(); // T4
		if (oldHead == null) { // T5
			return null;
		}
		newHead = oldHead.next; // T6
		} while (!top.compareAndSet(oldHead, newHead)); // T7

	return oldHead.value;
}
```

**pop() has two linearization points:**
	- T4 - if the stack is empty. After this execution, the evaluation of T5 is determined and whether the method will return null.
	- T7 - if successfully executed, the element has been removed from the stack and the method will return the value.
	**Correctness (informal but systematic, tries to cover all branches):**
	- If two threads execute pop() concurrently and the stack is not empty (T5), then T7 succeeds for only one of them. The other tries again by repeating the loop.
	- If a thread executes pop() after another thread updated the oldHead, then T7 fails and repeats the loop.
	- If a thread executes pop() while another thread executed T3 - push(), then T7 fails and the pop() thread repeats the loop (tries again), gets the new value of head and tries to update it.

4.  No, the tests in part 2 and 3 do not cover all linearization points, for instance T4 - in case the stack is empty and we try to pop().

### 6.3

1. `writerTryLock`
	- it is not *wait-free* because it does not guarantee that every thread will complete it's operations in a finite number of steps. For example: if two threads are trying to acquire the writer lock, while `holders` is empty, only one will eventually succeed while the other will have to go through the do loop, to try and acquire the writer lock again.
	- it is *lock-free* because it guarantees that at least one thread will make progress in a finite number of steps. This is ensured by the `compareAndSet` method, which makes sure that al least one thread may acquire the lock. If the lock is already held by another writer or a list or readers, then the method will return a boolean value `false.`
	- It is *obstruction-free* because it guarantees that a thread will complete its operation if it **runs in isolation** without interference from other threads. More specifically if the `holders` value is `null` and no other thread is trying to acquire the lock, the current thread will successfully acquire the lock.

	`writerUnlock`
	- it is *wait-free* because it guarantees that every thread will complete it's operations in a finite number of steps. At the moment of execution if the `holders` is not the current writer thread it will end up in throwing an exception. By the time the CAS method is executed, we are sure that the `oldHolders` is the current writer thread which guarantees that the method will return successfully.
	- it is *lock-free* because it guarantees that at least one thread will make progress in a finite number of steps. If the current thread is a writer that holds the lock, then the CAS method will return `true`, otherwise it will throw an error.
	- It is *obstruction-free* because it guarantees that a thread will complete its operation if it **runs in isolation** without interference from other threads. If the writers lock has been acquired then the method returns successfully, otherwise it will throw an error.

	`readerTryLock`
	- it is **not wait-free**, because a thread may keep looping in the `do-while` loop indefinitely if other threads are holding the write lock. If a `Writer` thread holds the lock, the `readerTryLock` will return `false`, preventing the reader from acquiring the lock. Therefore, there is no guarantee that a thread will always complete its execution in a finite number of steps.
	- it is **lock-free**. The `compareAndSet` ensures that at least one thread will eventually make progress. If a thread fails to acquire the lock due to another thread holding the lock (either a writer or a list of readers), the `do-while` loop continues. However, if a writer releases the lock, a reader thread can eventually succeed in acquiring the lock. This makes the operation lock-free, as at least one thread will succeed in acquiring the lock in a finite number of steps.
	- it is **obstruction-free**, because if the current thread runs in isolation, meaning no writer holds the lock or interferes, the thread will successfully acquire the read lock and exit the `do-while` loop. As long as no other thread modifies the `holders` atomic reference, the `compareAndSet` will succeed, making the operation obstruction-free.

	`readerUnlock`
	- it is **wait-free**, because the method will complete in a finite number of steps regardless of the state of other threads. The thread either successfully removes itself from the `ReaderList` or throws an exception if it does not hold the read lock. There's no looping involved here, so every thread is guaranteed to complete the operation in a finite number of steps.
	- it is **lock-free**, because if the current thread holds the read lock, it will make progress by removing itself from the `ReaderList`. Even if other threads are executing, the `compareAndSet` guarantees that at least one thread (the current one) will make progress.
	- it is **obstruction-free**, because if the current thread runs without interference from other threads, it will successfully remove itself from the `ReaderList`. The `compareAndSet` will succeed if there are no concurrent modifications, allowing the current thread to complete its operation.

### 6.4
The current implementation is not linearizable because it allows deq to observe an incomplete state of the queue where a slot has been reserved but the value hasn't yet been written.
The linearization points should be adjusted to ensure that enq fully completes (value written) before the queue is updated and ready for deq

One way to fix this issue would be to change the linearization point for enq to after the value has been written, like so:
```java
items[slot] = x;
while (!tail.compareAndSet(slot, slot + 1));
}
```
This ensures that deq never sees an uninitialized (null) slot and would satisfy the FIFO property of a queue.

### 6.4

The program/codebase is not correctly synchronized because there is a data race on the variable b. There is no happens-before relationship between the read of b in (3) (Thread 1) and the write to b in (6) (Thread 2).
The lock synchronization does not cover these accesses to b, meaning they could potentially be executed concurrently, leading to unpredictable results.
Moreover, the problem is also in the timing of the reads and writes to the shared variable b.
Since the read of b in Thread 1:
```java
x=b; // (3)
```
happens before Thread 2 writes to b,
```java
b=1; // (6)
```
Thread 1 could read a stale value of b (whatever its value was before the write at (6)).
