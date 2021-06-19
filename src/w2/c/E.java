package w2.c;

import java.util.*;

public class E {

    public static long dpFind(long n, long b, List<Long> divisors, Map<Long, Long> cache) {
        if (cache.containsKey(n)) {
            return cache.get(n);
        } else if (n == 1) {
            return 0;
        } else {
            List<Long> newDivisors = new ArrayList<>();
            for (long div: divisors) {
                if (n % div == 0)
                    newDivisors.add(div);
            }

            ArrayList<Long> candidates = new ArrayList<>();
            for (long div: newDivisors) {
                long found = dpFind(n / div, b, newDivisors, cache);
                if (found >= 0 && found <= (Long.MAX_VALUE - div) / b)  // check numerical overflow
                    candidates.add(div + found * b);
            }

            long ret = -1;

            if (!candidates.isEmpty()) {
                ret = Collections.min(candidates);
            }
            cache.put(n, ret);
            return ret;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int b = scanner.nextInt();
        long n = Long.parseLong(scanner.next());

        List<Long> divisors = new ArrayList<>();
        for (long i = 2; i < b; i++) {
            if (n % i == 0)
                divisors.add(i);
        }

        long ret = dpFind(n, b, divisors, new HashMap<>());
        if (ret == -1)
            System.out.println("impossible");
        else
            System.out.println(ret);


    }
}
