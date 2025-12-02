package structures.lists;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class ArrayUnorderedList<T> implements UnorderedListADT<T> {

    private final int DEFAULT_CAPACITY = 10;
    private T[] list;
    private int rear;

    public ArrayUnorderedList() {
        list = (T[]) new Object[DEFAULT_CAPACITY];
        rear = 0;
    }

    private void expandCapacity() {
        T[] newList = (T[]) new Object[list.length * 2];
        for (int i = 0; i < rear; i++) {
            newList[i] = list[i];
        }
        list = newList;
    }

    @Override
    public void addToFront(T element) {
        if (rear == list.length) {
            expandCapacity();
        }

        for (int i = rear; i > 0; i--) {
            list[i] = list[i - 1];
        }

        list[0] = element;
        rear++;
    }

    @Override
    public void addToRear(T element) {
        if (rear == list.length) {
            expandCapacity();
        }

        list[rear] = element;
        rear++;
    }

    @Override
    public void addAfter(T element, T target) {
        int index = -1;
        for (int i = 0; i < rear; i++) {
            if (list[i].equals(target)) {
                index = i;
                break;
            }
        }
        if (index == -1)
            throw new NoSuchElementException("Target not found.");

        if (rear == list.length) {
            expandCapacity();
        }

        for (int i = rear; i > index + 1; i--) {
            list[i] = list[i - 1];
        }

        list[index + 1] = element;
        rear++;
    }


    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }

        T result = list[0];
        for (int i = 0; i < rear - 1; i++) {
            list[i] = list[i + 1];
        }
        rear--;

        return result;
    }

    @Override
    public T removeLast() {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
        }
        T result = list[rear - 1];
        list[rear - 1] = null;
        rear--;

        return result;
    }

    @Override
    public T remove(T element) {
        int index = -1;
        for (int i = 0; i < rear; i++) {
            if (list[i].equals(element)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new NoSuchElementException("Element not found.");
        }

        T result = list[index];
        for (int i = index; i < rear - 1; i++) {
            list[i] = list[i + 1];
        }
        rear--;
        return result;
    }

    @Override
    public T first() {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
        }
        return list[0];
    }

    @Override
    public T last() {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
        }
        return list[rear - 1];
    }

    @Override
    public boolean contains(T target) {
        for (int i = 0; i < rear; i++) {
            if (list[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return rear == 0;
    }

    @Override
    public int size() {
        return rear;
    }

    @Override
    public Iterator<T> iterator() {
        return new BasicIterator();
    }

    private class BasicIterator implements Iterator<T> {
        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < rear;
        }

        @Override
        public T next() {
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            return list[current++];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rear; i++) {
            sb.append(list[i]);
            if (i < rear - 1){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
