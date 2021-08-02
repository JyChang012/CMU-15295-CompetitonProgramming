package w5c;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class A {


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), p = scanner.nextInt();
        List<Integer> ret = new ArrayList<>();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        int[] nodesTo = new int[n], nodesFrom = new int[n];  // each node have at most 1 out degree
        int[] diameters = new int[n];
        Arrays.fill(nodesTo, -1);
        Arrays.fill(nodesFrom, -1);

        for (int i = 0; i < p; i++) {
            scanner.nextLine();
            int u = scanner.nextInt() - 1, v = scanner.nextInt() - 1, d = scanner.nextInt();
            nodesTo[u] = v;
            nodesFrom[v] = u;
            diameters[u] = d;
        }

        // do DFS on graph
        boolean[] visited = new boolean[n];
        for (int tank = 0; tank < n; tank++) {
            if ((!visited[tank]) && nodesFrom[tank] == -1 && nodesTo[tank] != -1) {
                visited[tank] = true;
                int v = tank, dia = diameters[v];
                while (nodesTo[v] != -1) {
                    int prev = v;
                    v = nodesTo[v];
                    dia = Math.min(dia, diameters[prev]);
                }


                ret.add(tank);
                ret.add(v);
                ret.add(dia);
            }
        }


        writer.write(String.valueOf(ret.size() / 3));
        writer.newLine();

        for (int i = 0; i < ret.size(); i += 3) {
            writer.write(String.valueOf(ret.get(i) + 1));
            writer.write(' ');
            writer.write(String.valueOf(ret.get(i + 1) + 1));
            writer.write(' ');
            writer.write(String.valueOf(ret.get(i + 2)));
            writer.newLine();
        }

        writer.close();
    }

}
