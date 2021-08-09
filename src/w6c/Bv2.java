package w6c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.*;

// even slower

public class Bv2 {
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
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]), k = Integer.parseInt(line[1]);  // height of canyon and ninja's jump

        if (k >= n) {  // just one jump and im out
            System.out.println("YES");
            return;
        }

        line[0] = reader.readLine();
        line[1] = reader.readLine();
        int m = 0; // # of nodes
        int[][] ids = new int[2][n];  // id of each node
        for (int i = 0; i < 2; i++) {
            for (int h = 0; h < n; h++) {
                if (line[i].charAt(h) == '-') {
                    ids[i][h] = m;
                    m++;
                }
            }
        }
        Dijkstra solver = new Dijkstra(m);
        int[] heights = new int[m];
        for (int i = 0; i < 2; i++) {
            for (int h = 0; h < n; h++) {
                if (line[i].charAt(h) == '-') {
                    // up
                    if (h != n - 1 && line[i].charAt(h + 1) == '-')
                        solver.addEdge(ids[i][h], ids[i][h + 1], 1);
                    // down
                    if (h != 0 && line[i].charAt(h - 1) == '-')
                        solver.addEdge(ids[i][h], ids[i][h - 1], 1);
                    // to the other side
                    if (h + k < n && line[(i + 1) % 2].charAt(h + k) == '-')
                        solver.addEdge(ids[i][h], ids[(i + 1) % 2][h + k], 1);

                    heights[ids[i][h]] = h;
                }
            }
        }
        AbstractMap.SimpleEntry<int[], long[]> ret = solver.shortestPath(0);  // pred, dist
        int[] pred = ret.getKey();
        long[] dist = ret.getValue();

        ArrayList<Integer>[] tree = new ArrayList[m]; // shortest path tree
        for (int i = 1; i < m; i++) {
            int curPred = pred[i];
            if (curPred != -1) {
                if (tree[curPred] == null)
                    tree[curPred] = new ArrayList<>();
                tree[curPred].add(i);
            }
        }


        if (checkOK(0, tree, heights, dist, k, n))
            System.out.println("YES");
        else
            System.out.println("NO");




    }

    static boolean checkOK(int u, ArrayList<Integer>[] tree, int[] heights, long[] dist, int k, int n) {
        if (heights[u] < dist[u])
            return false;
        if (heights[u] >= n - k)  // entered the safe zone
            return true;
        if (tree[u] != null) {
            for (int child: tree[u]) {
                if (checkOK(child, tree, heights, dist, k, n)) {
                    return true;
                }
            }
        }
        return false;
    }

}
