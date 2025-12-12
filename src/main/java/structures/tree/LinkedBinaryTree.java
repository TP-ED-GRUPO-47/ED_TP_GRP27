package structures.tree;

import java.util.Iterator;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;
import structures.linear.LinkedUnorderedList;
import structures.queue.LinkedQueue;

/**
 * Linked implementation of a binary tree using BinaryTreeNode.
 * Each node maintains references to its left and right children.
 *
 * @param <T> the type of elements stored in this tree
 */
public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {

    /**
     * The root node of the tree.
     */
    protected BinaryTreeNode<T> root;

    /**
     * The count of the tree.
     */
    protected int count;

    /**
     * Constructor for an empty binary tree.
     */
    public LinkedBinaryTree() {
        root = null;
        count = 0;
    }

    /**
     * Constructor for a binary tree with a root element.
     *
     * @param element the element to be added to the root of the tree
     */
    public LinkedBinaryTree(T element) {
        root = new BinaryTreeNode<>(element);
        count = 1;
    }

    /**
     * Method that returns the root element of the tree.
     *
     * @return the root element of the tree
     */
    public T getRoot() {
        return root.getElement();
    }

    /**
     * Method that returns true if the tree is empty.
     *
     * @return true if the tree is empty
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Method that returns the count of the tree.
     *
     * @return the count of the tree
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Method that returns true if the tree contains the target element.
     *
     * @param targetElement the target element to be found in the tree
     * @return true if the tree contains the target element
     */
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
            return true;
        } catch (ElementNotFoundException ex) {
            return false;
        }
    }

    /**
     * Method that finds the target element in the tree.
     *
     * @param targetElement the element to be found in the tree
     * @return the current element
     */
    @Override
    public T find(T targetElement) {
        if (isEmpty()) {
            throw new EmptyCollectionException("Binary tree is empty");
        }

        BinaryTreeNode<T> current = findAgain(targetElement, root);

        if (current == null) {
            throw new ElementNotFoundException("Element not found");
        }

        return current.getElement();
    }

    /**
     * Method that uses recursivity to find the target element in the tree.
     *
     * @param targetElement the element to be found in the tree
     * @param next          the next node to be checked
     * @return the binary tree node
     */
    private BinaryTreeNode<T> findAgain(T targetElement, BinaryTreeNode<T> next) {
        if (next == null) {
            return null;
        }

        if (next.getElement().equals(targetElement)) {
            return next;
        }

        BinaryTreeNode<T> temp = findAgain(targetElement, next.getLeft());

        if (temp == null) {
            temp = findAgain(targetElement, next.getRight());
        }

        return temp;
    }

    /**
     * Method that returns the string representation of the tree.
     *
     * @return the string representation of the tree
     */
    @Override
    public String toString() {
        LinkedUnorderedList<T> tempList = new LinkedUnorderedList<>();

        inOrder(root, tempList);

        return tempList.toString();
    }

    /**
     * Method that iterates in order through the tree.
     *
     * @return an in order iterator for the tree
     */
    @Override
    public Iterator<T> iteratorInOrder() {
        LinkedUnorderedList<T> tempList = new LinkedUnorderedList<>();

        inOrder(root, tempList);

        return tempList.iterator();
    }

    /**
     * A recursive method that orders the temporary list in order.
     *
     * @param node     the current node
     * @param tempList the temporary list
     */
    private void inOrder(BinaryTreeNode<T> node, LinkedUnorderedList<T> tempList) {
        if (node != null) {
            inOrder(node.getLeft(), tempList);
            tempList.addToRear(node.getElement());
            inOrder(node.getRight(), tempList);
        }
    }

    /**
     * Method that iterates in pre order through the tree.
     *
     * @return a pre order iterator for the tree
     */
    @Override
    public Iterator<T> iteratorPreOrder() {
        LinkedUnorderedList<T> tempList = new LinkedUnorderedList<>();

        preOrder(root, tempList);

        return tempList.iterator();
    }

    /**
     * A recursive method that orders the temporary list in pre order.
     *
     * @param node     the current node
     * @param tempList the temporary list
     */
    private void preOrder(BinaryTreeNode<T> node, LinkedUnorderedList<T> tempList) {
        if (node != null) {
            tempList.addToRear(node.getElement());
            preOrder(node.getLeft(), tempList);
            preOrder(node.getRight(), tempList);
        }
    }

    /**
     * Method that iterates in post order through the tree.
     *
     * @return a post order iterator for the tree
     */
    @Override
    public Iterator<T> iteratorPostOrder() {
        LinkedUnorderedList<T> tempList = new LinkedUnorderedList<>();

        postOrder(root, tempList);

        return tempList.iterator();
    }

    /**
     * A recursive method that orders the temporary list in post order.
     *
     * @param node     the current node
     * @param tempList the temporary list
     */
    private void postOrder(BinaryTreeNode<T> node, LinkedUnorderedList<T> tempList) {
        if (node != null) {
            postOrder(node.getLeft(), tempList);
            postOrder(node.getRight(), tempList);
            tempList.addToRear(node.getElement());
        }
    }

    /**
     * Method that iterates in level order through the tree.
     *
     * @return a level order iterator for the tree
     */
    @Override
    public Iterator<T> iteratorLevelOrder() {
        LinkedUnorderedList<T> tempList = new LinkedUnorderedList<>();
        LinkedQueue<BinaryTreeNode<T>> queue = new LinkedQueue<>();

        if (!isEmpty()) {
            queue.enqueue(root);

            while (!queue.isEmpty()) {
                BinaryTreeNode<T> next = queue.dequeue();

                if (next.getLeft() != null) {
                    queue.enqueue(next.getLeft());
                }

                if (next.getRight() != null) {
                    queue.enqueue(next.getRight());
                }

                tempList.addToRear(next.getElement());
            }
        }

        return tempList.iterator();
    }
}