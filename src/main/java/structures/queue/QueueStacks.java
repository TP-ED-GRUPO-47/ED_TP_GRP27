package structures.queue;
import structures.stack.ArrayStack;

/**
 * Queue implementation backed by two stacks to provide FIFO ordering with
 * amortized constant-time operations.
 *
 * @param <T> element type stored in the queue
 */
public class QueueStacks<T> implements QueueADT<T>{
    private ArrayStack<T> inStack;
    private ArrayStack<T> outStack;

    /**
     * Creates an empty queue using two backing stacks.
     */
    public QueueStacks() {
        inStack = new ArrayStack<>();
        outStack = new ArrayStack<>();
    }

    /**
     * Adds an element to the back of the queue.
     *
     * @param element value to enqueue
     */
    @Override
    public void enqueue(T element) {
        inStack.push(element);
    }

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return dequeued element
     * @throws IllegalStateException if the queue is empty
     */
    @Override
    public T dequeue() {
        shiftStacks();
        if (outStack.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return outStack.pop();
    }

    /**
     * Returns the element at the front of the queue without removing it.
     *
     * @return front element
     * @throws IllegalStateException if the queue is empty
     */
    @Override
    public T first() {
        shiftStacks();
        if (outStack.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return outStack.peek();
    }

    /**
     * Checks whether the queue contains no elements.
     *
     * @return {@code true} when empty; otherwise {@code false}
     */
    @Override
    public boolean isEmpty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    /**
     * Returns the number of elements currently stored.
     *
     * @return current queue size
     */
    @Override
    public int size() {
        return inStack.size() + outStack.size();
    }

    /**
     * Moves elements from the enqueue stack to the dequeue stack when needed to
     * expose the next front element.
     */
    private void shiftStacks() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    /**
     * Returns a string representation including the current size.
     *
     * @return textual representation of the queue
     */
    @Override
    public String toString() {
        shiftStacks();
        return "Queue com Stacks (size=" + size() + ")";
    }
}
