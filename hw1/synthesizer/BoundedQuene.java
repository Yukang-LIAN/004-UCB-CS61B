package synthesizer;

public interface BoundedQuene<T> extends Iterable<T> {
    int capacity();

    int fillCount();

    void enquene(T x);

    T dequeued();

    T peek();

    default boolean isEmpty() {
        return fillCount() == 0;
    }

    default boolean isFull() {
        return fillCount() == capacity();
    }

}