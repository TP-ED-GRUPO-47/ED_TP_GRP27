package structures.tree;

import java.util.Iterator;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;

/**
 * Array-based binary tree implementation.
 * Stores elements in an array using implicit indexing where the root is at index 0,
 * left child at 2i+1, and right child at 2i+2.
 *
 * @param <T> the type of elements stored in this tree
 */
public class ArrayBinaryTree<T> implements BinaryTreeADT<T> {


    /**
     * The default capacity of the tree.
     */
    protected final int DEFAULT_CAPACITY = 100;

    /**
     * Backing array for the tree; kept protected for subclasses but hidden from callers.
     */
    protected T[] tree;

    /**
     * The size of the tree.
     */
    protected int size;

    /**
     * Constructor for an empty binary tree.
     */
    public ArrayBinaryTree() {
        tree = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Constructor for a binary tree with a root element.
     *
     * @param element the element to be added to the root of the tree
     */
    public ArrayBinaryTree(T element) {
        tree = (T[]) new Object[DEFAULT_CAPACITY];
        tree[0] = element;
        size = 1;
    }

    /**
     * Method that returns the root element of the tree.
     *
     * @return the root element of the tree
     */
    @Override
    public T getRoot() {
        return tree[0];
    }

    /**
     * Method that returns true if the tree is empty.
     *
     * @return {@code true} if the tree is empty , {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Method that returns the size of the tree.
     *
     * @return the size of the tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Method that returns true if the tree contains the target element.
     *
     * @param targetElement the element being sought in the tree
     * @return {@code true} if the tree contains the target element , {@code false} otherwise
     */
    @Override
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
            return true;
        } catch (ElementNotFoundException ex) {
            return false;
        }
    }

    /**
     * Method that returns the target element.
     *
     * @param targetElement the element being sought in the tree
     * @return the target element
     * @throws EmptyCollectionException if the collection is empty
     * @throws ElementNotFoundException if the target element does not exist
     */
    @Override
    public T find(T targetElement) throws EmptyCollectionException, ElementNotFoundException {
        if (isEmpty()) {
            throw new EmptyCollectionException("binary tree is empty");
        }

        T element = null;
        boolean found = false;

        for (int i = 0; i < size && !found; i++) {
            if (tree[i].equals(targetElement)) {
                found = true;
                element = tree[i];
            }
        }

        if (!found) {
            throw new ElementNotFoundException("Not found");
        }

        return element;
    }

    /**
     * Method that returns the left child of the target element.
     *
     * @return the left child of the target element
     */
    @Override
    public String toString() {
        String result = " { ";
        Iterator<T> it = iteratorInOrder();
        while (it.hasNext()) {
            result += it.next() + " ";
        }
        return result + "}";

    }

    /**
     * Method that iterates through the tree in order.
     *
     * @return an iterator of the tree in order
     */
    @Override
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();

        inOrder(0, tempList);

        return tempList.iterator();
    }

    /**
     * Recursive method that orders the temporary list in order.
     *
     * @param node     the current node
     * @param tempList the temporary list
     */
    private void inOrder(int node, ArrayUnorderedList<T> tempList) {
        if (node < size) {
            inOrder(node * 2 + 1, tempList);
            tempList.addToRear(tree[node]);
            inOrder(node * 2 + 2, tempList);
        }
    }

    /**
     * Method that iterates through the tree in pre order.
     *
     * @return an iterator of the tree in pre order
     */
    @Override
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();

        preOrder(0, tempList);

        return tempList.iterator();
    }

    /**
     * Recursive method that orders the temporary list in pre order.
     *
     * @param node     the current node
     * @param tempList the temporary list
     */
    private void preOrder(int node, ArrayUnorderedList<T> tempList) {
        if (node < size) {
            tempList.addToRear(tree[node]);
            preOrder(node * 2 + 1, tempList);
            preOrder(node * 2 + 2, tempList);
        }
    }

    /**
     * Method that iterates through the tree in post order.
     *
     * @return an iterator of the tree in post order
     */
    @Override
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();

        postOrder(0, tempList);

        return tempList.iterator();
    }

    /**
     * Recursive method that orders the temporary list in post order.
     *
     * @param node     the current node
     * @param tempList the temporary list
     */
    private void postOrder(int node, ArrayUnorderedList<T> tempList) {
        if (node < size) {
            postOrder(node * 2 + 1, tempList);
            postOrder(node * 2 + 2, tempList);
            tempList.addToRear(tree[node]);
        }
    }

    /**
     * Method that iterates through the tree in level order.
     *
     * @return an iterator of the tree in level order
     */
    @Override
    public Iterator<T> iteratorLevelOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();
        LinkedQueue<Integer> queue = new LinkedQueue<>();

        if (!isEmpty()) {
            queue.enqueue(0);
            while (!queue.isEmpty()) {
                int nodeIndex = queue.dequeue();
                tempList.addToRear(tree[nodeIndex]);

                int leftChildIndex = nodeIndex * 2 + 1;
                int rightChildIndex = nodeIndex * 2 + 2;

                if (leftChildIndex < size) {
                    queue.enqueue(leftChildIndex);
                }
                if (rightChildIndex < size) {
                    queue.enqueue(rightChildIndex);
                }
            }
        }

        return tempList.iterator();
    }
}