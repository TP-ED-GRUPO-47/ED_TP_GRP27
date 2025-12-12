package structures.linear;

import java.util.Iterator;

import exceptions.ElementNotFoundException;

/**
 * Array-based ordered list that maintains elements in natural order (Comparable).
 * Insertions shift elements to keep the array sorted.
 *
 * @param <T> element type (must be {@link Comparable})
 */
public class ArrayOrderedList<T> implements OrderedListADT<T> {

    private final int DEFAULT_CAPACITY = 10;
    private int rear;
    private T[] list;

    /**
     * Creates an empty ordered list with default capacity.
     */
    public ArrayOrderedList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        rear = 0;
    }

    @Override
    /**
     * Adds an element to the list, maintaining natural ordering.
     * Elements are inserted in their proper position to keep the list sorted.
     *
     * @param element the element to be added
     * @throws IllegalArgumentException if element is not Comparable
     */
    public void add(T element) {
        if (!(element instanceof Comparable)) {
            throw new IllegalArgumentException("Element must be Comparable!");
        }

        if (rear == list.length)
            expandCapacity();

        Comparable<T> comparableElement = (Comparable<T>) element;
        int i = 0;

        while (i < rear && comparableElement.compareTo(list[i]) > 0) {
            i++;
        }

        for (int j = rear; j > i; j--) {
            list[j] = list[j - 1];
        }

        list[i] = element;
        rear++;
    }

    private void expandCapacity() {
        T[] newList = (T[]) new Object[list.length * 2];

        for (int i = 0; i < rear; i++) {
            newList[i] = list[i];
        }

        list = newList;
    }

    @Override
    /**
     * Removes and returns the first element from this list.
     *
     * @return the removed element
     * @throws ElementNotFoundException if the list is empty
     */
    public T removeFirst() {
        if (isEmpty()) throw new ElementNotFoundException("Ordered List");
        T result = list[0];

        for (int i = 0; i < rear - 1; i++) {
            list[i] = list[i + 1];
        }

        rear--;
        return result;
    }

    @Override
    /**
     * Removes and returns the last element from this list.
     *
     * @return the removed element
     * @throws ElementNotFoundException if the list is empty
     */
    public T removeLast() {
        if (isEmpty()) throw new ElementNotFoundException("Ordered List");
        T result = list[rear - 1];
        list[rear - 1] = null;
        rear--;
        return result;
    }

    @Override
    /**
     * Removes and returns the specified element from this list.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws ElementNotFoundException if element is not found
     */
    public T remove(T element) {
        int index = -1;
        for (int i = 0; i < rear; i++) {
            if (list[i].equals(element)) {
                index = i;
                break;
            }
        }
        if (index == -1)
        {
            throw new ElementNotFoundException("Ordered List");
        }

        T result = list[index];
        for (int i = index; i < rear - 1; i++) {
            list[i] = list[i + 1];
        }

        rear--;
        return result;
    }

    @Override
    /**
     * Returns the first element in this list without removing it.
     *
     * @return the first element
     * @throws ElementNotFoundException if the list is empty
     */
    public T first() {
        if (isEmpty()) {
            throw new ElementNotFoundException("Ordered List");
        }
        return list[0];
    }

    @Override
    /**
     * Returns the last element in this list without removing it.
     *
     * @return the last element
     * @throws ElementNotFoundException if the list is empty
     */
    public T last() {
        if (isEmpty()) {
            throw new ElementNotFoundException("Ordered List");
        }
        return list[rear - 1];
    }

    @Override
    /**
     * Returns true if this list contains the specified element.
     *
     * @param target the element to search for
     * @return true if the element is found, false otherwise
     */
    public boolean contains(T target) {
        for (int i = 0; i < rear; i++) {
            if (list[i].equals(target))
                return true;
        }
        return false;
    }

    @Override
    /**
     * Returns true if this list contains no elements.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements
     */
    public int size() {
        return rear;
    }

    @Override
    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an iterator
     */
    public Iterator<T> iterator() {
        return new BasicIterator();
    }


    private class BasicIterator implements Iterator<T> {
        private int current = 0;

        @Override
        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if there are more elements, false otherwise
         */
        public boolean hasNext() {
            return current < rear;
        }

        @Override
        /**
         * Returns the next element in the iteration.
         *
         * @return the next element
         * @throws ElementNotFoundException if there are no more elements
         */
        public T next() {
            if (!hasNext()) {
                throw new ElementNotFoundException("Ordered List");
            }
            return list[current++];
        }
    }

    @Override
    /**
     * Returns a string representation of this list.
     *
     * @return a string showing all elements in the list
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rear; i++) {
            sb.append(list[i]);

            if (i < rear - 1){
                sb.append(", ");
            } ;

        }
        sb.append("]");
        return sb.toString();
    }
}

