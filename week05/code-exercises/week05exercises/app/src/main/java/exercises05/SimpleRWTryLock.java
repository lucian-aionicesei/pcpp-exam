
package exercises05;

import java.util.concurrent.atomic.AtomicReference;

import org.w3c.dom.Node;

class SimpleRWTryLock implements SimpleRWTryLockInterface {

    AtomicReference<Holders> holders = new AtomicReference<>();

    public boolean readerTryLock() {

        final Thread current = Thread.currentThread();

        Holders oldHolders;
        ReaderList updatedReadersList;
        do {
            oldHolders = holders.get();
            if (oldHolders instanceof Writer) {
                return false;
            }

            if (oldHolders == null) {
                updatedReadersList = new ReaderList(current, null);
            } else {
                updatedReadersList = new ReaderList(current, (ReaderList) oldHolders);
            }

        } while (!holders.compareAndSet(oldHolders, updatedReadersList));

        return true;
    }

    public void readerUnlock() {
        final Thread current = Thread.currentThread();

        Holders oldHolders;
        ReaderList updatedReadersList;

        do {
            oldHolders = holders.get();

            if (oldHolders instanceof Writer || oldHolders == null) {
                throw new IllegalMonitorStateException("Current thread does not hold the read lock!");
            }

            ReaderList currentReaders = (ReaderList) oldHolders;

            if (currentReaders.contains(current)) {
                updatedReadersList = currentReaders.remove(current);
            } else {
                throw new IllegalMonitorStateException("Current thread does not hold the read lock!");
            }
        } while (!holders.compareAndSet(oldHolders, updatedReadersList));

        return;
    }

    // writerTryLock is called by a thread that tries to obtain a write lock
    public boolean writerTryLock() {

        final Thread current = Thread.currentThread();

        Holders oldHolders;
        Writer newWriter = new Writer(current);
        do {
            oldHolders = holders.get();

            if (oldHolders != null) {
                return false;
            }
        } while (!holders.compareAndSet(null, newWriter));

        return true;
    }

    public void writerUnlock() {

        final Thread current = Thread.currentThread();

        Holders oldHolders;

        do {
            oldHolders = holders.get();

            if (oldHolders instanceof ReaderList) {
                throw new IllegalMonitorStateException("Current thread does not hold the write lock!");
            }

            if (oldHolders instanceof Writer) {
                Writer currentWriter = (Writer) oldHolders;
                if (currentWriter.thread != current) {
                    throw new IllegalMonitorStateException("Current thread does not hold the write lock!");
                }
            } else {
                throw new IllegalMonitorStateException("Current thread does not hold the write lock!");
            }

        } while (!holders.compareAndSet(oldHolders, null));

        return;
    }

    // // Challenging 5.2.7: You may add new methods

    private static abstract class Holders {
    }

    private static class ReaderList extends Holders {
        private final Thread thread;
        private final ReaderList next;

        public ReaderList(Thread thread, ReaderList next) {
            this.thread = thread;
            this.next = next;
        }

        // Contains method to check if a thread exists in the linked list
        private boolean contains(Thread t) {
            // Base case: check if the current node's thread matches the target thread
            if (this.thread == t) {
                return true;
            }

            // Recursive step: check the next node, if it exists
            if (this.next != null) {
                return this.next.contains(t);
            }

            // If no match found and no more nodes, return false
            return false;
        }

        // Remove method to remove a thread from the linked list
        private ReaderList remove(Thread t) {
            // Base case: if the current node's thread matches, skip it by returning the
            // next node
            if (this.thread == t) {
                return this.next; // Skip the current node
            }

            // Recursive step: if the next node is not null, try to remove the thread from
            // the next node
            if (this.next != null) {
                // Recursively call remove on the next node and link the result back to the
                // current node
                return new ReaderList(this.thread, this.next.remove(t));
            }

            // If no match found and no more nodes, return the current node (no
            // modification)
            return this;
        }
    }

    private static class Writer extends Holders {
        public final Thread thread;

        public Writer(Thread thread) {
            this.thread = thread;
        }

    }
}
