package structures.linear;

public class DoubleNode<E> {
    private DoubleNode<E> next;
    private DoubleNode<E> previous;
    private E element;

    public DoubleNode(E elem) {
        this.element = elem;
        this.next = null;
        this.previous = null;
    }

    public DoubleNode<E> getNext() {
        return next;
    }

    public void setNext(DoubleNode<E> next) {
        this.next = next;
    }

    public DoubleNode<E> getPrevious() {
        return previous;
    }

    public void setPrevious(DoubleNode<E> previous) {
        this.previous = previous;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }
}

