package structures.queue;

public class LinearNode<T> {
    private T element;
    private LinearNode<T> next;

    public LinearNode(T elem) {
        this.element = elem;
        this.next = null;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T elem) {
        this.element = elem;
    }

    public LinearNode<T> getNext() {
        return next;
    }

    public void setNext(LinearNode<T> node) {
        this.next = node;
    }
}

