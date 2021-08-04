package w5c;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


// time exceeded
public class D {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(reader.readLine());
        String[] ints = reader.readLine().split(" ");
        long[] a = new long[n];  // number on vertex
        int[] parent = new int[n];  // parent node
        long[] edges = new long[n];  // edge weight to parent
        int[] processed = new int[n];  // processed status, 0 for unvisited leaf node, 1 for un-visited non-leaf node
                                        // -1 for visited non-leaf node, -2 for visited leaf node
        processed[0] = 1;
        long[] ret = new long[n];
        edges[0] = -1;
        parent[0] = -1;  // root's parent is -1, root's id is 0
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(ints[i]);
        }

        for (int i = 1; i < n; i++) {  // 0 based indices
            ints = reader.readLine().split(" ");
            int p = Integer.parseInt(ints[0]) - 1;
            long w = Long.parseLong(ints[1]);
            parent[i] = p;
            edges[i] = w;
            processed[p] = 1;
        }

        for (int u = 1; u < n; u++) {
            if (processed[u] == 0) {
                /*
                int nn = 0;
                for (int v = u; v != 0; v = parent[v])
                    nn++;
                long[] down2root = new long[nn];
                down2root[0] = edges[u];
                for (int v = parent[u], i = 1; v != 0; v = parent[v], i++) {
                    down2root[i] = down2root[i - 1] + edges[v];
                }
                 */
                List<Long> down2root = new ArrayList<>();
                down2root.add(edges[u]);
                for (int v = parent[u], i = 1; v != 0; v = parent[v], i++) {
                    down2root.add(down2root.get(i - 1) + edges[v]);
                }

                for (int v = u, i = 0; v != 0 && processed[v] >= 0; v = parent[v], i++) {
                    long aa = a[v];
                    if (i != 0) {
                        aa += down2root.get(i - 1);
                        processed[v] = -1;
                    }
                    else {
                        processed[v] = -2;
                    }
                    for (int j = i, w = parent[v]; j < down2root.size(); j++, w = parent[w]) {
                        if (down2root.get(j) <= aa) {
                            ret[w]++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        for (long r: ret) {
            writer.write(String.valueOf(r));
            writer.write(' ');
        }

        writer.close();

    }
}
