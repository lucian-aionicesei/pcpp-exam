// For week 6
// raup@itu.dk * 2023-10-20
package exercises06;

import java.util.concurrent.atomic.AtomicReference;

// Treiber's LockFree Stack (Goetz 15.4 & Herlihy 11.2)
class LockFreeStack<T> {
    AtomicReference<Node<T>> top = new AtomicReference<Node<T>>(); // Initializes to null

    public void push(T value) {
        Node<T> newHead = new Node<T>(value);
        Node<T> oldHead;
        do {
            oldHead = top.get(); // T1
            newHead.next = oldHead; // T2
        } while (!top.compareAndSet(oldHead, newHead)); // T3

    }

    /*
     * push() has one linearization point:
     * -> T3 – if successfully executed, the element has been pushed to the stack
     * Correctness (informal but systematic, tries to cover all branches):
     * -> If two threads execute push() concurrently before the oldHead is updated,
     * then only one succeeds in executing T3 (and possibly auipdating the oldHead).
     * The other fails and repeats the do loop.
     * -> If a thread executes push() after another thread updated the oldHead, then
     * T3 fails and it repeats the loop.
     */

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

    /*
     * pop() has two linearization points:
     * -> T4 - if the stack is empty. After this execution, the evaluation of T5 is
     * determined and whether the method will return null.
     * -> T7 - if successfully executed, the element has been removed from the stack
     * and the method will return the value.
     * Correctness (informal but systematic, tries to cover all branches):
     * -> If two threads execute pop() concurrently and the stack is not empty (T5),
     * then T7 succeeds for only one of them. The other tries again by repeating the
     * loop.
     * -> If a thread executes pop() after another thread updated the oldHead, then
     * T7 fails and repeats the loop.
     * -> If a thread executes pop() while another thread executed T3 - push(), then
     * T7 fails and the pop() thread repeats the loop (tries again), gets the new
     * value of head and tries to update it.
     */

    // class for nodes
    private static class Node<T> {
        public final T value;
        public Node<T> next;

        public Node(T value) {
            this.value = value;
            this.next = null;
        }
    }
}
