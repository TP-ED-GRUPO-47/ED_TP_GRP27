package structures.linear;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.NoSuchElementException;

/**
 * Unit tests for the {@link DoubleLinkedUnorderedList} class.
 * <p>
 * Tests doubly-linked list operations with bidirectional navigation.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class DoubleLinkedUnorderedListTest {

    private DoubleLinkedUnorderedList<String> list;

    @BeforeEach
    void setUp() {
        list = new DoubleLinkedUnorderedList<>();
    }

    /**
     * Tests adding elements to the front of the list.
     */
    @Test
    void testAddToFront() {
        assertTrue(list.isEmpty());
        list.addToFront("A");
        assertEquals(1, list.size());
        assertEquals("A", list.first());
        assertEquals("A", list.last());

        list.addToFront("B");
        assertEquals(2, list.size());
        assertEquals("B", list.first());
        assertEquals("A", list.last());
    }

    /**
     * Tests adding elements to the rear of the list.
     */
    @Test
    void testAddToRear() {
        list.addToRear("A");
        assertEquals("A", list.first());

        list.addToRear("B");
        assertEquals("A", list.first());
        assertEquals("B", list.last());
        assertEquals(2, list.size());
    }

    /**
     * Tests inserting elements after a specific target element.
     */
    @Test
    void testAddAfter() {
        list.addToRear("A");
        list.addToRear("C");

        list.addAfter("B", "A");
        assertEquals(3, list.size());

        Iterator<String> it = list.iterator();
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());

        list.addAfter("D", "C");
        assertEquals("D", list.last());
        assertEquals(4, list.size());
    }

    /**
     * Tests that addAfter throws exceptions for invalid operations.
     */
    @Test
    void testAddAfterExceptions() {
        assertThrows(NoSuchElementException.class, () -> list.addAfter("A", "B"), "Lista vazia deve lançar erro");

        list.addToFront("A");
        assertThrows(NoSuchElementException.class, () -> list.addAfter("C", "B"), "Target inexistente deve lançar erro");
    }

    /**
     * Tests removing the first element from the list.
     */
    @Test
    void testRemoveFirst() {
        assertThrows(NoSuchElementException.class, () -> list.removeFirst());

        list.addToRear("A");
        list.addToRear("B");

        assertEquals("A", list.removeFirst());
        assertEquals(1, list.size());
        assertEquals("B", list.first());

        assertEquals("B", list.removeFirst());
        assertTrue(list.isEmpty());
    }

    /**
     * Tests removing the last element from the list.
     */
    @Test
    void testRemoveLast() {
        assertThrows(NoSuchElementException.class, () -> list.removeLast());

        list.addToRear("A");
        list.addToRear("B");

        assertEquals("B", list.removeLast());
        assertEquals(1, list.size());
        assertEquals("A", list.last());

        assertEquals("A", list.removeLast());
        assertTrue(list.isEmpty());
    }

    /**
     * Tests removing a specific element from the list.
     */
    @Test
    void testRemoveElement() {
        assertThrows(NoSuchElementException.class, () -> list.remove("A"));

        list.addToRear("A");
        list.addToRear("B");
        list.addToRear("C");

        assertEquals("B", list.remove("B"));
        assertEquals(2, list.size());
        assertFalse(list.contains("B"));

        assertEquals("A", list.remove("A"));
        assertEquals("C", list.first());

        assertEquals("C", list.remove("C"));
        assertTrue(list.isEmpty());
    }

    /**
     * Tests that removing a non-existent element throws an exception.
     */
    @Test
    void testRemoveElementNotFound() {
        list.addToFront("A");
        assertThrows(NoSuchElementException.class, () -> list.remove("Z"));
    }

    /**
     * Tests the contains method for checking element existence.
     */
    @Test
    void testContains() {
        list.addToRear("A");
        assertTrue(list.contains("A"));
        assertFalse(list.contains("B"));
    }

    /**
     * Tests iterator functionality for list traversal.
     */
    @Test
    void testIterator() {
        list.addToRear("1");
        list.addToRear("2");
        Iterator<String> it = list.iterator();

        assertTrue(it.hasNext());
        assertEquals("1", it.next());
        assertTrue(it.hasNext());
        assertEquals("2", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, () -> it.next());
    }

    /**
     * Tests string representation of the list.
     */
    @Test
    void testToString() {
        list.addToRear("A");
        list.addToRear("B");
        assertEquals("[A, B]", list.toString());
    }

    /**
     * Tests that accessing first/last on empty list throws exceptions.
     */
    @Test
    void testFirstLastExceptions() {
        assertThrows(NoSuchElementException.class, () -> list.first());
        assertThrows(NoSuchElementException.class, () -> list.last());
    }
}