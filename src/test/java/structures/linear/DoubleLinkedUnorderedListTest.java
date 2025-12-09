package structures.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DoubleLinkedUnorderedListTest {

    private DoubleLinkedUnorderedList<String> list;

    @BeforeEach
    void setUp() {
        list = new DoubleLinkedUnorderedList<>();
    }

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

    @Test
    void testAddToRear() {
        list.addToRear("A");
        assertEquals("A", list.first());

        list.addToRear("B");
        assertEquals("A", list.first());
        assertEquals("B", list.last());
        assertEquals(2, list.size());
    }

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

    @Test
    void testAddAfterExceptions() {
        assertThrows(NoSuchElementException.class, () -> list.addAfter("A", "B"), "Lista vazia deve lançar erro");

        list.addToFront("A");
        assertThrows(NoSuchElementException.class, () -> list.addAfter("C", "B"), "Target inexistente deve lançar erro");
    }

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

    @Test
    void testRemoveElementNotFound() {
        list.addToFront("A");
        assertThrows(NoSuchElementException.class, () -> list.remove("Z"));
    }

    @Test
    void testContains() {
        list.addToRear("A");
        assertTrue(list.contains("A"));
        assertFalse(list.contains("B"));
    }

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

    @Test
    void testToString() {
        list.addToRear("A");
        list.addToRear("B");
        assertEquals("[A, B]", list.toString());
    }

    @Test
    void testFirstLastExceptions() {
        assertThrows(NoSuchElementException.class, () -> list.first());
        assertThrows(NoSuchElementException.class, () -> list.last());
    }
}