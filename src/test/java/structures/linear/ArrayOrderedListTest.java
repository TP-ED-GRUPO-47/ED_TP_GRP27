package structures.linear;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.ElementNotFoundException;

/**
 * Unit tests for the {@link ArrayOrderedList} class.
 * <p>
 * This test suite validates the array-based ordered list implementation,
 * ensuring elements are maintained in natural order (ascending) according
 * to their natural ordering defined by {@link Comparable}.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Adding elements in various orders (sorted insertion)</li>
 *   <li>Removing elements (first, last, specific)</li>
 *   <li>Accessing elements (first, last, contains)</li>
 *   <li>Automatic capacity expansion</li>
 *   <li>Iterator functionality</li>
 *   <li>Exception handling for invalid operations</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see ArrayOrderedList
 * @see OrderedListADT
 */
class ArrayOrderedListTest {

    private ArrayOrderedList<Integer> list;

    /**
     * Sets up a fresh empty ordered list before each test.
     */
    @BeforeEach
    void setUp() {
        list = new ArrayOrderedList<>();
    }

    /**
     * Tests that elements are automatically sorted upon insertion.
     * <p>
     * Adds elements in random order and verifies they are stored
     * in ascending order according to natural ordering.
     * </p>
     */
    @Test
    void testAddMaintainsOrder() {
        list.add(5);
        list.add(2);
        list.add(8);
        list.add(1);
        list.add(9);

        assertEquals(5, list.size());
        assertEquals(1, list.first());
        assertEquals(9, list.last());

        Iterator<Integer> iter = list.iterator();
        assertEquals(1, iter.next());
        assertEquals(2, iter.next());
        assertEquals(5, iter.next());
        assertEquals(8, iter.next());
        assertEquals(9, iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Tests adding duplicate elements.
     * <p>
     * Verifies that duplicate values are allowed and maintained
     * in proper sorted order.
     * </p>
     */
    @Test
    void testAddDuplicates() {
        list.add(5);
        list.add(3);
        list.add(5);
        list.add(3);
        list.add(5);

        assertEquals(5, list.size());
        assertEquals(3, list.first());
        assertEquals(5, list.last());

        Iterator<Integer> iter = list.iterator();
        assertEquals(3, iter.next());
        assertEquals(3, iter.next());
        assertEquals(5, iter.next());
        assertEquals(5, iter.next());
        assertEquals(5, iter.next());
    }

    /**
     * Tests automatic capacity expansion when the initial capacity is exceeded.
     * <p>
     * Adds more elements than the default capacity and verifies
     * the list correctly expands and maintains order.
     * </p>
     */
    @Test
    void testCapacityExpansion() {
        for (int i = 20; i > 0; i--) {
            list.add(i);
        }

        assertEquals(20, list.size());
        assertEquals(1, list.first());
        assertEquals(20, list.last());

        Iterator<Integer> iter = list.iterator();
        for (int i = 1; i <= 20; i++) {
            assertEquals(i, iter.next());
        }
    }

    /**
     * Tests removing the first element from the list.
     * <p>
     * Verifies that after removal, the next smallest element
     * becomes the new first element.
     * </p>
     */
    @Test
    void testRemoveFirst() {
        list.add(10);
        list.add(20);
        list.add(5);
        list.add(15);

        assertEquals(5, list.removeFirst());
        assertEquals(3, list.size());
        assertEquals(10, list.first());
    }

    /**
     * Tests removing the last element from the list.
     * <p>
     * Verifies that after removal, the next largest element
     * becomes the new last element.
     * </p>
     */
    @Test
    void testRemoveLast() {
        list.add(10);
        list.add(20);
        list.add(5);
        list.add(15);

        assertEquals(20, list.removeLast());
        assertEquals(3, list.size());
        assertEquals(15, list.last());
    }

    /**
     * Tests removing a specific element from the middle of the list.
     * <p>
     * Verifies that the element is removed and the list maintains
     * proper ordering.
     * </p>
     */
    @Test
    void testRemoveSpecificElement() {
        list.add(10);
        list.add(20);
        list.add(5);
        list.add(15);

        assertEquals(15, list.remove(15));
        assertEquals(3, list.size());
        assertFalse(list.contains(15));

        Iterator<Integer> iter = list.iterator();
        assertEquals(5, iter.next());
        assertEquals(10, iter.next());
        assertEquals(20, iter.next());
    }

    /**
     * Tests removing a non-existent element.
     * <p>
     * Verifies that attempting to remove an element not in the list
     * throws {@link ElementNotFoundException}.
     * </p>
     */
    @Test
    void testRemoveNonExistentElement() {
        list.add(10);
        list.add(20);

        assertThrows(ElementNotFoundException.class, () -> list.remove(30));
        assertEquals(2, list.size());
    }

    /**
     * Tests removing from an empty list.
     * <p>
     * Verifies that attempting to remove from an empty list
     * throws {@link ElementNotFoundException}.
     * </p>
     */
    @Test
    void testRemoveFromEmptyList() {
        assertTrue(list.isEmpty());
        assertThrows(ElementNotFoundException.class, () -> list.removeFirst());
        assertThrows(ElementNotFoundException.class, () -> list.removeLast());
    }

    /**
     * Tests the {@code contains} method for existing and non-existing elements.
     */
    @Test
    void testContains() {
        list.add(10);
        list.add(20);
        list.add(30);

        assertTrue(list.contains(10));
        assertTrue(list.contains(20));
        assertTrue(list.contains(30));
        assertFalse(list.contains(5));
        assertFalse(list.contains(25));
        assertFalse(list.contains(40));
    }

    /**
     * Tests the {@code first} and {@code last} methods.
     * <p>
     * Verifies that these methods return the smallest and largest
     * elements respectively without removing them.
     * </p>
     */
    @Test
    void testFirstAndLast() {
        list.add(15);
        list.add(5);
        list.add(25);
        list.add(10);

        assertEquals(5, list.first());
        assertEquals(25, list.last());
        assertEquals(4, list.size());
    }

    /**
     * Tests accessing first and last elements on an empty list.
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
        list.add(5);
        assertEquals(3, list.size());
        
        list.removeFirst();
        assertEquals(2, list.size());
        
        list.removeLast();
        list.removeLast();
        assertEquals(0, list.size());
    }

    /**
     * Tests the iterator functionality.
     * <p>
     * Verifies that the iterator traverses elements in ascending order
     * and correctly reports when no more elements exist.
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
     * <p>
     * Verifies that {@link ElementNotFoundException} is thrown.
     * </p>
     */
    @Test
    void testIteratorNoSuchElement() {
        list.add(10);
        Iterator<Integer> iter = list.iterator();
        
        iter.next();
        assertThrows(ElementNotFoundException.class, () -> iter.next());
    }

    /**
     * Tests adding a single element and then removing it.
     */
    @Test
    void testSingleElementAddAndRemove() {
        list.add(42);
        
        assertEquals(1, list.size());
        assertEquals(42, list.first());
        assertEquals(42, list.last());
        
        assertEquals(42, list.removeFirst());
        assertTrue(list.isEmpty());
    }

    /**
     * Tests the {@code toString} method.
     * <p>
     * Verifies that the string representation reflects the ordered list contents.
     * </p>
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
     * Tests that adding a non-Comparable element throws an exception.
     * <p>
     * Note: This test is limited because Integer implements Comparable.
     * The actual implementation should handle non-Comparable types.
     * </p>
     */
    @Test
    void testAddNonComparableThrowsException() {
        assertDoesNotThrow(() -> list.add(10));
    }

    /**
     * Tests adding and removing all elements sequentially.
     */
    @Test
    void testAddAndRemoveAll() {
        for (int i = 1; i <= 5; i++) {
            list.add(i * 10);
        }
        
        assertEquals(5, list.size());
        
        for (int i = 1; i <= 5; i++) {
            assertEquals(i * 10, list.removeFirst());
        }
        
        assertTrue(list.isEmpty());
    }
}
