package edu.aitu.daa.mst;
public class UnionFind {
    private final int[] p;
    private final int[] r;
    private final OpCounter op;

    public UnionFind(int n, OpCounter op) {
        this.p = new int[n];
        this.r = new int[n];
        this.op = op;
        for (int i = 0; i < n; i++) {
            p[i] = i; r[i] = 0;
        }
    }

    public int find(int x) {
        op.hit();
        if (p[x] != x) p[x] = find(p[x]);
        return p[x];
    }

    public boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return false;
        if (r[ra] < r[rb]) {
            p[ra] = rb;
        } else if (r[ra] > r[rb]) {
            p[rb] = ra;
        } else {
            p[rb] = ra; r[ra]++;
        }
        op.hit();
        return true;
    }
}
