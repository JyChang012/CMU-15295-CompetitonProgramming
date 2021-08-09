package w6c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;


// Time limit exceeded on test 7

public class D {

    static class DistVexComparator implements Comparator<SimpleEntry<Long, Integer>> {
        @Override
        public int compare(SimpleEntry<Long, Integer> o1, SimpleEntry<Long, Integer> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        long[] u = new long[n], z = new long[n];
        ArrayList<Integer>[] to = new ArrayList[n], from = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            String[] line = reader.readLine().split(" ");
            u[i] = Long.parseLong(line[0]);
            z[i] = Long.parseLong(line[1]);
            to[i] = new ArrayList<>();
            for (int j = 3; j < line.length; j++) {
                int head = Integer.parseInt(line[j]) - 1;
                to[i].add(head);
                if (from[head] == null)
                    from[head] = new ArrayList<>();
                from[head].add(i);
            }
        }

        reader.close();

        long[] dist = Arrays.copyOf(z, z.length);
        long[] oldDist = Arrays.copyOf(z, z.length);
        long[] distByCut = new long[n];

        for (int i = 0; i < n; i++) {
            long curDist = u[i];
            for (int child : to[i]) {
                curDist += oldDist[child];
            }

            if (curDist < dist[i]) {
                dist[i] = curDist;
                distByCut[i] = -1;
            } else {
                distByCut[i] = curDist;
            }
        }

        PriorityQueue<SimpleEntry<Long, Integer>> pq = new PriorityQueue<>(new DistVexComparator());  // pair of current dist and vertices ID
        for (int i = 0; i < n; i++) {
            pq.add(new SimpleEntry<>(dist[i], i));
        }

        while (!pq.isEmpty()) {
            SimpleEntry<Long, Integer> cur = pq.poll();
            long curDist = cur.getKey();
            int curID = cur.getValue();
            long diff = oldDist[curID] - curDist;

            if (diff > 0) {
                if (dist[curID] == curDist && from[curID] != null) {  // update all its neighbor
                    for (int neighbor : from[curID]) {
                        if (distByCut[neighbor] < 0) {
                            dist[neighbor] -= diff;
                            pq.add(new SimpleEntry<>(dist[neighbor], neighbor));
                        } else {
                            distByCut[neighbor] -= diff;
                            if (distByCut[neighbor] < dist[neighbor]) {
                                dist[neighbor] = distByCut[neighbor];
                                distByCut[neighbor] = -1;
                                pq.add(new SimpleEntry<>(dist[neighbor], neighbor));
                            }
                        }

                    /*
                    long newDist = u[neighbor];
                    for (int nn : to[neighbor]) {
                        newDist += dist[nn];
                    }
                    if (newDist < dist[neighbor]) {
                        dist[neighbor] = newDist;
                        pq.add(new SimpleEntry<>(newDist, neighbor));
                    }

                     */
                    }
                }
                oldDist[curID] = curDist;
            }
        }
        System.out.println(dist[0]);

    }
}
