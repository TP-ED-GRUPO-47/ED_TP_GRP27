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
 * Unit tests for the {@link SingleLinkedList} class.
 * <p>
 * This test suite validates the base singly-linked list implementation,
 * which serves as the parent class for other linked list variants.
 * Tests focus on core list operations using single-direction node links.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Removing elements (first, last, specific)</li>
 *   <li>Accessing elements (first, last, contains)</li>
 *   <li>Iterator functionality with forward traversal</li>
 *   <li>Node link integrity after operations</li>
 *   <li>Edge cases (empty list, single element)</li>
 *   <li>Exception handling for invalid operations</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see SingleLinkedList
 * @see ListADT
 */
class SingleLinkedListTest {

    /**
     * Test list extending SingleLinkedList for testing purposes.
     */
    private static class TestLinkedList<T> extends SingleLinkedList<T> {
        public void addToRear(T element) {
            Node<T> newNode = new Node<>(element);
            if (isEmpty()) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
            count++;
        }

        public void addToFront(T element) {
            Node<T> newNode = new Node<>(element);
            if (isEmpty()) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head = newNode;
            }
            size++;
            count++;
        }
    }

    private TestLinkedList<Integer> list;

    /**
     * Sets up a fresh empty linked list before each test.
     */
    @BeforeEach
    void setUp() {
        list = new TestLinkedList<>();
    }

    /**
     * Tests removing the first element from the list.
     * <p>
     * Verifies that the head pointer advances to the next node.
     * </p>
     */
    @Test
    void testRemoveFirst() {
        list.addToRear(10);
        list.addToRear(20);
        list.addToRear(30);

        assertEquals(10, list.removeFirst());
        assertEquals(2, list.size());
        assertEquals(20, list.first());
    }

    /**
     * Tests removing the last element from the list.
     * <p>
     * Verifies that the tail pointer is updated correctly by
     * traversing to the second-to-last node.
     * </p>
     */
    @Test
    void testRemoveLast() {
        list.addToRear(10);
        list.addToRear(20);
        list.addToRear(30);

        assertEquals(30, list.removeLast());
        assertEquals(2, list.size());
        assertEquals(20, list.last());
    }

    /**
     * Tests removing a specific element from the middle of the list.
     * <p>
     * Verifies that the previous node's next reference is updated
     * to skip over the removed node.
     * </p>
     */
    @Test
    void testRemoveSpecificElement() {
        list.addToRear(10);
        list.addToRear(20);
        list.addToRear(30);
        list.addToRear(40);

        assertEquals(20, list.remove(20));
        assertEquals(3, list.size());
        assertFalse(list.contains(20));

        Iterator<Integer> iter = list.iterator();
        assertEquals(10, iter.next());
        assertEquals(30, iter.next());
        assertEquals(40, iter.next());
    }

    /**
     * Tests removing the first element when it's also the target.
     * <p>
     * Verifies that remove(element) correctly handles removing the head.
     * </p>
     */
    @Test
    void testRemoveFirstElement() {
        list.addToRear(10);
        list.addToRear(20);
        list.addToRear(30);

        assertEquals(10, list.remove(10));
        assertEquals(2, list.size());
        assertEquals(20, list.first());
    }

    /**
     * Tests removing the last element when it's the target.
     * <p>
     * Verifies that remove(element) correctly handles removing the tail.
     * </p>
     */
    @Test
    void testRemoveLastElement() {
        list.addToRear(10);
        list.addToRear(20);
        list.addToRear(30);

        assertEquals(30, list.remove(30));
        assertEquals(2, list.size());
        assertEquals(20, list.last());
    }

    /**
     * Tests removing the only element in a single-element list.
     * <p>
     * Verifies that both head and tail become null.
     * </p>
     */
    @Test
    void testRemoveFromSingleElementList() {
        list.addToRear(42);

        assertEquals(42, list.removeFirst());
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
        list.addToRear(10);
        list.addToRear(20);

        assertThrows(ElementNotFoundException.class, () -> list.remove(99));
        assertEquals(2, list.size());
    }

    /**
     * Tests the {@code contains} method for existing elements.
     */
    @Test
    void testContainsExistingElement() {
        list.addToRear(10);
        list.addToRear(20);
        list.addToRear(30);

        assertTrue(list.contains(10));
        assertTrue(list.contains(20));
        assertTrue(list.contains(30));
    }

    /**
     * Tests the {@code contains} method for non-existing elements.
     */
    @Test
    void testContainsNonExistingElement() {
        list.addToRear(10);
        list.addToRear(20);

        assertFalse(list.contains(5));
        assertFalse(list.contains(30));
        assertFalse(list.contains(99));
    }

    /**
     * Tests contains on an empty list.
     */
    @Test
    void testContainsOnEmptyList() {
        assertFalse(list.contains(10));
    }

    /**
     * Tests the {@code first} method returns the head element.
     * <p>
     * Verifies that the element is not removed from the list.
     * </p>
     */
    @Test
    void testFirst() {
        list.addToRear(100);
        list.addToRear(200);
        list.addToRear(300);

        assertEquals(100, list.first());
        assertEquals(3, list.size());
        assertEquals(100, list.first());
    }

    /**
     * Tests the {@code last} method returns the tail element.
     * <p>
     * Verifies that the element is not removed from the list.
     * </p>
     */
    @Test
    void testLast() {
        list.addToRear(100);
        list.addToRear(200);
        list.addToRear(300);

        assertEquals(300, list.last());
        assertEquals(3, list.size());
        assertEquals(300, list.last());
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
     * Tests accessing first and last on a single-element list.
     */
    @Test
    void testFirstAndLastOnSingleElement() {
        list.addToRear(777);

        assertEquals(777, list.first());
        assertEquals(777, list.last());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(list.isEmpty());

        list.addToRear(10);
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

        list.addToRear(10);
        assertEquals(1, list.size());

        list.addToRear(20);
        list.addToRear(30);
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
     * Verifies that the iterator traverses nodes in order from head to tail.
     * </p>
     */
    @Test
    void testIterator() {
        list.addToRear(5);
        list.addToRear(10);
        list.addToRear(15);

        Iterator<Integer> iter = list.iterator();

        assertTrue(iter.hasNext());
        assertEquals(5, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(10, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(15, iter.next());
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
        list.addToRear(100);
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
        list.addToRear(1);
        list.addToRear(2);
        list.addToRear(3);

        String result = list.toString();
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
        assertTrue(result.contains("3"));
    }

    /**
     * Tests adding duplicate elements.
     * <p>
     * Verifies that duplicates are allowed in the list.
     * </p>
     */
    @Test
    void testDuplicateElements() {
        list.addToRear(5);
        list.addToRear(5);
        list.addToRear(5);

        assertEquals(3, list.size());
        assertTrue(list.contains(5));

        list.remove(5);
        assertEquals(2, list.size());
        assertTrue(list.contains(5));
    }

    /**
     * Tests sequential add and remove operations.
     * <p>
     * Adds many elements and removes them all to test robustness.
     * </p>
     */
    @Test
    void testSequentialOperations() {
        for (int i = 1; i <= 20; i++) {
            list.addToRear(i);
        }

        assertEquals(20, list.size());

        for (int i = 1; i <= 20; i++) {
            assertEquals(i, list.removeFirst());
        }

        assertTrue(list.isEmpty());
    }

    /**
     * Tests that tail pointer is correctly maintained.
     * <p>
     * After various operations, verifies that last() returns
     * the actual last element in the chain.
     * </p>
     */
    @Test
    void testTailIntegrity() {
        list.addToRear(1);
        assertEquals(1, list.last());

        list.addToRear(2);
        assertEquals(2, list.last());

        list.addToRear(3);
        assertEquals(3, list.last());

        list.removeLast();
        assertEquals(2, list.last());
    }

    /**
     * Tests removing multiple elements in sequence.
     */
    @Test
    void testMultipleRemovals() {
        list.addToRear(10);
        list.addToRear(20);
        list.addToRear(30);
        list.addToRear(40);
        list.addToRear(50);

        list.remove(20);
        list.remove(40);

        assertEquals(3, list.size());

        Iterator<Integer> iter = list.iterator();
        assertEquals(10, iter.next());
        assertEquals(30, iter.next());
        assertEquals(50, iter.next());
    }

    /**
     * Tests head pointer integrity after various operations.
     */
    @Test
    void testHeadIntegrity() {
        list.addToFront(30);
        assertEquals(30, list.first());

        list.addToFront(20);
        assertEquals(20, list.first());

        list.addToFront(10);
        assertEquals(10, list.first());

        list.removeFirst();
        assertEquals(20, list.first());
    }
}
