package model;

import java.util.Iterator;
import java.util.Scanner;

/**
 * Handles all room-specific events: Riddles, Levers, and room interactions.
 * <p>
 * Separates event logic from the main game engine to improve maintainability.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class RoomEventHandler {
    private final Scanner scanner;
    private final Maze maze;

    /**
     * Creates a new RoomEventHandler.
     *
     * @param scanner Scanner for user input.
     * @param maze    The game maze.
     */
    public RoomEventHandler(Scanner scanner, Maze maze) {
        this.scanner = scanner;
        this.maze = maze;
    }

    /**
     * Handles riddle solving for human players.
     *
     * @param room   The riddle room.
     * @param player The player solving the riddle.
     */
    public void handleRiddle(RiddleRoom room, Player player) {
        if (room.isSolved()) {
            System.out.println("Este enigma já foi resolvido.");
            return;
        }

        Riddle r = room.getRiddle();
        if (r == null){
            return;
        } 

        System.out.println(r.getQuestion());
        Iterator<String> it = r.getOptions().iterator();
        int i = 1;
        while (it.hasNext()) {
            System.out.println(i + ". " + it.next());
            i++;
        }

        System.out.print("\nResposta (número): ");
        String raw = scanner.nextLine();
        try {
            int choice = Integer.parseInt(raw.trim());

            if (r.checkAnswer(choice - 1)) {
                System.out.println("Correto! Podes continuar.");
                room.setSolved(true);
                player.recordSolvedRiddle(room.getId());
            } else {
                System.out.println("Errado! Estás bloqueado nesta sala até responder corretamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Usa um número para responder ao enigma.");
        }
    }

    /**
     * Handles riddle solving for bots (random guess).
     *
     * @param room The riddle room.
     * @param bot  The bot solving the riddle.
     */
    public void handleBotRiddle(RiddleRoom room, Bot bot) {
        if (room.isSolved()) {
            return;
        }

        Riddle r = room.getRiddle();
        if (r == null){
            return;
        }

        System.out.println(r.getQuestion());
        java.util.Random rnd = new java.util.Random();
        int totalOptions = 0;
        Iterator<String> countIt = r.getOptions().iterator();
        while (countIt.hasNext()) {
            countIt.next();
            totalOptions++;
        }

        int botGuess = rnd.nextInt(totalOptions);

        System.out.println(bot.getName() + " responde: " + (botGuess + 1));

        if (r.checkAnswer(botGuess)) {
            System.out.println("Correto! " + bot.getName() + " continuou.");
            room.setSolved(true);
            bot.recordSolvedRiddle(room.getId());
        } else {
            System.out.println("Errado! " + bot.getName() + " perdeu tempo.");
        }
    }

    /**
     * Handles lever activation logic.
     *
     * @param p    The player activating the lever.
     * @param room The lever room.
     */
    public void handleLever(Player p, LeverRoom room) {
        if (room.isSolved()) {
            System.out.println("Esta alavanca já foi ativada com sucesso.");
            return;
        }

        System.out.println("\nTentando ativar a alavanca...");
        p.recordEncounteredEvent("Lever: " + room.getId());

        LeverRoom.LeverResult result = room.attemptLever();

        switch (result) {
            case CORRECT_CHOICE:
                System.out.println("A alavanca foi ativada corretamente!\n");
                System.out.println("Crack! Ouves uma parede a mover-se...");

                String targetId = maze.createSecretPassage(p.getCurrentRoom());
                if (targetId != null) {
                    System.out.println("Uma passagem secreta abriu-se para: " + targetId + "!");
                    System.out.println("Esta passagem está agora DISPONÍVEL PARA TODOS os jogadores!");
                    p.recordAppliedEffect("LEVER_UNLOCKED");
                    maze.activateLever(room.getId());

                    System.out.println("\nUma nova passagem foi desbloqueada por " + p.getName() + "!");
                } else {
                    System.out.println("O mecanismo parece encravado.");
                }
                break;

            case INCORRECT_CHOICE:
                System.out.println("A alavanca não respondeu.");
                System.out.println("O mecanismo fica bloqueado. Terás de tentar novamente noutra jogada.");
                p.recordAppliedEffect("LEVER_FAILED");
                break;

            case ALREADY_SOLVED:
                System.out.println("Esta alavanca já foi ativada com sucesso.");
                break;
        }
    }
}
