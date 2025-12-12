package ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link TextUI} class.
 * <p>
 * Tests user interface navigation and menu options.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
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