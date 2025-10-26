package edu.aitu.daa.mst;

import java.util.*;

public class PrimSolver {
    public static MstResult run(Graph g) {
        long t0 = System.nanoTime();
        OpCounter op = new OpCounter();

        List<List<Edge>> adj = g.toAdj();
        boolean[] used = new boolean[g.n];
        double[] best = new double[g.n];
        int[] parent = new int[g.n];
        Arrays.fill(best, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> Double.longBitsToDouble(a[1])));
        best[0] = 0.0;
        pq.add(new int[]{0, (int)Double.doubleToRawLongBits(0.0)});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int v = cur[0];
            if (used[v]) continue;
            used[v] = true;
            if (parent[v] != -1) {
            }
            for (Edge e : adj.get(v)) {
                op.hit();
                if (!used[e.v] && e.w < best[e.v]) {
                    best[e.v] = e.w;
                    parent[e.v] = v;
                    pq.add(new int[]{e.v, (int)Double.doubleToRawLongBits(e.w)});
                }
            }
        }

        MstResult res = new MstResult();
        for (int v = 1; v < g.n; v++) {
            if (parent[v] == -1) {
                res.status = "DISCONNECTED";
                res.mst_edges.clear();
                res.total_cost = 0;
                res.operations = op.get();
                res.time_ms = (System.nanoTime() - t0) / 1e6;
                return res;
            }
            res.mst_edges.add(new int[]{parent[v], v});
            res.total_cost += best[v];
        }
        res.operations = op.get();
        res.time_ms = (System.nanoTime() - t0) / 1e6;
        return res;
    }
}
