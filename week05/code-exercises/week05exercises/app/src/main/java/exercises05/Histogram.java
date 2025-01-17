// For week 5
// raup@itu.dk * 10/10/2021
package exercises05;

/* 
 A histogram is a collection of bins, each of which is an integer count. The span of the histogram is the number of bins. In the problems below a span of 30 will be sufficient; in that case the bins are numbered 0 . . . 29.
Consider this Histogram interface for creating histograms:

--- see below

Intuitively, these methods work as follows: increment(7) will add one to bin 7; method call getCount(7) will return the current count in bin 7; method getSpan() will return the number of bins; and, finally, the method getAndClear(7) returns the current count in bin 7 and reset the count to 0. The class Histogram1 in app/src/main/java/exercises05/Histogram1.java contains a non-thread-safe implementation with this behavior.
 */

interface Histogram {
    public void increment(int bin);

    public int getCount(int bin);

    public int getSpan();

    public int getAndClear(int bin);
}
