package w4.c;

import java.util.Arrays;
import java.util.Scanner;

// still exceed time limit
// TODO: need further further optimization

public class Ev2 {
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

        int[][] topple = calculateTopple(trees, h);
        double[][][][] cache = new double[n + 1][n + 1][2][2];
        System.out.println(solve(1, n, 0, 0, h, p, trees, cache, topple[0], topple[1]));
    }


    private static int[][] calculateTopple(long[] trees, long h) {
        int[][] ret = new int[2][];
        int[] right = new int[trees.length], left = new int[trees.length];
        int start = 0, end = 1;
        while (end < trees.length) {
            long dist = trees[end] - trees[end - 1];
            if (dist < h)   // end - 1 will topple end
                end++;
            else {          // end - 1 will not
                for (int i = start; i < end; i++) {
                    right[i] = end - 1;
                }
                start = end;
                end++;
            }
        }
        right[right.length - 1] = right.length - 1;
        /*
        start = trees.length - 1;
        end = trees.length - 2;
        while (end > -1) {
            long dist = trees[end + 1] - trees[end];
            if (dist < h)
                end--;
            else {
                for (int i = start; i > end; i--) {
                    left[i] = end + 1;
                }
                start = end;
                end--;
            }
        }
         */
        start = 0;
        while (start < trees.length) {
            end = right[start];
            for (int i = start; i <= end; i++) {
                left[i] = start;
            }
            start = end + 1;
        }

        ret[0] = left;
        ret[1] = right;
        return ret;
    }

    private static double solve(int left, int right, int leftState, int rightState, long h, double p, long[] trees, double[][][][] cache, int[] tplLeft, int[] tplRight) {
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
                int rightmostFall = Math.min(right - 1, tplRight[left - 1]);
                ret = trees[rightmostFall] - trees[left - 1] + solve(rightmostFall + 1, right, 1, rightState, h, p, trees, cache, tplLeft, tplRight);
            } else if (rightState == 1 && rDist < h) {
                int leftmostFall = Math.max(left + 1, tplLeft[right + 1]);
                ret = trees[right + 1] - trees[leftmostFall] + solve(left, leftmostFall - 1, leftState, 1, h, p, trees, cache, tplLeft, tplRight);
            } else {
                // if leftmost is chosen
                if (leftState == 1) {
                    // if leftmost is chosen and fall down outward (left)
                    ret += (Math.min(lDist, 2 * h) + solve(left + 1, right, 0, rightState, h, p, trees, cache, tplLeft, tplRight)) * .5 * p;
                    // if leftmost is chosen and fall down inward (right)
                    ret += (h + solve(left + 1, right, 1, rightState, h, p, trees, cache, tplLeft, tplRight)) * .5 * (1 - p);
                } else {
                    // if leftmost is chosen and fall down outward (left)
                    ret += (Math.min(lDist, h) + solve(left + 1, right, 0, rightState, h, p, trees, cache, tplLeft, tplRight)) * .5 * p;
                    // if leftmost is chosen and fall down inward (right)
                    ret += (solve(left + 1, right, 1, rightState, h, p, trees, cache, tplLeft, tplRight)) * .5 * (1 - p);
                }

                // if rightmost is chosen
                if (rightState == 1) {
                    // if rightmost is chosen and fall down outward (right)
                    ret += (Math.min(rDist, 2 * h) + solve(left, right - 1, leftState, 0, h, p, trees, cache, tplLeft, tplRight)) * .5 * (1 - p);
                    // if rightmost is chosen and fall down inward (left)
                    ret += (h + solve(left, right - 1, leftState, 1, h, p, trees, cache, tplLeft, tplRight)) * .5 * p;
                } else {
                    // if rightmost is chosen and fall down outward (right)
                    ret += (Math.min(rDist, h) + solve(left, right - 1, leftState, 0, h, p, trees, cache, tplLeft, tplRight)) * .5 * (1 - p);
                    // if rightmost is chosen and fall down inward (left)
                    ret += (solve(left, right - 1, leftState, 1, h, p, trees, cache, tplLeft, tplRight)) * .5 * p;
                }
            }
        }

        cache[left][right][leftState][rightState] = ret;
        return ret;
    }


}
