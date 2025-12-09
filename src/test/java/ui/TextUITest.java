package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TextUITest {

    private final InputStream systemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
    }

    @Test
    void testRunExitOption() {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TextUI ui = new TextUI();
        assertDoesNotThrow(ui::run);
    }

    @Test
    void testInvalidInputThenExit() {
        String input = "invalid\n99\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TextUI ui = new TextUI();
        assertDoesNotThrow(ui::run);
    }
}