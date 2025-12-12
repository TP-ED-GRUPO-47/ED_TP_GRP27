package model;

import java.util.Random;

/**
 * Represents a room containing a lever that requires correct interaction to unlock paths.
 * <p>
 * According to the specification:
 * "Divisões com alavancas: o jogador deve tentar ativá-las para desbloquear passagens.
 * Uma escolha correta abre o caminho; uma escolha errada mantém o bloqueio e o jogador
 * terá de tentar de novo numa jogada futura."
 * </p>
 * <p>
 * The lever has a binary outcome (correct/incorrect attempt), not random probabilities.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class LeverRoom extends Room {

    /**
     * Defines the possible outcomes of interacting with the lever.
     */
    public enum LeverResult {
        /** The correct choice opens a new path or secret passage. */
        CORRECT_CHOICE,
        /** The incorrect choice fails to open the path. The player can try again. */
        INCORRECT_CHOICE,
        /** The lever has already been successfully solved. */
        ALREADY_SOLVED
    }

    private boolean solved = false;
    private int attemptCount = 0;

    /**
     * Constructs a new LeverRoom.
     *
     * @param id          The unique identifier of the room.
     * @param description The text description of the room.
     */
    public LeverRoom(String id, String description) {
        super(id, description);
    }

    /**
     * Executed when a player enters the room.
     */
    @Override
    public void onEnter() {
        System.out.println("\n" + getDescription());
        if (solved) {
            System.out.println("A alavanca foi ativada com sucesso. O caminho está aberto.");
        } else {
            System.out.println("Uma alavanca misteriosa está na parede. Consegues ativá-la?");
        }
    }

    /**
     * Checks if the lever has been successfully activated (correct choice made).
     *
     * @return true if the lever was solved, false otherwise.
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Attempts to activate the lever with a given choice.
     * <p>
     * The player has a 50% chance to guess correctly on each attempt.
     * A correct guess unlocks the path.
     * An incorrect guess requires trying again in a future turn.
     * </p>
     *
     * @return The result of the attempt (CORRECT_CHOICE, INCORRECT_CHOICE, or ALREADY_SOLVED).
     */
    public LeverResult attemptLever() {
        if (solved) {
            return LeverResult.ALREADY_SOLVED;
        }

        attemptCount++;
        Random random = new Random();
        boolean isCorrect = random.nextBoolean();

        if (isCorrect) {
            solved = true;
            return LeverResult.CORRECT_CHOICE;
        } else {
            return LeverResult.INCORRECT_CHOICE;
        }
    }

}