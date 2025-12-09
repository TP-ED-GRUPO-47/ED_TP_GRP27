package io;

import model.Player;
import model.Center;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class responsible for exporting the final game report.
 * <p>
 * This class generates a JSON file containing the player's performance details,
 * including the path taken, the final result, and the timestamp.
 * </p>
 * <p>
 * It fulfills the requirement: "At the end of the match, a report in JSON format must be generated."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class ReportExporter {

    /**
     * Generates and saves a mission report for a specific player.
     * <p>
     * The report includes:
     * <ul>
     * <li>Player Name</li>
     * <li>Date and Time of the match</li>
     * <li>Final Result (VICTORY or DEFEAT)</li>
     * <li>Full path (movement history)</li>
     * </ul>
     * The file is saved as "report_PlayerName.json".
     * </p>
     *
     * @param player The player who finished the game.
     */
    public static void exportMissionReport(Player player) {
        JSONObject report = new JSONObject();

        report.put("player", player.getName());
        report.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        boolean victory = player.getCurrentRoom() instanceof Center;
        report.put("result", victory ? "VICTORY" : "DEFEAT");

        report.put("path_taken", player.getHistoryString());

        String safeName = player.getName().replaceAll("\\s+", "_");
        String filename = "report_" + safeName + ".json";

        try (FileWriter file = new FileWriter(filename)) {
            file.write(report.toJSONString());
            file.flush();
            System.out.println("📄 Report successfully saved: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving report: " + e.getMessage());
        }
    }
}