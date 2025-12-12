package structures.stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.EmptyCollectionException;

/**
 * Unit tests for the {@link LinkedStack} class.
 * <p>
 * Tests stack operations following LIFO (Last In, First Out) principle.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class LinkedStackTest {

    private LinkedStack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new LinkedStack<>();
    }

    /**
     * Tests pushing elements and size tracking.
     */
    @Test
    void testPushAndSize() {
        assertTrue(stack.isEmpty(), "Stack deve iniciar vazia");
        assertEquals(0, stack.size());

        stack.push("A");
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
        assertEquals("A", stack.peek());

        stack.push("B");
        assertEquals(2, stack.size());
        assertEquals("B", stack.peek());
    }

    /**
     * Tests popping elements from the stack in LIFO order.
     */
    @Test
    void testPop() {
        stack.push("A");
        stack.push("B");
        stack.push("C");

        assertEquals("C", stack.pop());
        assertEquals(2, stack.size());
        assertEquals("B", stack.peek());

        assertEquals("B", stack.pop());
        assertEquals(1, stack.size());
        assertEquals("A", stack.peek());

        assertEquals("A", stack.pop());
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    /**
     * Tests peeking at the top element without removing it.
     */
    @Test
    void testPeek() {
        stack.push("X");
        assertEquals("X", stack.peek());
        assertEquals(1, stack.size(), "Peek não deve remover o elemento");

        stack.push("Y");
        assertEquals("Y", stack.peek());
        assertEquals(2, stack.size());
    }

    /**
     * Tests the isEmpty functionality of the stack.
     */
    @Test
    void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push("Test");
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    /**
     * Tests string representation of the stack.
     */
    @Test
    void testToString() {
        stack.push("Base");
        stack.push("Meio");
        stack.push("Topo");

        String str = stack.toString();
        assertEquals("Stack (top → bottom): [Topo, Meio, Base]", str);
    }

    /**
     * Tests that exceptions are thrown for invalid operations on empty stack.
     */
    @Test
    void testExceptions() {
        assertThrows(EmptyCollectionException.class, () -> {
            stack.pop();
        }, "Pop em stack vazia deve lançar EmptyCollectionException");

        assertThrows(EmptyCollectionException.class, () -> {
            stack.peek();
        }, "Peek em stack vazia deve lançar EmptyCollectionException");
    }

    @Test
    void testPushNull() {
        stack.push(null);
        assertEquals(1, stack.size());
        assertNull(stack.peek());
        assertNull(stack.pop());
    }
}