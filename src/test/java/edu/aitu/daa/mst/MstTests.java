package edu.aitu.daa.mst;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.nio.file.*;

public class MstTests {

    Graph tiny() {
        List<String> nodes = List.of("A","B","C","D");
        List<Edge> edges = List.of(
            new Edge(0,1,1),
            new Edge(1,2,2),
            new Edge(2,3,3),
            new Edge(0,2,4),
            new Edge(1,3,5)
        );
        return new Graph(nodes, edges);
    }

    @Test
    void prim_kruskal_same_cost_and_size() {
        Graph g = tiny();
        MstResult a = PrimSolver.run(g);
        MstResult b = KruskalSolver.run(g);
        assertEquals(a.total_cost, b.total_cost, 1e-9);
        assertEquals(g.n - 1, a.mst_edges.size());
        assertEquals(g.n - 1, b.mst_edges.size());
        assertEquals("OK", a.status);
        assertEquals("OK", b.status);
        assertTrue(a.time_ms >= 0);
        assertTrue(b.time_ms >= 0);
        assertTrue(a.operations >= 0);
        assertTrue(b.operations >= 0);
    }

    @Test
    void disconnected_graph_is_handled() {
        List<String> nodes = List.of("A","B","C"); // no edges
        List<Edge> edges = List.of();
        Graph g = new Graph(nodes, edges);
        MstResult a = PrimSolver.run(g);
        MstResult b = KruskalSolver.run(g);
        assertEquals("DISCONNECTED", a.status);
        assertEquals("DISCONNECTED", b.status);
    }
}
