package structures.tree;

import java.util.Iterator;

/**
 * Common operations for a binary tree, including traversals and basic queries.
 *
 * @param <T> element type stored in the tree
 */
public interface BinaryTreeADT<T> {

    /**
     * Retrieves the root element of the tree.
     *
     * @return root element
     */
    public T getRoot();

    /**
     * Indicates whether the tree contains no elements.
     *
     * @return {@code true} if empty; otherwise {@code false}
     */
    public boolean isEmpty();

    /**
     * Returns the number of elements stored in the tree.
     *
     * @return element count
     */
    public int size();

    /**
     * Returns a string representation of the tree contents.
     *
     * @return textual representation
     */
    public String toString();

    /**
     * Checks whether the specified element exists in the tree.
     *
     * @param targetElement value to search
     * @return {@code true} when found; otherwise {@code false}
     */
    public boolean contains(T targetElement);

    /**
     * Returns the matching element if present.
     *
     * @param targetElement value to locate
     * @return found element
     */
    public T find(T targetElement);

    /**
     * Provides an iterator that traverses the tree in-order.
     *
     * @return in-order iterator
     */
    public Iterator<T> iteratorInOrder();

    /**
     * Provides an iterator that traverses the tree in pre-order.
     *
     * @return pre-order iterator
     */
    public Iterator<T> iteratorPreOrder();

    /**
     * Provides an iterator that traverses the tree in post-order.
     *
     * @return post-order iterator
     */
    public Iterator<T> iteratorPostOrder();

    /**
     * Provides an iterator that traverses the tree in level-order.
     *
     * @return level-order iterator
     */
    public Iterator<T> iteratorLevelOrder();
}

