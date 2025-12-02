package structures.lists;


import java.util.Iterator;
import java.util.NoSuchElementException;


public class DoubleLinkedUnorderedList<T> implements UnorderedListADT<T> {

    private int count;
    private DoubleNode<T> head;
    private DoubleNode<T> tail;

    public DoubleLinkedUnorderedList() {
        count = 0;
        head = null;
        tail = null;
    }

    @Override
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
    public void addAfter(T element, T target) {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
        }

        DoubleNode<T> current = head;
        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException("Target not found.");
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
    public T removeFirst() {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
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
    public T removeLast() {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
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
    public T remove(T element) {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
        }

        DoubleNode<T> current = head;
        while (current != null && !current.getElement().equals(element)){
            current = current.getNext();
        }

        if (current == null) {
            throw new NoSuchElementException("Element not found.");
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
    public T first() {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
        }
        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()){
            throw new NoSuchElementException("List is empty.");
        }
        return tail.getElement();
    }

    @Override
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
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public Iterator<T> iterator() {
        return new BasicIterator();
    }

    private class BasicIterator implements Iterator<T> {
        private DoubleNode<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T element = current.getElement();
            current = current.getNext();
            return element;
        }
    }

    @Override
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
}

