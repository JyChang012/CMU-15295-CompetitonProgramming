package w5c;

import javax.xml.stream.FactoryConfigurationError;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


// TODO: unfinished, need dijkstra

public class G {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]), m = Integer.parseInt(line[1]);
        int[][] graph = new int[n][m];  // 0 for empty, -1 for obstacles, 1 for exit
        int rr = 0, rc = 0;
        for (int r = 0; r < n; r++) {
            String nLine = reader.readLine();
            for (int c = 0; c < m; c++) {
                char symbol = nLine.charAt(c);
                if (symbol == 'R') {
                    rr = r;
                    rc = c;
                } else if (symbol == 'E') {
                    graph[r][c] = 1;
                } else if (symbol == '#') {
                    graph[r][c] = -1;
                }
            }
        }


        String command = reader.readLine();
        System.out.println(solve(rr, rc, 0, graph, command, new int[n][m][command.length() + 1]));

    }


    private static int solve(int i, int j, int k, int[][] graph, String command, int[][][] cache) {
        int ret;

        k = Math.min(k, command.length());

        if (checkInvalidCell(i, j, graph)) {
            return Integer.MAX_VALUE / 2;
        } else if (graph[i][j] == 1) {
            return 0;
        } else if (cache[i][j][k] > 0) {
            return cache[i][j][k];
        } else {
            ret = Integer.MAX_VALUE / 2;

            cache[i][j][k] = Integer.MAX_VALUE / 2;  // lock the cell until finish

            /*
            if (k < command.length()) {
                // delete
                ret = Math.min(ret, 1 + solve(i, j, k + 1, graph, command, cache));

                // proceed
                int newI = i, newJ = j;
                char cmd = command.charAt(k);
                if (cmd == 'U')
                    newI++;
                else if (cmd == 'D')
                    newI--;
                else if (cmd == 'R')
                    newJ++;
                else if (cmd == 'L')
                    newJ--;
                if ((!checkInBox(newI, newJ, graph)) || checkObstacles(newI, newJ, graph))
                    ret = Math.min(ret, solve(i, j, k + 1, graph, command, cache));
                else
                    ret = Math.min(ret, solve(newI, newJ, k + 1, graph, command, cache));
            }

            // insert

            // U
            ret = Math.min(ret, 1 + solve(i + 1, j, k, graph, command, cache));
            // D
            ret = Math.min(ret, 1 + solve(i - 1, j, k, graph, command, cache));
            // L
            ret = Math.min(ret, 1 + solve(i, j - 1, k, graph, command, cache));
            // R
            ret = Math.min(ret, 1 + solve(i, j + 1, k, graph, command, cache));
             */

            boolean invalidCommand = false, isCommand;

            // move to a new node
            // Up
            int newI = i - 1, newJ = j, newRet;
            isCommand = k < command.length() && command.charAt(k) == 'U';
            if (!checkInvalidCell(newI, newJ, graph)) {
                if (isCommand)
                    newRet = solve(newI, newJ, k + 1, graph, command, cache);
                else
                    newRet = 1 + solve(newI, newJ, k, graph, command, cache);
                ret = Math.min(ret, newRet);
            } else if (isCommand) {
                invalidCommand = true;
            }

            // right
            newI = i;
            newJ = j + 1;
            isCommand = k < command.length() && command.charAt(k) == 'R';
            if (!checkInvalidCell(newI, newJ, graph)) {
                if (isCommand)
                    newRet = solve(newI, newJ, k + 1, graph, command, cache);
                else
                    newRet = 1 + solve(newI, newJ, k, graph, command, cache);
                ret = Math.min(ret, newRet);
            } else if (isCommand) {
                invalidCommand = true;
            }

            // down
            newI = i + 1;
            newJ = j;
            isCommand = k < command.length() && command.charAt(k) == 'D';
            if (!checkInvalidCell(newI, newJ, graph)) {
                if (isCommand)
                    newRet = solve(newI, newJ, k + 1, graph, command, cache);
                else
                    newRet = 1 + solve(newI, newJ, k, graph, command, cache);
                ret = Math.min(ret, newRet);
            } else if (isCommand) {
                invalidCommand = true;
            }

            // left
            newI = i;
            newJ = j - 1;
            isCommand = k < command.length() && command.charAt(k) == 'L';
            if (!checkInvalidCell(newI, newJ, graph)) {
                if (isCommand)
                    newRet = solve(newI, newJ, k + 1, graph, command, cache);
                else
                    newRet = 1 + solve(newI, newJ, k, graph, command, cache);
                ret = Math.min(ret, newRet);
            } else if (isCommand) {
                invalidCommand = true;
            }

            // not moving
            if (k < command.length()) {
                newRet = solve(i, j, k + 1, graph, command, cache);
                if (!invalidCommand)
                    newRet++;
                ret = Math.min(ret, newRet);
            }

        }

        cache[i][j][k] = ret;
        return ret;
    }


    private static int[] calculateCoordinates(int i, int j, char command, int[][] graph) {
        if (command == 'U')
            j++;
        else if (command == 'D')
            j--;
        else if (command == 'R')
            i++;
        else if (command == 'L')
            i--;
        return new int[]{i, j};

    }


    private static boolean checkOutBox(int i, int j, int[][] graph) {
        return i < 0 || j < 0 || i >= graph.length || j >= graph[0].length;
    }

    private static boolean checkObstacles(int i, int j, int[][] graph) {
        return graph[i][j] == -1;
    }

    private static boolean checkInvalidCell(int i, int j, int[][] graph) {
        return checkOutBox(i, j, graph) || checkObstacles(i, j, graph);
    }


    private static char checkMove(int i, int j, int newI, int newJ) {
        if (newI > i)
            return 'U';
        else if (newI < i)
            return 'D';
        else if (newJ > j)
            return 'R';
        else if (newJ < j)
            return 'L';
        else
            return 'X';
    }


}
