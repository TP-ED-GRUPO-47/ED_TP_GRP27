package structures.linear;

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
 * Unit tests for the {@link DoubleLinkedOrderedList} class.
 * <p>
 * This test suite validates the doubly-linked ordered list implementation,
 * ensuring elements are maintained in natural order with bidirectional links.
 * Each node has both forward (next) and backward (previous) references.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Adding elements in various orders (automatic sorting)</li>
 *   <li>Removing elements (first, last, specific)</li>
 *   <li>Accessing elements (first, last, contains)</li>
 *   <li>Bidirectional node link integrity</li>
 *   <li>Iterator functionality (forward traversal)</li>
 *   <li>Edge cases (empty list, single element, duplicates)</li>
 *   <li>Exception handling for invalid operations</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see DoubleLinkedOrderedList
 * @see OrderedListADT
 * @see DoubleNode
 */
class DoubleLinkedOrderedListTest {

    private DoubleLinkedOrderedList<Integer> list;

    /**
     * Sets up a fresh empty doubly-linked ordered list before each test.
     */
    @BeforeEach
    void setUp() {
        list = new DoubleLinkedOrderedList<>();
    }

    /**
     * Tests that elements are automatically sorted upon insertion.
     * <p>
     * Adds elements in random order and verifies they are stored
     * in ascending order with proper forward and backward links.
     * </p>
     */
    @Test
    void testAddMaintainsOrder() {
        list.add(50);
        list.add(20);
        list.add(80);
        list.add(10);
        list.add(90);

        assertEquals(5, list.size());
        assertEquals(10, list.first());
        assertEquals(90, list.last());

        Iterator<Integer> iter = list.iterator();
        assertEquals(10, iter.next());
        assertEquals(20, iter.next());
        assertEquals(50, iter.next());
        assertEquals(80, iter.next());
        assertEquals(90, iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests adding elements in ascending order.
     * <p>
     * Verifies that sequential insertion at the rear works correctly.
     * </p>
     */
    @Test
    void testAddAscendingOrder() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        assertEquals(4, list.size());
        assertEquals(10, list.first());
        assertEquals(40, list.last());
    }

    /**
     * Tests adding elements in descending order.
     * <p>
     * Verifies that sequential insertion at the head works correctly.
     * </p>
     */
    @Test
    void testAddDescendingOrder() {
        list.add(40);
        list.add(30);
        list.add(20);
        list.add(10);

        assertEquals(4, list.size());
        assertEquals(10, list.first());
        assertEquals(40, list.last());

        Iterator<Integer> iter = list.iterator();
        assertEquals(10, iter.next());
        assertEquals(20, iter.next());
        assertEquals(30, iter.next());
        assertEquals(40, iter.next());
    }

    /**
     * Tests adding duplicate elements.
     * <p>
     * Verifies that duplicates are allowed and properly positioned
     * in the sorted sequence.
     * </p>
     */
    @Test
    void testAddDuplicates() {
        list.add(50);
        list.add(30);
        list.add(50);
        list.add(30);
        list.add(50);

        assertEquals(5, list.size());
        assertEquals(30, list.first());
        assertEquals(50, list.last());

        Iterator<Integer> iter = list.iterator();
        assertEquals(30, iter.next());
        assertEquals(30, iter.next());
        assertEquals(50, iter.next());
        assertEquals(50, iter.next());
        assertEquals(50, iter.next());
    }

    /**
     * Tests adding elements to an empty list.
     * <p>
     * Verifies that head and tail point to the same node.
     * </p>
     */
    @Test
    void testAddToEmptyList() {
        list.add(42);

        assertEquals(1, list.size());
        assertEquals(42, list.first());
        assertEquals(42, list.last());
        assertFalse(list.isEmpty());
    }

    /**
     * Tests removing the first element from the list.
     * <p>
     * Verifies that the head pointer advances and the new head's
     * previous reference is set to null.
     * </p>
     */
    @Test
    void testRemoveFirst() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        assertEquals(10, list.removeFirst());
        assertEquals(3, list.size());
        assertEquals(20, list.first());
    }

    /**
     * Tests removing the last element from the list.
     * <p>
     * Verifies that the tail pointer moves backward and the new tail's
     * next reference is set to null.
     * </p>
     */
    @Test
    void testRemoveLast() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        assertEquals(40, list.removeLast());
        assertEquals(3, list.size());
        assertEquals(30, list.last());
    }

    /**
     * Tests removing a specific element from the middle of the list.
     * <p>
     * Verifies that bidirectional links are properly maintained:
     * previous node's next and next node's previous are updated.
     * </p>
     */
    @Test
    void testRemoveSpecificElement() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);
        list.add(50);

        assertEquals(30, list.remove(30));
        assertEquals(4, list.size());
        assertFalse(list.contains(30));

        Iterator<Integer> iter = list.iterator();
        assertEquals(10, iter.next());
        assertEquals(20, iter.next());
        assertEquals(40, iter.next());
        assertEquals(50, iter.next());
    }

    /**
     * Tests removing the first element using {@code remove(element)}.
     * <p>
     * Verifies that removing the head element works correctly.
     * </p>
     */
    @Test
    void testRemoveFirstElement() {
        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(10, list.remove(10));
        assertEquals(2, list.size());
        assertEquals(20, list.first());
    }

    /**
     * Tests removing the last element using {@code remove(element)}.
     * <p>
     * Verifies that removing the tail element works correctly.
     * </p>
     */
    @Test
    void testRemoveLastElement() {
        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(30, list.remove(30));
        assertEquals(2, list.size());
        assertEquals(20, list.last());
    }

    /**
     * Tests removing the only element in a single-element list.
     * <p>
     * Verifies that both head and tail become null after removal.
     * </p>
     */
    @Test
    void testRemoveFromSingleElementList() {
        list.add(100);

        assertEquals(100, list.removeFirst());
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    /**
     * Tests removing from an empty list.
     * <p>
     * Verifies that {@link ElementNotFoundException} is thrown.
     * </p>
     */
    @Test
    void testRemoveFromEmptyList() {
        assertTrue(list.isEmpty());

        assertThrows(ElementNotFoundException.class, () -> list.removeFirst());
        assertThrows(ElementNotFoundException.class, () -> list.removeLast());
    }

    /**
     * Tests removing a non-existent element.
     * <p>
     * Verifies that {@link ElementNotFoundException} is thrown.
     * </p>
     */
    @Test
    void testRemoveNonExistentElement() {
        list.add(10);
        list.add(20);

        assertThrows(ElementNotFoundException.class, () -> list.remove(99));
        assertEquals(2, list.size());
    }

    /**
     * Tests removing duplicate elements.
     * <p>
     * Verifies that only the first occurrence is removed.
     * </p>
     */
    @Test
    void testRemoveDuplicate() {
        list.add(10);
        list.add(20);
        list.add(20);
        list.add(30);

        assertEquals(20, list.remove(20));
        assertEquals(3, list.size());
        assertTrue(list.contains(20));

        Iterator<Integer> iter = list.iterator();
        assertEquals(10, iter.next());
        assertEquals(20, iter.next());
        assertEquals(30, iter.next());
    }

    /**
     * Tests the {@code contains} method for existing and non-existing elements.
     */
    @Test
    void testContains() {
        list.add(100);
        list.add(200);
        list.add(300);

        assertTrue(list.contains(100));
        assertTrue(list.contains(200));
        assertTrue(list.contains(300));
        assertFalse(list.contains(50));
        assertFalse(list.contains(250));
        assertFalse(list.contains(400));
    }

    /**
     * Tests the {@code first} method.
     * <p>
     * Verifies that the first (smallest) element is returned without removal.
     * </p>
     */
    @Test
    void testFirst() {
        list.add(50);
        list.add(20);
        list.add(80);

        assertEquals(20, list.first());
        assertEquals(3, list.size());
    }

    /**
     * Tests the {@code last} method.
     * <p>
     * Verifies that the last (largest) element is returned without removal.
     * </p>
     */
    @Test
    void testLast() {
        list.add(50);
        list.add(20);
        list.add(80);

        assertEquals(80, list.last());
        assertEquals(3, list.size());
    }

    /**
     * Tests accessing first and last on an empty list.
     * <p>
     * Verifies that {@link ElementNotFoundException} is thrown.
     * </p>
     */
    @Test
    void testFirstAndLastOnEmptyList() {
        assertThrows(ElementNotFoundException.class, () -> list.first());
        assertThrows(ElementNotFoundException.class, () -> list.last());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(list.isEmpty());

        list.add(10);
        assertFalse(list.isEmpty());

        list.removeFirst();
        assertTrue(list.isEmpty());
    }

    /**
     * Tests the {@code size} method throughout various operations.
     */
    @Test
    void testSize() {
        assertEquals(0, list.size());

        list.add(10);
        assertEquals(1, list.size());

        list.add(20);
        list.add(30);
        assertEquals(3, list.size());

        list.removeFirst();
        assertEquals(2, list.size());

        list.removeLast();
        assertEquals(1, list.size());

        list.remove(20);
        assertEquals(0, list.size());
    }

    /**
     * Tests the iterator functionality.
     * <p>
     * Verifies that the iterator traverses nodes in ascending order
     * from head to tail using forward links.
     * </p>
     */
    @Test
    void testIterator() {
        list.add(30);
        list.add(10);
        list.add(20);

        Iterator<Integer> iter = list.iterator();

        assertTrue(iter.hasNext());
        assertEquals(10, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(20, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(30, iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests iterator on an empty list.
     */
    @Test
    void testIteratorOnEmptyList() {
        Iterator<Integer> iter = list.iterator();
        assertFalse(iter.hasNext());
        assertThrows(ElementNotFoundException.class, () -> iter.next());
    }

    /**
     * Tests iterator behavior when calling {@code next} beyond available elements.
     */
    @Test
    void testIteratorNoSuchElement() {
        list.add(100);
        Iterator<Integer> iter = list.iterator();

        iter.next();
        assertFalse(iter.hasNext());
        assertThrows(ElementNotFoundException.class, () -> iter.next());
    }

    /**
     * Tests the {@code toString} method.
     */
    @Test
    void testToString() {
        list.add(3);
        list.add(1);
        list.add(2);

        String result = list.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("3"));
    }

    /**
     * Tests adding and removing many elements.
     * <p>
     * Stress test to verify list integrity with multiple operations.
     * </p>
     */
    @Test
    void testManyOperations() {
        for (int i = 20; i > 0; i--) {
            list.add(i);
        }

        assertEquals(20, list.size());
        assertEquals(1, list.first());
        assertEquals(20, list.last());

        for (int i = 1; i <= 20; i++) {
            assertEquals(i, list.removeFirst());
        }

        assertTrue(list.isEmpty());
    }

    /**
     * Tests bidirectional link integrity.
     * <p>
     * After insertions and deletions, verifies that traversal
     * still works correctly (which depends on proper link maintenance).
     * </p>
     */
    @Test
    void testBidirectionalLinkIntegrity() {
        list.add(5);
        list.add(3);
        list.add(7);
        list.add(1);
        list.add(9);

        list.remove(3);
        list.remove(7);

        assertEquals(3, list.size());

        Iterator<Integer> iter = list.iterator();
        assertEquals(1, iter.next());
        assertEquals(5, iter.next());
        assertEquals(9, iter.next());
    }

    /**
     * Tests adding elements with equal values.
     * <p>
     * Verifies that elements with the same value are inserted
     * consecutively in the sorted list.
     * </p>
     */
    @Test
    void testEqualElements() {
        list.add(5);
        list.add(5);
        list.add(5);

        assertEquals(3, list.size());
        assertEquals(5, list.first());
        assertEquals(5, list.last());

        Iterator<Integer> iter = list.iterator();
        assertEquals(5, iter.next());
        assertEquals(5, iter.next());
        assertEquals(5, iter.next());
    }

    /**
     * Tests adding a single element and verifying head equals tail.
     */
    @Test
    void testSingleElementHeadEqualsTail() {
        list.add(777);

        assertEquals(1, list.size());
        assertEquals(777, list.first());
        assertEquals(777, list.last());
    }
}
