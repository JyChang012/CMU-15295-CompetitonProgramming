package p;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class A {
    private static boolean compare(String s1, String s2) {
        return s1.length() < s2.length();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            titles.add(scanner.nextLine());
        }

        // int maxL = Collections.max(titles);


    }

    private static <T> int compare(T a, T b) {
        return 0;
    }
}
