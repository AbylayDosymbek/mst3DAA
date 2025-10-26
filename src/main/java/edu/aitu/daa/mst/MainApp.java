package edu.aitu.daa.mst;
import java.nio.file.*;
import java.util.*;
public class MainApp {
    private static void println(String s){ System.out.println(s); }

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "help".equals(args[0])) {
            println("Usage:");
            println("  run -in <input.json> -out <output.json> -csv <summary.csv>");
            return;
        }
        String in = null, out = "out/output.json", csv = "out/summary.csv";
        for (int i = 0; i < args.length; i++) {
            if ("-in".equals(args[i]) && i+1 < args.length) in = args[++i];
            else if ("-out".equals(args[i]) && i+1 < args.length) out = args[++i];
            else if ("-csv".equals(args[i]) && i+1 < args.length) csv = args[++i];
        }
        if (in == null) throw new IllegalArgumentException("input json is required: -in <path>");

        List<Graph> graphs = JsonIO.readGraphs(in);
        List<MstResult> prims = new ArrayList<>();
        List<MstResult> krusks = new ArrayList<>();

        for (Graph g : graphs) {
            prims.add(PrimSolver.run(g));
            krusks.add(KruskalSolver.run(g));
        }

        JsonIO.writeOutput(out, graphs, prims, krusks);
        JsonIO.writeCsv(csv, prims, krusks);

        println("Done. Wrote: " + out + " and " + csv);
    }
}
