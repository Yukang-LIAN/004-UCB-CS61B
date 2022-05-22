public class LinkedListDeque<T> {

    private class LinkedList {
        private T item;
        private LinkedList prev;
        private LinkedList next;

        public LinkedList(T item, LinkedList prev, LinkedList next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private LinkedList sentinel = new LinkedList(null, null, null);
    private int size = 0;

    /**
     * * Creates an empty linked list deque.
     */
    public LinkedListDeque() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /**
     * * Adds an item of type T to the front of the deque.
     */
    public void addFirst(T item) {
        this.size++;
        LinkedList first = new LinkedList(item, this.sentinel, this.sentinel.next);
        this.sentinel.next.prev = first;
        this.sentinel.next = first;
    }

    /**
     * * Adds an item of type T to the back of the deque.
     */
    public void addLast(T item) {
        this.size++;
        LinkedList last = new LinkedList(item, this.sentinel.prev, this.sentinel);
        this.sentinel.prev.next = last;
        this.sentinel.prev = last;
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
        LinkedList ptr = this.sentinel.next;
        while (ptr != sentinel) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    }

    /**
     * * Removes and returns the item at the front of the deque. If no such item
     * * exists, returns null.
     */
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        T firstItem = this.sentinel.next.item;
        this.sentinel.next.next.prev = this.sentinel;
        this.sentinel.next = this.sentinel.next.next;
        this.size--;
        return firstItem;
    }

    /**
     * * Removes and returns the item at the back of the deque. If no such item
     * * exists, returns null.
     */
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        T lastItem = this.sentinel.prev.item;
        this.sentinel.prev.prev.next = this.sentinel;
        this.sentinel.prev = this.sentinel.prev.prev;
        this.size--;
        return lastItem;
    }

    /**
     * * Gets the item at the given index, where 0 is the front, 1 is the next item,
     * * and so forth.
     * * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        if (index >= this.size) {
            return null;
        }
        LinkedList ptr = this.sentinel.next;
        while (index > 0) {
            ptr = ptr.next;
            index--;
        }
        return ptr.item;
    }

    /**
     * * Same as get, but uses recursion.
     */
    public T getRecursive(int index) {
        if (index >= this.size) {
            return null;
        }
        return helper(this.sentinel.next, index);
    }

    /**
     * * helper
     */
    private T helper(LinkedList ptr, int index) {
        
        if (index == 0) {
            return ptr.item;
        }
        ptr = ptr.next;
        return helper(ptr, index - 1);
    }

}
