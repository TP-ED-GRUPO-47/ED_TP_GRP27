package structures.linear;

/**
 * Node for a singly-linked list.
 * Contains an element and a reference to the next node.
 *
 * @param <T> the type of element stored in this node
 */
public class Node<T> {
    T element;
    Node<T> next;

    /**
     * Creates a new node with the specified element.
     *
     * @param elem the element to store in this node
     */
    Node(T elem) {
        element = elem;
    }

    /**
     * Getter for the element inside the node
     *
     * @return - The generic element
     */
    public T getElement() {
        return this.element;
    }

    /**
     * Setter for the element i want to set inside the node
     *
     * @param element - The generic element
     */
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * Getter for the next node
     *
     * @return - The next node
     */
    public Node<T> getNext() {
        return this.next;
    }

    /**
     * Setter for the next node
     *
     * @param nextNode - The next node i want to set
     */
    public void setNext(Node<T> nextNode) {
        this.next = nextNode;
    }
}
