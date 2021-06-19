package w2.c;

import java.util.*;

public class B {
    private static class CompareStart implements Comparator<int[]> {

        @Override
        public int compare(int[] o1, int[] o2) {
            return o1[0] - o2[0];
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        // TreeMap<Integer, Integer> startMap = new TreeMap<>();
        ArrayList<int[]> startMap = new ArrayList<>();
        for (int i = 0; i < n; i++) {  // nlog(n)
            scanner.nextLine();
            int start = scanner.nextInt(), end = scanner.nextInt();
            startMap.add(new int[] {start, end});
        }

        startMap.sort(new CompareStart());

        int maxEnd = -1;
        int ret = 0;
        for (int[] startEntry: startMap) {
            if (startEntry[1] < maxEnd)
                ret++;
            maxEnd = Math.max(maxEnd, startEntry[1]);
        }

        System.out.println(ret);
    }
}
