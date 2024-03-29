package w4.c;

import java.util.Arrays;
import java.util.Scanner;

public class B {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), k = scanner.nextInt();
        scanner.nextLine();
        int[] locs = new int[n + 1];
        for (int i = 0; i < n; i++) {
            locs[i] = scanner.nextInt();
        }

        locs[locs.length - 1] = Integer.MAX_VALUE;  // make an additional village at inf pos to simplify code

        minDist(n, k, locs);

    }


    private static void setConst(int[][][] arr, int n) {
        for (int[][] c: arr)
            for (int[] cc: c)
                Arrays.fill(cc, n);
    }


    private static void minDist(int n, int k, int[] locs) {
        int[][][] cache = new int[n + 1][k + 2][n + 1];
        int[][][] decision = new int[n + 1][k + 2][n + 1];

        setConst(cache, -1);
        setConst(decision, -1);


        int ret = solve(n, k + 1, n, locs, cache, decision);
        /*
        for (int p = k - 1; p < n; p++) {
            ret = Math.min(ret, solve(n - 1, k, p, locs, cache));
        }
         */
        StringBuilder out = new StringBuilder();
        out.append(ret).append('\n');

        // restore the decision using decision table
        int[] post = new int[k];
        int ii = 0, nn = n;
        for (int kk = k + 1; kk > 1; kk--) {
            post[ii] = decision[nn][kk][nn];
            nn = post[ii];
            ii++;
        }

        for (int i = k - 1; i >= 0; i--) {
            out.append(locs[post[i]]).append(' ');
        }
        System.out.print(out);
    }

    /*
    n: # of villages
    k: # of post office
    p: # of village with the rightmost post office
    p <= n
     */
    private static int solve(int n, int k, int p, int[] locs, int[][][] cache, int[][][] decision) {
        if (cache[n][k][p] >= 0)
            return cache[n][k][p];

        int ret = 0;

        if (p < n) {
            for (int i = p + 1; i <= n; i++)
                ret += locs[i] - locs[p];
            ret += solve(p, k, p, locs, cache, decision);
        } else { // p == n
            if (k == 1) {
                for (int i = 0; i < n; i++) {
                    ret += locs[p] - locs[i];
                }
            } else {
                ret = Integer.MAX_VALUE;
                int opt = 0;
                for (int pp = k - 1; pp <= p - 1; pp++) {
                    int newRet = 0;
                    for (int i = pp + 1; i <= p - 1; i++) {
                        newRet += Math.min(locs[i] - locs[pp], locs[p] - locs[i]);  // TODO: use binary search to find the partition  point, dont need this loop any more!
                    }
                    newRet += solve(pp, k - 1, pp, locs, cache, decision);
                    if (newRet < ret) {
                        ret = newRet;
                        opt = pp;
                    }
                }
                decision[n][k][p] = opt;
            }
        }
        cache[n][k][p] = ret;
        return ret;
    }
}


/*
comment: notice that solve() is always called with p == n, which shows that we could only recurse
on n == p and k. See v2
 */