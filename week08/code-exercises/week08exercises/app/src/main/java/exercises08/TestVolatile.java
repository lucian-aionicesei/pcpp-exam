package exercises08;

import benchmarking.Benchmark;

public class TestVolatile {
    private volatile int vCtr;
    private int ctr;

    public static void main(String[] args) {
        new TestVolatile();
    }

    public TestVolatile() {
        Benchmark.SystemInfo();
        System.out.println("Mark 7 measurements");
        Benchmark.Mark7("Testing volatile int",
                i -> {
                    vInc();
                    return 0.0;
                });
        Benchmark.Mark7("Testing regular int",
                i -> {
                    inc();
                    return 0.0;
                });
        System.out.printf("Volatile int value = %d%n", vCtr);
        System.out.printf("Regular int value = %d%n", ctr);
    }

    public void vInc() {
        vCtr++;
    }

    public void inc() {
        ctr++;
    }

}