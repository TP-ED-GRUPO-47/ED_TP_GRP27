package structures.queue;

/**
 * Circular array-based queue implementation offering constant-time enqueue/dequeue.
 *
 * @param <T> element type stored
 */
public class CircularArrayQueue<T> implements QueueADT<T> {
    private final static int DEFAULT_CAPACITY = 10;
    private int front, rear, count;
    private T[] queue;

    /**
     * Creates an empty queue with the specified capacity.
     *
     * @param initialCapacity the initial capacity of the queue
     */
    public CircularArrayQueue(int initialCapacity) {
        front = rear = count = 0;
        queue = (T[]) new Object[initialCapacity];
    }

    /**
     * Creates an empty queue with default capacity.
     */
    public CircularArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    /**
     * Adds an element to the rear of this queue.
     *
     * @param element the element to be added
     */
    public void enqueue(T element) {
        if (size() == queue.length) {
            expandCapacity();
        }

        queue[rear] = element;
        rear = (rear + 1) % queue.length;
        count++;
    }

    @Override
    /**
     * Removes and returns the element at the front of this queue.
     *
     * @return the element at the front
     * @throws IllegalStateException if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        T result = queue[front];
        queue[front] = null;
        front = (front + 1) % queue.length;
        count--;

        return result;
    }

    @Override
    /**
     * Returns the element at the front of this queue without removing it.
     *
     * @return the first element
     * @throws IllegalStateException if the queue is empty
     */
    public T first() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        return queue[front];
    }

    @Override
    /**
     * Returns true if this queue contains no elements.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements
     */
    public int size() {
        return count;
    }

    @Override
    /**
     * Returns a string representation of this queue.
     *
     * @return a string showing all elements in the queue
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < count; i++) {
            int index = (front + i) % queue.length;
            sb.append(queue[index]);

            if (i < count - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    private void expandCapacity() {
        T[] larger = (T[]) new Object[queue.length * 2];
        for (int i = 0; i < count; i++) {
            larger[i] = queue[(front + i) % queue.length];
        }
        queue = larger;
        front = 0;
        rear = count;
    }
}


