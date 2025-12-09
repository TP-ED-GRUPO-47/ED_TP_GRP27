package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

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
                "1\nE1\nENTRADA\nEntry\n" +
                        "1\nC1\nTESOURO\nCenter\n" +
                        "2\nE1\nC1\n5,0\n" +
                        "3\n" +
                        "4\ntest_save_map\n" +
                        "5\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(MapEditor::start);

        File f = new File("src/main/resources/test_save_map.json");
        assertTrue(f.exists(), "Map Editor should save the file");
    }
}