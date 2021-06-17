package c;

import java.util.*;

public class D {
    static final int MAX_WAIT = 1800;
    static final int OP_TIME = 60;
    static final int SAIL_TIME = 20;


    private static int cost(int j, int i, int[] arrival) {
        return 120 + Math.max(arrival[i] + 20 - (arrival[j] + 1800), 20 * (i - j + 1));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] arrival = new int[n];
        for (int i = 0; i < n; i++){
            scanner.nextLine();
            arrival[i] = scanner.nextInt();
        }
        int[] dp = new int[n];
        dp[0] = cost(0, 0, arrival);
        for (int i = 1; i < n; i++) {
            List<Integer> candidates = new ArrayList<>();
            for (int k = 0; k < i; k++) {
                candidates.add(dp[k] + cost(k + 1, i, arrival));
            }
            dp[i] = Collections.min(candidates);
        }

        System.out.println(dp[n - 1]);

    }
}
