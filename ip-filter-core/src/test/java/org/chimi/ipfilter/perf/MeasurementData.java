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
        sum += after - before;
    }

    public long getCount() {
        return count;
    }

    public void printReport() {
        System.out.printf("Count=%d, Sum=%d mills, Average=%.6f mills\n", getCount(), getSum() / 1000000, getAverage());
    }

    public double getAverage() {
        return (double) sum / (double) count / 1000000;
    }

    public long getSum() {
        return sum;
    }

    public void add(MeasurementData data) {
        this.count += data.count;
        this.sum += data.sum;
    }

    public void increaseCount() {
        count++;
    }
}
