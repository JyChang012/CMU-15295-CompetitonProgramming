package w4.c;


import java.util.Arrays;
import java.util.Scanner;


// pre-comoute cost

public class Bv3 {
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
        for (int[] c : arr)
            Arrays.fill(c, -1);
    }


    private static void minDist(int n, int k, int[] locs) {
        int[][] cache = new int[n + 1][k + 2];
        int[][] decision = new int[n + 1][k + 2];

        setConst(cache);
        setConst(decision);


        int ret = solve(n, k + 1, locs, cache, decision, getCost(locs));
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

    // calculate cost[i][j]: the cost of villages between post offices at i and j, easily in O(n^3), but could be in amortized O(n^2) as follows
    private static int[][] getCost(int[] loc) {
        int[][] cost = new int[loc.length][loc.length];
        for (int i = 0; i < loc.length - 1; i++) {
            cost[i][i] = 0;
            cost[i][i + 1] = 0;
            int partition = i + 1; // this is the first point closer to the right post office
            for (int j = i + 2; j < loc.length; j++) {
                cost[i][j] = cost[i][j - 1];
                for (int k = partition; k <= j - 2; k++) {
                    int toLeft = loc[k] - loc[i], toRight = loc[j] - loc[k];  // for each i, the total number of comparison is at most n, so the total time is squared
                    if (toLeft < toRight) {
                        cost[i][j] += (toLeft - (loc[j - 1] - loc[k]));
                        partition = k + 1;
                    } else {
                        break;
                    }
                }

                int toLeft = loc[j - 1] - loc[i], toRight = loc[j] - loc[j - 1];
                if (partition <= j - 2) {
                    cost[i][j] += (j - partition - 1) * toRight;
                }

                if (toLeft < toRight) {
                    cost[i][j] += toLeft;
                    partition = j;
                } else {
                    cost[i][j] += toRight;
                }

            }
        }
        return cost;

    }

    /*
    n: # of villages and # of village with the rightmost post office
    k: # of post office
     */
    private static int solve(int n, int k, int[] locs, int[][] cache, int[][] decision, int[][] cost) {
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
                int newRet = cost[pp][n];
                newRet += solve(pp, k - 1, locs, cache, decision, cost);
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
