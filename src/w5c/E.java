package w5c;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TransferQueue;

// convert to check reacheability of the graph, time exceeded
public class E {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        ArrayList<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            String[] ints = reader.readLine().split(" ");
            for (int j = 0; j < n; j++)
                if (Integer.parseInt(ints[j]) > 0) {  // i -> j
                    if (graph[i] == null)
                        graph[i] = new ArrayList<>();
                    graph[i].add(j);
                }
        }

        for (int i = 0; i < n; i++) {
            if (!reachAll(i, graph)) {
                System.out.println("NO");
                return;
            }
        }

        System.out.println("YES");


    }


    private static boolean reachAll(int u, ArrayList<Integer>[] graph) {
        Set<Integer> reach = new HashSet<>();
        Queue<Integer> frontier = new LinkedList<>();
        frontier.add(u);
        while (!frontier.isEmpty()) {
            int v = frontier.remove();
            if (v == u || reach.contains(v))
                for (int child : graph[v]) {
                    if (!reach.contains(child)) {
                        if (child != u)
                            frontier.add(child);
                        reach.add(child);
                    }
                }
        }

        return reach.size() == graph.length;

    }
}
