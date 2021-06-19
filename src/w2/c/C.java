package w2.c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class C {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(-1);
        for (int i = 0; i < n; i++) {
            list.add(scanner.nextInt());
        }
        int max = Collections.max(list);
        list.set(0, max);
        list.add(max);
        int[] maxBack = new int[list.size()], maxFore = new int[list.size()];
        maxBack[0] = maxBack[maxBack.length - 1] = maxFore[0] = maxFore[maxFore.length - 1] = 0;

        for (int i = 1; i <= n; i++) {
            int j = i - 1;
            while (list.get(j) < list.get(i))
                j = maxBack[j];
            maxBack[i] = j;
        }

        for (int i = n; i >= 1; i--) {
            int j = i + 1;
            while (list.get(j) < list.get(i))
                j = maxFore[j];
            maxFore[i] = j;
        }

        int ret = -1;
        for (int i = 1; i <= n; i++) {
            int fore = -1, back = -1;
            if (maxFore[i] != list.size() - 1)
                fore = list.get(maxFore[i]) ^ list.get(i);
            if (maxBack[i] != 0)
                back = list.get(maxBack[i]) ^ list.get(i);
            ret = Math.max(Math.max(fore, back), ret);
        }
        System.out.println(ret);
    }
}
