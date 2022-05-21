public class ArrayDequeTest{
    public static void main(String[] args) {
        ArrayDeque a=new ArrayDeque();
        a.addFirst(0);
        a.addFirst(1);
        a.addFirst(2);
        a.addFirst(3);
        a.addFirst(4);
        a.addFirst(5);
        a.addFirst(6);
        a.removeFirst();
        a.removeLast();
        a.get(2);
        a.printDeque();
    }
}