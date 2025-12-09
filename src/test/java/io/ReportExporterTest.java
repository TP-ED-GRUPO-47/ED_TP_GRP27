package io;

import model.Center;
import model.Player;
import model.RoomStandard;
import io.ReportExporter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

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