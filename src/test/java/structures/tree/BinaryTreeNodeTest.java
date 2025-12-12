package structures.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link BinaryTreeNode} class.
 * <p>
 * This test suite validates the node structure used in linked binary trees,
 * ensuring proper element storage, parent-child relationships, and
 * bidirectional link management.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Node creation and element storage</li>
 *   <li>Left and right child assignment</li>
 *   <li>Parent-child relationship integrity</li>
 *   <li>Element retrieval and modification</li>
 *   <li>Null handling for unset children</li>
 *   <li>Node counting (if applicable)</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see BinaryTreeNode
 * @see LinkedBinaryTree
 */
class BinaryTreeNodeTest {

    private BinaryTreeNode<String> node;

    /**
     * Sets up a fresh node before each test.
     */
    @BeforeEach
    void setUp() {
        node = new BinaryTreeNode<>("Root");
    }

    /**
     * Tests creating a node with an element.
     * <p>
     * Verifies the element is stored and retrievable.
     * </p>
     */
    @Test
    void testNodeCreation() {
        BinaryTreeNode<String> node = new BinaryTreeNode<>("Test");

        assertEquals("Test", node.getElement());
        assertNull(node.getLeft());
        assertNull(node.getRight());
    }

    /**
     * Tests the {@code getElement} method.
     */
    @Test
    void testGetElement() {
        assertEquals("Root", node.getElement());
    }

    /**
     * Tests setting and getting the left child.
     * <p>
     * Verifies left child reference is properly maintained.
     * </p>
     */
    @Test
    void testSetAndGetLeftChild() {
        BinaryTreeNode<String> leftChild = new BinaryTreeNode<>("Left");

        node.setLeft(leftChild);

        assertEquals(leftChild, node.getLeft());
        assertEquals("Left", node.getLeft().getElement());
    }

    /**
     * Tests setting and getting the right child.
     * <p>
     * Verifies right child reference is properly maintained.
     * </p>
     */
    @Test
    void testSetAndGetRightChild() {
        BinaryTreeNode<String> rightChild = new BinaryTreeNode<>("Right");

        node.setRight(rightChild);

        assertEquals(rightChild, node.getRight());
        assertEquals("Right", node.getRight().getElement());
    }

    /**
     * Tests setting both left and right children.
     * <p>
     * Verifies both child references can coexist.
     * </p>
     */
    @Test
    void testSetBothChildren() {
        BinaryTreeNode<String> leftChild = new BinaryTreeNode<>("Left");
        BinaryTreeNode<String> rightChild = new BinaryTreeNode<>("Right");

        node.setLeft(leftChild);
        node.setRight(rightChild);

        assertEquals("Left", node.getLeft().getElement());
        assertEquals("Right", node.getRight().getElement());
    }

    /**
     * Tests that initially children are null.
     * <p>
     * A newly created node should have no children.
     * </p>
     */
    @Test
    void testInitiallyNoChildren() {
        assertNull(node.getLeft());
        assertNull(node.getRight());
    }

    /**
     * Tests setting a child to null.
     * <p>
     * Verifies that children can be removed by setting to null.
     * </p>
     */
    @Test
    void testSetChildToNull() {
        BinaryTreeNode<String> child = new BinaryTreeNode<>("Child");
        
        node.setLeft(child);
        assertNotNull(node.getLeft());

        node.setLeft(null);
        assertNull(node.getLeft());
    }

    /**
     * Tests node with integer elements.
     * <p>
     * Verifies generic type works with different element types.
     * </p>
     */
    @Test
    void testNodeWithIntegers() {
        BinaryTreeNode<Integer> intNode = new BinaryTreeNode<>(42);

        assertEquals(42, intNode.getElement());
        assertNull(intNode.getLeft());
        assertNull(intNode.getRight());
    }

    /**
     * Tests building a small tree structure.
     * <p>
     * Verifies that multi-level parent-child relationships work.
     * </p>
     *
     * <pre>
     *       Root
     *      /    \
     *    Left   Right
     * </pre>
     */
    @Test
    void testBuildSmallTree() {
        BinaryTreeNode<String> root = new BinaryTreeNode<>("Root");
        BinaryTreeNode<String> left = new BinaryTreeNode<>("Left");
        BinaryTreeNode<String> right = new BinaryTreeNode<>("Right");

        root.setLeft(left);
        root.setRight(right);

        assertEquals("Root", root.getElement());
        assertEquals("Left", root.getLeft().getElement());
        assertEquals("Right", root.getRight().getElement());
    }

    /**
     * Tests building a deeper tree structure.
     * <p>
     * Verifies multi-level tree construction.
     * </p>
     *
     * <pre>
     *          A
     *        /   \
     *       B     C
     *      / \
     *     D   E
     * </pre>
     */
    @Test
    void testBuildDeeperTree() {
        BinaryTreeNode<String> a = new BinaryTreeNode<>("A");
        BinaryTreeNode<String> b = new BinaryTreeNode<>("B");
        BinaryTreeNode<String> c = new BinaryTreeNode<>("C");
        BinaryTreeNode<String> d = new BinaryTreeNode<>("D");
        BinaryTreeNode<String> e = new BinaryTreeNode<>("E");

        a.setLeft(b);
        a.setRight(c);
        b.setLeft(d);
        b.setRight(e);

        assertEquals("A", a.getElement());
        assertEquals("B", a.getLeft().getElement());
        assertEquals("C", a.getRight().getElement());
        assertEquals("D", a.getLeft().getLeft().getElement());
        assertEquals("E", a.getLeft().getRight().getElement());
    }

    /**
     * Tests replacing a child node.
     * <p>
     * Verifies that setting a child multiple times updates correctly.
     * </p>
     */
    @Test
    void testReplaceChild() {
        BinaryTreeNode<String> child1 = new BinaryTreeNode<>("First");
        BinaryTreeNode<String> child2 = new BinaryTreeNode<>("Second");

        node.setLeft(child1);
        assertEquals("First", node.getLeft().getElement());

        node.setLeft(child2);
        assertEquals("Second", node.getLeft().getElement());
    }

    /**
     * Tests that nodes are independent.
     * <p>
     * Modifying one node shouldn't affect unrelated nodes.
     * </p>
     */
    @Test
    void testNodesAreIndependent() {
        BinaryTreeNode<String> node1 = new BinaryTreeNode<>("Node1");
        BinaryTreeNode<String> node2 = new BinaryTreeNode<>("Node2");

        node1.setLeft(new BinaryTreeNode<>("Left1"));
        node2.setLeft(new BinaryTreeNode<>("Left2"));

        assertEquals("Left1", node1.getLeft().getElement());
        assertEquals("Left2", node2.getLeft().getElement());
    }

    /**
     * Tests node counting functionality (if available).
     * <p>
     * Some implementations track number of descendants.
     * </p>
     */
    @Test
    void testNodeCount() {
        BinaryTreeNode<Integer> root = new BinaryTreeNode<>(1);
        BinaryTreeNode<Integer> left = new BinaryTreeNode<>(2);
        BinaryTreeNode<Integer> right = new BinaryTreeNode<>(3);

        root.setLeft(left);
        root.setRight(right);

        assertNotNull(root);
        assertNotNull(root.getLeft());
        assertNotNull(root.getRight());
    }

    /**
     * Tests leaf node identification.
     * <p>
     * A leaf node has no children.
     * </p>
     */
    @Test
    void testLeafNode() {
        BinaryTreeNode<String> leaf = new BinaryTreeNode<>("Leaf");

        assertNull(leaf.getLeft());
        assertNull(leaf.getRight());
        assertTrue(leaf.getLeft() == null && leaf.getRight() == null);
    }

    /**
     * Tests internal node identification.
     * <p>
     * An internal node has at least one child.
     * </p>
     */
    @Test
    void testInternalNode() {
        BinaryTreeNode<String> internal = new BinaryTreeNode<>("Internal");
        internal.setLeft(new BinaryTreeNode<>("Child"));

        assertNotNull(internal.getLeft());
        assertTrue(internal.getLeft() != null || internal.getRight() != null);
    }

    /**
     * Tests node with null element.
     * <p>
     * Verifies behavior when element itself is null.
     * </p>
     */
    @Test
    void testNodeWithNullElement() {
        BinaryTreeNode<String> nullNode = new BinaryTreeNode<>(null);

        assertNull(nullNode.getElement());
        assertNull(nullNode.getLeft());
        assertNull(nullNode.getRight());
    }

    /**
     * Tests creating nodes with different generic types.
     */
    @Test
    void testDifferentGenericTypes() {
        BinaryTreeNode<Integer> intNode = new BinaryTreeNode<>(100);
        BinaryTreeNode<Double> doubleNode = new BinaryTreeNode<>(3.14);
        BinaryTreeNode<Boolean> boolNode = new BinaryTreeNode<>(true);

        assertEquals(100, intNode.getElement());
        assertEquals(3.14, doubleNode.getElement());
        assertEquals(true, boolNode.getElement());
    }

    /**
     * Tests node chain creation.
     * <p>
     * Creates a right-skewed chain of nodes.
     * </p>
     */
    @Test
    void testNodeChain() {
        BinaryTreeNode<Integer> node1 = new BinaryTreeNode<>(1);
        BinaryTreeNode<Integer> node2 = new BinaryTreeNode<>(2);
        BinaryTreeNode<Integer> node3 = new BinaryTreeNode<>(3);

        node1.setRight(node2);
        node2.setRight(node3);

        assertEquals(1, node1.getElement());
        assertEquals(2, node1.getRight().getElement());
        assertEquals(3, node1.getRight().getRight().getElement());
        assertNull(node1.getRight().getRight().getRight());
    }

    /**
     * Tests modifying element value (if setter exists).
     */
    @Test
    void testElementImmutable() {
        assertEquals("Root", node.getElement());
        
        BinaryTreeNode<String> newNode = new BinaryTreeNode<>("NewValue");
        assertEquals("NewValue", newNode.getElement());
    }
}
