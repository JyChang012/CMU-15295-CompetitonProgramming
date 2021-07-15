package w4.c;

import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Scanner;

public class A {


    public static long char2digit(char c) {
        return c - '0';
    }


    public static long solveBox(long wide, long height) {
        /*
        int[] repeat = new int[wide + 1];
        for (int i = 1; i <= wide; i++) {
            if (wide - i + 1 > height) {
                repeat[i] = 0;
            } else {
                int temp = height;
                for (int j = 1; j < wide - i + 1; j++) {
                    temp *= (height - j);
                }
                repeat[i] = temp;
            }
        }
         */
        if (wide == 1) {
            return height;
        } else if (height == 0) {
            return 0;
        }


        long ret = 0;
        for (int i = 1; i <= wide; i++) {
            if (wide - i + 1 <= height) {
                long temp = height;
                for (int j = 1; j < wide - i + 1; j++) {
                    temp *= (height - j);
                }
                ret += temp;
            }
        }
        return ret;

    }


    public static long calculateRamp(long[] arr) {
        int i = arr.length - 2, wide = 1;
        long prev = arr[arr.length - 1];
        long ret = 0;
        while (i >= 0) {
            long diff = prev - arr[i];
            if (diff != 0) {
                ret += solveBox(wide, diff + 1);
                prev = arr[i];
            }
            wide += 1;
            i--;
        }
        if (ret == 0)
            return solveBox(arr.length, arr[0] + 1);
        else
            return ret;
    }


    public static boolean checkRamp(long[] arr) {
        long last = Long.MIN_VALUE;
        for (long cur: arr) {
            if (cur < last)
                return false;
            else
                last = cur;
        }
        return true;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char[] cA = scanner.nextLine().toCharArray();
            long[] digits = new long[cA.length];
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
