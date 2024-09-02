# Practical Concurrent and Parallel Programming 2024

## (PCPP) E2024

In this MSc course, you learn how to write correct and efficient concurrent and parallel software, primarily using Java, on standard shared-memory multicore hardware. The course covers basic mechanisms such as threads, locks and shared memory as well as more advanced mechanisms such as parallel streams for bulk data, and lock-free data structures with compare-and-swap. It covers concepts such as atomicity, safety, liveness and deadlock. It covers how to measure and understand the performance and scalability of parallel programs. It covers methods to find bugs in concurrent programs. Also, we will take a look at some of the concurrency mechanisms proposed for other programming languages than Java.


## Weekly assignments, slides, code, etc.

Each week we will update this repository with the material used in this week.


## Lectures Schedule (Tentative)

| Lecture | Week | Topic                                              | Speaker (tentative)      |
|---------|------|----------------------------------------------------|--------------------------|
| 1       | 35   | [Intro to concurrency & Mutual exclusion](week01/) | Jørgen+Raúl              |
| 2       | 36   | [Shared memory I](week02/)                         | Raúl                     |
| 3       | 37   | [Shared memory II](week03/)                        | Raúl                     |
| 4       | 38   | Testing & Verification                             | Raúl                     |
| 5       | 39   | Lock free data structures                          | Raúl                     |
| 6       | 40   | Linearizability                                    | Raúl                     |
| 7       | 41   | Guest lecture                                      | Viet (CTO @ Hypefactors) |
|         | 42   | *No Lecture: Fall break*                           | --                       |
| 8       | 43   | Performance measurements                           | Jørgen                   |
| 9       | 44   | Performance and scalability                        | Jørgen                   |
| 10      | 45   | Parallel Streams+React                             | Jørgen                   |
| 11      | 46   | Java Networking & Intro to Erlang (TBD)            | Jørgen+Raúl              |
| 12      | 47   | Message passing I                                  | Raúl                     |
| 13      | 48   | Message Passing II                                 | Raúl                     |
| 14      | 49   | Exam prep                                          | Jørgen+Raúl              |


## Oral Feedback Sessions Schedule

The schedule below shows the weeks when we will have [oral feedback sessions](general-info/assignment-submissions-and-oral-feedback.md).
Note that this schedule is preliminary and subject to changes.
More precisely, **the dates of the oral feedback sessions will not change**, but the exercises included in each session may vary.

Remember that when you book a slot in the [Oral Feedback Scheduler in LearnIT](https://learnit.itu.dk/mod/organizer/view.php?id=206699), the **slot is recurrent, i.e., it is for all the weeks below in the day of the week and time that you selected**.

| Course week | Calendar Week | Exercises                                                 |
|-------------|---------------|-----------------------------------------------------------|
| 3           | 37            | Intro to concurrency & Mutual exclusion + Shared memory I |
| 5           | 39            | Shared memory II  + Testing & Verification                |
| 8           | 43            | Lock-free data structure + Linearizability                |
| 10          | 45            | Performance measurements + Performance and scalability    |
| 12          | 47            | Parallel Streams/React + Java Networking & Erlang Intro   |
| 14          | 49            | Message passing I + Message passing II                    |
