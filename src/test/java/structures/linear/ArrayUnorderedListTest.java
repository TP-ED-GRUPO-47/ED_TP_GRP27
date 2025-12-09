package structures.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUnorderedListTest {

    private ArrayUnorderedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new ArrayUnorderedList<>();
    }

    @Test
    void testAddAndExpandCapacity() {
        for (int i = 0; i < 15; i++) {
            list.addToRear(i);
        }
        assertEquals(15, list.size());
        assertEquals(0, list.first());
        assertEquals(14, list.last());
    }

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

    @Test
    void testRemoveFirst() {
        list.addToRear(10);
        list.addToRear(20);

        assertEquals(10, list.removeFirst());
        assertEquals(1, list.size());
        assertEquals(20, list.first());

        assertThrows(NoSuchElementException.class, () -> new ArrayUnorderedList<>().removeFirst());
    }

    @Test
    void testRemoveLast() {
        list.addToRear(10);
        list.addToRear(20);

        assertEquals(20, list.removeLast());
        assertEquals(1, list.size());
        assertEquals(10, list.last());

        assertThrows(NoSuchElementException.class, () -> new ArrayUnorderedList<>().removeLast());
    }

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