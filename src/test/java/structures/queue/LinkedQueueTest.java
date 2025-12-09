package structures.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkedQueueTest {

    private LinkedQueue<String> queue;

    @BeforeEach
    void setUp() {
        queue = new LinkedQueue<>();
    }

    @Test
    void testEnqueueAndSize() {
        assertTrue(queue.isEmpty(), "Fila deve começar vazia");
        assertEquals(0, queue.size());

        queue.enqueue("A");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals("A", queue.first());

        queue.enqueue("B");
        assertEquals(2, queue.size());
        assertEquals("A", queue.first(), "O primeiro elemento deve continuar a ser A (FIFO)");
    }

    @Test
    void testDequeue() {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        assertEquals("A", queue.dequeue());
        assertEquals(2, queue.size());
        assertEquals("B", queue.first());

        assertEquals("B", queue.dequeue());
        assertEquals(1, queue.size());
        assertEquals("C", queue.first());

        assertEquals("C", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    void testFirst() {
        queue.enqueue("X");
        queue.enqueue("Y");

        assertEquals("X", queue.first());
        assertEquals(2, queue.size(), "First não deve remover o elemento");

        queue.dequeue();
        assertEquals("Y", queue.first());
    }

    @Test
    void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Data");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    void testToString() {
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");

        assertEquals("[1, 2, 3]", queue.toString());
    }

    @Test
    void testExceptions() {
        Exception exceptionDequeue = assertThrows(IllegalStateException.class, () -> {
            queue.dequeue();
        });
        assertEquals("Queue is empty", exceptionDequeue.getMessage());

        Exception exceptionFirst = assertThrows(IllegalStateException.class, () -> {
            queue.first();
        });
        assertEquals("Queue is empty", exceptionFirst.getMessage());
    }

    @Test
    void testClearLogicViaDequeue() {
        queue.enqueue("A");
        queue.dequeue();
        assertTrue(queue.isEmpty());

        queue.enqueue("B");
        assertEquals("B", queue.first());
        assertEquals(1, queue.size());
    }
}