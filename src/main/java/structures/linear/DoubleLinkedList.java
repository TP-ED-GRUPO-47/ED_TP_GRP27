package structures.linear;

import java.util.Iterator;

import exceptions.ElementNotFoundException;

/**
 * Doubly linked implementation of an unordered list.
 * Supports insertions/removals at both ends and iteration in linear time.
 *
 * @param <T> element type stored
 */
public class DoubleLinkedList<T> implements UnorderedListADT<T> {

    private int count;
    private DoubleNode<T> head;
    private DoubleNode<T> tail;

    /**
     * Creates an empty doubly-linked list.
     */
    public DoubleLinkedList() {
        count = 0;
        head = null;
        tail = null;
    }

    @Override
    /**
     * Adds an element to the front of this list.
     *
     * @param element the element to be added
     */
    public void addToFront(T element) {
        DoubleNode<T> newNode = new DoubleNode<>(element);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }

        count++;
    }

    @Override
    /**
     * Adds an element to the rear of this list.
     *
     * @param element the element to be added
     */
    public void addToRear(T element) {
        DoubleNode<T> newNode = new DoubleNode<>(element);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrevious(tail);
            tail = newNode;
        }

        count++;
    }

    @Override
    /**
     * Adds an element after a target element in this list.
     *
     * @param element the element to be added
     * @param target the element after which to add the new element
     * @throws ElementNotFoundException if target element is not found or list is empty
     */
    public void addAfter(T element, T target) {
        if (isEmpty()){
            throw new ElementNotFoundException("List");
        }

        DoubleNode<T> current = head;
        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }

        if (current == null) {
            throw new ElementNotFoundException("List");
        }

        DoubleNode<T> newNode = new DoubleNode<>(element);

        newNode.setNext(current.getNext());
        newNode.setPrevious(current);

        if (current.getNext() != null) {
            current.getNext().setPrevious(newNode);
        }
        else {
            tail = newNode;
        }

        current.setNext(newNode);
        count++;
    }

    @Override
    /**
     * Removes and returns the first element from this list.
     *
     * @return the removed element
     * @throws ElementNotFoundException if the list is empty
     */
    public T removeFirst() {
        if (isEmpty()){
            throw new ElementNotFoundException("List");
        }

        T result = head.getElement();
        head = head.getNext();

        if (head == null) {
            tail = null;
        }
        else {
            head.setPrevious(null);
        }

        count--;
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
        if (isEmpty()){
            throw new ElementNotFoundException("List");
        }

        T result = tail.getElement();
        tail = tail.getPrevious();

        if (tail == null) {
            head = null;
        }
        else {
            tail.setNext(null);
        }

        count--;
        return result;
    }

    @Override
    /**
     * Removes and returns the specified element from this list.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws ElementNotFoundException if the element is not found or list is empty
     */
    public T remove(T element) {
        if (isEmpty()){
            throw new ElementNotFoundException("List");
        }

        DoubleNode<T> current = head;
        while (current != null && !current.getElement().equals(element)){
            current = current.getNext();
        }

        if (current == null) {
            throw new ElementNotFoundException("List");
        }

        T result = current.getElement();

        if (current == head) {
            removeFirst();
        }
        else if (current == tail) {
            removeLast();
        }
        else {
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
            count--;
        }

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
        if (isEmpty()){
            throw new ElementNotFoundException("List");
        }
        return head.getElement();
    }

    @Override
    /**
     * Returns the last element in this list without removing it.
     *
     * @return the last element
     * @throws ElementNotFoundException if the list is empty
     */
    public T last() {
        if (isEmpty()){
            throw new ElementNotFoundException("List");
        }
        return tail.getElement();
    }

    @Override
    /**
     * Returns true if this list contains the specified element.
     *
     * @param target the element to search for
     * @return true if the element is found, false otherwise
     */
    public boolean contains(T target) {
        DoubleNode<T> current = head;
        while (current != null) {
            if (current.getElement().equals(target)) {
                return true;
            }
            current = current.getNext();
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
        return count == 0;
    }

    @Override
    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements
     */
    public int size() {
        return count;
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
        private DoubleNode<T> current = head;

        @Override
        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if there are more elements, false otherwise
         */
        public boolean hasNext() {
            return current != null;
        }

        @Override
        /**
         * Returns the next element in the iteration.
         *
         * @return the next element
         * @throws ElementNotFoundException if there are no more elements
         */
        public T next() {
            if (!hasNext()) throw new ElementNotFoundException("List");
            T element = current.getElement();
            current = current.getNext();
            return element;
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
        DoubleNode<T> current = head;
        while (current != null) {
            sb.append(current.getElement());
            if (current.getNext() != null){
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    private void printForward(DoubleNode<T> node) {
        if (node == null) return;
        System.out.print(node.getElement() + " ");
        printForward(node.getNext());
    }

    private void printBackward(DoubleNode<T> node) {
        if (node == null) return;
        System.out.print(node.getElement() + " ");
        printBackward(node.getPrevious());
    }

    /**
     * Prints elements of this list from head to tail.
     */
    public void printForward() {
        printForward(head);
    }

    /**
     * Prints elements of this list from tail to head.
     */
    public void printBackward() {
        printBackward(tail);
    }




}





