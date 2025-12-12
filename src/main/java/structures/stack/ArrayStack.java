package structures.stack;
import exceptions.EmptyCollectionException;


/**
 * Array-based stack implementation with automatic capacity expansion.
 *
 * @param <T> element type stored in the stack
 */
public class ArrayStack<T> implements StackADT<T> {
    private final int DEFAULT_CAPACITY = 100;

    private int top;
    private T[] stack;

    /**
     * Creates an empty stack with default capacity.
     */
    public ArrayStack() {
        top = 0;
        stack = (T[]) new Object[DEFAULT_CAPACITY];
    }

    /**
     * Creates an empty stack with the specified initial capacity.
     *
     * @param initialCapacity initial backing array size
     */
    public ArrayStack(int initialCapacity) {
        top = 0;
        stack = (T[]) new Object[initialCapacity];
    }

    /**
     * Pushes an element onto the top of the stack, expanding capacity when
     * necessary.
     *
     * @param element value to push
     */
    @Override
    public void push(T element) {
        if (size() == stack.length)
            expandCapacity();

        stack[top] = element;
        top++;
    }

    /**
     * Removes and returns the top element of the stack.
     *
     * @return removed element
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("Stack");

        top--;
        T result = stack[top];
        stack[top] = null;
        return result;
    }

    /**
     * Returns the top element without removing it.
     *
     * @return top element
     * @throws EmptyCollectionException if the stack is empty
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("Stack");

        return stack[top - 1];
    }

    /**
     * Indicates whether the stack contains no elements.
     *
     * @return {@code true} when empty; otherwise {@code false}
     */
    @Override
    public boolean isEmpty() {
        return (top == 0);
    }

    /**
     * Returns the number of elements currently stored.
     *
     * @return current stack size
     */
    @Override
    public int size() {
        return top;
    }

    /**
     * Returns a string representation from top to bottom.
     *
     * @return textual representation of the stack
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack (top → bottom): [");
        int i = top - 1;
        while (i >= 0) {
            sb.append(stack[i]);
            if (i > 0)
                sb.append(", ");
            i--;
        }
        sb.append("]");
        return sb.toString();
    }


    /**
     * Doubles the underlying array capacity to accommodate additional elements.
     */
    private void expandCapacity() {
        T[] larger = (T[]) new Object[stack.length * 2];
        for (int i = 0; i < stack.length; i++) {
            larger[i] = stack[i];
        }
        stack = larger;
    }
}
