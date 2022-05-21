public class LinkedListDeque<T> {

    public class LinkedList{
        public T item;
        public LinkedList prev;
        public LinkedList next;

        public LinkedList(T item, LinkedList prev, LinkedList next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedList sentinel = new LinkedList(null, null, null);
    public int size = 0;

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
        LinkedList last = new LinkedList(item, this.sentinel.next, this.sentinel);
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
        if (this.size() == 0) {
            return null;
        }
        this.size--;
        T firstItem = this.sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        this.sentinel.next = sentinel.next.next;
        return firstItem;
    }

    /**
     * * Removes and returns the item at the back of the deque. If no such item
     * * exists, returns null.
     */
    public T removeLast() {
        if (this.size() == 0) {
            return null;
        }
        this.size--;
        T lastItem = this.sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        this.sentinel.prev = sentinel.prev.prev;
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
        for (int i = 0; i < index; i++) {
            ptr = ptr.next;
        }
        return ptr.item;
    }
}