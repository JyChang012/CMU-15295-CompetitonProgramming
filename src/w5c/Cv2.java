package w5c;

import java.io.*;
import java.util.*;


// bfs is actually not needed, 2 times faster

public class Cv2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        int n = Integer.parseInt(reader.readLine()), root = 0;
        List<Integer>[] tree = new List[n];
        boolean[] respect = new boolean[n];
        for (int i = 0; i < n; i++) {
            String[] ints = reader.readLine().split(" ");
            int parent = Integer.parseInt(ints[0]) - 1, c = Integer.parseInt(ints[1]);
            if (parent == -2)
                root = i;
            else {
                if (tree[parent] == null)
                    tree[parent] = new ArrayList<>();
                tree[parent].add(i);
            }
            respect[i] = c == 0;

        }

        List<Integer> deleteList = new ArrayList<>();

        for (int u = 0; u < n; u++) {
            List<Integer> children = tree[u];
            boolean delete = !respect[u];
            if (children != null) {
                for (int child: children) {
                    if (respect[child]) {
                        delete = false;
                        break;
                    }
                }

            }
            if (delete) {
                deleteList.add(u);
            }
        }

        if (deleteList.isEmpty())
            writer.write(String.valueOf(-1));
        else {
            Collections.sort(deleteList);
            for (int u: deleteList) {
                writer.write(String.valueOf(u + 1));
                writer.write(' ');
            }
        }

        writer.close();
    }
}
