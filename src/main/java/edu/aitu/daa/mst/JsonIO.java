package edu.aitu.daa.mst;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class JsonIO {

    public static class InputGraph {
        int id;
        List<String> nodes;
        List<Map<String, Object>> edges;
    }

    public static class OutputItem {
        int graph_id;
        Map<String, Integer> input_stats = new LinkedHashMap<>();
        Map<String, Object> prim = new LinkedHashMap<>();
        Map<String, Object> kruskal = new LinkedHashMap<>();
    }

    public static List<Graph> readGraphs(String path) throws IOException {
        String s = Files.readString(Path.of(path));
        JsonObject root = JsonParser.parseString(s).getAsJsonObject();
        JsonArray arr = root.getAsJsonArray("graphs");
        List<Graph> out = new ArrayList<>();

        for (JsonElement el : arr) {
            JsonObject obj = el.getAsJsonObject();
            List<String> nodes = new ArrayList<>();
            for (JsonElement n : obj.getAsJsonArray("nodes")) nodes.add(n.getAsString());
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < nodes.size(); i++) idx.put(nodes.get(i), i);
            List<Edge> edges = new ArrayList<>();
            for (JsonElement ee : obj.getAsJsonArray("edges")) {
                JsonObject eo = ee.getAsJsonObject();
                String a = eo.get("from").getAsString();
                String b = eo.get("to").getAsString();
                double w = eo.get("weight").getAsDouble();
                edges.add(new Edge(idx.get(a), idx.get(b), w));
            }
            out.add(new Graph(nodes, edges));
        }
        return out;
    }

    public static void writeOutput(String path, List<Graph> gs, List<MstResult> prims, List<MstResult> krusks) throws IOException {
        JsonObject root = new JsonObject();
        JsonArray results = new JsonArray();
        for (int i = 0; i < gs.size(); i++) {
            Graph g = gs.get(i);
            MstResult pr = prims.get(i);
            MstResult kr = krusks.get(i);

            JsonObject item = new JsonObject();
            item.addProperty("graph_id", i + 1);

            JsonObject stats = new JsonObject();
            stats.addProperty("vertices", g.n);
            stats.addProperty("edges", g.edges.size());
            item.add("input_stats", stats);

            item.add("prim", mstToJson(g, pr));
            item.add("kruskal", mstToJson(g, kr));

            results.add(item);
        }
        root.add("results", results);
        Files.createDirectories(Path.of(path).getParent());
        Files.writeString(Path.of(path), new GsonBuilder().setPrettyPrinting().create().toJson(root));
    }

    private static JsonObject mstToJson(Graph g, MstResult r) {
        JsonObject o = new JsonObject();
        o.addProperty("status", r.status);
        JsonArray arr = new JsonArray();
        for (int[] e : r.mst_edges) {
            JsonObject ee = new JsonObject();
            ee.addProperty("from", g.labels.get(e[0]));
            ee.addProperty("to", g.labels.get(e[1]));
            double w = findWeight(g, e[0], e[1]);
            ee.addProperty("weight", w);
            arr.add(ee);
        }
        o.add("mst_edges", arr);
        o.addProperty("total_cost", r.total_cost);
        o.addProperty("operations_count", r.operations);
        o.addProperty("execution_time_ms", r.time_ms);
        return o;
    }

    private static double findWeight(Graph g, int u, int v) {
        for (Edge e : g.edges) {
            if ((e.u == u && e.v == v) || (e.u == v && e.v == u)) return e.w;
        }
        return Double.NaN;
    }

    public static void writeCsv(String path, List<MstResult> prims, List<MstResult> krusks) throws IOException {
        Files.createDirectories(Path.of(path).getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(path))) {
            bw.write("graph_id,algo,total_cost,time_ms,operations\n");
            for (int i = 0; i < prims.size(); i++) {
                bw.write((i+1)+",Prim,"+prims.get(i).total_cost+","+prims.get(i).time_ms+","+prims.get(i).operations+"\n");
                bw.write((i+1)+",Kruskal,"+krusks.get(i).total_cost+","+krusks.get(i).time_ms+","+krusks.get(i).operations+"\n");
            }
        }
    }
}
