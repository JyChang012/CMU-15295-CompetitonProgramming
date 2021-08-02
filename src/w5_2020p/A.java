package w5_2020p;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// kinda slow, hashset is even slower
// O(m + n)

public class A {
    public static void main(String[] args) {
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

        boolean[] visited = new boolean[n];
        int ret = 0;
        for (int u = 0; u < n; u++) {
            if (!visited[u]) {
                // find all components by dfs
                List<Integer> visits = new ArrayList<>();
                dfs(u, graph, visited, visits);

                // check whether components are cycles
                boolean isCycle = true;
                for (int v : visits) {
                    if (graph[v].size() != 2) {
                        isCycle = false;
                        break;
                    }
                }
                if (isCycle)
                    ret++;

            }
        }

        System.out.println(ret);

    }

    private static void dfs(int u, ArrayList<Integer>[] graph, boolean[] visited, List<Integer> visits) {
        visits.add(u);
        visited[u] = true;
        for (int v : graph[u])  // forall neighbors
            if (!visited[v])
                dfs(v, graph, visited, visits);

    }

}
