package structures.linear;

import java.util.Iterator;


public interface ListADT<T> extends Iterable<T> {
    T removeFirst();
    T removeLast();
    T remove(T element);
    T first();
    T last();
    boolean contains(T target);
    boolean isEmpty();
    int size();
    Iterator<T> iterator();
    String toString();
}
