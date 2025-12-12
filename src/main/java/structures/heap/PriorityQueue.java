package structures.heap;

/**
 * Priority queue implemented on top of {@link ArrayHeap}, ordering elements
 * by the integer priority carried in {@link PriorityQueueNode}.
 *
 * @param <T> element type stored alongside its priority
 */
public class PriorityQueue<T> extends
    ArrayHeap<PriorityQueueNode<T>> {

    /**
     * Creates an empty priority queue.
     */
    public PriorityQueue() {
        super();
    }

    /**
     * Inserts an element with a given priority.
     *
     * @param object   element to enqueue
     * @param priority priority key (smaller value dequeues first)
     */
    public void addElement (T object, int priority) {
        PriorityQueueNode<T> node =
                new PriorityQueueNode<T> (object, priority);
        super.addElement(node);
    }

    /**
     * Removes and returns the element with highest priority (smallest key).
     *
     * @return next element according to priority order
     */
    public T removeNext() {
        PriorityQueueNode<T> temp =
                (PriorityQueueNode<T>)super.removeMin();
        return temp.getElement();
    }
}



