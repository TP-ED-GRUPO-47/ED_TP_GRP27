package structures.heap;

/**
 * Abstract definition for a min-heap ADT.
 *
 * @param <T> element type stored in the heap
 */
public interface HeapADT<T> {
    /**
     * Inserts a new element while preserving heap order.
     *
     * @param obj element to insert
     */
    void addElement(T obj);

    /**
     * Removes and returns the minimum element.
     *
     * @return the removed minimum element
     */
    T removeMin();

    /**
     * Returns (without removing) the minimum element.
     *
     * @return the current minimum element
     */
    T findMin();

    /**
     * Indicates whether the heap contains no elements.
     *
     * @return true if the heap is empty, false otherwise
     */
    boolean isEmpty();
}

