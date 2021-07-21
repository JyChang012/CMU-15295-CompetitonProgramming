package w4.c;

import java.util.Arrays;
import java.util.Scanner;

// see notes

public class A {

    public static int char2digit(char c) {
        return c - '0';
    }


    public static boolean checkRamp(int[] arr) {
        long last = Long.MIN_VALUE;
        for (long cur: arr) {
            if (cur < last)
                return false;
            else
                last = cur;
        }
        return true;
    }


    public static long calculateRamp(int[] digits) {

        long[][] cache = new long[digits.length][10];
        for (long[] ca: cache)
            Arrays.fill(ca, -1);

        return solveUpperBound(digits.length - 1, 0, digits, cache);
    }

    public static long solve(int i, int d, long[][] cache) { // starting from 0

        if (i == 0) {
            return 9 - d + 1;
        }

        if (cache[i][d] >= 0) {
            return cache[i][d];
        }

        long ret = 0;
        for (int dd = d; dd < 10; dd++) {
            ret += solve(i - 1, dd, cache);
        }
        cache[i][d] = ret;
        return ret;
    }

    public static long solveUpperBound(int i, int d, int[] digits, long[][] cache) { // starting from 0
        if (i == 0) {
            return digits[digits.length - 1] - d;
        }

        long ret = 0;
        for (int dd = d; dd < digits[digits.length - i - 1]; dd++) {
            ret += solve(i - 1, dd, cache);
        }
        ret += solveUpperBound(i - 1, digits[digits.length - i - 1], digits, cache);
        return ret;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char[] cA = scanner.nextLine().toCharArray();
            int[] digits = new int[cA.length];
            for (int j = 0; j < digits.length; j++) {
                digits[j] = char2digit(cA[j]);
            }

            if (checkRamp(digits)) {
                out.append(calculateRamp(digits));
            } else {
                out.append(-1);
            }
            out.append('\n');


        }
        System.out.print(out);


    }
}
