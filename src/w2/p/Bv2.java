package w2.p;

import java.util.*;

public class Bv2 {
    /*
    final private static int INSERT = 1;
    final private static int GET_MIN = 2;
    final private static int REMOVE_MIN = 3;

     */
    final private static String INSERT = "insert";
    final private static String GET_MIN = "getMin";
    final private static String REMOVE_MIN = "removeMin";

    private static class Op {
        public String op;
        public int val;

        public Op(String op, int val) {
            this.op = op;
            this.val = val;
        }

        public Op(String op) {
            this.op = op;
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        /*
        List<Op> ops = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            scanner.nextLine();
            String op_string = scanner.next();
            if (scanner.hasNextInt()) {
                ops.add(new Op(op_string, scanner.nextInt()));
            } else {
                ops.add(new Op(op_string));
            }

        }
         */
        ArrayList<Op> new_ops = new ArrayList<>();
        // TreeMap<Integer, Integer> heap = new TreeMap<>();
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int j = 0; j < n; j++) {
            scanner.nextLine();
            Op opp = new Op(scanner.next());
            if (opp.op.charAt(0) != 'r') {
                opp.val = scanner.nextInt();
            }
            switch (opp.op) {
                case INSERT:
                    heap.add(opp.val);
                    new_ops.add(opp);
                    break;
                case REMOVE_MIN:
                    if (heap.isEmpty()) {
                        // new_ops.add(new int[] {INSERT, 5});
                        new_ops.add(new Op(INSERT, 5));
                    } else {
                        heap.remove();
                    }
                    new_ops.add(opp);
                    break;
                case GET_MIN:
                    if (heap.isEmpty()) {
                        heap.add(opp.val);
                        // new_ops.add(new int[] {INSERT, opp.val});
                        new_ops.add(new Op(INSERT, opp.val));
                        new_ops.add(opp);
                    } else if (heap.peek() < opp.val) {
                        // remove all smaller int
                        /*
                        Set<Map.Entry<Integer, Integer>> removeSet = new HashSet<>(heap.headMap(opp.val, false).entrySet());
                        for (Map.Entry<Integer, Integer> entry : removeSet) {
                            Op opToAdd = new Op(REMOVE_MIN, entry.getKey());
                            for (int i = 0; i < entry.getValue(); i++)
                                // new_ops.add(new int[] {REMOVE_MIN, entry.getKey()});  // TODO: what happen if iterator modified
                                new_ops.add(opToAdd);
                            heap.remove(entry.getKey());
                        }
                        if (!heap.containsKey(opp.val)) {
                            // new_ops.add(new int[] {INSERT, opp.val});
                            new_ops.add(new Op(INSERT, opp.val));
                            heap.put(opp.val, 1);
                        }
                        */
                        do {
                            int val = heap.remove();
                            new_ops.add(new Op(REMOVE_MIN, val));
                        } while (heap.peek() != null && heap.peek() < opp.val);
                        if (heap.peek() == null || heap.peek() != opp.val) {  // if not contain
                            new_ops.add(new Op(INSERT, opp.val));
                            heap.add(opp.val);
                        }
                        new_ops.add(opp);
                    } else if (heap.peek() > opp.val) {
                        heap.add(opp.val);
                        new_ops.add(new Op(INSERT, opp.val));
                        new_ops.add(opp);
                    } else {
                        new_ops.add(opp);
                    }
            }
        }
        scanner.close();

        // TODO: multiline edit, update
        System.out.println(new_ops.size());
        for (Op newO : new_ops) {
            if (newO.op.charAt(0) == 'r')
                System.out.println(newO.op);
            else
                System.out.println(newO.op + " " + newO.val);

        }
    }
}