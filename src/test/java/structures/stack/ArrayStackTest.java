package structures.stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.EmptyCollectionException;

/**
 * Unit tests for the {@link ArrayStack} class.
 * <p>
 * This test suite validates the array-based stack implementation,
 * ensuring LIFO (Last In, First Out) behavior and proper capacity
 * management with automatic expansion.
 * </p>
 * <p>
 * Tests cover:
 * <ul>
 *   <li>Push and pop operations following LIFO principle</li>
 *   <li>Peek operation (viewing top without removal)</li>
 *   <li>Stack properties (size, isEmpty)</li>
 *   <li>Automatic capacity expansion</li>
 *   <li>Custom initial capacity</li>
 *   <li>Edge cases (empty stack, single element)</li>
 *   <li>Exception handling for invalid operations</li>
 *   <li>String representation</li>
 * </ul>
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 * @see ArrayStack
 * @see StackADT
 */
class ArrayStackTest {

    private ArrayStack<Integer> stack;

    /**
     * Sets up a fresh empty array-based stack before each test.
     */
    @BeforeEach
    void setUp() {
        stack = new ArrayStack<>();
    }

    /**
     * Tests creating an empty stack with default capacity.
     * <p>
     * Verifies initial state with no elements.
     * </p>
     */
    @Test
    void testEmptyStackCreation() {
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    /**
     * Tests constructor with custom initial capacity.
     */
    @Test
    void testConstructorWithCapacity() {
        ArrayStack<String> customStack = new ArrayStack<>(50);

        assertTrue(customStack.isEmpty());
        assertEquals(0, customStack.size());
    }

    /**
     * Tests pushing a single element onto the stack.
     */
    @Test
    void testPushSingleElement() {
        stack.push(10);

        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());
        assertEquals(10, stack.peek());
    }

    /**
     * Tests pushing multiple elements.
     * <p>
     * Verifies LIFO behavior - most recently pushed element is on top.
     * </p>
     */
    @Test
    void testPushMultipleElements() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(3, stack.size());
        assertEquals(30, stack.peek());
        assertFalse(stack.isEmpty());
    }

    /**
     * Tests popping an element from the stack.
     * <p>
     * Verifies that pop returns and removes the top element.
     * </p>
     */
    @Test
    void testPop() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(30, stack.pop());
        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());

        assertEquals(20, stack.pop());
        assertEquals(1, stack.size());
        assertEquals(10, stack.peek());
    }

    /**
     * Tests LIFO (Last In, First Out) behavior.
     */
    @Test
    void testLIFOBehavior() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        assertEquals(5, stack.pop());
        assertEquals(4, stack.pop());
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    /**
     * Tests peek operation.
     * <p>
     * Verifies that peek returns the top element without removing it.
     * </p>
     */
    @Test
    void testPeek() {
        stack.push(10);
        stack.push(20);

        assertEquals(20, stack.peek());
        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());
    }

    /**
     * Tests popping from an empty stack.
     * <p>
     * Should throw {@link EmptyCollectionException}.
     * </p>
     */
    @Test
    void testPopFromEmptyStack() {
        assertThrows(EmptyCollectionException.class, () -> stack.pop());
    }

    /**
     * Tests peeking at an empty stack.
     * <p>
     * Should throw {@link EmptyCollectionException}.
     * </p>
     */
    @Test
    void testPeekOnEmptyStack() {
        assertThrows(EmptyCollectionException.class, () -> stack.peek());
    }

    /**
     * Tests the {@code isEmpty} method.
     */
    @Test
    void testIsEmpty() {
        assertTrue(stack.isEmpty());

        stack.push(10);
        assertFalse(stack.isEmpty());

        stack.pop();
        assertTrue(stack.isEmpty());
    }

    /**
     * Tests the {@code size} method.
     */
    @Test
    void testSize() {
        assertEquals(0, stack.size());

        stack.push(10);
        assertEquals(1, stack.size());

        stack.push(20);
        stack.push(30);
        assertEquals(3, stack.size());

        stack.pop();
        assertEquals(2, stack.size());

        stack.pop();
        stack.pop();
        assertEquals(0, stack.size());
    }

    /**
     * Tests automatic capacity expansion.
     * <p>
     * Creates a stack with small initial capacity and adds more elements
     * to verify that the stack automatically expands.
     * </p>
     */
    @Test
    void testCapacityExpansion() {
        ArrayStack<Integer> smallStack = new ArrayStack<>(5);

        for (int i = 1; i <= 10; i++) {
            smallStack.push(i);
        }

        assertEquals(10, smallStack.size());

        assertEquals(10, smallStack.pop());
        assertEquals(9, smallStack.pop());
        assertEquals(8, smallStack.size());
    }

    /**
     * Tests pushing many elements to stress test capacity expansion.
     */
    @Test
    void testLargeCapacityExpansion() {
        for (int i = 0; i < 200; i++) {
            stack.push(i);
        }

        assertEquals(200, stack.size());
        assertEquals(199, stack.peek());

        for (int i = 199; i >= 150; i--) {
            assertEquals(i, stack.pop());
        }

        assertEquals(150, stack.size());
    }

    /**
     * Tests pushing and popping a single element repeatedly.
     */
    @Test
    void testRepeatedPushPop() {
        for (int i = 0; i < 5; i++) {
            stack.push(100);
            assertEquals(100, stack.peek());
            assertEquals(100, stack.pop());
            assertTrue(stack.isEmpty());
        }
    }

    /**
     * Tests pushing null elements.
     */
    @Test
    void testPushNull() {
        stack.push(null);

        assertEquals(1, stack.size());
        assertNull(stack.peek());
        assertNull(stack.pop());
        assertTrue(stack.isEmpty());
    }

    /**
     * Tests stack with mixed null and non-null elements.
     */
    @Test
    void testMixedNullElements() {
        stack.push(10);
        stack.push(null);
        stack.push(20);

        assertEquals(20, stack.pop());
        assertNull(stack.pop());
        assertEquals(10, stack.pop());
    }

    /**
     * Tests alternating push and pop operations.
     */
    @Test
    void testAlternatingOperations() {
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pop());

        stack.push(3);
        stack.push(4);
        assertEquals(4, stack.pop());
        assertEquals(3, stack.pop());

        stack.push(5);
        assertEquals(5, stack.peek());
        assertEquals(5, stack.pop());
        assertEquals(1, stack.pop());

        assertTrue(stack.isEmpty());
    }

    /**
     * Tests popping all elements until empty.
     */
    @Test
    void testPopAllElements() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());

        assertThrows(EmptyCollectionException.class, () -> stack.pop());
    }

    /**
     * Tests stack with duplicate elements.
     */
    @Test
    void testDuplicateElements() {
        stack.push(5);
        stack.push(5);
        stack.push(5);

        assertEquals(3, stack.size());
        assertEquals(5, stack.pop());
        assertEquals(5, stack.pop());
        assertEquals(5, stack.pop());
        assertTrue(stack.isEmpty());
    }

    /**
     * Tests stack with negative numbers.
     */
    @Test
    void testNegativeNumbers() {
        stack.push(-10);
        stack.push(-20);
        stack.push(0);
        stack.push(10);

        assertEquals(10, stack.pop());
        assertEquals(0, stack.pop());
        assertEquals(-20, stack.pop());
        assertEquals(-10, stack.pop());
    }

    /**
     * Tests stack with String elements.
     */
    @Test
    void testStringElements() {
        ArrayStack<String> stringStack = new ArrayStack<>();

        stringStack.push("First");
        stringStack.push("Second");
        stringStack.push("Third");

        assertEquals("Third", stringStack.pop());
        assertEquals("Second", stringStack.peek());
        assertEquals(2, stringStack.size());
    }

    /**
     * Tests the {@code toString} method.
     */
    @Test
    void testToString() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        String result = stack.toString();
        assertNotNull(result);
        assertTrue(result.contains("30"));
    }

    /**
     * Tests peek after pop.
     */
    @Test
    void testPeekAfterPop() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        stack.pop();
        assertEquals(20, stack.peek());

        stack.pop();
        assertEquals(10, stack.peek());
    }

    /**
     * Tests single element stack operations.
     */
    @Test
    void testSingleElementStack() {
        stack.push(42);

        assertEquals(42, stack.peek());
        assertEquals(1, stack.size());
        assertFalse(stack.isEmpty());

        assertEquals(42, stack.pop());
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    /**
     * Tests stack behavior with maximum integer values.
     */
    @Test
    void testExtremeValues() {
        stack.push(Integer.MAX_VALUE);
        stack.push(Integer.MIN_VALUE);
        stack.push(0);

        assertEquals(0, stack.pop());
        assertEquals(Integer.MIN_VALUE, stack.pop());
        assertEquals(Integer.MAX_VALUE, stack.pop());
    }

    /**
     * Tests multiple peek operations without modifying stack.
     */
    @Test
    void testMultiplePeeks() {
        stack.push(100);

        for (int i = 0; i < 10; i++) {
            assertEquals(100, stack.peek());
            assertEquals(1, stack.size());
        }

        assertEquals(100, stack.pop());
        assertTrue(stack.isEmpty());
    }

    /**
     * Tests stack after clearing all elements.
     */
    @Test
    void testStackAfterClearing() {
        stack.push(1);
        stack.push(2);
        stack.push(3);

        stack.pop();
        stack.pop();
        stack.pop();

        assertTrue(stack.isEmpty());

        stack.push(10);
        assertEquals(1, stack.size());
        assertEquals(10, stack.peek());
    }

    /**
     * Tests exception message for empty stack operations.
     */
    @Test
    void testExceptionMessages() {
        EmptyCollectionException popException = 
            assertThrows(EmptyCollectionException.class, () -> stack.pop());
        assertTrue(popException.getMessage().contains("Stack"));

        EmptyCollectionException peekException = 
            assertThrows(EmptyCollectionException.class, () -> stack.peek());
        assertTrue(peekException.getMessage().contains("Stack"));
    }

    /**
     * Tests stack performance with sequential operations.
     */
    @Test
    void testSequentialOperations() {
        for (int i = 0; i < 100; i++) {
            stack.push(i);
            assertEquals(i + 1, stack.size());
        }

        for (int i = 99; i >= 0; i--) {
            assertEquals(i, stack.pop());
            assertEquals(i, stack.size());
        }

        assertTrue(stack.isEmpty());
    }

    /**
     * Tests that operations maintain stack integrity.
     */
    @Test
    void testStackIntegrity() {
        stack.push(1);
        stack.push(2);
        int top1 = stack.pop();
        stack.push(3);
        int top2 = stack.peek();
        int top3 = stack.pop();
        int top4 = stack.pop();

        assertEquals(2, top1);
        assertEquals(3, top2);
        assertEquals(3, top3);
        assertEquals(1, top4);
        assertTrue(stack.isEmpty());
    }
}
