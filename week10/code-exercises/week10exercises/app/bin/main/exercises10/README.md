# Week10 exercises answers

## Exercise 10.1

Answers for 10.1.1, 10.1.2, 10.1.4 and 10.1.5 are to be found in the .txt files.
10.1.3 -> [PrimeCounntingPerf.java](./PrimeCounntingPerf.java)

## Exercise 10.2

Answers are found in -> [TestWordStream.java](./TestWordStream.java)

## Exercise 10.3

1. When we use a sequential stream the main thread is doing all the work. It waits for current iteration to complete and then works on the next iteration. In case of parallel stream, 5 threads are spawned simultaneously, and it uses ForkJoinPool internally to create an manage threads. Parallel streams create `ForkJoinPool` instance via static `ForkJoinPool.commonPool()` method.

2. There seems to be a direct relation with the number of cores, since there are only about 8 worker threads and this computer has 8 cores.

3.

After running `isPrime()`method, a main thread occurred multiple times, which might be caused by running more computational demanding method. For heavier tasks, like `isPrime`, the `main` thread is often involved in the workload for a longer duration because each task requires more processing time, causing the pool to consistently use all available threads.

Implemented in [Java8ParallelStreamMain.java](./Java8ParallelStreamMain.java)

## Exercise 10.4

1. As a JavaStream the above operation "filters" only the words that have the length bigger than 5 and presumably accumulates these words in an ArrayList or counts them.
   As a RxJava statement, the operation is triggered whenever there has been some user input, with the condition that the length of the input string is bigger than 5.
2. As a java stream, running these operations might output 2 different results: the first one contains all the words that are bigger than 5 and the second on all the words that are bigger than 10. It will do these operations sequentially.
   As a RxJava statement, considering that the observers are "listening" on the same source, there is a possibility that these events will be triggered at the same time, if for example there is an input string with length > 10. If the input is bigger than 5 but smaller or equal to 10 only the first statement will be executed.

JavaStream

- In Java Streams, we can only consume a stream once. Streams are single-use, meaning that after applying a terminal operation (like `sink()` here), the stream is considered "closed" and cannot be reused.
- Therefore, the code shown would not work directly because the `source()` stream would be exhausted after the first `sink()` call.

RxJava
In RxJava, the scenario works differently. RxJava operates with observables, which emit data over time and can be subscribed to multiple times.

- In this case, both `filter` operations (`w.length() > 5` and `w.length() > 10`) can run concurrently or independently, and each can emit a distinct result to its respective `sink()` or `subscribe()` action.
