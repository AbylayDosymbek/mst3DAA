# City Transportation MST (Assignment 3)
Student: Abylay Dosymbek

Group: SE-2435

Course instructor: Aidana Aidynkyzy

small student project where i compare prim vs kruskal on a city road network (minimum spanning tree).just clean java + maven and a couple datasets to see performance and correctness. 

## Quick Start (in INTELLIJ)

1. open the project in intellij idea.
2. go to run → edit configurations… → + → application:
   - main class: edu.aitu.daa.mst.MainApp
   - program arguments:
     ```
     run -in src/main/resources/datasets/assign_3_input.json -out out/output.json -csv out/summary.csv
     ```
   - jre: java 17 (temurin or jetbrains runtime 17)
3. press the green run button (or shift+f10).
4. results will appear in out/:
   - out/output.json — detailed results per graph for both algos
   - out/summary.csv — compact table with total cost, time, ops

note: every run overwrites these files. if you want to keep older stuff, change names like -out out/output_v2.json -csv out/summary_v2.csv.

## Quick Start (for jar)

if you prefer terminal:
```bash
mvn -q -DskipTests package
java -jar target/city-mst-assignment-unique-1.0.0-shaded.jar run   -in src/main/resources/datasets/assign_3_input.json   -out out/output.json   -csv out/summary.csv
```
windows paths use backslashes \. make sure java -version shows 17.

## Datasets 

src/main/resources/datasets/assign_3_input.json — main dataset with several graphs:
- small (for debugging and correctness)
- medium (timings start to differ a bit)
- large (light stress, still fast)
- disconnected (to check we dont pretend there is an mst)

you can add your own graphs by appending to graphs array. ids can be 1,2,3,… no need to overthink it.

### JSON Format Reminder

```json
{
  "graphs": [
    {
      "id": 1,
      "nodes": ["A","B","C","D"],
      "edges": [
        {"from":"A","to":"B","weight":3.0},
        {"from":"B","to":"C","weight":1.5},
        {"from":"C","to":"D","weight":2.0}
      ]
    }
  ]
}
```
- graph is undirected
- weights are non-negative numbers (double)
- node labels are unique strings

## What The Program Writes

out/output.json (per input graph):
- input_stats: vertices and edges count
- prim: status, mst edges (labels), total cost, operations, time in ms
- kruskal: same structure
- if disconnected → status = "DISCONNECTED" and no mst edges

out/summary.csv (two rows per graph):
```
graph_id,algo,total_cost,time_ms,operations
1,Prim,XX.XX,YY.YY,ZZZ
1,Kruskal,XX.XX,YY.YY,ZZZ
...
```
the outputs are updating each time the code runs.
## Tests

run tests:
```bash
mvn test
```
checks include:
- same total mst cost for prim and kruskal on connected graphs
- |mst| = V - 1
- non-negative time and op counters
- disconnected graphs handled gracefully

## How I Count Operations

- kruskal: increases a counter on each comparison during sort plus union/find steps
- prim: bumps counter on relax attempts from the current vertex

not a lab-grade benchmark (jvm warmup is a thing), but consistent enough inside one run for comparing tendencies.

## Tiny Theory To Practice

- kruskal usually shines on sparse graphs (lots of vertices, not so many edges). sort E log E plus fast union-find is nice.
- prim is comfy on denser graphs with a good priority queue.
- real life depends on your graph shape. 

## Repo Map (Short)

```
src/main/java/edu/aitu/daa/mst/
  Edge.java          // tiny edge holder
  Graph.java         // labels, edges, adjacency
  PrimSolver.java    // heap-based prim
  KruskalSolver.java // sort + union-find
  UnionFind.java     // rank + path compression
  OpCounter.java     // tiny counter
  MstResult.java     // dto for results
  JsonIO.java        // read/write json and csv
  MainApp.java       // cli

src/test/java/...    // junit tests
src/main/resources/datasets/
  assign_3_input.json   // main dataset
docs/graphs/
  graph_1.png, graph_2.png // static examples only (dont auto update)
```

## Common Oops And Quick Fixes

- unsupported class version (61 vs 52) → you ran java 8; install java 17.
- NoClassDefFoundError: com/google/gson → run shaded jar or run from intellij app config (module classpath).
- run button gone → view → appearance → toolbar, and check your run configuration exists.


