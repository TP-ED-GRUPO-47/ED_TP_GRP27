package structures.tree;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;

/**
 * Linked-node binary search tree maintaining elements in sorted order.
 *
 * @param <T> element type stored in the tree
 */
public class LinkedBinarySearchTree<T> extends LinkedBinaryTree<T>
        implements BinarySearchTreeADT<T> {
    /**
     * Creates an empty binary search tree.
     */
    public LinkedBinarySearchTree() {
        super();
    }

    /**
     * Creates a binary search tree containing the specified root element.
     *
     * @param element value to place at the root
     */
    public LinkedBinarySearchTree(T element) {
        super(element);
    }

    /**
     * Inserts an element maintaining the binary search tree ordering.
     *
     * @param element value to insert
     */
    @Override
    public void addElement(T element) {
        BinaryTreeNode<T> temp = new BinaryTreeNode<>(element);
        Comparable<T> comparableElement = (Comparable<T>) element;

        if (isEmpty()) {
            root = temp;
        } else {
            BinaryTreeNode<T> current = root;
            boolean added = false;
            while (!added) {
                if (comparableElement.compareTo(current.element) < 0) {
                    if (current.left == null) {
                        current.left = temp;
                        added = true;
                    } else {
                        current = current.left;
                    }
                } else {
                    if (current.right == null) {
                        current.right = temp;
                        added = true;
                    } else {
                        current = current.right;
                    }
                }
            }
        }
        count++;
    }

    /**
     * Removes the element matching the target value.
     *
     * @param targetElement value to remove
     * @return removed element
     * @throws ElementNotFoundException if the element is not present
     */
    @Override
    public T removeElement(T targetElement)
            throws ElementNotFoundException {
        T result = null;
        if (!isEmpty()) {
            if (((Comparable) targetElement).equals(root.element)) {
                result = root.element;
                root = replacement(root);
                count--;
            } else {
                BinaryTreeNode<T> current, parent = root;
                boolean found = false;
                if (((Comparable) targetElement).compareTo(root.element) < 0)
                    current = root.left;
                else
                    current = root.right;
                while (current != null && !found) {
                    if (targetElement.equals(current.element)) {
                        found = true;
                        count--;
                        result = current.element;
                        if (current == parent.left) {
                            parent.left = replacement(current);
                        } else {
                            parent.right = replacement(current);
                        }
                    } else {
                        parent = current;
                        if (((Comparable) targetElement).compareTo(current.element) < 0)
                            current = current.left;
                        else
                            current = current.right;
                    }
                }
                if (!found)
                    throw new ElementNotFoundException("binary search tree");
            }
        }
        return result;
    }

    /**
     * Removes every occurrence of the target element.
     *
     * @param targetElement value to remove
     */
    @Override
    public void removeAllOccurrences(T targetElement) {
        while (true) {
            try {
                removeElement(targetElement);
            } catch (ElementNotFoundException e) {
                break;
            }
        }
    }

    /**
     * Removes and returns the smallest element.
     *
     * @return minimum element
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T removeMin() {
        if (isEmpty())
            throw new EmptyCollectionException("binary search tree");

        BinaryTreeNode<T> parent = null;
        BinaryTreeNode<T> current = root;

        T result = findMin();

        removeElement(result);
        return result;
    }

    /**
     * Removes and returns the largest element.
     *
     * @return maximum element
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T removeMax() {
        if (isEmpty())
            throw new EmptyCollectionException("binary search tree");

        BinaryTreeNode<T> parent = null;
        BinaryTreeNode<T> current = root;

        T result = findMax();

        removeElement(result);
        return result;
    }

    /**
     * Returns the smallest element without removing it.
     *
     * @return minimum element
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T findMin() {
        if (isEmpty())
            throw new EmptyCollectionException("binary search tree");

        BinaryTreeNode<T> current = root;
        while (current.left != null)
            current = current.left;

        return current.element;
    }

    /**
     * Returns the largest element without removing it.
     *
     * @return maximum element
     * @throws EmptyCollectionException if the tree is empty
     */
    @Override
    public T findMax() {
        if (isEmpty())
            throw new EmptyCollectionException("binary search tree");

        BinaryTreeNode<T> current = root;
        while (current.right != null)
            current = current.right;

        return current.element;
    }

    /**
     * Determines the subtree that replaces the specified node when it is
     * removed.
     *
     * @param node node being removed
     * @return replacement subtree root
     */
    protected BinaryTreeNode<T> replacement(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> result = null;
        if ((node.left == null) && (node.right == null)) {
            result = null;
        } else if ((node.left != null) && (node.right == null)) {
            result = node.left;
        } else if ((node.left == null) && (node.right != null)) {
            result = node.right;
        } else {
            BinaryTreeNode<T> current = node.right;
            BinaryTreeNode<T> parent = node;
            while (current.left != null) {
                parent = current;
                current = current.left;
            }
            if (node.right == current) {
                current.left = node.left;
            } else {
                parent.left = current.right;
                current.right = node.right;
                current.left = node.left;
            }
            result = current;
        }
        return result;
    }
}
