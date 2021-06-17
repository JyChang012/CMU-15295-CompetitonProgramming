package c;

import java.util.*;

public class A {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String moves = scanner.nextLine();

        Deque<Integer> deque = new LinkedList<>();
        for (int i = moves.length() - 1; i >= 0; i--) {
            switch (moves.charAt(i)) {
                case 'l':
                    deque.addLast(i + 1);
                    break;
                case 'r':
                    deque.addFirst(i + 1);
            }
        }
        StringBuilder s = new StringBuilder();
        for (int ston: deque) {
            s.append(ston).append('\n');
        }
        System.out.print(s);
    }
}
