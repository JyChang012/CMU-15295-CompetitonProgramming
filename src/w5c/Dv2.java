package w5c;

import java.io.*;
import java.nio.file.attribute.AclEntryType;
import java.util.*;


// time exceeded on test 42

public class Dv2 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(reader.readLine());
        String[] ints = reader.readLine().split(" ");
        long[] a = new long[n];  // number on vertex
        int[] parent = new int[n];  // parent node
        long[] edges = new long[n];  // edge weight to parent
        int[] processed = new int[n];  // processed status, 0 for unvisited leaf node, 1 for un-visited non-leaf node
                                        // -1 for visited node
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
                int ancestorsN = 0;
                for (int v = u; v != 0; v = parent[v])
                    ancestorsN++;
                long[] dist = new long[ancestorsN];
                int[] ancestors = new int[ancestorsN];
                int[] record = new int[ancestorsN];
                dist[0] = edges[u];
                ancestors[0] = parent[u];
                for (int v = parent[u], i = 1; v != 0; v = parent[v], i++) {
                    dist[i] = dist[i - 1] + edges[v];
                    ancestors[i] = parent[v];
                }

                long prevDist = 0;
                for (int v = u, i = 0; v != 0 && processed[v] >= 0; v = parent[v], i++) {
                    long aa = a[v] + prevDist;
                    int found = Arrays.binarySearch(dist, i, dist.length, aa);
                    if (found >= 0)
                        found++;
                    else
                        found = -found - 1;  // (insertion point)
                    // now found is the ancestor where adding should stop
                    if (found > i) {
                        record[i]++;
                        if (found < record.length)  // if the ending point is behind the last element, no need to --
                            record[found]--;
                    }
                    processed[v] = -1;
                    prevDist = dist[i];
                }

                // now take cumulative sum over the record
                int prev = 0;
                for (int i = 0; i < record.length; i++) {
                    record[i] += prev;
                    ret[ancestors[i]] += record[i];
                    prev = record[i];
                }


            }

        }
        for (long r : ret) {
            writer.write(String.valueOf(r));
            writer.write(' ');
        }

        writer.close();

    }
}
