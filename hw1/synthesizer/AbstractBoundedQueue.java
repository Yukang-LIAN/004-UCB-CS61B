package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQuene<T> {
    protected int fillCount;
    protected int capacity;

    public int capacity() {
        return capacity;
    }

    public int fillCount() {
        return fillCount;
    }
}
