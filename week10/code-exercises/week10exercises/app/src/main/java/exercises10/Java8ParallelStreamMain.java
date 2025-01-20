package exercises10;

import java.util.Arrays;
import java.util.stream.IntStream;

// gradle -PmainClass=exercises10.Java8ParallelStreamMain run

public class Java8ParallelStreamMain {
    public static void main(String[] args) {
        // System.out.println("=================================");
        // System.out.println("Using Sequential Stream");
        // System.out.println("=================================");
        // int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
        // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1,
        // 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        // IntStream intArrStream = Arrays.stream(array);
        // intArrStream.forEach(s -> {
        // System.out.println(s + " " + Thread.currentThread().getName());
        // });
        // System.out.println("=================================");
        // System.out.println("Using Parallel Stream");
        // System.out.println("=================================");
        // IntStream intParallelStream = Arrays.stream(array).parallel();
        // intParallelStream.forEach(s -> {
        // System.out.println(s + " " + Thread.currentThread().getName());
        // });

        // 10.3.3
        System.out.println("=================================");
        System.out.println("Using Sequential Stream");
        System.out.println("=================================");
        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        IntStream intArrStream = Arrays.stream(array);
        intArrStream.map(number -> performComputation(number))
                .forEach(s -> System.out.println(s + " " +
                        Thread.currentThread().getName()));
        System.out.println("=================================");
        System.out.println("Using Parallel Stream");
        System.out.println("=================================");
        IntStream intParallelStream = Arrays.stream(array);
        intParallelStream.parallel()
                .map(i -> (int) Math.sqrt(i))
                .map(number -> performComputation(number))
                .forEach(s -> System.out.println(s + " " +
                        Thread.currentThread().getName()));
    }

    public static int performComputation(int number) {
        int sum = 0;
        for (int i = 1; i < 1000000; i++) {
            int div = (number / i);
            sum += div;
        }
        return sum;
    }
}