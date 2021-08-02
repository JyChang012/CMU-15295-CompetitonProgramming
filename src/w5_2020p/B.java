package w5_2020p;

import java.io.*;
import java.util.*;

public class B {

    // equivalent to find whether the graph is bipartite <=> whether it has odd cycle
    public static void main(String[] args) throws IOException {

        // read in graph
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), m = scanner.nextInt();
        ArrayList<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            scanner.nextLine();
            int u = scanner.nextInt() - 1, v = scanner.nextInt() - 1;  // convert to 0 based index
            graph[u].add(v);
            graph[v].add(u);
        }

        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        // partition the vertices in each of the components of the graph
        for (int u = 0; u < n; u++) {
            if (dist[u] == -1) {
                // bfs
                Queue<Integer> queue = new LinkedList<>();
                queue.add(u);
                dist[u] = 0;
                while (!queue.isEmpty()) {
                    int w = queue.poll();
                    for (int v : graph[w]) {
                        if (dist[v] == -1) {  // unvisited
                            dist[v] = (dist[w] + 1) % 2;
                            queue.add(v);
                        } else if (dist[w] == dist[v]) {  // visited and form a odd cycle
                            System.out.println(-1);
                            return;
                        }  // otherwise the dist will not change
                    }
                }
            }

        }

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        // vertices with the same dist are in the same group
        List<Integer>[] groups = new ArrayList[2];
        groups[0] = new ArrayList<>();
        groups[1] = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            groups[dist[u]].add(u + 1);
        }
        for (List<Integer> g: groups) {
            writer.write(String.valueOf(g.size()));
            writer.newLine();
            for (int u: g) {
                writer.write(String.valueOf(u));
                writer.write(' ');
            }
            writer.newLine();
        }

        writer.close();


    }
}
