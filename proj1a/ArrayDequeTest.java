public class ArrayDequeTest{
    public static void main(String[] args) {
        ArrayDeque a=new ArrayDeque();
        a.addFirst(1);
        a.addFirst(1);
        a.addFirst(1);
        a.addFirst(1);
        a.addFirst(1);
        a.removeFirst();
        a.removeLast();
        a.get(2);
        a.printDeque();
    }
}