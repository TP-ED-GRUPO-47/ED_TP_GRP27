package io;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.Center;
import model.Player;
import model.RoomStandard;

/**
 * Unit tests for the {@link ReportExporter} class.
 * <p>
 * Tests the generation of game reports in JSON format.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class ReportExporterTest {

    private String generatedFileName;

    @AfterEach
    void tearDown() {
        if (generatedFileName != null) {
            File file = new File(generatedFileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Tests exporting a victory report when player reaches the treasure.
     */
    @Test
    void testExportVictoryReport() {
        Player p = new Player("WinnerBot");
        Center treasureRoom = new Center("C1", "Treasure");
        p.setCurrentRoom(treasureRoom);

        generatedFileName = "report_WinnerBot.json";

        assertDoesNotThrow(() -> ReportExporter.exportMissionReport(p));

        File file = new File(generatedFileName);
        assertTrue(file.exists(), "Victory report file should be created");
        assertTrue(file.length() > 0, "Report file should not be empty");
    }

    /**
     * Tests exporting a defeat report when player doesn't reach the treasure.
     */
    @Test
    void testExportDefeatReport() {
        Player p = new Player("LoserBot");
        RoomStandard normalRoom = new RoomStandard("S1", "Normal");
        p.setCurrentRoom(normalRoom);

        generatedFileName = "report_LoserBot.json";

        assertDoesNotThrow(() -> ReportExporter.exportMissionReport(p));

        File file = new File(generatedFileName);
        assertTrue(file.exists(), "Defeat report file should be created");
    }

    /**
     * Tests that player names with spaces are handled correctly in filenames.
     */
    @Test
    void testExportWithSpacesInName() {
        Player p = new Player("Bot With Spaces");
        p.setCurrentRoom(new RoomStandard("S1", "A"));

        generatedFileName = "report_Bot_With_Spaces.json";

        ReportExporter.exportMissionReport(p);

        File file = new File(generatedFileName);
        assertTrue(file.exists(), "Filename should handle spaces by replacing with underscores");
    }
}