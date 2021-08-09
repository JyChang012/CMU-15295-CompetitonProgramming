package w5c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Ev2 {


    // determine if the graph is a single strong component (each node connects to all nodes (including itself))
    // standard algo for determine whether a graph is strongly connected O(n + m)


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

        int[] lowPoints = new int[n];  // lowest numbered nodes reached by a node by using at most 1 back/cross edge

        int[] num = new int[n];  // the number/discovery time of a node
        Arrays.fill(num, -1);
        // boolean[] mark = new boolean[n];  // no need for mark since no need for distinguishing cross/back edge
        dfs4SC(0, graph, num, lowPoints, -1);

        // check if all points are reached by node 0
        for (int nn: num) {
            if (nn < 0) {
                no();
                return;
            }
        }

        // check if all low points of node except 0 < their num
        for (int i = 1; i < n; i++) {
            if (lowPoints[i] >= num[i]) {
                no();
                return;
            }
        }

        yes();

    }


    private static void dfs4SC(int u, ArrayList<Integer>[] graph, int[] num, int[] lowPoints, int i) {
        i++;
        num[u] = i;
        lowPoints[u] = num[u];

        if (graph[u] != null)
            for (int child : graph[u]) {
                if (num[child] < 0) {  // if tree edge
                    dfs4SC(child, graph, num, lowPoints, i);
                    lowPoints[u] = Math.min(lowPoints[u], lowPoints[child]);
                } else if (num[child] <= num[u]) {  // if back edge or cross edge
                    lowPoints[u] = Math.min(lowPoints[u], num[child]);
                }
                // nothing to do for forward edge
            }
    }

    private static void yes() {
        System.out.println("YES");
    }

    private static void no() {
        System.out.println("NO");
    }

}
