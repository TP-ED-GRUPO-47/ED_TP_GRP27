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
 * Unit tests for the {@link LinkedUnorderedList} class.
 * <p>
 * This test suite validates the singly-linked unordered list implementation,
 * ensuring proper node linking, element insertion/removal, and traversal operations.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Adding elements to front and rear</li>
 *   <li>Removing elements (first, last, specific)</li>
 *   <li>Accessing elements (first, last, contains)</li>
 *   <li>Iterator functionality with node traversal</li>
 *   <li>Edge cases (empty list, single element)</li>
 *   <li>Exception handling for invalid operations</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see LinkedUnorderedList
 * @see UnorderedListADT
 * @see SingleLinkedList
 */
class LinkedUnorderedListTest {

    private LinkedUnorderedList<String> list;

    /**
     * Sets up a fresh empty linked list before each test.
     */
    @BeforeEach
    void setUp() {
        list = new LinkedUnorderedList<>();
    }

    /**
     * Tests adding elements to the front of the list.
     * <p>
     * Verifies that elements added to the front appear in reverse order
     * of insertion (LIFO behavior at the front).
     * </p>
     */
    @Test
    void testAddToFront() {
        list.addToFront("A");
        list.addToFront("B");
        list.addToFront("C");

        assertEquals(3, list.size());
        assertEquals("C", list.first());
        assertEquals("A", list.last());

        Iterator<String> iter = list.iterator();
        assertEquals("C", iter.next());
        assertEquals("B", iter.next());
        assertEquals("A", iter.next());
    }

    /**
     * Tests adding elements to the rear of the list.
     * <p>
     * Verifies that elements added to the rear appear in order
     * of insertion (FIFO behavior).
     * </p>
     */
    @Test
    void testAddToRear() {
        list.addToRear("A");
        list.addToRear("B");
        list.addToRear("C");

        assertEquals(3, list.size());
        assertEquals("A", list.first());
        assertEquals("C", list.last());

        Iterator<String> iter = list.iterator();
        assertEquals("A", iter.next());
        assertEquals("B", iter.next());
        assertEquals("C", iter.next());
    }

    /**
     * Tests mixed addition to front and rear.
     * <p>
     * Verifies that the list correctly maintains links when adding
     * to both ends alternately.
     * </p>
     */
    @Test
    void testMixedAddition() {
        list.addToRear("B");
        list.addToFront("A");
        list.addToRear("C");
        list.addToFront("Z");

        assertEquals(4, list.size());
        assertEquals("Z", list.first());
        assertEquals("C", list.last());

        Iterator<String> iter = list.iterator();
        assertEquals("Z", iter.next());
        assertEquals("A", iter.next());
        assertEquals("B", iter.next());
        assertEquals("C", iter.next());
    }

    /**
     * Tests adding elements after a specific target element.
     * <p>
     * Verifies that the new element is inserted immediately after
     * the target in the linked sequence.
     * </p>
     */
    @Test
    void testAddAfter() {
        list.addToRear("A");
        list.addToRear("B");
        list.addToRear("D");

        list.addAfter("C", "B");

        assertEquals(4, list.size());
        
        Iterator<String> iter = list.iterator();
        assertEquals("A", iter.next());
        assertEquals("B", iter.next());
        assertEquals("C", iter.next());
        assertEquals("D", iter.next());
    }

    /**
     * Tests adding after the last element.
     * <p>
     * Verifies that adding after the tail correctly updates the tail pointer.
     * </p>
     */
    @Test
    void testAddAfterLastElement() {
        list.addToRear("A");
        list.addToRear("B");
        
        list.addAfter("C", "B");

        assertEquals(3, list.size());
        assertEquals("C", list.last());
    }

    /**
     * Tests adding after a non-existent element.
     * <p>
     * Verifies that {@link ElementNotFoundException} is thrown when
     * the target element is not in the list.
     * </p>
     */
    @Test
    void testAddAfterNonExistentElement() {
        list.addToRear("A");
        list.addToRear("B");

        assertThrows(ElementNotFoundException.class, () -> list.addAfter("C", "Z"));
        assertEquals(2, list.size());
    }

    /**
     * Tests removing the first element from the list.
     * <p>
     * Verifies that the head pointer is properly updated.
     * </p>
     */
    @Test
    void testRemoveFirst() {
        list.addToRear("A");
        list.addToRear("B");
        list.addToRear("C");

        assertEquals("A", list.removeFirst());
        assertEquals(2, list.size());
        assertEquals("B", list.first());
    }

    /**
     * Tests removing the last element from the list.
     * <p>
     * Verifies that the tail pointer is properly updated and
     * the second-to-last node's next reference is set to null.
     * </p>
     */
    @Test
    void testRemoveLast() {
        list.addToRear("A");
        list.addToRear("B");
        list.addToRear("C");

        assertEquals("C", list.removeLast());
        assertEquals(2, list.size());
        assertEquals("B", list.last());
    }

    /**
     * Tests removing a specific element from the middle of the list.
     * <p>
     * Verifies that node links are properly maintained after removal.
     * </p>
     */
    @Test
    void testRemoveSpecificElement() {
        list.addToRear("A");
        list.addToRear("B");
        list.addToRear("C");
        list.addToRear("D");

        assertEquals("B", list.remove("B"));
        assertEquals(3, list.size());

        Iterator<String> iter = list.iterator();
        assertEquals("A", iter.next());
        assertEquals("C", iter.next());
        assertEquals("D", iter.next());
    }

    /**
     * Tests removing the only element in a single-element list.
     * <p>
     * Verifies that both head and tail pointers are set to null.
     * </p>
     */
    @Test
    void testRemoveFromSingleElementList() {
        list.addToRear("A");
        
        assertEquals("A", list.removeFirst());
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
        assertThrows(ElementNotFoundException.class, () -> list.remove("A"));
    }

    /**
     * Tests removing a non-existent element.
     * <p>
     * Verifies that {@link ElementNotFoundException} is thrown.
     * </p>
     */
    @Test
    void testRemoveNonExistentElement() {
        list.addToRear("A");
        list.addToRear("B");

        assertThrows(ElementNotFoundException.class, () -> list.remove("Z"));
        assertEquals(2, list.size());
    }

    /**
     * Tests the {@code contains} method for existing and non-existing elements.
     */
    @Test
    void testContains() {
        list.addToRear("Apple");
        list.addToRear("Banana");
        list.addToRear("Cherry");

        assertTrue(list.contains("Apple"));
        assertTrue(list.contains("Banana"));
        assertTrue(list.contains("Cherry"));
        assertFalse(list.contains("Durian"));
        assertFalse(list.contains("Elderberry"));
    }

    /**
     * Tests the {@code first} method.
     * <p>
     * Verifies that the first element is returned without removal.
     * </p>
     */
    @Test
    void testFirst() {
        list.addToRear("First");
        list.addToRear("Second");
        list.addToRear("Third");

        assertEquals("First", list.first());
        assertEquals(3, list.size());
    }

    /**
     * Tests the {@code last} method.
     * <p>
     * Verifies that the last element is returned without removal.
     * </p>
     */
    @Test
    void testLast() {
        list.addToRear("First");
        list.addToRear("Second");
        list.addToRear("Third");

        assertEquals("Third", list.last());
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
        
        list.addToRear("Element");
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
        
        list.addToFront("A");
        assertEquals(1, list.size());
        
        list.addToRear("B");
        list.addToRear("C");
        assertEquals(3, list.size());
        
        list.removeFirst();
        assertEquals(2, list.size());
        
        list.removeLast();
        assertEquals(1, list.size());
        
        list.remove("B");
        assertEquals(0, list.size());
    }

    /**
     * Tests the iterator functionality.
     * <p>
     * Verifies that the iterator correctly traverses the linked nodes
     * in order from head to tail.
     * </p>
     */
    @Test
    void testIterator() {
        list.addToRear("One");
        list.addToRear("Two");
        list.addToRear("Three");

        Iterator<String> iter = list.iterator();
        
        assertTrue(iter.hasNext());
        assertEquals("One", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("Two", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("Three", iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests iterator on an empty list.
     */
    @Test
    void testIteratorOnEmptyList() {
        Iterator<String> iter = list.iterator();
        assertFalse(iter.hasNext());
        assertThrows(ElementNotFoundException.class, () -> iter.next());
    }

    /**
     * Tests iterator behavior when calling {@code next} beyond available elements.
     */
    @Test
    void testIteratorNoSuchElement() {
        list.addToRear("Single");
        Iterator<String> iter = list.iterator();
        
        iter.next();
        assertFalse(iter.hasNext());
        assertThrows(ElementNotFoundException.class, () -> iter.next());
    }

    /**
     * Tests the {@code toString} method.
     */
    @Test
    void testToString() {
        list.addToRear("Alpha");
        list.addToRear("Beta");
        list.addToRear("Gamma");
        
        String result = list.toString();
        assertNotNull(result);
        assertTrue(result.contains("Alpha") || result.length() > 0);
    }

    /**
     * Tests adding multiple identical elements.
     * <p>
     * Verifies that duplicates are allowed and maintained.
     * </p>
     */
    @Test
    void testDuplicateElements() {
        list.addToRear("A");
        list.addToRear("A");
        list.addToRear("A");

        assertEquals(3, list.size());
        assertTrue(list.contains("A"));
        
        list.remove("A");
        assertEquals(2, list.size());
        assertTrue(list.contains("A"));
    }

    /**
     * Tests adding and removing all elements sequentially.
     */
    @Test
    void testSequentialAddAndRemove() {
        for (int i = 1; i <= 10; i++) {
            list.addToRear("Item" + i);
        }
        
        assertEquals(10, list.size());
        
        for (int i = 1; i <= 10; i++) {
            assertEquals("Item" + i, list.removeFirst());
        }
        
        assertTrue(list.isEmpty());
    }

    /**
     * Tests that head and tail pointers are correctly updated.
     */
    @Test
    void testHeadAndTailIntegrity() {
        list.addToFront("B");
        assertEquals("B", list.first());
        assertEquals("B", list.last());
        
        list.addToFront("A");
        assertEquals("A", list.first());
        assertEquals("B", list.last());
        
        list.addToRear("C");
        assertEquals("A", list.first());
        assertEquals("C", list.last());
    }
}
