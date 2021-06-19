package w3.c;

import java.util.*;



// write arraylist are expansive!!
public class D {
    public static double calculateWeakness(double[] prefix, double x) {
        // maximum subarrays
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (int i = 0; i < prefix.length; i++) {
            double val = prefix[i] - (i + 1) * x;
            max = Math.max(val, max);
            min = Math.min(val, min);
        }
        double ret = max - min;
        ret = Math.max(ret, Math.abs(max));
        ret = Math.max(ret, Math.abs(min));

        return ret;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        double[] prefix = new double[n];
        prefix[0] = scanner.nextInt();
        double x0 = Integer.MAX_VALUE, x3 = Integer.MIN_VALUE, x1, x2, f1 = 0, f2 = 0;

        for (int i = 1; i < n; i++) {
            int val = scanner.nextInt();
            prefix[i] = val + prefix[i - 1];

            x0 = Math.min(x0, val);
            x3 = Math.max(x3, val);
        }

        int it = 100;

        // ternary search
        for (int i = 0; i < it; i++) {
            x1 = (2 * x0 + x3) / 3;
            x2 = (x0 + 2 * x3) / 3;

            f1 = calculateWeakness(prefix, x1);
            f2 = calculateWeakness(prefix, x2);

            if (f1 > f2)
                x0 = x1;
            else
                x3 = x2;
        }

        System.out.println((f1 + f2) / 2);


    }
}
