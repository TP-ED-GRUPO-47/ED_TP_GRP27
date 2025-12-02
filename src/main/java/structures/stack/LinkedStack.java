package structures.stack;
import exceptions.EmptyCollectionException;

public class LinkedStack<T> implements StackADT<T> {

    private int count;
    private LinearNode<T> top;


    public LinkedStack() {
        count = 0;
        top = null;
    }

    @Override
    public void push(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(top);
        top = newNode;
        count++;
    }

    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("Stack");

        T result = top.getElement();
        top = top.getNext();
        count--;

        return result;
    }

    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("Stack");

        return top.getElement();
    }

    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    @Override
    public int size() {
        return count;
    }

    @Override
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
