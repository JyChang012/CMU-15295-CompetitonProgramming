package c;

import java.util.ArrayList;
import java.util.Scanner;

public class E {

    public static boolean close(double a, double b) {
        return Math.abs(a - b) <= 1e-7;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int b = scanner.nextInt();
        long n = Long.parseLong(scanner.next());
        ArrayList<long[]> out = new ArrayList<>();
        long remaining = n;
        for (int i = b - 1; i > 1 && remaining != 1; i--) {
            int m = 0;
            if (remaining >= i) {
                while (remaining % i == 0) {
                    m++;
                    remaining /= i;
                }

                if (m != 0) {
                    out.add(new long[] {i, m});
                }

            }
        }
        if (out.isEmpty() || remaining != 1) {
            System.out.println("impossible");
        } else {
            long ret = 0;
            int i = 0;
            for (long[] arr: out) {
                long digit = arr[0], m = arr[1];

                for (int j = 0; j < m; j++) {
                    ret += digit * (long) Math.pow((double) b, (double) i + j);
                }
                i += m;
            }

            System.out.println(ret);
        }

    }
}
