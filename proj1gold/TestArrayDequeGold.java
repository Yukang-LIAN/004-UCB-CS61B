import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    static StudentArrayDeque<Integer> student = new StudentArrayDeque();
    static ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<Integer>();
    String msg = new String();
    int num = 0;

    @Test
    public void testArrayDequeGold() {
        for (int i = 0; i < 10000; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.25) {
                student.addFirst(i);
                solution.addFirst(i);
                num++;
                msg += "addFirst(" + i + ")\n";
                assertEquals(msg, solution.get(0), student.get(0));
            } else if (i < 0.5) {
                student.addLast(i);
                solution.addLast(i);
                num++;
                msg += "addLast(" + num + ")\n";
                assertEquals(msg, solution.get(num - 1), student.get(num - 1));
            } else if (i < 0.75) {
                if (solution.isEmpty()) {
                    assertTrue(student.isEmpty());
                    continue;
                }
                Integer stu = student.removeFirst();
                Integer sol = solution.removeFirst();
                num--;
                msg += "removeFirst()\n";
                assertEquals(msg, sol, stu);
            } else {
                if (solution.isEmpty()) {
                    assertTrue(student.isEmpty());
                    continue;
                }
                Integer stu = student.removeLast();
                Integer sol = solution.removeLast();
                num--;
                msg += "removeLast()\n";
                assertEquals(msg, sol, stu);
            }
        }
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests("all", TestArrayDequeGold.class);
    }
}