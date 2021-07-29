package w4.c;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.LogManager;


// Time limit exceeded on test 11

public class E {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long h = scanner.nextLong();
        double p = scanner.nextDouble();
        long[] trees = new long[n + 2];

        trees[0] = Long.MIN_VALUE;
        trees[n + 1] = Long.MAX_VALUE;

        scanner.nextLine();
        for (int i = 1; i <= n; i++) {
            trees[i] = scanner.nextLong();
        }
        Arrays.sort(trees);

        trees[0] = trees[1] - h - 1;        // to prevent possible numerical over/underflow
        trees[n + 1] = trees[n] + h + 1;

        double[][][][] cache = new double[n + 1][n + 1][2][2];
        /*
        for (double[][][] c: cache)
            for (double[][] cc: c)
                for (double[] ccc: cc)
                    Arrays.fill(ccc, Double.MIN_VALUE);
         */

        System.out.println(solve(1, n, 0, 0, h, p, trees, cache));

    }

    // leftState: the state of the first tree on the left of left
    // state 0: falls down outward
    // state 1: falls down inward
    // calculate the expected length of fallen tree between left - 1 and right + 1
    // at most one of the states is (state == 1 && dist < h)
    private static double solve(int left, int right, int leftState, int rightState, long h, double p, long[] trees, double[][][][] cache) {
        if (cache[left][right][leftState][rightState] > 0.)
            return cache[left][right][leftState][rightState];


        double ret = 0;
        long lDist = trees[left] - trees[left - 1], rDist = trees[right + 1] - trees[right];


        if (left == right) {
            if (leftState == 1 && lDist < h) {
                ret = lDist;
                if (rightState == 1) {
                    ret += Math.min(rDist, 2 * h);
                } else {
                    ret += Math.min(rDist, h);
                }
            } else if (rightState == 1 && rDist < h) {
                ret = rDist;
                if (leftState == 1) {
                    ret += Math.min(lDist, 2 * h);
                } else {
                    ret += Math.min(lDist, h);
                }
            } else {
                if (leftState == 1 && rightState == 1) {
                    ret = (1 - p) * (Math.min(rDist, 2 * h) + h);
                    ret += p * (Math.min(lDist, 2 * h) + h);
                } else if (leftState == 1 && rightState == 0) {
                    ret = p * Math.min(lDist, 2 * h);
                    ret += (1 - p) * (h + Math.min(rDist, h));
                } else if (leftState == 0 && rightState == 1) {
                    ret = (1 - p) * (Math.min(rDist, 2 * h));
                    ret += p * (h + Math.min(lDist, h));
                } else if (leftState == 0 && rightState == 0) {
                    ret = p * Math.min(lDist, h);
                    ret += (1 - p) * Math.min(rDist, h);
                }

            }


        } else {
            if (leftState == 1 && lDist < h) {
                ret = lDist + solve(left + 1, right, 1, rightState, h, p, trees, cache);
            } else if (rightState == 1 && rDist < h) {
                ret = rDist + solve(left, right - 1, leftState, 1, h, p, trees, cache);
            } else {
                // if leftmost is chosen
                if (leftState == 1) {
                    // if leftmost is chosen and fall down outward (left)
                    ret += (Math.min(lDist, 2 * h) + solve(left + 1, right, 0, rightState, h, p, trees, cache)) * .5 * p;
                    // if leftmost is chosen and fall down inward (right)
                    ret += (h + solve(left + 1, right, 1, rightState, h, p, trees, cache)) * .5 * (1 - p);
                } else {
                    // if leftmost is chosen and fall down outward (left)
                    ret += (Math.min(lDist, h) + solve(left + 1, right, 0, rightState, h, p, trees, cache)) * .5 * p;
                    // if leftmost is chosen and fall down inward (right)
                    ret += (solve(left + 1, right, 1, rightState, h, p, trees, cache)) * .5 * (1 - p);
                }

                // if rightmost is chosen
                if (rightState == 1) {
                    // if rightmost is chosen and fall down outward (right)
                    ret += (Math.min(rDist, 2 * h) + solve(left, right - 1, leftState, 0, h, p, trees, cache)) * .5 * (1 - p);
                    // if rightmost is chosen and fall down inward (left)
                    ret += (h + solve(left, right - 1, leftState, 1, h, p, trees, cache)) * .5 * p;
                } else {
                    // if rightmost is chosen and fall down outward (right)
                    ret += (Math.min(rDist, h) + solve(left, right - 1, leftState, 0, h, p, trees, cache)) * .5 * (1 - p);
                    // if rightmost is chosen and fall down inward (left)
                    ret += (solve(left, right - 1, leftState, 1, h, p, trees, cache)) * .5 * p;
                }
            }
        }

        cache[left][right][leftState][rightState] = ret;
        return ret;
    }
}
