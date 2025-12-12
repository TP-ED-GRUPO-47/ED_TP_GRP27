package structures.linear;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.NoSuchElementException;

/**
 * Unit tests for the {@link ArrayUnorderedList} class.
 * <p>
 * Tests array-based list operations including add, remove, and iteration.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class ArrayUnorderedListTest {

    private ArrayUnorderedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new ArrayUnorderedList<>();
    }

    /**
     * Tests adding elements and automatic capacity expansion.
     */
    @Test
    void testAddAndExpandCapacity() {
        for (int i = 0; i < 15; i++) {
            list.addToRear(i);
        }
        assertEquals(15, list.size());
        assertEquals(0, list.first());
        assertEquals(14, list.last());
    }

    /**
     * Tests adding elements to the front of the list.
     */
    @Test
    void testAddToFront() {
        list.addToFront(1);
        list.addToFront(2);

        assertEquals(2, list.first());
        assertEquals(1, list.last());
        assertEquals(2, list.size());

        for(int i = 0; i < 15; i++) {
            list.addToFront(i);
        }
        assertEquals(17, list.size());
    }

    /**
     * Tests adding an element after a specific target element.
     */
    @Test
    void testAddAfter() {
        list.addToRear(1);
        list.addToRear(3);

        list.addAfter(2, 1);

        assertEquals(3, list.size());
        assertTrue(list.contains(2));

        Iterator<Integer> it = list.iterator();
        assertEquals(1, it.next());
        assertEquals(2, it.next());
        assertEquals(3, it.next());

        for (int i=10; i<20; i++) list.addToRear(i);
        assertDoesNotThrow(() -> list.addAfter(99, 1));
    }

    /**
     * Tests removing the first element from the list.
     */
    @Test
    void testRemoveFirst() {
        list.addToRear(10);
        list.addToRear(20);

        assertEquals(10, list.removeFirst());
        assertEquals(1, list.size());
        assertEquals(20, list.first());

        assertThrows(NoSuchElementException.class, () -> new ArrayUnorderedList<>().removeFirst());
    }

    /**
     * Tests removing the last element from the list.
     */
    @Test
    void testRemoveLast() {
        list.addToRear(10);
        list.addToRear(20);

        assertEquals(20, list.removeLast());
        assertEquals(1, list.size());
        assertEquals(10, list.last());

        assertThrows(NoSuchElementException.class, () -> new ArrayUnorderedList<>().removeLast());
    }

    /**
     * Tests removing a specific element from the list.
     */
    @Test
    void testRemoveElement() {
        list.addToRear(1);
        list.addToRear(2);
        list.addToRear(3);

        assertEquals(2, list.remove(2));
        assertEquals(2, list.size());
        assertFalse(list.contains(2));

        assertEquals(1, list.remove(1));
        assertEquals(3, list.remove(3));

        assertTrue(list.isEmpty());
    }

    /**
     * Tests that appropriate exceptions are thrown for invalid remove operations.
     */
    @Test
    void testRemoveExceptions() {
        list.addToRear(1);
        assertThrows(NoSuchElementException.class, () -> list.remove(99));
        assertThrows(NoSuchElementException.class, () -> new ArrayUnorderedList<>().remove(1));
    }

    @Test
    void testToString() {
        list.addToRear(1);
        list.addToRear(2);
        assertEquals("[1, 2]", list.toString());
    }
}