package edu.aitu.daa.mst;

import java.util.*;

public class KruskalSolver {
    public static MstResult run(Graph g) {
        long t0 = System.nanoTime();
        OpCounter op = new OpCounter();
        List<Edge> edges = new ArrayList<>(g.edges);
        edges.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge a, Edge b) {
                op.hit();
                return Double.compare(a.w, b.w);
            }
        });

        UnionFind uf = new UnionFind(g.n, op);
        MstResult res = new MstResult();
        for (Edge e : edges) {
            if (uf.union(e.u, e.v)) {
                res.mst_edges.add(new int[]{e.u, e.v});
                res.total_cost += e.w;
                if (res.mst_edges.size() == g.n - 1) break;
            }
        }
        if (res.mst_edges.size() != g.n - 1) {
            res.status = "DISCONNECTED";
            res.mst_edges.clear();
            res.total_cost = 0.0;
        }
        res.operations = op.get();
        res.time_ms = (System.nanoTime() - t0) / 1e6;
        return res;
    }
}
