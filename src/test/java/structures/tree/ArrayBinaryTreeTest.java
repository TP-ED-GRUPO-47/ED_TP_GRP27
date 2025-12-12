package structures.tree;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.ElementNotFoundException;
import exceptions.EmptyCollectionException;

/**
 * Unit tests for the {@link ArrayBinaryTree} class.
 * <p>
 * This test suite validates the array-based binary tree implementation,
 * focusing on the basic functionality available through public methods.
 * Note: ArrayBinaryTree is primarily a base class, so tests focus on
 * constructor behavior, basic properties, and iterator functionality
 * with trees created via constructors.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Tree creation (empty and with root element)</li>
 *   <li>Basic properties (isEmpty, size, getRoot)</li>
 *   <li>Element searching (contains, find)</li>
 *   <li>String representation</li>
 *   <li>Edge cases (empty tree, single element)</li>
 *   <li>Exception handling</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see ArrayBinaryTree
 * @see BinaryTreeADT
 */
class ArrayBinaryTreeTest {

    private ArrayBinaryTree<Integer> tree;

    /**
     * Sets up a fresh empty array-based binary tree before each test.
     */
    @BeforeEach
    void setUp() {
        tree = new ArrayBinaryTree<>();
    }

    /**
     * Tests creating an empty tree.
     * <p>
     * Verifies initial state with no elements.
     * </p>
     */
    @Test
    void testEmptyTreeCreation() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertNull(tree.getRoot());
    }

    /**
     * Tests constructor with root element.
     */
    @Test
    void testConstructorWithElement() {
        ArrayBinaryTree<Integer> treeWithRoot = new ArrayBinaryTree<>(50);

        assertFalse(treeWithRoot.isEmpty());
        assertEquals(1, treeWithRoot.size());
        assertEquals(50, treeWithRoot.getRoot());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(tree.isEmpty());

        ArrayBinaryTree<Integer> treeWithRoot = new ArrayBinaryTree<>(10);
        assertFalse(treeWithRoot.isEmpty());
    }

    /**
     * Tests the {@code size} method.
     */
    @Test
    void testSize() {
        assertEquals(0, tree.size());

        ArrayBinaryTree<Integer> singleNode = new ArrayBinaryTree<>(10);
        assertEquals(1, singleNode.size());
    }

    /**
     * Tests the {@code getRoot} method.
     */
    @Test
    void testGetRoot() {
        assertNull(tree.getRoot());

        ArrayBinaryTree<Integer> treeWithRoot = new ArrayBinaryTree<>(42);
        assertEquals(42, treeWithRoot.getRoot());
    }

    /**
     * Tests the {@code contains} method on empty tree.
     * <p>
     * Note: contains calls find which throws EmptyCollectionException on empty tree.
     * </p>
     */
    @Test
    void testContainsOnEmptyTree() {
        assertThrows(EmptyCollectionException.class, () -> tree.contains(10));
    }

    /**
     * Tests the {@code contains} method on single-node tree.
     */
    @Test
    void testContainsSingleNode() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(50);
        
        assertTrue(singleTree.contains(50));
        assertFalse(singleTree.contains(100));
    }

    /**
     * Tests the {@code find} method on single-node tree.
     */
    @Test
    void testFindSingleNode() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(50);
        
        assertEquals(50, singleTree.find(50));
    }

    /**
     * Tests {@code find} on empty tree.
     * <p>
     * Should throw {@link EmptyCollectionException}.
     * </p>
     */
    @Test
    void testFindOnEmptyTree() {
        assertThrows(EmptyCollectionException.class, () -> tree.find(10));
    }

    /**
     * Tests {@code find} with non-existent element.
     * <p>
     * Should throw {@link ElementNotFoundException}.
     * </p>
     */
    @Test
    void testFindNonExistentElement() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(10);

        assertThrows(ElementNotFoundException.class, () -> singleTree.find(20));
    }

    /**
     * Tests inOrder traversal on single-node tree.
     */
    @Test
    void testInOrderTraversalSingleNode() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(42);
        
        Iterator<Integer> iterator = singleTree.iteratorInOrder();
        
        assertTrue(iterator.hasNext());
        assertEquals(42, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests preOrder traversal on single-node tree.
     */
    @Test
    void testPreOrderTraversalSingleNode() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(42);
        
        Iterator<Integer> iterator = singleTree.iteratorPreOrder();
        
        assertTrue(iterator.hasNext());
        assertEquals(42, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests postOrder traversal on single-node tree.
     */
    @Test
    void testPostOrderTraversalSingleNode() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(42);
        
        Iterator<Integer> iterator = singleTree.iteratorPostOrder();
        
        assertTrue(iterator.hasNext());
        assertEquals(42, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests levelOrder traversal on single-node tree.
     */
    @Test
    void testLevelOrderTraversalSingleNode() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(42);
        
        Iterator<Integer> iterator = singleTree.iteratorLevelOrder();
        
        assertTrue(iterator.hasNext());
        assertEquals(42, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Tests iterators on empty tree.
     */
    @Test
    void testIteratorsOnEmptyTree() {
        assertFalse(tree.iteratorInOrder().hasNext());
        assertFalse(tree.iteratorPreOrder().hasNext());
        assertFalse(tree.iteratorPostOrder().hasNext());
        assertFalse(tree.iteratorLevelOrder().hasNext());
    }

    /**
     * Tests the {@code toString} method on single-node tree.
     */
    @Test
    void testToStringSingleNode() {
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(42);

        String result = singleTree.toString();
        assertNotNull(result);
        assertTrue(result.contains("42"));
        assertTrue(result.contains("{"));
        assertTrue(result.contains("}"));
    }

    /**
     * Tests toString on empty tree.
     */
    @Test
    void testToStringOnEmptyTree() {
        String result = tree.toString();
        assertNotNull(result);
        assertTrue(result.contains("{"));
        assertTrue(result.contains("}"));
    }

    /**
     * Tests tree with String elements.
     */
    @Test
    void testStringElements() {
        ArrayBinaryTree<String> stringTree = new ArrayBinaryTree<>("Root");

        assertEquals("Root", stringTree.getRoot());
        assertTrue(stringTree.contains("Root"));
        assertFalse(stringTree.contains("Missing"));
        assertEquals(1, stringTree.size());
    }

    /**
     * Tests exception messages.
     */
    @Test
    void testExceptionMessages() {
        EmptyCollectionException emptyEx = 
            assertThrows(EmptyCollectionException.class, () -> tree.find(10));
        assertTrue(emptyEx.getMessage().contains("empty"));

        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(1);
        
        ElementNotFoundException notFoundEx = 
            assertThrows(ElementNotFoundException.class, () -> singleTree.find(99));
        assertTrue(notFoundEx.getMessage().contains("Not found"));
    }

    /**
     * Tests tree with null root.
     */
    @Test
    void testNullRoot() {
        assertNull(tree.getRoot());
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    /**
     * Tests multiple operations on same tree.
     */
    @Test
    void testMultipleOperations() {
        ArrayBinaryTree<Integer> myTree = new ArrayBinaryTree<>(100);
        
        assertEquals(100, myTree.getRoot());
        assertEquals(1, myTree.size());
        assertFalse(myTree.isEmpty());
        
        assertTrue(myTree.contains(100));
        assertEquals(100, myTree.find(100));
        
        assertFalse(myTree.contains(200));
    }

    /**
     * Tests tree properties consistency.
     */
    @Test
    void testTreePropertiesConsistency() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertNull(tree.getRoot());
        
        ArrayBinaryTree<Integer> singleTree = new ArrayBinaryTree<>(50);
        assertFalse(singleTree.isEmpty());
        assertEquals(1, singleTree.size());
        assertEquals(50, singleTree.getRoot());
    }

    /**
     * Tests find returns correct element reference.
     */
    @Test
    void testFindReturnsCorrectElement() {
        ArrayBinaryTree<Integer> myTree = new ArrayBinaryTree<>(42);
        
        Integer found = myTree.find(42);
        assertEquals(42, found);
        assertEquals(myTree.getRoot(), found);
    }

    /**
     * Tests that iterators work consistently.
     */
    @Test
    void testIteratorsConsistency() {
        ArrayBinaryTree<Integer> myTree = new ArrayBinaryTree<>(10);
        
        assertEquals(10, myTree.iteratorInOrder().next());
        assertEquals(10, myTree.iteratorPreOrder().next());
        assertEquals(10, myTree.iteratorPostOrder().next());
        assertEquals(10, myTree.iteratorLevelOrder().next());
    }

    /**
     * Tests contains behavior.
     */
    @Test
    void testContainsBehavior() {
        ArrayBinaryTree<String> myTree = new ArrayBinaryTree<>("test");
        
        assertTrue(myTree.contains("test"));
        assertFalse(myTree.contains("other"));
        assertFalse(myTree.contains("TEST"));
    }

    /**
     * Tests tree with negative numbers.
     */
    @Test
    void testNegativeNumbers() {
        ArrayBinaryTree<Integer> negTree = new ArrayBinaryTree<>(-50);
        
        assertEquals(-50, negTree.getRoot());
        assertTrue(negTree.contains(-50));
        assertEquals(-50, negTree.find(-50));
    }

    /**
     * Tests tree with zero.
     */
    @Test
    void testZeroValue() {
        ArrayBinaryTree<Integer> zeroTree = new ArrayBinaryTree<>(0);
        
        assertEquals(0, zeroTree.getRoot());
        assertTrue(zeroTree.contains(0));
        assertEquals(0, zeroTree.find(0));
        assertEquals(1, zeroTree.size());
    }

    /**
     * Tests tree with large values.
     */
    @Test
    void testLargeValues() {
        ArrayBinaryTree<Integer> largeTree = new ArrayBinaryTree<>(Integer.MAX_VALUE);
        
        assertEquals(Integer.MAX_VALUE, largeTree.getRoot());
        assertTrue(largeTree.contains(Integer.MAX_VALUE));
    }
}
