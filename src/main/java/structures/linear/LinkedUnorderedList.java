package structures.linear;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;


/**
 * Linked-node unordered list implementation.
 *
 * @param <T> element type stored
 */
public class LinkedUnorderedList<T> extends SingleLinkedList<T> implements UnorderedListADT<T> {

    /**
     * Default constructor
     */
    public LinkedUnorderedList() {
        super();
    }

    /**
     * Method that adds an element to the front of the list
     *
     * @param element the element to be added to the front of this list
     */
    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            newNode.setNext(this.head);
            this.head = newNode;
        }
        this.size++;
        this.count++;
    }

    /**
     * Method that adds an element to the rear of the list
     *
     * @param element the element to be added to the rear of this list
     */
    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.setNext(newNode);
            this.tail = newNode;
        }
        this.size++;
        this.count++;
    }

    /**
     * Method that adds an element after the target
     *
     * @param element the element to be added after the target
     * @param target  the target is the item that the element is added after
     * @throws EmptyCollectionException if the collection is empty
     * @throws ElementNotFoundException if the target element does not exist
     */
    @Override
    public void addAfter(T element, T target)  {
    
        if (!contains(target)) {
            throw new ElementNotFoundException("Element not found");
        }

        Node<T> newNode = new Node<>(element);
        Node<T> current = this.head;

        while (!current.getElement().equals(target)) {
            current = current.getNext();
        }

        newNode.setNext(current.getNext());
        current.setNext(newNode);

        if (newNode.getNext() == null) {
            this.tail = newNode;
        }

        size++;
        count++;
    }


}
