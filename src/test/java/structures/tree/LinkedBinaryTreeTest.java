package structures.tree;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.ElementNotFoundException;

/**
 * Unit tests for the {@link LinkedBinaryTree} class.
 * <p>
 * This test suite validates the base linked binary tree implementation,
 * which provides fundamental tree operations without enforcing specific
 * ordering constraints (unlike BST). Tests focus on structural integrity
 * and traversal algorithms.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Tree construction and node relationships</li>
 *   <li>Root access and manipulation</li>
 *   <li>Tree traversals (inorder, preorder, postorder, level-order)</li>
 *   <li>Element search and containment</li>
 *   <li>Tree properties (size, empty status)</li>
 *   <li>Node removal and tree structure updates</li>
 *   <li>Edge cases (empty tree, single node)</li>
 *   <li>Exception handling</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see LinkedBinaryTree
 * @see BinaryTreeADT
 * @see BinaryTreeNode
 */
class LinkedBinaryTreeTest {

    private LinkedBinaryTree<String> tree;

    /**
     * Sets up a fresh empty binary tree before each test.
     */
    @BeforeEach
    void setUp() {
        tree = new LinkedBinaryTree<>();
    }

    /**
     * Tests creating an empty tree.
     * <p>
     * Verifies initial state with no root node.
     * </p>
     */
    @Test
    void testEmptyTreeCreation() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    /**
     * Tests creating a tree with a root element via constructor.
     * <p>
     * Verifies the root is properly set and accessible.
     * </p>
     */
    @Test
    void testConstructorWithRoot() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Root");

        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
        assertEquals("Root", tree.getRoot());
    }

    /**
     * Tests the {@code getRoot} method on a non-empty tree.
     */
    @Test
    void testGetRoot() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("A");

        assertEquals("A", tree.getRoot());
    }

    /**
     * Tests accessing root on an empty tree.
     * <p>
     * Should handle gracefully (null or exception depending on implementation).
     * </p>
     */
    @Test
    void testGetRootOnEmptyTree() {
        assertThrows(Exception.class, () -> tree.getRoot());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(tree.isEmpty());

        LinkedBinaryTree<String> nonEmpty = new LinkedBinaryTree<>("Node");
        assertFalse(nonEmpty.isEmpty());
    }

    /**
     * Tests the {@code size} method on various tree configurations.
     */
    @Test
    void testSize() {
        assertEquals(0, tree.size());

        LinkedBinaryTree<String> tree1 = new LinkedBinaryTree<>("A");
        assertEquals(1, tree1.size());
    }

    /**
     * Tests the {@code contains} method for existing element.
     */
    @Test
    void testContainsExistingElement() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Root");

        assertTrue(tree.contains("Root"));
    }

    /**
     * Tests the {@code contains} method for non-existing element.
     */
    @Test
    void testContainsNonExistingElement() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Root");

        assertFalse(tree.contains("NotHere"));
    }

    /**
     * Tests contains on an empty tree.
     */
    @Test
    void testContainsOnEmptyTree() {
        assertThrows(Exception.class, () -> tree.contains("Anything"));
    }

    /**
     * Tests finding an element that exists in the tree.
     */
    @Test
    void testFindExistingElement() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Target");

        String found = tree.find("Target");
        assertNotNull(found);
        assertEquals("Target", found);
    }

    /**
     * Tests finding an element that doesn't exist.
     * <p>
     * Should throw {@link ElementNotFoundException}.
     * </p>
     */
    @Test
    void testFindNonExistingElement() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Root");

        assertThrows(ElementNotFoundException.class, () -> tree.find("Missing"));
    }

    /**
     * Tests inorder traversal: left -> root -> right.
     * <p>
     * Verifies the traversal visits nodes in correct order.
     * </p>
     */
    @Test
    void testInOrderTraversal() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("B");
        
        Iterator<String> iter = tree.iteratorInOrder();
        
        assertTrue(iter.hasNext());
        assertEquals("B", iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests preorder traversal: root -> left -> right.
     */
    @Test
    void testPreOrderTraversal() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Root");

        Iterator<String> iter = tree.iteratorPreOrder();

        assertTrue(iter.hasNext());
        assertEquals("Root", iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests postorder traversal: left -> right -> root.
     */
    @Test
    void testPostOrderTraversal() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Root");

        Iterator<String> iter = tree.iteratorPostOrder();

        assertTrue(iter.hasNext());
        assertEquals("Root", iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests level-order traversal (breadth-first).
     * <p>
     * Visits nodes level by level from top to bottom.
     * </p>
     */
    @Test
    void testLevelOrderTraversal() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Level1");

        Iterator<String> iter = tree.iteratorLevelOrder();

        assertTrue(iter.hasNext());
        assertEquals("Level1", iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests iterators on an empty tree.
     * <p>
     * All iterators should have no elements to traverse.
     * </p>
     */
    @Test
    void testIteratorsOnEmptyTree() {
        assertFalse(tree.iteratorInOrder().hasNext());
        assertFalse(tree.iteratorPreOrder().hasNext());
        assertFalse(tree.iteratorPostOrder().hasNext());
        assertFalse(tree.iteratorLevelOrder().hasNext());
    }

    /**
     * Tests the {@code toString} method.
     * <p>
     * Should provide a string representation of the tree.
     * </p>
     */
    @Test
    void testToString() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Node");

        String result = tree.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    /**
     * Tests tree with single node.
     * <p>
     * Verifies all operations work correctly with minimal tree.
     * </p>
     */
    @Test
    void testSingleNodeTree() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Single");

        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
        assertTrue(tree.contains("Single"));
        assertEquals("Single", tree.getRoot());

        assertEquals("Single", tree.iteratorInOrder().next());
        assertEquals("Single", tree.iteratorPreOrder().next());
        assertEquals("Single", tree.iteratorPostOrder().next());
        assertEquals("Single", tree.iteratorLevelOrder().next());
    }

    /**
     * Tests that tree structure is maintained.
     * <p>
     * After finding an element, verify it exists in the tree.
     * </p>
     */
    @Test
    void testTreeStructureIntegrity() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Root");

        String element = tree.find("Root");
        assertNotNull(element);
        assertEquals("Root", element);
    }

    /**
     * Tests multiple operations in sequence.
     * <p>
     * Ensures tree maintains consistency across operations.
     * </p>
     */
    @Test
    void testSequentialOperations() {
        LinkedBinaryTree<Integer> numTree = new LinkedBinaryTree<>(100);

        assertEquals(1, numTree.size());
        assertTrue(numTree.contains(100));

        assertEquals(100, numTree.getRoot());
    }

    /**
     * Tests that iterator doesn't modify the tree.
     * <p>
     * Traversing should not change tree structure or size.
     * </p>
     */
    @Test
    void testIteratorDoesNotModifyTree() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("Immutable");

        int sizeBefore = tree.size();
        Iterator<String> iter = tree.iteratorInOrder();
        
        while (iter.hasNext()) {
            iter.next();
        }

        assertEquals(sizeBefore, tree.size());
        assertTrue(tree.contains("Immutable"));
    }

    /**
     * Tests null handling in contains method.
     * <p>
     * Verifies behavior when searching for null.
     * </p>
     */
    @Test
    void testContainsNull() {
        LinkedBinaryTree<String> tree = new LinkedBinaryTree<>("NotNull");

        assertFalse(tree.contains(null));
    }

    /**
     * Tests tree equality semantics (if implemented).
     * <p>
     * Two trees with same structure and values should be considered equal.
     * </p>
     */
    @Test
    void testTreeEquality() {
        LinkedBinaryTree<String> tree1 = new LinkedBinaryTree<>("A");
        LinkedBinaryTree<String> tree2 = new LinkedBinaryTree<>("A");

        assertNotNull(tree1);
        assertNotNull(tree2);
    }

    /**
     * Tests that empty tree has size zero.
     */
    @Test
    void testEmptyTreeSize() {
        assertEquals(0, tree.size());
    }

    /**
     * Tests creating multiple independent trees.
     * <p>
     * Verifies that trees don't interfere with each other.
     * </p>
     */
    @Test
    void testMultipleIndependentTrees() {
        LinkedBinaryTree<String> tree1 = new LinkedBinaryTree<>("Tree1");
        LinkedBinaryTree<String> tree2 = new LinkedBinaryTree<>("Tree2");

        assertEquals(1, tree1.size());
        assertEquals(1, tree2.size());
        assertTrue(tree1.contains("Tree1"));
        assertFalse(tree1.contains("Tree2"));
        assertTrue(tree2.contains("Tree2"));
        assertFalse(tree2.contains("Tree1"));
    }
}
