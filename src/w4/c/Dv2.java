package w4.c;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


// Note: see the answer about the key for cache
// O(2*n^2*k^2)

public class Dv2 {
    private static class Node {
        ArrayList<Node> children = new ArrayList<>();
        int id = 0;
        int w;  // weight or tree cut each year
        int d;  // length to parent
        int parentId;

        public void addChild(Node newChild) {
            children.add(newChild);
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), k = scanner.nextInt();
        Node root = new Node();
        Node[] villages = new Node[n + 1];
        villages[0] = root;
        for (int i = 1; i <= n; i++) {
            scanner.nextLine();
            Node village = new Node();
            villages[i] = village;
            village.id = i;
            village.w = scanner.nextInt();
            village.parentId = scanner.nextInt();
            village.d = scanner.nextInt();
        }

        for (int i = 1; i <= n; i++) {
            villages[villages[i].parentId].addChild(villages[i]);
        }

        int[][][] cache = new int[n + 1][n + 1][k + 1];
        for (int[][] c : cache)
            for (int[] cc : c)
                Arrays.fill(cc, Integer.MIN_VALUE);

        System.out.println(solve(root, 0, k, 0, 0, cache));
    }



    private static int solve(Node parent, int lastMillId, int k, int childId, int dist2lastMill, int[][][] cache) {
        if (parent.children.isEmpty() || childId >= parent.children.size()) {
            return 0;
        }


        Node child = parent.children.get(childId);
        int childGlobalId = child.id;

        if (cache[childGlobalId][lastMillId][k] != Integer.MIN_VALUE) {
            return cache[childGlobalId][lastMillId][k];
        }



        int ret = Integer.MAX_VALUE;
        int kk = 0, maxKk = k;  // kk is the # of mills allocated to the subtree rooted at the current child

        if (child.children.isEmpty()) {  // if the current child does not have children, allocate at most 1 mills to it
            maxKk = Math.min(1, maxKk);
        }

        if (childId == parent.children.size() - 1) {  // if it's the last child, allocate all k mills to it
            kk = maxKk;
        }


        for (; kk <= maxKk; kk++) {
            int ret1 = Integer.MAX_VALUE;
            // if place a mill on the current child
            if (kk != 0) {
                ret1 = solve(child, childGlobalId, kk - 1, 0, 0, cache);
            }

            // if not place a mill on the current child
            int ret2 = solve(child, lastMillId, kk, 0, dist2lastMill + child.d, cache) + (dist2lastMill + child.d) * child.w;

            ret1 = Math.min(ret1, ret2);
            ret1 += solve(parent, lastMillId, k - kk, childId + 1, dist2lastMill, cache);

            ret = Math.min(ret, ret1);
        }

        cache[childGlobalId][lastMillId][k] = ret;
        return ret;
    }



}
