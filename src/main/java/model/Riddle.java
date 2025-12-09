package model;

import structures.linear.ArrayUnorderedList;
import java.util.Iterator;

/**
 * Represents a riddle or logic puzzle in the game.
 * <p>
 * Each riddle consists of a question, a list of possible options, and the index
 * of the correct answer. These are typically loaded from an external JSON file
 * and assigned to {@link RiddleRoom}s.
 * </p>
 * <p>
 * It fulfills the requirement regarding "reasoning challenges loaded from an external file."
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Riddle {
    private String question;
    private ArrayUnorderedList<String> options;
    private int correctAnswerIndex; // 0 to N

    /**
     * Constructs a new Riddle.
     *
     * @param question           The text of the riddle or question.
     * @param options            A list of possible answers (options).
     * @param correctAnswerIndex The index (0-based) of the correct answer within the options list.
     */
    public Riddle(String question, ArrayUnorderedList<String> options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    /**
     * Retrieves the question text.
     *
     * @return The question string.
     */
    public String getQuestion() { return question; }

    /**
     * Retrieves the list of possible answer options.
     *
     * @return An {@link ArrayUnorderedList} of option strings.
     */
    public ArrayUnorderedList<String> getOptions() { return options; }

    /**
     * Verifies if the provided answer index is correct.
     *
     * @param index The index selected by the player.
     * @return true if the index matches the correct answer, false otherwise.
     */
    public boolean checkAnswer(int index) {
        return index == correctAnswerIndex;
    }
}