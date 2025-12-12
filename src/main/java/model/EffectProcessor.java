package model;

import java.util.Iterator;
import java.util.Scanner;

import exceptions.GameOverException;
import io.ReportExporter;
import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;
import structures.stack.LinkedStack;

/**
 * Processes all game effects: DAMAGE, HEAL, SWAP, RECEDE, etc.
 * <p>
 * Centralizes effect logic to keep GameEngine clean and maintainable.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class EffectProcessor {
    private final Scanner scanner;
    private final Maze maze;
    private final ArrayUnorderedList<Player> allPlayers;
    private final LinkedQueue<Player> turnQueue;
    private boolean isRunning;

    /**
     * Creates a new EffectProcessor.
     *
     * @param scanner    Scanner for user input.
     * @param maze       The game maze.
     * @param allPlayers List of all players.
     * @param turnQueue  The turn queue.
     */
    public EffectProcessor(Scanner scanner, Maze maze, ArrayUnorderedList<Player> allPlayers, LinkedQueue<Player> turnQueue) {
        this.scanner = scanner;
        this.maze = maze;
        this.allPlayers = allPlayers;
        this.turnQueue = turnQueue;
        this.isRunning = true;
    }

    /**
     * Sets the running state (used for GameOverException).
     *
     * @param running True if game is running.
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    /**
     * Applies a random event effect to a player.
     *
     * @param p     The player affected.
     * @param event The random event.
     */
    public void applyEffect(Player p, RandomEvent event) {
        if (event == null) return;

        Effect effect = event.getDirectEffect();
        if (effect != null) {
            switch (effect) {
                case DAMAGE:
                case TRAP:
                    p.updatePower(-Math.abs(effect.getValue()));
                    p.recordEncounteredEvent("TRAP");
                    p.recordAppliedEffect(effect.toString());
                    if (p.getPower() <= 0) {
                        System.out.println("\n" + p.getName() + " morreu! Fim do jogo.");
                        System.out.println("\nA gerar relatório final...");
                        ReportExporter.exportMissionReport(p);
                        isRunning = false;
                        throw new GameOverException(p.getName() + " morreu com poder <= 0");
                    }
                    break;
                case HEAL:
                case BONUS_POWER:
                    p.updatePower(Math.abs(effect.getValue()));
                    p.recordEncounteredEvent("HEAL/BONUS");
                    p.recordAppliedEffect(effect.toString());
                    break;
                case SKIP_TURN:
                    System.out.println(p.getName() + " ficou atordoado e perderá a próxima jogada!");
                    p.setSkipNextTurn(true);
                    p.recordAppliedEffect(effect.toString());
                    break;
                case SWAP_POSITION:
                    performSwap(p);
                    p.recordAppliedEffect(effect.toString());
                    break;
                case SWAP_ALL:
                    System.out.println("Todos os jogadores trocam de posição!");
                    performSwapAll();
                    p.recordAppliedEffect(effect.toString());
                    break;
                case EXTRA_TURN:
                    System.out.println(p.getName() + " ganhou uma jogada extra!");
                    turnQueue.enqueue(p);
                    p.recordAppliedEffect(effect.toString());
                    break;
                case RECEDE:
                    System.out.println(p.getName() + " foi forçado a recuar!");
                    performRecede(p, Math.abs(effect.getValue()));
                    p.recordAppliedEffect(effect.toString());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Swaps the active player's position with another player.
     *
     * @param activePlayer The player triggering the swap.
     */
    public void performSwap(Player activePlayer) {
        ArrayUnorderedList<Player> otherPlayers = new ArrayUnorderedList<>();
        Iterator<Player> it = allPlayers.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            if (!p.equals(activePlayer)) {
                otherPlayers.addToRear(p);
            }
        }

        if (otherPlayers.isEmpty()) {
            System.out.println("Não há mais ninguém com quem trocar!");
            return;
        }

        Player target = null;

        if (!(activePlayer instanceof Bot)) {
            System.out.println("\nEscolhe um jogador para trocar de posição:\n");
            int index = 1;
            Iterator<Player> listIt = otherPlayers.iterator();
            while (listIt.hasNext()) {
                Player p = listIt.next();
                System.out.println(index + ". " + p.getName() + " (em " + p.getCurrentRoom().getId() + ")");
                index++;
            }

            System.out.print("\nEscolha (1-" + (index - 1) + "): ");
            if (scanner.hasNextLine()) {
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice < index) {
                        Iterator<Player> choiceIt = otherPlayers.iterator();
                        for (int i = 1; i <= choice; i++) {
                            target = choiceIt.next();
                        }
                    } else {
                        System.out.println("Escolha inválida. Cancelando troca.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Cancelando troca.");
                    return;
                }
            }
        } else {
            int randomIndex = new java.util.Random().nextInt(otherPlayers.size());
            Iterator<Player> botIt = otherPlayers.iterator();
            for (int i = 0; i <= randomIndex; i++) {
                target = botIt.next();
            }
        }

        if (target == null) {
            System.out.println("Erro ao selecionar jogador.");
            return;
        }

        Room myRoom = activePlayer.getCurrentRoom();
        Room targetRoom = target.getCurrentRoom();

        activePlayer.setCurrentRoomSilent(targetRoom);
        target.setCurrentRoomSilent(myRoom);

        activePlayer.setLastSwappedPosition(targetRoom);
        target.setLastSwappedPosition(myRoom);

        System.out.println(activePlayer.getName() + " trocou de lugar com " + target.getName());
        System.out.println(activePlayer.getName() + " está agora em: " + targetRoom.getId());
    }

    /**
     * Swaps ALL players' positions simultaneously (circular rotation).
     */
    public void performSwapAll() {
        if (allPlayers.size() <= 1) {
            System.out.println("Não há jogadores suficientes para trocar.");
            return;
        }

        ArrayUnorderedList<Room> currentRooms = new ArrayUnorderedList<>();
        Iterator<Player> it = allPlayers.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            currentRooms.addToRear(p.getCurrentRoom());
        }

        ArrayUnorderedList<Player> playerList = new ArrayUnorderedList<>();
        it = allPlayers.iterator();
        while (it.hasNext()) {
            playerList.addToRear(it.next());
        }
        
        for (int i = 0; i < playerList.size(); i++) {
            Player p = getPlayerAtIndex(playerList, i);
            Room newRoom = getRoomAtIndex(currentRooms, (i + 1) % currentRooms.size());
            
            if (p != null && newRoom != null) {
                p.setCurrentRoomSilent(newRoom);
                p.setLastSwappedPosition(newRoom);
                System.out.println("  " + p.getName() + " → " + newRoom.getId());
            }
        }
    }
    
    /**
     * Helper method to get a player at a specific index.
     */
    private Player getPlayerAtIndex(ArrayUnorderedList<Player> list, int index) {
        Iterator<Player> it = list.iterator();
        int current = 0;
        while (it.hasNext()) {
            Player p = it.next();
            if (current == index) {
                return p;
            }
            current++;
        }
        return null;
    }
    
    /**
     * Helper method to get a room at a specific index.
     */
    private Room getRoomAtIndex(ArrayUnorderedList<Room> list, int index) {
        Iterator<Room> it = list.iterator();
        int current = 0;
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
     * Makes the player step back through their movement history.
     *
     * @param p     The player to move backward.
     * @param steps Number of steps to go back.
     */
    public void performRecede(Player p, int steps) {
        if (steps <= 0) {
            return;
        }

        LinkedStack<String> history = p.getMovementHistory();
        Room limitRoom = p.getLastSwappedPosition();
        String limitId = (limitRoom != null) ? limitRoom.getId() : null;

        try {
            history.pop();
        } catch (Exception e) {
            System.out.println(p.getName() + " não tem histórico para recuar.");
            return;
        }

        int actualSteps = 0;
        String previousRoomId = null;

        for (int i = 0; i < steps; i++) {
            if (history.isEmpty()) {
                break;
            }

            try {
                previousRoomId = history.peek();

                if (limitId != null && previousRoomId.equalsIgnoreCase(limitId)) {
                    break;
                }

                history.pop();
                actualSteps++;
            } catch (Exception e) {
                break;
            }
        }

        Room targetRoom = maze.getRoomById(previousRoomId != null ? previousRoomId : p.getCurrentRoom().getId());

        if (targetRoom != null && !targetRoom.equals(p.getCurrentRoom())) {
            p.setCurrentRoomSilent(targetRoom);
            System.out.println(p.getName() + " recuou " + actualSteps + " sala(s) e está agora em: " + targetRoom.getId());
        } else {
            System.out.println(p.getName() + " não conseguiu recuar (limite atingido ou sem histórico).");
        }
    }
}
