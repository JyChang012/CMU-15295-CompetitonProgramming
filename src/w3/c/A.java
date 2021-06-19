package w3.c;

import java.util.Arrays;
import java.util.Scanner;

public class A {

    /*
    check if d could be achieved by some arranagements
     */
    public static boolean checkMin(int d, int c, int[] positions) {
        int lastCow = positions[0], nC = 1;
        for (int i = 1; i < positions.length; i++) {
            if (positions[i] - lastCow >= d) {
                if (nC >= c)
                    break;
                lastCow = positions[i];
                nC++;
            }
        }

        return nC >= c;
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), c = scanner.nextInt();
        int[] positions = new int[n];
        for (int i = 0; i < n; i++) {
            scanner.nextLine();
            positions[i] = scanner.nextInt();
        }

        Arrays.sort(positions);

        int lo = 0, hi = (int) 1e9, mid;

        while (lo + 1 < hi) {
            mid = lo + (hi - lo) / 2;
            if (checkMin(mid, c, positions)) {
                lo = mid;
            } else{
                hi = mid;
            }
        }

        System.out.println(lo);

    }
}
