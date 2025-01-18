package exercises08;
// jst@itu.dk * 2023-09-05

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import benchmarking.Benchmark;
import benchmarking.Benchmarkable;

// gradle -PmainClass=exercises08.TestTimeSearch run
public class TestTimeSearch {
    public static void main(String[] args) {
        new TestTimeSearch();
    }

    public TestTimeSearch() {
        final String filename = "src/main/resources/long-text-file.txt";
        final String target = "ipsum";

        final PrimeCounter lc = new PrimeCounter(); // name is a bit misleading, it is just a counter
        String[] lineArray = readWords(filename);

        System.out.println("Array Size: " + lineArray.length);

        // Occurences of ipsum :1430
        // System.out.println("# Occurences of " + target + " :" + search(target,
        // lineArray, 0, lineArray.length, lc));
        System.out.println("# Occurences of " + target + " :" +
                countParallelN(target, lineArray, 10, lc));

        // Exercise 8.4.3 TODO: Comment out the following code to test the
        // countParallelN function

        Benchmark.Mark7("Testing search function",
                i -> {
                    search(target, lineArray, 0, lineArray.length, lc);
                    return 0.0;
                });

        // Exercise 8.4.5 TODO: Uncomment the following code to test the countParallelN
        // function

        // for (int c = 1; c <= 20; c++) {
        // final int threadCount = c;
        // Benchmark.Mark7(String.format("countParallelN %7d", threadCount),
        // i -> countParallelN(target, lineArray, threadCount, lc));
        // }
    }

    static long search(String x, String[] lineArray, int from, int to, PrimeCounter lc) {
        // Search each line of file
        for (int i = from; i < to; i++)
            lc.add(linearSearch(x, lineArray[i]));
        // System.out.println("Found: "+lc.get());
        return lc.get();
    }

    static int linearSearch(String x, String line) {
        // Search for occurences of c in line
        String[] arr = line.split(" ");
        int count = 0;
        for (int i = 0; i < arr.length; i++)
            if ((arr[i].equals(x)))
                count++;
        return count;
    }

    public static String[] readWords(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            return reader.lines().toArray(String[]::new); // will be explained in Week10;
        } catch (IOException exn) {
            return null;
        }
    }

    // 8.4.3
    private static long countParallelN(String target,
            String[] lineArray, int N, PrimeCounter lc) {
        final int range = lineArray.length;
        final int linesPerThread = range / N;
        Thread[] threads = new Thread[N];
        for (int t = 0; t < N; t++) {
            final int fromLine = linesPerThread * t,
                    to = (t + 1 == N) ? range : linesPerThread * (t + 1);
            threads[t] = new Thread(() -> {
                for (int i = fromLine; i < to; i++)
                    lc.add(linearSearch(target, lineArray[i]));
            });
        }
        for (int t = 0; t < N; t++)
            threads[t].start();
        try {
            for (int t = 0; t < N; t++)
                threads[t].join();
            // System.out.println("Primes: "+lc.get());
        } catch (InterruptedException exn) {
        }
        return lc.get();
        // uses N threads to search lineArray
    }

}
