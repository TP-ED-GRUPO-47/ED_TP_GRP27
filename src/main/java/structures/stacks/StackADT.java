package structures.stacks;

import java.util.EmptyStackException;

public interface StackADT<E> {

    void push(E element);

    E pop() throws EmptyStackException;

    E peek() throws EmptyStackException;

    boolean isEmpty();

    int size();

    @Override
    String toString();
}
