package w3.c;

import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;


// hard to solve analytically, search for answer

public class C {


    public static class ComparatorDiffDesc implements Comparator<Bug> {


        @Override
        public int compare(Bug o1, Bug o2) {
            return o2.difficulty - o1.difficulty;
        }
    }

    public static class ComparatorBySkillDesc implements Comparator<Student> {

        @Override
        public int compare(Student o1, Student o2) {
            return o2.skill - o1.skill;
        }
    }

    public static class ComparatorByPassesAsc implements Comparator<Student> {

        @Override
        public int compare(Student o1, Student o2) {
            return o1.passes - o2.passes;
        }
    }

    public static class Student {
        public int id;
        public int skill;
        public int passes;
    }

    public static class Bug {
        public int id;
        public int difficulty;
    }


    public static boolean checkDay(int day, Student[] sortedStudents, Bug[] sortedBugs, int maxPass) {
        PriorityQueue<Student> qualified = new PriorityQueue<>(new ComparatorByPassesAsc());
        int b = 0, s = 0, passes = 0;
        while (b < sortedBugs.length) {
            Bug bug = sortedBugs[b];
            Student best;

            while (s < sortedStudents.length && sortedStudents[s].skill >= bug.difficulty) {
                qualified.add(sortedStudents[s]);
                s++;
            }

            best = qualified.poll();
            if (best != null && best.passes + passes <= maxPass) {
                b += day;
                passes += best.passes;
            } else {
                break;
            }
        }
        return b >= sortedBugs.length;
    }

    public static int[] findAssignment(int day, Student[] sortedStudents, Bug[] sortedBugs) {
        PriorityQueue<Student> qualified = new PriorityQueue<>(new ComparatorByPassesAsc());
        int b = 0, s = 0;
        int[] ans = new int[sortedBugs.length];
        while (b < sortedBugs.length) {
            Bug bug = sortedBugs[b];
            Student best;

            while (s < sortedStudents.length && sortedStudents[s].skill >= bug.difficulty) {
                qualified.add(sortedStudents[s]);
                s++;
            }
            best = qualified.poll();

            for (int i = b; i < Math.min(b + day, sortedBugs.length); i++) {
                ans[sortedBugs[i].id] = best.id;
            }

            b += day;
        }
        return ans;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), m = scanner.nextInt(), s = scanner.nextInt();
        Student[] students = new Student[n];
        Bug[] bugs = new Bug[m];

        scanner.nextLine();

        for (int i = 0; i < m; i++) {
            Bug bug = new Bug();
            bug.id = i;
            bug.difficulty = scanner.nextInt();
            bugs[i] = bug;
        }

        scanner.nextLine();

        for (int i = 0; i < n; i++) {
            Student st = new Student();
            st.id = i;
            st.skill = scanner.nextInt();
            students[i] = st;
        }

        scanner.nextLine();
        for (Student st : students) {
            st.passes = scanner.nextInt();
        }

        Bug[] sortedBugs = bugs.clone();
        Student[] sortedStudents = students.clone();
        Arrays.sort(sortedBugs, new ComparatorDiffDesc());
        Arrays.sort(sortedStudents, new ComparatorBySkillDesc());

        int lo = 0, hi = m;

        if (!checkDay(hi, sortedStudents, sortedBugs, s)) {
            System.out.println("NO");
            return;
        }
        // binary search
        while (lo + 1 < hi) {
            int mid = (lo + hi) / 2;
            if (checkDay(mid, sortedStudents, sortedBugs, s)) {
                hi = mid;
            } else {
                lo = mid;
            }
        }

        int[] ans = findAssignment(hi, sortedStudents, sortedBugs);

        StringBuilder out = new StringBuilder("YES\n");
        for (int ss : ans) {
            out.append(ss + 1).append(' ');
        }

        System.out.print(out);


    }
}
