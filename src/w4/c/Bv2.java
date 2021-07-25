package w4.c;


import java.util.Arrays;
import java.util.Scanner;

public class Bv2 {
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


    private static void setConst(int[][] arr) {
        for (int[] c: arr)
                Arrays.fill(c, -1);
    }


    private static void minDist(int n, int k, int[] locs) {
        int[][] cache = new int[n + 1][k + 2];
        int[][] decision = new int[n + 1][k + 2];

        setConst(cache);
        setConst(decision);


        int ret = solve(n, k + 1, locs, cache, decision);
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
            post[ii] = decision[nn][kk];
            nn = post[ii];
            ii++;
        }

        for (int i = k - 1; i >= 0; i--) {
            out.append(locs[post[i]]).append(' ');
        }
        System.out.print(out);
    }

    /*
    n: # of villages and # of village with the rightmost post office
    k: # of post office
     */
    private static int solve(int n, int k, int[] locs, int[][] cache, int[][] decision) {
        if (cache[n][k] >= 0)
            return cache[n][k];

        int ret = 0;

            if (k == 1) {
                for (int i = 0; i < n; i++) {
                    ret += locs[n] - locs[i];
                }
            } else {
                ret = Integer.MAX_VALUE;
                int opt = 0;
                for (int pp = k - 1; pp <= n - 1; pp++) {
                    int newRet = 0;
                    for (int i = pp + 1; i <= n - 1; i++) {
                        newRet += Math.min(locs[i] - locs[pp], locs[n] - locs[i]);  // TODO: use binary search to find the partition  point, dont need this loop any more!
                    }
                    newRet += solve(pp, k - 1, locs, cache, decision);
                    if (newRet < ret) {
                        ret = newRet;
                        opt = pp;
                    }
                }
                decision[n][k] = opt;
            }

        cache[n][k] = ret;
        return ret;
    }
}
