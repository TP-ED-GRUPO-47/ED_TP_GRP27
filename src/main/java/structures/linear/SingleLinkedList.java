package structures.linear;

import java.util.Iterator;
import exceptions.ElementNotFoundException;

/**
 * Singly-linked list implementation of the ListADT interface.
 * Maintains references to head and tail nodes for efficient operations.
 *
 * @param <T> the type of elements stored in this list
 */
public class SingleLinkedList<T> implements ListADT<T> {
    /**
     * The head node of the list.
     */
    protected Node<T> head;

    /**
     * The tail node of the list.
     */
    protected Node<T> tail;

    /**
     * The size of the list.
     */
    protected int size;

    /** Number of elements currently stored (legacy counter). */
    protected int count;

    /**
     * Constructor for an empty linked list.
     */
    public SingleLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Adds an element to the front of the list.
     *
     * @param element the element to be added to the front of the list
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.next = head;
        head = newNode;
        count++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        T result = head.element;
        head = head.next;
        count--;
        return result;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        if (head.next == null) return removeFirst();

        Node<T> current = head;
        while (current.next.next != null)
            current = current.next;

        T result = current.next.element;
        current.next = null;
        count--;
        return result;
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) throw new ElementNotFoundException("List");

        if (head.element.equals(element)) return removeFirst();

        Node<T> current = head;
        while (current.next != null && !current.next.element.equals(element))
            current = current.next;

        if (current.next == null)
            throw new ElementNotFoundException("List");

        T result = current.next.element;
        current.next = current.next.next;
        count--;
        return result;
    }

    @Override
    public T first() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        return head.element;
    }

    @Override
    public T last() {
        if (isEmpty()) throw new ElementNotFoundException("List");

        Node<T> current = head;
        while (current.next != null)
            current = current.next;
        return current.element;
    }

    @Override
    public boolean contains(T target) {
        Node<T> current = head;
        while (current != null) {
            if (current.element.equals(target)) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new ElementNotFoundException("List");
                T elem = current.element;
                current = current.next;
                return elem;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.element);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns the head node of this list.
     *
     * @return the head node
     * @throws ElementNotFoundException if the list is empty
     */
    public Node<T> getHead() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        return head;
    }

    /**
     * Recursively prints all elements starting from the given node.
     *
     * @param node the starting node for printing
     */
    public void printRec(Node<T> node) {
        if (node == null) return;
        System.out.print(node.element + " ");
        printRec(node.next);
    }

}



