package structures.heap;

/**
 * Node wrapper storing an element with an integer priority and insertion order
 * to break ties when used inside {@link PriorityQueue}.
 *
 * @param <T> element type stored
 */
public class PriorityQueueNode<T> implements
    Comparable<PriorityQueueNode<T>> {
    private static int nextorder = 0;
    private int priority;
    private int order;
    private T element;

    /**
     * Creates a node with a given element and priority.
     *
     * @param obj  element to wrap
     * @param prio priority key (smaller means higher priority)
     */
    public PriorityQueueNode (T obj, int prio) {
        element = obj;
        priority = prio;
        order = nextorder;
        nextorder++;
    }

    /**
     * Returns the wrapped element.
     *
     * @return element carried by this node
     */
    public T getElement() {
        return element;
    }

    /**
     * Returns the priority key.
     *
     * @return priority value (smaller means higher priority)
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Returns insertion order used for tie-breaking.
     *
     * @return insertion order assigned when the node was created
     */
    public int getOrder() {
        return order;
    }

    /**
     * Returns a string representation of this node containing the element, priority, and order.
     *
     * @return a string representation of this node
     */
    public String toString() {
        String temp = (element.toString() + priority + order);
        return temp;
    }

    /**
     * Compares nodes by priority, then insertion order (FIFO for equal priority).
     *
     * @param obj other node to compare against
     * @return negative, zero, or positive according to natural ordering
     */
    public int compareTo(PriorityQueueNode<T> obj)
    {
        int result;
        if (priority > obj.getPriority())
            result = 1;
        else if (priority < obj.getPriority())
            result = -1;
        else if (order > obj.getOrder())
            result = 1;
        else
            result = -1;
        return result;
    }
}

