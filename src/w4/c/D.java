package w4.c;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


// Time limit exceeded on test 14.

// Problem of this implementation: the saved cache[n][k][m], which is the cost of subtree rooted at n, with nearest mill
// at m and k additional mill to add, does not save the intermediate cost. The correct way is to save subtree rooted at
// V, ignoring the first C children of V, given K additional sawmills to place and nearest mill at m.

public class D {

    private static class Node {
        ArrayList<Node> children = new ArrayList<>();
        int id = 0;
        // int rightSiblingID = -1;
        int w;  // weight or tree cut each year
        int d;  // length to parent
        int parentId;

        public void addChild(Node newChild) {
            // Node prevLastChild = children.get(children.size() - 1);
            // prevLastChild.rightSiblingID = children.size();
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

        // int[][] dist = new int[n + 1][n + 1];
        // for (int[] c : dist)
        //     Arrays.fill(c, -1);
        // calDist(root, dist, villages);

        int[][][] cache = new int[n + 1][n + 1][k + 1];
        for (int[][] c : cache)
            for (int[] cc : c)
                Arrays.fill(cc, Integer.MIN_VALUE);

        System.out.println(solve(root, 0, k, 0, cache));

    }

    /*
    private static void calDist(Node root, int[][] dist, Node[] villages) {
        calDistFromSink(root, root, dist, villages);
        for (Node child : root.children)
            calDist(child, dist, villages);
    }

    private static void calDistFromSink(Node sink, Node subtree, int[][] dist, Node[] villages) {
        if (subtree.children.isEmpty())
            return;

        for (Node child : subtree.children) {
            calDistBetween(sink.id, child.id, dist, villages);
            calDistFromSink(sink, child, dist, villages);
        }
    }

    private static int calDistBetween(int sinkId, int sourceId, int[][] dist, Node[] villages) {
        Node sink = villages[sinkId], source = villages[sourceId];
        if (dist[sink.id][source.id] >= 0)
            return dist[sink.id][source.id];

        int ret;
        if (source.parentId == sinkId) {
            ret = source.d;
        } else {
            ret = calDistBetween(sinkId, source.parentId, dist, villages) + source.d;

        }
        dist[sink.id][source.id] = ret;
        return ret;

    }

     */


    private static int variableNestedLoop(Node node, int lastMill, int childId, int left, int dist, int[][][] cache) {
        Node child = node.children.get(childId);
        int childGlobalId = child.id;

        ArrayList<Node> children = node.children;
        int ret = Integer.MAX_VALUE;
        if (childId == children.size() - 1) {
            int min = Math.min(1, left);
            for (int hasMill = 0; hasMill <= min; hasMill++) {
                int newLastMill, newDist;
                if (hasMill == 1) {
                    newLastMill = childGlobalId;
                    newDist = 0;
                } else {
                    newLastMill = lastMill;
                    newDist = dist + child.d;
                }

                int newRet = solve(child, newLastMill, left - hasMill, newDist, cache);
                ret = Math.min(ret, newRet);
            }


        } else {
            for (int alloc = 0; alloc <= left; alloc++) {
                int min = Math.min(1, alloc);
                for (int hasMill = 0; hasMill <= min; hasMill++) {
                    int newLastMill, newDist;
                    if (hasMill == 1) {
                        newLastMill = childGlobalId;
                        newDist = 0;
                    } else {
                        newLastMill = lastMill;
                        newDist = dist + child.d;
                    }

                    int newRet = solve(child, newLastMill, alloc - hasMill, newDist, cache);
                    newRet += variableNestedLoop(node, lastMill, childId + 1, left - alloc, dist, cache);
                    ret = Math.min(ret, newRet);
                }

            }
        }

        return ret;

    }

    private static int solve(Node node, int lastMill, int k, int dist, int[][][] cache) {
        if (cache[node.id][lastMill][k] != Integer.MIN_VALUE)
            return cache[node.id][lastMill][k];


        int ret = 0;

        if (!node.children.isEmpty()) {
            ret = variableNestedLoop(node, lastMill, 0, k, dist, cache);
        }

        if (node.id != lastMill)
            ret += dist * node.w;

        cache[node.id][lastMill][k] = ret;
        return ret;
    }


}
