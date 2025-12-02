package structures.queues;

public class LinkedQueue<T> implements QueueADT<T> {
    private int count;
    private LinearNode<T> front, rear;

    public LinkedQueue() {
        count = 0;
        front = rear = null;
    }

    @Override
    public void enqueue(T element) {
        LinearNode<T> node = new LinearNode<>(element);

        if (isEmpty()) {
            front = node;
        }
        else {
            rear.setNext(node);
        }

        rear = node;
        count++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        T result = front.getElement();
        front = front.getNext();
        count--;

        if (isEmpty()) {
            rear = null;
        }

        return result;
    }

    @Override
    public T first() {

        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        return front.getElement();
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
        StringBuilder sb = new StringBuilder("[");
        LinearNode<T> current = front;

        while (current != null) {
            sb.append(current.getElement());
            current = current.getNext();

            if (current != null) {
                sb.append(", ");
            }

        }

        sb.append("]");
        return sb.toString();
    }
}

