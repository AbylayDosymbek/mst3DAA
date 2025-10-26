package edu.aitu.daa.mst;

import java.util.*;

public class MstResult {
    public String status = "OK";
    public List<int[]> mst_edges = new ArrayList<>(); // pairs of (u,v)
    public double total_cost = 0.0;
    public long operations = 0L;
    public double time_ms = 0.0;
}
