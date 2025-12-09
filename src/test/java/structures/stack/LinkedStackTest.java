package structures.stack;

import exceptions.EmptyCollectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedStackTest {

    private LinkedStack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new LinkedStack<>();
    }

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

    @Test
    void testPeek() {
        stack.push("X");
        assertEquals("X", stack.peek());
        assertEquals(1, stack.size(), "Peek não deve remover o elemento");

        stack.push("Y");
        assertEquals("Y", stack.peek());
        assertEquals(2, stack.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push("Test");
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testToString() {
        stack.push("Base");
        stack.push("Meio");
        stack.push("Topo");

        String str = stack.toString();
        assertEquals("Stack (top → bottom): [Topo, Meio, Base]", str);
    }

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