4.1.1)

Describe the interleavings of buggy add() :

The interleavings in the add() method occur because the operation is not atomic and the method lacks proper synchronization. Multiple threads can read a stale state of the set, leading them to believe that the element 1 has not been added, even though another thread may be in the process of adding it. As a result, two or more threads may attempt to add the same element concurrently, causing the set to contain multiple copies of the same element (even though sets should not allow duplicates). This race condition is caused by the interleaving of operations, where the check for the element’s existence and the insertion of the element are performed by different threads in an overlapping manner.



4.1.2)

Describe the interleavings of buggy remove() :

The interleavings in the remove() method occur because the operation is not atomic, and multiple threads can perform stale reads on the size of the set. This leads to a race condition where two or more threads may incorrectly think the set contains the element they are trying to remove. As a result, the same element can be removed multiple times, causing the size of the set to decrease below zero or other inconsistent behavior. The race condition arises from the interleaving of operations where threads check the size, add, or remove elements in a non-synchronized manner.

4.1.3)

Implement fixes to the errors you found in the previous exercises. Explain your solution :
Our solution fixes the problems by implementing a lock object, and synchronizing the add and remove functions on this object, to ensure mutual exclusion

4.1.4)

ConcurrentIntegerSetLibrary. Discuss the results. :
Using the concurrent implementations of collections in Java achieves the same goal as implementing your own version of mutual exclusion on non-concurrent collections

4.1.5)

Does failures on tests prove that a collection is not thread-safe? :
Not necessarily. We may have implemented faulty tests, so it's not "proved".

4.1.6)

Does passing tests prove thread-safety? :

While passing the tests for add() and remove() increases our confidence that the collection behaves correctly under certain concurrent conditions, it does not definitively prove that the collection is thread-safe. Thread safety issues can arise from untested interleavings or scenarios that weren't covered in the tests. Testing is a tool for finding bugs, not for proving the absence of them. Therefore, we cannot guarantee that the collection is fully thread-safe based solely on the fact that the tests passed.


4.1.7)
The size() method can return an incorrect value due to the non-atomic nature of the operation. In a concurrent environment, while one thread is traversing the collection to count the elements, another thread can modify the collection (such as adding or removing elements). This leads to stale reads, where the thread calling size() sees an outdated view of the collection and returns a count that does not reflect the current state.A

Although, I tried to run it multiple times with 100 threads, I couldn't get any test to fail, so I assume that we need to have first of all a computational power to detect an interleaving.

4.2.1)

Provide an interleaving showing a counterexample of the property, and explain why the interleaving violates the property :
The release function allows for a state that can be less than 0, which means the allowance for threads in the semaphore can move towards infinity. Therefore the implementation violates the specification of "capacity".

4.2.3)

Write a test that triggers the above interleaving. Explain it :
SemaphoreImpTest.java triggers the interleaving mentioned above by first having the main thread release a spot in a semaphore initialized with 0 capacity. Then it starts a thread which attempts to acquire a spot. This succeeds and then the test fails.

The interleaving shows how multiple threads can incorrectly acquire the semaphore when it should be blocked by the capacity.
The test synchronizes the threads to create contention on the semaphore, trying to reproduce this interleaving.
By adjusting the number of threads and repetition, this test should detect violations in the semaphore’s capacity property.
