package io;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import model.Center;
import model.Player;

/**
 * Utility class responsible for exporting the final game report.
 * <p>
 * This class generates a JSON file containing the player's performance details,
 * including the path taken, the final result, obstacles faced, and effects applied.
 * </p>
 * <p>
 * It fulfills the requirement: "At the end of the match, a report in JSON format must be generated."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class ReportExporter {

    /** Utility class; no instances allowed. */
    private ReportExporter() { }

    /**
     * Generates and saves a mission report for a specific player.
     * <p>
     * The report includes:
     * <ul>
     * <li>Player Name</li>
     * <li>Date and Time of the match</li>
     * <li>Final Result (VICTORY or DEFEAT)</li>
     * <li>Full path (movement history)</li>
     * <li>Power remaining</li>
     * <li>Riddles solved</li>
     * <li>Effects applied</li>
     * <li>Events encountered</li>
     * </ul>
     * The file is saved as "report_PlayerName.json".
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

        report.put("final_power", player.getPower());

        JSONArray riddlesArray = new JSONArray();
        Iterator<String> riddlesIt = player.getSolvedRiddles();
        int riddleCount = 0;
        while (riddlesIt.hasNext()) {
            riddlesArray.add(riddlesIt.next());
            riddleCount++;
        }
        report.put("riddles_solved", riddlesArray);

        JSONArray effectsArray = new JSONArray();
        Iterator<String> effectsIt = player.getAppliedEffects();
        int effectCount = 0;
        while (effectsIt.hasNext()) {
            effectsArray.add(effectsIt.next());
            effectCount++;
        }
        report.put("effects_applied", effectsArray);

        JSONArray eventsArray = new JSONArray();
        Iterator<String> eventsIt = player.getEncounteredEvents();
        int eventCount = 0;
        while (eventsIt.hasNext()) {
            eventsArray.add(eventsIt.next());
            eventCount++;
        }
        report.put("events_encountered", eventsArray);

        JSONObject stats = new JSONObject();
        stats.put("total_riddles_encountered", riddleCount);
        stats.put("total_effects_applied", effectCount);
        stats.put("total_events_encountered", eventCount);
        stats.put("final_power_percentage", (player.getPower() * 100) / 100);
        stats.put("game_status", victory ? "COMPLETED_SUCCESSFULLY" : "ABANDONED_OR_DEFEATED");
        
        report.put("statistics", stats);

        String safeName = player.getName().replaceAll("\\s+", "_");
        String filename = "report_" + safeName + ".json";

        try (FileWriter file = new FileWriter(filename)) {
            file.write(report.toJSONString());
            file.flush();
            System.out.println("Relatório guardado com sucesso: " + filename + "!");
        } catch (IOException e) {
            System.err.println("Erro ao guardar relatório: " + e.getMessage());
        }
    }

    /**
     * Generates a match-wide report for all players.
     *
     * @param players list of all players that participated
     * @param winner  player who reached the treasure (may be null if aborted)
     */
    public static void exportMatchSummary(structures.linear.ArrayUnorderedList<Player> players, Player winner) {
        JSONObject report = new JSONObject();

        report.put("date", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        report.put("winner", winner != null ? winner.getName() : "NONE");

        JSONArray playersArray = new JSONArray();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            JSONObject pj = new JSONObject();
            pj.put("name", p.getName());
            pj.put("power", p.getPower());
            pj.put("current_room", p.getCurrentRoom() != null ? p.getCurrentRoom().getId() : "UNKNOWN");
            pj.put("path_taken", p.getHistoryString());

            JSONArray riddles = new JSONArray();
            Iterator<String> rIt = p.getSolvedRiddles();
            while (rIt.hasNext()) riddles.add(rIt.next());
            pj.put("riddles_solved", riddles);

            JSONArray effects = new JSONArray();
            Iterator<String> eIt = p.getAppliedEffects();
            while (eIt.hasNext()) effects.add(eIt.next());
            pj.put("effects_applied", effects);

            JSONArray events = new JSONArray();
            Iterator<String> evIt = p.getEncounteredEvents();
            while (evIt.hasNext()) events.add(evIt.next());
            pj.put("events_encountered", events);

            playersArray.add(pj);
        }

        report.put("players", playersArray);

        try (FileWriter file = new FileWriter("report_match.json")) {
            file.write(report.toJSONString());
            file.flush();
            System.out.println("Relatório global guardado: report_match.json");
        } catch (IOException e) {
            System.err.println("Erro ao guardar sumário da partida: " + e.getMessage());
        }
    }
}
