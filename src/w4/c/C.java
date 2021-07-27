package w4.c;

import java.util.Arrays;
import java.util.Scanner;

/*
    state:
    0. go through row 0 and never come back
    1. go through row 1 and never come back
    2. go through row 2 and never come back
    3. go through row 0 and come back
    4. go through row 2 and come back
 */

// Note: Long[] is much slower (~40%) and uses (>=2 times) more space than long[]!!!


public class C {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int column = scanner.nextInt();
        long[][] grid = new long[3][column];

        for (int r = 0; r < 3; r++) {
            scanner.nextLine();
            for (int c = 0; c < column; c++)
                grid[r][c] = scanner.nextLong();
        }

        long[][] cache = new long[column][5];
        for (long[] c: cache)
            Arrays.fill(c, Long.MIN_VALUE);

        long ret = solve(0, 0, grid, cache);
        System.out.print(ret);

    }


    private static long solve(int bar, int state, long[][] grid, long[][] cache) {
        if (cache[bar][state] != Long.MIN_VALUE) {
            return cache[bar][state];
        }


        long ret = 0;
        if (bar == grid[0].length - 1) {
            switch (state) {
                case 3:
                case 0:
                    ret += grid[0][bar];
                case 1:
                    ret += grid[1][bar];
                case 2:
                    ret += grid[2][bar];
                    break;
                case 4:
                    ret = Long.MIN_VALUE;  // should never go there!! Note: if a small negative num was added to this, it would underflow to a large positive number, ruining the whole process!!
            }
        } else {
            switch (state) {
                case 0:
                    ret = grid[0][bar] + solve(bar + 1, 0, grid, cache);
                    ret = Math.max(ret, grid[0][bar] + grid[1][bar] + solve(bar + 1, 1, grid, cache));
                    ret = Math.max(ret, grid[0][bar] + grid[1][bar] + grid[2][bar] + solve(bar + 1, 2, grid, cache));
                    ret = Math.max(ret, grid[0][bar] + grid[1][bar] + grid[2][bar] + solve(bar + 1, 3, grid, cache));
                    break;
                case 1:
                    ret = grid[1][bar] + solve(bar + 1, 1, grid, cache);
                    ret = Math.max(ret, grid[1][bar] + grid[0][bar] + solve(bar + 1, 0, grid, cache));
                    ret = Math.max(ret, grid[1][bar] + grid[2][bar] + solve(bar + 1, 2, grid, cache));
                    break;
                case 2:
                    ret = grid[2][bar] + solve(bar + 1, 2, grid, cache);
                    ret = Math.max(ret, grid[2][bar] + grid[1][bar] + solve(bar + 1, 1, grid, cache));
                    ret = Math.max(ret, grid[2][bar] + grid[1][bar] + grid[0][bar] + solve(bar + 1, 0, grid, cache));
                    if (bar < grid[0].length - 2)
                        ret = Math.max(ret, grid[2][bar] + grid[1][bar] + grid[0][bar] + solve(bar + 1, 4, grid, cache));
                    break;
                case 3:
                    ret = grid[0][bar] + grid[1][bar] + grid[2][bar] + solve(bar + 1, 2, grid, cache);  // go back immediately
                    ret = Math.max(ret, grid[0][bar] + grid[1][bar] + grid[2][bar] + solve(bar + 1, 3, grid, cache));
                    break;
                case 4:
                    ret = grid[2][bar] + grid[1][bar] + grid[0][bar] + solve(bar + 1, 0, grid, cache);
                    if (bar < grid[0].length - 2)
                        ret = Math.max(ret, grid[2][bar] + grid[1][bar] + grid[0][bar] + solve(bar + 1, 4, grid, cache));
            }
        }

        cache[bar][state] = ret;
        return ret;

    }


}
