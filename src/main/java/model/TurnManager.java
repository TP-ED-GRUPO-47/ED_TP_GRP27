package model;

import java.util.Iterator;
import java.util.Scanner;

import exceptions.GameOverException;
import exceptions.InvalidMoveException;
import structures.linear.ArrayUnorderedList;

/**
 * Manages player turns, movement, and user input.
 * <p>
 * Separates turn logic from the main game engine.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class TurnManager {
    private final Scanner scanner;
    private final Maze maze;
    private final RoomEventHandler eventHandler;
    private final EffectProcessor effectProcessor;

    /**
     * Creates a new TurnManager.
     *
     * @param scanner        Scanner for user input.
     * @param maze           The game maze.
     * @param eventHandler   Handler for room events.
     * @param effectProcessor Processor for effects.
     */
    public TurnManager(Scanner scanner, Maze maze, RoomEventHandler eventHandler, EffectProcessor effectProcessor) {
        this.scanner = scanner;
        this.maze = maze;
        this.eventHandler = eventHandler;
        this.effectProcessor = effectProcessor;
    }

    /**
     * Handles a human player's turn.
     *
     * @param player The human player.
     */
    public void playHumanTurn(Player player) throws GameOverException {
        Room current = player.getCurrentRoom();
        printStatus(player);

        if (current instanceof LeverRoom) {
            handleLeverPrompt(player, (LeverRoom) current);
        }

        ArrayUnorderedList<Room> exits = listAvailableExits(current);
        if (exits.isEmpty()) {
            System.out.println("Sem saídas disponíveis.");
            return;
        }

        promptAndMove(player, exits);
    }

    /**
     * Asks the player if they want to pull the lever and dispatches the event.
     */
    private void handleLeverPrompt(Player player, LeverRoom leverRoom) {
        if (leverRoom.isSolved()) {
            return;
        }

        System.out.print("Queres puxar a alavanca (S/N)? ");
        if (!scanner.hasNextLine()) {
            return;
        }

        String resp = scanner.nextLine().toLowerCase();
        if (resp.equals("sim") || resp.equals("s")) {
            eventHandler.handleLever(player, leverRoom);
        }
    }

    /**
     * Prints and returns a de-duplicated list of exits for the current room.
     */
    private ArrayUnorderedList<Room> listAvailableExits(Room current) {
        ArrayUnorderedList<Room> exits = new ArrayUnorderedList<>();
        Iterator<Room> neighbors = maze.getNeighbors(current);

        System.out.println("\nSalas disponíveis:\n");
        int option = 1;
        while (neighbors.hasNext()) {
            Room r = neighbors.next();
            if (isRoomIdAbsent(exits, r.getId())) {
                exits.addToRear(r);
                System.out.println(option + ". " + r.getId() + " - " + r.getDescription());
                option++;
            }
        }
        return exits;
    }

    /**
     * Reads user commands to move, re-print status, or exit the game.
     */
    private void promptAndMove(Player player, ArrayUnorderedList<Room> exits) throws GameOverException {
        while (true) {
            System.out.print("\nEscolhe o número da sala (ou 'look' para rever, 'exit' para sair): ");
            if (!scanner.hasNextLine()) {
                return;
            }

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("A sair do jogo...");
                effectProcessor.setRunning(false);
                throw new GameOverException("Jogador encerrou o jogo manualmente");
            }

            if (input.equalsIgnoreCase("look")) {
                printStatus(player);
                continue;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= exits.size()) {
                    Room targetRoom = getRoomByIndex(exits, choice);
                    if (targetRoom != null) {
                        movePlayer(player, targetRoom.getId());
                        return;
                    }
                    System.out.println("Opção inválida. Tenta novamente.");
                } else {
                    System.out.println("Número fora do intervalo. Escolhe entre 1 e " + exits.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Usa um número, 'look' ou 'exit'.");
            } catch (InvalidMoveException e) {
                System.out.println("Movimento inválido: " + e.getMessage());
                System.out.println("Tenta outra sala.");
            }
        }
    }

    /**
     * Handles a bot player's turn.
     *
     * @param bot The bot player.
     */
    public void playBotTurn(Bot bot) {
        Room current = bot.getCurrentRoom();
        System.out.println(bot.getName() + " está em: " + current.getId());

        if (current instanceof LeverRoom) {
            LeverRoom leverRoom = (LeverRoom) current;
            if (!leverRoom.isSolved() && Math.random() < 0.5) {
                eventHandler.handleLever(bot, leverRoom);
            }
        }

        if (current instanceof RiddleRoom) {
            RiddleRoom riddleRoom = (RiddleRoom) current;
            if (!riddleRoom.isSolved()) {
                eventHandler.handleBotRiddle(riddleRoom, bot);
                return;
            }
        }

        String targetId = bot.decideMove(maze);
        if (targetId != null) {
            movePlayer(bot, targetId);
        }
    }

    /**
     * Moves a player to a target room.
     *
     * @param p        The player moving.
     * @param targetId The destination room ID.
     */
    public void movePlayer(Player p, String targetId) {
        Room current = p.getCurrentRoom();

        if (current instanceof RiddleRoom) {
            RiddleRoom riddleRoom = (RiddleRoom) current;
            if (!riddleRoom.isSolved()) {
                System.out.println("Não podes sair! Tens de resolver o enigma primeiro.");
                return;
            }
        }

        Iterator<Room> neighbors = maze.getNeighbors(current);
        boolean found = false;
        Room targetRoom = null;

        while (neighbors.hasNext()) {
            Room r = neighbors.next();
            if (r.getId().equalsIgnoreCase(targetId)) {
                targetRoom = r;
                found = true;
                break;
            }
        }

        if (found) {
            Corridor corridor = maze.getCorridorBetween(current, targetRoom);
            boolean swapAllTriggered = false;
            boolean recedeTriggered = false;

            if (corridor != null) {
                RandomEvent event = corridor.getEvent();
                if (event != null) {
                    System.out.println("\nAlerta de Evento!");
                    corridor.triggerEvent(p);

                    if (event.getDirectEffect() == Effect.SWAP_ALL) {
                        swapAllTriggered = true;
                    }
                    if (event.getDirectEffect() == Effect.RECEDE) {
                        recedeTriggered = true;
                    }

                    effectProcessor.applyEffect(p, event);
                }
            }

            if (swapAllTriggered) {
                System.out.println(p.getName() + " foi teletransportado antes de completar o movimento!");
                return;
            }

            if (recedeTriggered) {
                return;
            }

            p.setCurrentRoom(targetRoom);

            if (targetRoom instanceof RiddleRoom) {
                RiddleRoom riddleRoom = (RiddleRoom) targetRoom;
                if (p instanceof Bot) {
                    eventHandler.handleBotRiddle(riddleRoom, (Bot) p);
                } else {
                    eventHandler.handleRiddle(riddleRoom, p);
                }
            }

            if (targetRoom instanceof LeverRoom) {
                eventHandler.handleLever(p, (LeverRoom) targetRoom);
            }

        } else {
            throw new InvalidMoveException("Caminho para " + targetId + " inexistente a partir de " + current.getId());
        }
    }

    /**
     * Prints the current status of a player.
     *
     * @param p The player.
     */
    private void printStatus(Player p) {
        Room current = p.getCurrentRoom();
        System.out.println("Localização: " + current.getId() + " (" + current.getDescription() + ")");
        System.out.println("Poder: " + p.getPower());
        ArrayUnorderedList<Room> unique = new ArrayUnorderedList<>();
        Iterator<Room> it = maze.getNeighbors(current);
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            Room r = it.next();
            if (isRoomIdAbsent(unique, r.getId())) {
                unique.addToRear(r);
                sb.append(r.getId()).append(" | ");
            }
        }
        System.out.println("Saídas: [" + sb.toString() + "]");
    }

    /**
     * Helper to fetch a room by its 1-based index.
     */
    private Room getRoomByIndex(ArrayUnorderedList<Room> rooms, int index) {
        if (index < 1 || index > rooms.size()) {
            return null;
        }

        Iterator<Room> it = rooms.iterator();
        int current = 1;
        while (it.hasNext()) {
            Room r = it.next();
            if (current == index) {
                return r;
            }
            current++;
        }
        return null;
    }

    /**
     * Checks if a room ID is absent from the provided list.
     */
    private boolean isRoomIdAbsent(ArrayUnorderedList<Room> list, String roomId) {
        Iterator<Room> it = list.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r.getId().equalsIgnoreCase(roomId)) {
                return false;
            }
        }
        return true;
    }
}
