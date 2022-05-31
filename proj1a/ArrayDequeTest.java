public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque a = new ArrayDeque();
        a.addFirst(0);
        a.removeFirst();
        a.addFirst(2);
        a.removeFirst();
        a.addLast(4);
        a.addFirst(5);
        a.addFirst(6);
        a.addLast(7);
        a.addLast(8);
        a.addLast(9);
        a.removeLast();
        a.addFirst(11);
        a.addFirst(12);
        a.addFirst(13);
        a.addLast(14);
        a.removeLast();
        a.removeFirst();
    }
}
