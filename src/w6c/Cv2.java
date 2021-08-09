package w6c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;


// much faster

public class Cv2 {

    static class Dijkstra {
        int n;  // number of vertices
        ArrayList<AbstractMap.SimpleEntry<Integer, Long>>[] edges;

        static class DistVexComparator implements Comparator<AbstractMap.SimpleEntry<Long, Integer>> {
            @Override
            public int compare(AbstractMap.SimpleEntry<Long, Integer> o1, AbstractMap.SimpleEntry<Long, Integer> o2) {
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
            edges[u].add(new AbstractMap.SimpleEntry<>(v, w));
        }

        public AbstractMap.SimpleEntry<int[], long[]> shortestPath(int s) {  // s is the source
            long[] dist = new long[n];
            int[] pred = new int[n]; // predecessor
            Arrays.fill(pred, -1);
            Arrays.fill(dist, Long.MAX_VALUE / 2);
            dist[s] = 0;

            PriorityQueue<AbstractMap.SimpleEntry<Long, Integer>> pq = new PriorityQueue<>(new Dijkstra.DistVexComparator());  // pair of current dist and vertices ID
            pq.add(new AbstractMap.SimpleEntry<>(0L, s));
            while (!pq.isEmpty()) {
                AbstractMap.SimpleEntry<Long, Integer> dv = pq.poll();
                long d = dv.getKey();
                int v = dv.getValue();
                if (edges[v] != null && dist[v] == d) {  // if it's the up to date dist, and the vertices v has out neighbor
                    for (AbstractMap.SimpleEntry<Integer, Long> edge : edges[v]) {
                        int neighbor = edge.getKey();
                        long w = edge.getValue();
                        long newD = d + w;
                        if (dist[neighbor] > newD) {
                            dist[neighbor] = newD;
                            pred[neighbor] = v;
                            pq.add(new AbstractMap.SimpleEntry<>(newD, neighbor));
                        }
                    }
                }
            }
            return new AbstractMap.SimpleEntry<>(pred, dist);
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
        // N is the # of fake node
        int[] degree = new int[n];
        Dijkstra solver = new Dijkstra(n);  // # of fake nodes is the same as m
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            int a = Integer.parseInt(line[0]) - 1, b = Integer.parseInt(line[1]) - 1, w = Integer.parseInt(line[2]);
            degree[a]++;
            degree[b]++;
            solver.addEdge(a, b, w);
            solver.addEdge(b, a, w);
        }

        for (int u = 0; u < n; u++) {  // iterate through fake nodes
            ArrayList<AbstractMap.SimpleEntry<Integer, Long>> entryList = solver.edges[u];
            for (AbstractMap.SimpleEntry<Integer, Long> entry : entryList) {
                entry.setValue(entry.getValue() + degree[entry.getKey()]);
            }
        }

        double time = Double.MAX_VALUE;
        for (int u = 0; u < n; u++) {
            AbstractMap.SimpleEntry<int[], long[]> ret = solver.shortestPath(u);
            long[] dist = ret.getValue();
            int[] pred = ret.getKey();
            double[] realDist = new double[n];

            for (int i = 0; i < n; i++) {
                realDist[i] = dist[i];
            }

            // iterate through all non tree edges and update the dist in endpoint
            for (int v = 0; v < n; v++) {
                ArrayList<SimpleEntry<Integer, Long>> neighbors = solver.edges[v];
                for (SimpleEntry<Integer, Long> entry: neighbors) {
                    int w = entry.getKey();
                    if (v < w) {  // since multiple connections between twp nodes might occur, even if pred[w] == v, there might still be unburnt fibers
                        long len = entry.getValue() - degree[w];
                        // note that abs(dist[v] - dist[w]) <= len always holds)
                        int a, b;
                        if (dist[v] > dist[w]) {  // a has smaller dist now
                            a = w;
                            b = v;
                        } else {
                            a = v;
                            b = w;
                        }
                        double newBDist;
                        if (dist[b] - dist[a] < len) {
                            newBDist = dist[b] + (dist[a] + len - dist[b]) / 2.;
                            realDist[b] = Math.max(realDist[b], newBDist);
                        }  // else if they are equal, don't need to update
                    }
                }
            }

            time = Math.min(time, max(realDist) + degree[u]);
        }

        System.out.format("%.1f", time);

    }

    static double max(double[] arr) {
        double max = Long.MIN_VALUE;
        for (double n : arr)
            max = Math.max(max, n);
        return max;
    }
}
