# Week 2 - answers
## Exercise 2.1

1. See [ReadWriteMonitorV1.java](./ReadWriteMonitorV1.java) or [ReadWriteMonitorV2.java](./ReadWriteMonitorV2.java) located in the current directory.

2. We can argue that our solution is fair towards the writer threads, because once the writer has acquired the lock, no additional reader can increment `readAcquires`, so no additional reader can acquire the read lock, and eventually all the readers currently holding the lock will release it, allowing the writer to proceed.

## Exercise 2.2

1. **MutableInteger** is not thread-safe because the `value` field is accessed from both `get` and `set` without synchronization. Moreover it is susceptible to stale values: if one thread calls `set`, other threads calling `get` may or may not see the updated value. As in our case, the main thread sets the `value` to 42, however the `t` thread was not made aware of those changes. The `t` thread still reads `value` as 0 and therefore executes a never ending loop.
2. By using the Java Intrinsic Locks (`synchronized`)  on the methods of the `MutableInteger` we can ensure not only **mutual exclusion** of a critical section, but also memory visibility: *To ensure that all threads see the most up-to-date values of shared mutable variables, the reading and writing threads must **synchronize** on a common lock.*
	Implementation: [NewMutableInteger.java](./NewMutableInteger.java)
3. It would not, as the get function is the relevant function with which the `t` thread reads the variable, it is precisely get() that must be synchronous. The thread terminates fine with only get() being defined as synchronous.
4. It is good enough to make the `value` field volatile, to ensure that thread `t` always executes in our case. The variables contents is pushed to the register, even without the main function finishing and threads will be able to access the updated value.By using volatile variables we can ensure visibility, and  however this is not enough to ensure that the `this.value = value` statement is atomic: *Accessing a volatile variable performs no locking and so cannot cause the executing thread to block.* In our case the `set` method is only accessed by one thread at a time.
	Implementation: [NewMutableIntegerV2.java](./NewMutableIntegerV2.java)

## Exercise 2.3

1. There are clearly race conditions, as the returns of the two threads overlap their interleavings on the sum variable. As such the results never return the expected 2.000.000

2. - As written in Goetz: 
"Static initializers are executed by the JVM at class initialization time; because of internal synchronization in the JVM, this mechanism is guaranteed to safely publish any objects initialized in this way"

Since only one of the functions are defined using static, but both are used on the same variable, the results of the calculations are not safely published.

3. We create a locking object. Either specified as a synchronized object or by using a Java Lock and then we synchronize the functions on this object or this lock, before they are allowed to use sum. This ensures mutual exclusion.
	Implementation: [TestLocking0.java](./TestLocking0.java)

4. There are not race conditions if the synchronized keyword is removed the the sum() method, as we are now synchronizing on our lock object instead.