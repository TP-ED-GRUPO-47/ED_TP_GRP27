package structures.queue;

/**
 * Linked implementation of a queue using LinearNode.
 * Maintains references to both front and rear for efficient operations.
 *
 * @param <T> the type of elements stored in this queue
 */
public class LinkedQueue<T> implements QueueADT<T> {
    private int count;
    private LinearNode<T> front, rear;

    /**
     * Creates an empty linked queue.
     */
    public LinkedQueue() {
        count = 0;
        front = rear = null;
    }

    @Override
    /**
     * Adds an element to the rear of this queue.
     *
     * @param element the element to be added
     */
    public void enqueue(T element) {
        LinearNode<T> node = new LinearNode<>(element);

        if (isEmpty()) {
            front = node;
        }
        else {
            rear.setNext(node);
        }

        rear = node;
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

        T result = front.getElement();
        front = front.getNext();
        count--;

        if (isEmpty()) {
            rear = null;
        }

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

        return front.getElement();
    }

    @Override
    /**
     * Returns true if this queue contains no elements.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return (count == 0);
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
        LinearNode<T> current = front;

        while (current != null) {
            sb.append(current.getElement());
            current = current.getNext();

            if (current != null) {
                sb.append(", ");
            }

        }

        sb.append("]");
        return sb.toString();
    }
}

