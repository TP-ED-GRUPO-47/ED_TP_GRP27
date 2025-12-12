package structures.stack;
import exceptions.EmptyCollectionException;

/**
 * Linked implementation of a stack using LinearNode.
 * Maintains a reference to the top node for efficient operations.
 *
 * @param <T> the type of elements stored in this stack
 */
public class LinkedStack<T> implements StackADT<T> {

    private int count;
    private LinearNode<T> top;


    /**
     * Creates an empty linked stack.
     */
    public LinkedStack() {
        count = 0;
        top = null;
    }

    @Override
    /**
     * Pushes an element onto the top of this stack.
     *
     * @param element the element to be pushed
     */
    public void push(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(top);
        top = newNode;
        count++;
    }

    @Override
    /**
     * Removes and returns the top element from this stack.
     *
     * @return the element at the top
     * @throws EmptyCollectionException if the stack is empty
     */
    public T pop() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("Stack");

        T result = top.getElement();
        top = top.getNext();
        count--;

        return result;
    }

    @Override
    /**
     * Returns the top element without removing it.
     *
     * @return the element at the top
     * @throws EmptyCollectionException if the stack is empty
     */
    public T peek() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("Stack");

        return top.getElement();
    }

    @Override
    /**
     * Returns true if this stack contains no elements.
     *
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return (count == 0);
    }

    @Override
    /**
     * Returns the number of elements in this stack.
     *
     * @return the number of elements
     */
    public int size() {
        return count;
    }

    @Override
    /**
     * Returns a string representation of this stack from top to bottom.
     *
     * @return a string showing all elements in the stack
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack (top → bottom): [");

        LinearNode<T> current = top;
        while (current != null) {
            sb.append(current.getElement());
            current = current.getNext();
            if (current != null)
                sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
