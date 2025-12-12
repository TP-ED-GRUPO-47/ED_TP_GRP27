package ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link MapEditor} class.
 * <p>
 * Tests map creation and saving functionality.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class MapEditorTest {

    private final InputStream systemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        File f = new File("src/main/resources/test_save_map.json");
        if(f.exists()) f.delete();
    }

    @Test
    void testMapEditorFlow() {
        String input =
                "1\n" +
                "E1\n" +
                "ENTRADA\n" +
                "Entry\n" +
                "1\n" +
                "C1\n" +
                "TESOURO\n" +
                "Center\n" +
                "2\n" +
                "E1\n" +
                "C1\n" +
                "5.0\n" +
                "3\n" +
                "4\n" +
                "test_save_map\n" +
                "0\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(MapEditor::start);

        File f = new File("src/main/resources/test_save_map.json");
        assertTrue(f.exists(), "Map Editor should save the file");
    }
}