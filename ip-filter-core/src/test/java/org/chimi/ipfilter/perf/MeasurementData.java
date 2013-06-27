package org.chimi.ipfilter.perf;

public class MeasurementData {
    private long before;
    private long after;
    private long count;
    private long sum;

    public void start() {
        before = System.nanoTime();
    }

    public void stop() {
        after = System.nanoTime();
        count++;
        sum += after - before;
    }

    public long getCount() {
        return count;
    }

    public void printReport() {
        System.out.printf("Count=%d, Sum=%d mills, Average=%.6f mills", getCount(), getSum() / 1000000, getAverage());
    }

    public double getAverage() {
        return (double) sum / (double) count / 1000000;
    }

    public long getSum() {
        return sum;
    }
}
