package edu.aitu.daa.mst;

public class OpCounter {
    private long count = 0L;
    public void hit() { count++; }
    public void add(long c) { count += c; }
    public long get() { return count; }
    public void reset() { count = 0L; }
}
