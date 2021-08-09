package w6c;

import java.io.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class A {


    static class Dijkstra {
        int n;  // number of vertices
        ArrayList<SimpleEntry<Integer, Long>>[] edges;

        static class DistVexComparator implements Comparator<SimpleEntry<Long, Integer>> {
            @Override
            public int compare(SimpleEntry<Long, Integer> o1, SimpleEntry<Long, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        }

        public Dijkstra(int n) {
            this.n = n;
            edges = new ArrayList[n];
        }

        public void addEdge(int u, int v, long w) {
            if (edges[u] == null)
                edges[u] = new ArrayList<>();
            edges[u].add(new SimpleEntry<>(v, w));
        }

        public SimpleEntry<int[], long[]> shortestPath(int s) {  // s is the source
            long[] dist = new long[n];
            int[] pred = new int[n]; // predecessor
            Arrays.fill(pred, -1);
            Arrays.fill(dist, Long.MAX_VALUE / 2);
            dist[s] = 0;

            PriorityQueue<SimpleEntry<Long, Integer>> pq = new PriorityQueue<>(new DistVexComparator());  // pair of current dist and vertices ID
            pq.add(new SimpleEntry<>(0L, s));
            while (!pq.isEmpty()) {
                SimpleEntry<Long, Integer> dv = pq.poll();
                long d = dv.getKey();
                int v = dv.getValue();
                if (edges[v] != null && dist[v] == d) {  // if it's the up to date dist, and the vertices v has out neighbor
                    for (SimpleEntry<Integer, Long> edge : edges[v]) {
                        int neighbor = edge.getKey();
                        long w = edge.getValue();
                        long newD = d + w;
                        if (dist[neighbor] > newD) {
                            dist[neighbor] = newD;
                            pred[neighbor] = v;
                            pq.add(new SimpleEntry<>(newD, neighbor));
                        }
                    }
                }
            }
            return new SimpleEntry<>(pred, dist);
        }

        static public List<Integer> getPath(int dest, int[] pred) {
            ArrayList<Integer> path = new ArrayList<>();
            path.add(dest);
            while (pred[dest] >= 0) {
                path.add(pred[dest]);
                dest = pred[dest];
            }
            Collections.reverse(path);
            return path;
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]), m = Integer.parseInt(line[1]);
        Dijkstra solver = new Dijkstra(n);
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            int a = Integer.parseInt(line[0]) - 1, b = Integer.parseInt(line[1]) - 1;
            long w = Long.parseLong(line[2]);
            solver.addEdge(a, b, w);
            solver.addEdge(b, a, w);
        }

        SimpleEntry<int[], long[]> ret = solver.shortestPath(0);
        if (ret.getKey()[n - 1] == -1) {
            System.out.println(-1);  // no path
        } else {
            List<Integer> path = Dijkstra.getPath(n - 1, ret.getKey());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
            for (int v : path) {
                writer.write(String.valueOf(v + 1));
                writer.write(' ');
            }
            writer.newLine();
            writer.close();

        }

    }
}
