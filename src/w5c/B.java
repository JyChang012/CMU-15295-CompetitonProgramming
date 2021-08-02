package w5c;

import java.io.*;
import java.util.*;

public class B {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] ints = reader.readLine().split(" ");

        int rows = Integer.parseInt(ints[0]), columns = Integer.parseInt(ints[1]);
        int[][] graph = new int[rows][columns];
        for (int r = 0; r < rows; r++) {
            String line = reader.readLine();
            for (int c = 0; c < columns; c++) {
                if (line.charAt(c) == '*')
                    graph[r][c] = -1;
            }
        }

        // empty cell ares 0 before discover and >= 1 (componetId) after, impassable cells are -1

        Map<Integer, Integer> compId2n = new HashMap<>();
        int componentId = 1;
        boolean add = false;
        for (int r = 0; r < rows; r++) {
            char[] line = new char[columns];
            Arrays.fill(line, '.');
            for (int c = 0; c < columns; c++) {
                if (graph[r][c] == -1) {
                    Set<Integer> componentSet = new HashSet<>();
                    add = calculateConnected(r + 1, c, graph, add? (++componentId) : componentId, compId2n);
                    if (!(checkOuttaBoard(r + 1, c, graph) ||checkImpassable(r + 1, c, graph)))
                        componentSet.add(graph[r + 1][c]);
                    add = calculateConnected(r - 1, c, graph, add? (++componentId) : componentId, compId2n);
                    if (!(checkOuttaBoard(r - 1, c, graph) ||checkImpassable(r - 1, c, graph)))
                        componentSet.add(graph[r - 1][c]);
                    add = calculateConnected(r, c + 1, graph, add? (++componentId) : componentId, compId2n);
                    if (!(checkOuttaBoard(r, c + 1, graph) ||checkImpassable(r, c + 1, graph)))
                        componentSet.add(graph[r][c + 1]);
                    add = calculateConnected(r, c - 1, graph, add? (++componentId) : componentId, compId2n);
                    if (!(checkOuttaBoard(r, c - 1, graph) ||checkImpassable(r, c - 1, graph)))
                        componentSet.add(graph[r][c - 1]);

                    int ret = 1;
                    for (int comp: componentSet)  // lazy way to avoid repeated addition of componets
                        ret += compId2n.get(comp);
                    line[c] = (char) ('0' + ret % 10);
                }
            }
            writer.write(line);
            writer.newLine();
        }
        writer.close();

    }

    private static boolean calculateConnected(int r, int c, int[][] graph, int componentId, Map<Integer, Integer> compId2n) {
        int ret = dfs(r, c, graph, componentId);
        if (ret != 0) {
            compId2n.put(componentId, ret);
            return true;
        }
        return false;
    }


    private static int dfs(int r, int c, int[][] graph, int componentId) {
        // if out of bounds or cell impassable or have visited
        if (checkOuttaBoard(r, c, graph) || graph[r][c] != 0)
            return 0;
        graph[r][c] = componentId;
        return 1 + dfs(r + 1, c, graph, componentId) + dfs(r - 1, c, graph, componentId) + dfs(r, c + 1, graph, componentId) +
                dfs(r, c - 1, graph, componentId);
    }

    private static boolean checkOuttaBoard(int r, int c, int[][] graph) {
        return r < 0 || c < 0 || r >= graph.length || c >= graph[0].length;
    }

    private static boolean checkImpassable(int r, int c, int[][] graph) {
        return graph[r][c] == -1;
    }

}
