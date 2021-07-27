package w4.c;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class D {
    private static class Tree {
        Node root = new Node();
    }

    private static class Node {
        ArrayList<Node> children = new ArrayList<>(10);
        int id = 0;
        // int rightSiblingID = -1;
        int w;  // weight or tree cut each year
        int d;  // length to parent
        Node parent;
        /*
        public Node getRightSibling() {
            if (rightSiblingID > 0)
                return parent.children.get(rightSiblingID);
            else
                return null;
        }
         */

        public void addChild(Node newChild) {
            // Node prevLastChild = children.get(children.size() - 1);
            // prevLastChild.rightSiblingID = children.size();
            children.add(newChild);
        }
    }

    /*
    private static void calculateWeight(Node node) {
        if (node.children.isEmpty()) {
            return;
        }

        for (Node child: node.children) {
            calculateWeight(child);
            node.curW += child.curW + child.w;
        }
    }
     */


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), k = scanner.nextInt();
        Tree tree = new Tree();
        Node[] villages = new Node[n + 1];
        villages[0] = tree.root;
        for (int i = 1; i <= n; i++) {
            scanner.nextLine();
            Node village = new Node();
            villages[i] = village;
            village.id = i;
            village.w = scanner.nextInt();
            village.parent = villages[scanner.nextInt()];
            village.d = scanner.nextInt();
            village.parent.addChild(village);
        }

        int[][] dist = new int[n + 1][n + 1];
        for (int[] c : dist)
            Arrays.fill(c, -1);
        calDist(tree.root, dist);

        int[][][] cache = new int[n + 1][n + 1][k + 1];
        for (int[][] c : cache)
            for (int[] cc : c)
                Arrays.fill(cc, Integer.MIN_VALUE);

        System.out.println(solve(tree.root, 0, k, dist, cache));

    }


    private static void calDist(Node root, int[][] dist) {
        calDistFromSink(root, root, dist);
        for (Node child : root.children)
            calDist(child, dist);
    }

    private static void calDistFromSink(Node sink, Node subtree, int[][] dist) {
        if (subtree.children.isEmpty())
            return;

        for (Node child : subtree.children) {
            calDistBetween(sink, child, dist);
            calDistFromSink(sink, child, dist);
        }
    }

    private static int calDistBetween(Node sink, Node source, int[][] dist) {
        if (dist[sink.id][source.id] >= 0)
            return dist[sink.id][source.id];

        int ret;
        if (source.parent == sink) {
            ret = source.d;
        } else {
            ret = calDistBetween(sink, source.parent, dist) + source.d;

        }
        dist[sink.id][source.id] = ret;
        return ret;

    }


    private static int variableNestedLoop(Node node, int lastMill, int childId, int left, int[][] dist, int[][][] cache) {
        ArrayList<Node> children = node.children;
        int ret = Integer.MAX_VALUE;
        if (childId == children.size() - 1) {
            int min = Math.min(1, left);
            for (int hasMill = 0; hasMill <= min; hasMill++) {
                int newRet = solve(node.children.get(childId), hasMill == 1 ? node.children.get(childId).id : lastMill, left - hasMill, dist, cache);
                ret = Math.min(ret, newRet);
            }


        } else {
            for (int alloc = 0; alloc <= left; alloc++) {
                int min = Math.min(1, alloc);
                for (int hasMill = 0; hasMill <= min; hasMill++) {
                    int newRet = solve(node.children.get(childId), hasMill == 1 ? node.children.get(childId).id : lastMill, alloc - hasMill, dist, cache);
                    newRet += variableNestedLoop(node, lastMill, childId + 1, left - alloc, dist, cache);
                    ret = Math.min(ret, newRet);
                }

            }
        }

        return ret;

    }

    private static int solve(Node node, int lastMill, int k, int[][] dist, int[][][] cache) {
        if (cache[node.id][lastMill][k] != Integer.MIN_VALUE)
            return cache[node.id][lastMill][k];


        int ret = 0;

        if (!node.children.isEmpty()) {
            ret = variableNestedLoop(node, lastMill, 0, k, dist, cache);
        }

        if (node.id != lastMill)
            ret += dist[lastMill][node.id] * node.w;

        cache[node.id][lastMill][k] = ret;
        return ret;
    }


}
