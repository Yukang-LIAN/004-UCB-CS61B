package synthesizer;
public interface BoundedQuene<T>{
    int capacity();
    int fillCount();
    void enquene(T x);
    T dequeued();
    T peek();
    default boolean isEmpty(){
        return true;
    }
    default boolean isFull(){
        return true;
    }
}