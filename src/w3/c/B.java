package w3.c;

import java.util.Arrays;
import java.util.Scanner;

public class B {
    // O(qlog(n))


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), q = scanner.nextInt();
        long[] strengths = new long[n], attacks = new long[q];

        scanner.nextLine();
        strengths[0] = scanner.nextLong();
        for (int i = 1; i < n; i++)
            strengths[i] = scanner.nextLong() + strengths[i - 1];

        scanner.nextLine();

        for (int i = 0; i < q; i++)
            attacks[i] = scanner.nextLong();

        StringBuilder out = new StringBuilder();

        long damage = 0;

        for (int t = 0; t < q; t++) {
            damage += attacks[t];
            int found = Arrays.binarySearch(strengths, damage);

            if (found >= 0) {  // found!
                if (found == n - 1) {  // all die!
                    out.append(n).append('\n');
                    damage = 0;
                } else
                    out.append(n - found - 1).append('\n');
            } else {
                found = -found - 1;  // insertion point
                if (found == strengths.length) {  // all die
                    out.append(n).append('\n');
                    damage = 0;
                } else {
                    out.append(n - found).append('\n');
                }
            }
        }

        System.out.print(out);



    }
}
