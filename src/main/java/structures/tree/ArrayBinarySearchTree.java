package structures.tree;

import exceptions.ElementNotFoundException;
import structures.linear.ArrayUnorderedList;

/**
 * Array-based binary search tree keeping elements in sorted order according to
 * their natural ordering.
 *
 * @param <T> element type stored in the tree
 */
public class ArrayBinarySearchTree<T> extends ArrayBinaryTree<T>
        implements BinarySearchTreeADT<T> {
    /** Backing array representing the complete binary tree layout. */
    protected T[] tree;
    /** Number of elements currently stored. */
    protected int count;
    /** Highest occupied index in the backing array. */
    protected int maxIndex;
    /** Cached height of the tree. */
    protected int height;
    private static final int DEFAULT_CAPACITY = 50;

    /**
     * Creates an empty binary search tree with default capacity.
     */
    public ArrayBinarySearchTree() {
        tree = (T[]) new Object[DEFAULT_CAPACITY];
        count = 0;
        maxIndex = -1;
        height = 0;
    }

    /**
     * Creates a binary search tree containing the provided root element.
     *
     * @param element value to place at the root
     */
    public ArrayBinarySearchTree(T element) {
        tree = (T[]) new Object[DEFAULT_CAPACITY];
        tree[0] = element;
        count = 1;
        maxIndex = 0;
        height = 1;
    }

    /**
     * Indicates whether the tree has no elements.
     *
     * @return {@code true} when empty; otherwise {@code false}
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Doubles the backing array to allow further insertions.
     */
    protected void expandCapacity() {
        T[] newTree = (T[]) new Object[tree.length * 2];
        System.arraycopy(tree, 0, newTree, 0, tree.length);
        tree = newTree;
    }

    /**
     * Inserts an element maintaining the binary search tree ordering.
     *
     * @param element value to insert
     */
    public void addElement(T element) {
        if (tree.length < maxIndex * 2 + 3)
            expandCapacity();

        Comparable<T> comparableElement = (Comparable<T>) element;

        if (isEmpty()) {
            tree[0] = element;
            maxIndex = 0;
        } else {
            boolean added = false;
            int currentIndex = 0;

            while (!added) {
                if (comparableElement.compareTo(tree[currentIndex]) < 0) {
                    int left = currentIndex * 2 + 1;
                    if (left >= tree.length) expandCapacity();

                    if (tree[left] == null) {
                        tree[left] = element;
                        added = true;
                        if (left > maxIndex) maxIndex = left;
                    } else {
                        currentIndex = left;
                    }
                } else {
                    int right = currentIndex * 2 + 2;
                    if (right >= tree.length) expandCapacity();

                    if (tree[right] == null) {
                        tree[right] = element;
                        added = true;
                        if (right > maxIndex) maxIndex = right;
                    } else {
                        currentIndex = right;
                    }
                }
            }
        }

        count++;
        height = (int) (Math.log(maxIndex + 1) / Math.log(2)) + 1;
    }

    /**
     * Removes all occurrences of the target element from the tree.
     *
     * @param targetElement value to remove
     */
    @Override
    public void removeAllOccurrences(T targetElement) {
        try {
            while (contains(targetElement)) {
                removeElement(targetElement);
            }
        } catch (ElementNotFoundException e) {
        }
    }


    /**
     * Returns the smallest element in the tree.
     *
     * @return minimum element
     * @throws ElementNotFoundException if the tree is empty
     */
    public T findMin() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        int current = 0;
        while (current * 2 + 1 < tree.length && tree[current * 2 + 1] != null) {
            current = current * 2 + 1;
        }
        return tree[current];
    }

    /**
     * Returns the largest element in the tree.
     *
     * @return maximum element
     * @throws ElementNotFoundException if the tree is empty
     */
    public T findMax() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        int current = 0;
        while (current * 2 + 2 < tree.length && tree[current * 2 + 2] != null) {
            current = current * 2 + 2;
        }
        return tree[current];
    }

    /**
     * Removes and returns the smallest element in the tree.
     *
     * @return removed minimum element
     */
    public T removeMin() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        T result = findMin();

        removeElement(result);
        return result;
    }

    /**
     * Removes and returns the largest element in the tree.
     *
     * @return removed maximum element
     */
    public T removeMax() {
        if (isEmpty()) throw new ElementNotFoundException("List");
        T result = findMax();

        removeElement(result);
        return result;
    }

    /**
     * Adds the in-order traversal to the provided list starting at the given
     * index.
     *
     * @param index starting node index
     * @param out list collecting traversal elements
     */
    protected void inorderToList(int index, ArrayUnorderedList<T> out) {
        if (index >= tree.length) return;
        if (tree[index] == null) return;

        inorderToList(index * 2 + 1, out);
        out.addToRear(tree[index]);
        inorderToList(index * 2 + 2, out);
    }

    /**
     * Clears the backing array and resets bookkeeping fields.
     */
    protected void clearTree() {
        for (int i = 0; i < tree.length; i++) tree[i] = null;
        count = 0;
        maxIndex = -1;
        height = 0;
    }


    /**
     * Removes the first occurrence of the target element.
     *
     * @param targetElement value to remove
     * @return removed element
     * @throws ElementNotFoundException if the element is not found
     */
    public T removeElement(T targetElement) {
        if (isEmpty()) throw new ElementNotFoundException("List");

        ArrayUnorderedList<T> elems = new ArrayUnorderedList<>();
        inorderToList(0, elems);

        int idx = 0;
        int found = -1;
        for (T e : elems) {
            if (e.equals(targetElement)) {
                found = idx;
                break;
            }
            idx++;
        }
        if (found == -1) throw new ElementNotFoundException("List");

        ArrayUnorderedList<T> rebuilt = new ArrayUnorderedList<>();
        idx = 0;
        for (T e : elems) {
            if (idx != found) {
                rebuilt.addToRear(e);
            }
            idx++;
        }

        clearTree();
        for (T e : rebuilt) {
            addElement(e);
        }

        return targetElement;
    }

    /**
     * Prints an in-order traversal starting at the given index to standard
     * output.
     *
     * @param index starting node index
     */
    public void inorderPrint(int index) {
        if (index >= tree.length) return;
        if (tree[index] == null) return;
        inorderPrint(index * 2 + 1);
        System.out.print(tree[index] + " ");
        inorderPrint(index * 2 + 2);
    }

    /**
     * Prints the full in-order traversal of the tree to standard output.
     */
    public void printInOrder() {
        System.out.print("Inorder: ");
        inorderPrint(0);
        System.out.println();
    }
}


