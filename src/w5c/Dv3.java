package w5c;


import java.io.*;
import java.util.*;

// try top down, passed
public class Dv3 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(reader.readLine());
        String[] ints = reader.readLine().split(" ");
        long[] a = new long[n];  // number on vertex
        long[] edges = new long[n];
        int[] ret = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(ints[i]);
        }
        List<Integer>[] tree = new List[n];
        for (int i = 1; i < n; i++) {  // 0 based indices
            ints = reader.readLine().split(" ");
            int p = Integer.parseInt(ints[0]) - 1;  // parent of node i is node p
            long w = Long.parseLong(ints[1]);

            if (tree[p] == null)
                tree[p] = new ArrayList<>();
            tree[p].add(i);
            edges[i] = w;  // edge length of node i to its parent
        }


        dfs(0, tree, ret, new ArrayList<>(), new ArrayList<>(), a, edges);

        for (long r : ret) {
            writer.write(String.valueOf(r));
            writer.write(' ');
        }

        writer.close();

    }


    private static void dfs(int u, List<Integer>[] tree, int[] ret, ArrayList<Integer> ancestorStack,
                            ArrayList<Long> ancestorsL2Root, long[] a, long[] edges) {
        long myL2root = edges[u];
        if (!ancestorStack.isEmpty()) {
            myL2root += ancestorsL2Root.get(ancestorStack.size() - 1);
            long partitionPoint = myL2root - a[u];
            int found = Collections.binarySearch(ancestorsL2Root, partitionPoint);
            if (found >= 0)
                found--;
            else
                found = -found - 2;  // (insertion point)
            // now found is the ancestor where adding should stop
            int addStart = ancestorStack.size() - 1;
            if (found < addStart) {
                if (found >= 0)
                    ret[ancestorStack.get(found)]--;
                ret[ancestorStack.get(addStart)]++;
            }
        }
        ancestorStack.add(u);
        ancestorsL2Root.add(myL2root);

        if (tree[u] != null)
            for (int v : tree[u]) {
                dfs(v, tree, ret, ancestorStack, ancestorsL2Root, a, edges);
            }

        // now last in ancestor is u
        if (ancestorStack.size() - 2 >= 0)
            ret[ancestorStack.get(ancestorStack.size() - 2)] += ret[u];
        ancestorStack.remove(ancestorStack.size() - 1);
        ancestorsL2Root.remove(ancestorsL2Root.size() - 1);

    }

}
