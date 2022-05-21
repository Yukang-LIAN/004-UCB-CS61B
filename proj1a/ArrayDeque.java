public class ArrayDeque<T> {

    private T[] array = (T[]) new Object[8];
    private int size = 0;
    private int next = 4;
    private int prev = 3;

    /**
     * * Creates an empty linked list deque.
     */
    public ArrayDeque() {

    }

    /**
     * * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        this.size++;
        this.array[prev] = item;
        prev = (prev + 7) % 8;
    }

    /**
     * * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {
        this.size++;
        this.array[next] = item;
        next = (next + 1) % 8;
    }

    /**
     * * Returns true if deque is empty, false otherwise.
     */
    public boolean isEmpty() {
        if (this.size == 0) {
            return true;
        }
        return false;
    }

    /**
     * * Returns the number of items in the deque.
     */
    public int size() {
        return this.size;
    }

    /**
     * * Prints the items in the deque from first to last, separated by a
     * * space.
     */
    public void printDeque() {
        for (int i = 0; i < this.size; i++) {
            System.out.print(array[(prev + i + 1) % 8] + " ");
        }
    }

    /**
     * * Removes and returns the item at the front of the deque. If no such item
     * * exists, returns null.
     */
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        prev = (prev + 1) % 8;
        T tmp = array[prev];
        array[prev] = null;
        this.size--;
        return tmp;
    }

    /**
     * * Removes and returns the item at the back of the deque. If no such item
     * * exists, returns null.
     */
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        next = (next + 7) % 8;
        T tmp = array[next];
        array[next] = null;
        this.size--;
        return tmp;
    }

    /**
     * * Gets the item at the given index, where 0 is the front, 1 is the next
     * item,
     * * and so forth.
     * * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        if (index >= this.size) {
            return null;
        }
        return array[(prev + 1 + index) % 8];
    }

}