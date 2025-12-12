package structures.tree;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;

/**
 * Unit tests for the {@link LinkedBinarySearchTree} class.
 * <p>
 * This test suite validates the linked-node binary search tree implementation,
 * ensuring elements are maintained in proper BST order where:
 * - Left subtree contains elements less than the parent
 * - Right subtree contains elements greater than or equal to the parent
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Element insertion maintaining BST properties</li>
 *   <li>Element removal (min, max, specific)</li>
 *   <li>Search operations (find element, min, max)</li>
 *   <li>Tree traversals (inorder, preorder, postorder, level-order)</li>
 *   <li>Tree properties (size, height, balance)</li>
 *   <li>Edge cases (empty tree, single element, duplicates)</li>
 *   <li>Exception handling for invalid operations</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see LinkedBinarySearchTree
 * @see BinarySearchTreeADT
 * @see LinkedBinaryTree
 */
class LinkedBinarySearchTreeTest {

    private LinkedBinarySearchTree<Integer> bst;

    /**
     * Sets up a fresh empty binary search tree before each test.
     */
    @BeforeEach
    void setUp() {
        bst = new LinkedBinarySearchTree<>();
    }

    /**
     * Tests adding elements to an empty tree.
     * <p>
     * Verifies that the first element becomes the root.
     * </p>
     */
    @Test
    void testAddToEmptyTree() {
        bst.addElement(50);

        assertFalse(bst.isEmpty());
        assertEquals(1, bst.size());
        assertEquals(50, bst.getRoot());
    }

    /**
     * Tests adding multiple elements maintaining BST ordering.
     * <p>
     * Adds elements in random order and verifies inorder traversal
     * produces sorted sequence.
     * </p>
     */
    @Test
    void testAddMultipleElements() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);
        bst.addElement(60);
        bst.addElement(80);

        assertEquals(7, bst.size());
        assertEquals(50, bst.getRoot());

        Iterator<Integer> inorder = bst.iteratorInOrder();
        assertEquals(20, inorder.next());
        assertEquals(30, inorder.next());
        assertEquals(40, inorder.next());
        assertEquals(50, inorder.next());
        assertEquals(60, inorder.next());
        assertEquals(70, inorder.next());
        assertEquals(80, inorder.next());
        assertFalse(inorder.hasNext());
    }

    /**
     * Tests adding elements in ascending order.
     * <p>
     * Results in a right-skewed tree (worst case for BST).
     * </p>
     */
    @Test
    void testAddAscendingOrder() {
        bst.addElement(10);
        bst.addElement(20);
        bst.addElement(30);
        bst.addElement(40);
        bst.addElement(50);

        assertEquals(5, bst.size());
        assertEquals(10, bst.getRoot());
    }

    /**
     * Tests adding elements in descending order.
     * <p>
     * Results in a left-skewed tree (worst case for BST).
     * </p>
     */
    @Test
    void testAddDescendingOrder() {
        bst.addElement(50);
        bst.addElement(40);
        bst.addElement(30);
        bst.addElement(20);
        bst.addElement(10);

        assertEquals(5, bst.size());
        assertEquals(50, bst.getRoot());
    }

    /**
     * Tests adding duplicate elements.
     * <p>
     * Duplicates are typically placed in the right subtree.
     * </p>
     */
    @Test
    void testAddDuplicates() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(50);
        bst.addElement(30);

        assertEquals(4, bst.size());
    }

    /**
     * Tests finding the minimum element in the tree.
     * <p>
     * The minimum is the leftmost node.
     * </p>
     */
    @Test
    void testFindMin() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);

        assertEquals(20, bst.findMin());
    }

    /**
     * Tests finding the maximum element in the tree.
     * <p>
     * The maximum is the rightmost node.
     * </p>
     */
    @Test
    void testFindMax() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(60);
        bst.addElement(80);

        assertEquals(80, bst.findMax());
    }

    /**
     * Tests finding min on an empty tree.
     * <p>
     * Verifies that {@link EmptyCollectionException} is thrown.
     * </p>
     */
    @Test
    void testFindMinOnEmptyTree() {
        assertThrows(EmptyCollectionException.class, () -> bst.findMin());
    }

    /**
     * Tests finding max on an empty tree.
     * <p>
     * Verifies that {@link EmptyCollectionException} is thrown.
     * </p>
     */
    @Test
    void testFindMaxOnEmptyTree() {
        assertThrows(EmptyCollectionException.class, () -> bst.findMax());
    }

    /**
     * Tests removing the minimum element.
     * <p>
     * Verifies that the tree structure is maintained after removal.
     * </p>
     */
    @Test
    void testRemoveMin() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);

        assertEquals(20, bst.removeMin());
        assertEquals(4, bst.size());
        assertEquals(30, bst.findMin());
    }

    /**
     * Tests removing the maximum element.
     * <p>
     * Verifies that the tree structure is maintained after removal.
     * </p>
     */
    @Test
    void testRemoveMax() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(60);
        bst.addElement(80);

        assertEquals(80, bst.removeMax());
        assertEquals(4, bst.size());
        assertEquals(70, bst.findMax());
    }

    /**
     * Tests removing a specific element from the tree.
     * <p>
     * Verifies BST properties are maintained after removal.
     * </p>
     */
    @Test
    void testRemoveElement() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);
        bst.addElement(60);
        bst.addElement(80);

        bst.removeElement(30);
        assertEquals(6, bst.size());
        assertFalse(bst.contains(30));
    }

    /**
     * Tests removing the root element.
     * <p>
     * Verifies that a successor replaces the root correctly.
     * </p>
     */
    @Test
    void testRemoveRoot() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);

        bst.removeElement(50);
        assertEquals(2, bst.size());
        assertNotEquals(50, bst.getRoot());
    }

    /**
     * Tests removing a non-existent element.
     * <p>
     * Verifies that {@link ElementNotFoundException} is thrown.
     * </p>
     */
    @Test
    void testRemoveNonExistentElement() {
        bst.addElement(50);
        bst.addElement(30);

        assertThrows(ElementNotFoundException.class, () -> bst.removeElement(99));
        assertEquals(2, bst.size());
    }

    /**
     * Tests removing from an empty tree.
     * <p>
     * Verifies that appropriate exceptions are thrown.
     * </p>
     */
    @Test
    void testRemoveFromEmptyTree() {
        assertThrows(EmptyCollectionException.class, () -> bst.removeMin());
        assertThrows(EmptyCollectionException.class, () -> bst.removeMax());
    }

    /**
     * Tests the {@code contains} method for existing elements.
     */
    @Test
    void testContainsExistingElement() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);

        assertTrue(bst.contains(50));
        assertTrue(bst.contains(30));
        assertTrue(bst.contains(70));
        assertTrue(bst.contains(20));
    }

    /**
     * Tests the {@code contains} method for non-existing elements.
     */
    @Test
    void testContainsNonExistingElement() {
        bst.addElement(50);
        bst.addElement(30);

        assertFalse(bst.contains(20));
        assertFalse(bst.contains(70));
        assertFalse(bst.contains(100));
    }

    /**
     * Tests inorder traversal producing sorted sequence.
     * <p>
     * Inorder: left -> root -> right
     * For BST, this produces elements in ascending order.
     * </p>
     */
    @Test
    void testInOrderTraversal() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);
        bst.addElement(60);
        bst.addElement(80);

        Iterator<Integer> iter = bst.iteratorInOrder();
        int[] expected = {20, 30, 40, 50, 60, 70, 80};
        
        for (int value : expected) {
            assertTrue(iter.hasNext());
            assertEquals(value, iter.next());
        }
        assertFalse(iter.hasNext());
    }

    /**
     * Tests preorder traversal.
     * <p>
     * Preorder: root -> left -> right
     * </p>
     */
    @Test
    void testPreOrderTraversal() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);

        Iterator<Integer> iter = bst.iteratorPreOrder();
        
        assertTrue(iter.hasNext());
        assertEquals(50, iter.next());
        assertEquals(30, iter.next());
        assertEquals(20, iter.next());
        assertEquals(40, iter.next());
        assertEquals(70, iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests postorder traversal.
     * <p>
     * Postorder: left -> right -> root
     * </p>
     */
    @Test
    void testPostOrderTraversal() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);

        Iterator<Integer> iter = bst.iteratorPostOrder();
        
        assertTrue(iter.hasNext());
        assertEquals(20, iter.next());
        assertEquals(40, iter.next());
        assertEquals(30, iter.next());
        assertEquals(70, iter.next());
        assertEquals(50, iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests level-order traversal (breadth-first).
     * <p>
     * Level-order: visits nodes level by level from top to bottom.
     * </p>
     */
    @Test
    void testLevelOrderTraversal() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);
        bst.addElement(60);
        bst.addElement(80);

        Iterator<Integer> iter = bst.iteratorLevelOrder();
        
        assertEquals(50, iter.next());
        assertEquals(30, iter.next());
        assertEquals(70, iter.next());
        assertEquals(20, iter.next());
        assertEquals(40, iter.next());
        assertEquals(60, iter.next());
        assertEquals(80, iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(bst.isEmpty());

        bst.addElement(50);
        assertFalse(bst.isEmpty());

        bst.removeElement(50);
        assertTrue(bst.isEmpty());
    }

    /**
     * Tests the {@code size} method throughout various operations.
     */
    @Test
    void testSize() {
        assertEquals(0, bst.size());

        bst.addElement(50);
        assertEquals(1, bst.size());

        bst.addElement(30);
        bst.addElement(70);
        assertEquals(3, bst.size());

        bst.removeElement(30);
        assertEquals(2, bst.size());

        bst.removeMin();
        assertEquals(1, bst.size());

        bst.removeMax();
        assertEquals(0, bst.size());
    }

    /**
     * Tests the {@code toString} method.
     */
    @Test
    void testToString() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);

        String result = bst.toString();
        assertNotNull(result);
    }

    /**
     * Tests creating a tree with a root element via constructor.
     */
    @Test
    void testConstructorWithElement() {
        LinkedBinarySearchTree<Integer> tree = new LinkedBinarySearchTree<>(100);

        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
        assertEquals(100, tree.getRoot());
    }

    /**
     * Tests removing all elements sequentially.
     */
    @Test
    void testRemoveAllElements() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);

        while (!bst.isEmpty()) {
            bst.removeMin();
        }

        assertEquals(0, bst.size());
        assertTrue(bst.isEmpty());
    }

    /**
     * Tests BST property with complex insertions.
     * <p>
     * Verifies that regardless of insertion order,
     * inorder traversal produces sorted output.
     * </p>
     */
    @Test
    void testBSTPropertyMaintained() {
        int[] values = {45, 23, 67, 12, 34, 56, 78, 9, 15, 30, 40};
        
        for (int val : values) {
            bst.addElement(val);
        }

        assertEquals(values.length, bst.size());

        Iterator<Integer> iter = bst.iteratorInOrder();
        int previous = Integer.MIN_VALUE;
        
        while (iter.hasNext()) {
            int current = iter.next();
            assertTrue(current >= previous, "BST property violated");
            previous = current;
        }
    }

    /**
     * Tests single element tree operations.
     */
    @Test
    void testSingleElementTree() {
        bst.addElement(42);

        assertEquals(42, bst.findMin());
        assertEquals(42, bst.findMax());
        assertEquals(42, bst.getRoot());
        assertTrue(bst.contains(42));

        assertEquals(42, bst.removeMin());
        assertTrue(bst.isEmpty());
    }

    /**
     * Tests removing elements with two children.
     * <p>
     * Verifies proper successor replacement.
     * </p>
     */
    @Test
    void testRemoveNodeWithTwoChildren() {
        bst.addElement(50);
        bst.addElement(30);
        bst.addElement(70);
        bst.addElement(20);
        bst.addElement(40);
        bst.addElement(60);
        bst.addElement(80);

        bst.removeElement(50);
        assertEquals(6, bst.size());
        assertFalse(bst.contains(50));

        Iterator<Integer> iter = bst.iteratorInOrder();
        int previous = Integer.MIN_VALUE;
        while (iter.hasNext()) {
            int current = iter.next();
            assertTrue(current >= previous);
            previous = current;
        }
    }
}
