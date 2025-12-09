package model;

import java.util.Iterator;
import java.util.Scanner;

import exceptions.EmptyCollectionException;
import io.MapLoader;
import io.ReportExporter; // Required for the final JSON report
import structures.queue.LinkedQueue;

/**
 * Core engine of the "Labirinto da Glória" game.
 * <p>
 * This class manages the game loop, player turns (queue-based),
 * movement logic, event handling (traps, items), and victory conditions.
 * It supports both Human and Bot players in a multiplayer environment.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class GameEngine {
    private Maze maze;
    private LinkedQueue<Player> turnQueue;
    private boolean isRunning;
    private Scanner scanner;

    /**
     * Initializes the Game Engine with an empty queue and scanner.
     */
    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.turnQueue = new LinkedQueue<>();
        this.isRunning = false;
    }

    /**
     * Sets up the game by loading the map and registering players.
     *
     * @param mapFile The filename of the JSON map to load.
     */
    public void initGame(String mapFile) {
        System.out.println(">>> A carregar o Labirinto...");
        this.maze = MapLoader.loadMaze(mapFile);

        if (this.maze == null || this.maze.toString().isEmpty()) {
            System.err.println("CRÍTICO: Erro ao carregar o mapa.");
            return;
        }
        if (!maze.isConnected()) {
            System.err.println("ERRO CRÍTICO: O mapa carregado não é conexo (existem salas inalcançáveis).");
            return;
        }

        System.out.print("Quantos jogadores humanos? ");
        int numHumans = 0;
        if (scanner.hasNextInt()) {
            numHumans = scanner.nextInt();
            scanner.nextLine();
        }

        for (int i = 1; i <= numHumans; i++) {
            System.out.print("Nome do Jogador " + i + ": ");
            String name = scanner.nextLine();
            addPlayerToGame(new Player(name));
        }

        System.out.print("Quantos Bots? ");
        int numBots = 0;
        if (scanner.hasNextInt()) {
            numBots = scanner.nextInt();
            scanner.nextLine();
        }

        for (int i = 1; i <= numBots; i++) {
            addPlayerToGame(new Bot("Bot_" + i));
        }

        if (turnQueue.isEmpty()) {
            System.out.println("Sem jogadores, o jogo não pode começar.");
            return;
        }

        System.out.println("O jogo vai começar!");
        this.isRunning = true;
    }

    /**
     * Helper to place a player at the entrance and add them to the turn queue.
     * @param p The player to add.
     */
    private void addPlayerToGame(Player p) {
        Room entrada = maze.getEntrance();
        if (entrada != null) {
            p.setCurrentRoom(entrada);
            turnQueue.enqueue(p);
            System.out.println(">> " + p.getName() + " entrou no jogo em: " + entrada.getId());
        } else {
            System.err.println("Erro: Não há entrada definida no mapa.");
        }
    }

    /**
     * Starts the main game loop.
     * Manages turns, checks for victory, and handles turn skipping effects.
     */
    public void start() {
        System.out.println("\n=== O JOGO COMEÇOU! ===");

        while (isRunning && !turnQueue.isEmpty()) {
            try {
                Player currentPlayer = turnQueue.dequeue();

                System.out.println("\n>>> Turno de: " + currentPlayer.getName() + " <<<");

                if (currentPlayer.skipsTurn()) {
                    System.out.println("⛔ " + currentPlayer.getName() + " perdeu a vez devido a um efeito anterior!");
                    currentPlayer.setSkipNextTurn(false);
                    turnQueue.enqueue(currentPlayer);
                    continue;
                }

                if (currentPlayer instanceof Bot) {
                    playBotTurn((Bot) currentPlayer);
                } else {
                    playHumanTurn(currentPlayer);
                }

                if (currentPlayer.getCurrentRoom() instanceof Center) {
                    System.out.println("\n🎉 VITÓRIA! " + currentPlayer.getName() + " encontrou o tesouro!");
                    System.out.println("A gerar relatório final...");
                    ReportExporter.exportMissionReport(currentPlayer);
                    isRunning = false;
                    break;
                }

                if (isRunning) {
                    turnQueue.enqueue(currentPlayer);
                    if (currentPlayer instanceof Bot) {
                        try { Thread.sleep(1000); } catch (InterruptedException e) { }
                    }
                }

            } catch (EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Jogo terminado.");
    }

    /**
     * Handles the interaction logic for a human player.
     */
    private void playHumanTurn(Player player) {
        Room current = player.getCurrentRoom();
        printStatus(player);

        // Interaction with Lever Room
        if (current instanceof LeverRoom) {
            LeverRoom leverRoom = (LeverRoom) current;
            if (!leverRoom.isActivated()) {
                System.out.println("Queres puxar a alavanca? (sim/nao)");
                System.out.print("> ");
                if (scanner.hasNextLine()) {
                    String resp = scanner.nextLine().toLowerCase();
                    if (resp.equals("sim") || resp.equals("s")) {
                        handleLever(player, leverRoom);
                    }
                }
            }
        }

        System.out.println("O que queres fazer? (move <ID> | look | exit)");
        System.out.print("> ");

        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            processCommand(player, input);
        }
    }

    /**
     * Handles the automated logic for a bot player.
     */
    private void playBotTurn(Bot bot) {
        String targetId = bot.decideMove(maze);

        if (targetId != null) {
            System.out.println(bot.getName() + " decide mover-se para: " + targetId);
            movePlayer(bot, targetId);
        } else {
            System.out.println(bot.getName() + " está confuso e passa a vez.");
        }
    }

    /**
     * Processes text commands from the user.
     */
    private void processCommand(Player player, String input) {
        String[] parts = input.split(" ");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "exit":
                System.out.println("A sair do jogo...");
                isRunning = false;
                break;
            case "look":
                printStatus(player);
                break;
            case "move":
                if (parts.length < 2) {
                    System.out.println("Uso incorreto. Tenta: move <ID_DA_SALA>");
                } else {
                    movePlayer(player, parts[1]);
                }
                break;
            default:
                System.out.println("Comando inválido.");
        }
    }

    /**
     * Moves a player (Human or Bot) to a target room ID, checking validity and events.
     *
     * @param p        The player moving.
     * @param targetId The ID of the destination room.
     */
    private void movePlayer(Player p, String targetId) {
        Room current = p.getCurrentRoom();

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
            if (corridor != null) {
                RandomEvent event = corridor.getEvent();
                if (event != null) {
                    System.out.println("\n ALERTA DE EVENTO NO CORREDOR!");
                    corridor.triggerEvent();
                    applyEffect(p, event);
                }
            }

            p.setCurrentRoom(targetRoom);
            System.out.println(">> " + p.getName() + " entrou em: " + targetRoom.getId());

            if (targetRoom instanceof RiddleRoom && !(p instanceof Bot)) {
                handleRiddle((RiddleRoom) targetRoom);
            }

        } else {
            System.out.println("(!) Movimento inválido: Caminho para " + targetId + " inexistente.");
        }
    }

    /**
     * Applies the logic of a Random Event Effect to a player.
     */
    private void applyEffect(Player p, RandomEvent event) {
        if (event == null) return;

        Effect effect = event.getDirectEffect();
        if (effect != null) {
            switch (effect) {
                case DAMAGE:
                case TRAP:
                    p.updatePower(-Math.abs(effect.getValue()));
                    break;
                case HEAL:
                case BONUS_POWER:
                    p.updatePower(Math.abs(effect.getValue()));
                    break;
                case SKIP_TURN:
                    System.out.println("⏳ " + p.getName() + " ficou atordoado e perderá a próxima jogada!");
                    p.setSkipNextTurn(true);
                    break;
                case SWAP_POSITION:
                    performSwap(p);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handles the logic for Riddle Rooms (Display question, check answer).
     */
    private void handleRiddle(RiddleRoom room) {
        if (room.isSolved()) {
            System.out.println("Este enigma já foi resolvido.");
            return;
        }

        Riddle r = room.getRiddle();
        if (r == null) return;

        System.out.println("\n--- 🧩 ENIGMA 🧩 ---");
        System.out.println(r.getQuestion());

        Iterator<String> it = r.getOptions().iterator();
        int i = 1;
        while(it.hasNext()) {
            System.out.println(i + ". " + it.next());
            i++;
        }

        System.out.print("Resposta (número): ");
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (r.checkAnswer(choice - 1)) {
                System.out.println("Correto! Podes continuar.");
                room.setSolved(true);
            } else {
                System.out.println("Errado! Perdeste tempo.");
            }
        }
    }

    /**
     * Handles the interaction with a Lever Room (Open secret path, Trap, or Nothing).
     */
    private void handleLever(Player p, LeverRoom room) {
        LeverRoom.LeverResult result = room.pullLever();

        switch (result) {
            case UNLOCK_PATH:
                System.out.println("CRACK! Ouves uma parede a mover-se...");
                String targetId = maze.createSecretPassage(p.getCurrentRoom());
                if (targetId != null) {
                    System.out.println("Uma passagem secreta abriu-se para: " + targetId + "!");
                } else {
                    System.out.println("O mecanismo parece encravado.");
                }
                break;
            case TRAP:
                System.out.println("CLANG! Uma armadilha disparou!");
                p.updatePower(-20);
                break;
            case NOTHING:
                System.out.println("Apenas um clique seco. Nada aconteceu.");
                break;
        }
    }

    /**
     * Swaps the active player's position with the next player in the queue.
     */
    private void performSwap(Player activePlayer) {
        if (turnQueue.isEmpty()) {
            System.out.println("Não há mais ninguém com quem trocar!");
            return;
        }

        try {
            Player target = turnQueue.first();

            Room myRoom = activePlayer.getCurrentRoom();
            Room targetRoom = target.getCurrentRoom();

            activePlayer.setCurrentRoom(targetRoom);
            target.setCurrentRoom(myRoom);

            System.out.println("TROCA! " + activePlayer.getName() + " trocou de lugar com " + target.getName());
            System.out.println(activePlayer.getName() + " está agora em: " + targetRoom.getId());

        } catch (Exception e) {
            System.out.println("Erro ao trocar posições.");
        }
    }

    private void printStatus(Player p) {
        Room current = p.getCurrentRoom();
        System.out.println("\n--- Estado de " + p.getName() + " ---");
        System.out.println("Localização: " + current.getId() + " (" + current.getDescription() + ")");
        System.out.println("Poder: " + p.getPower());
        System.out.println("Saídas: [" + maze.getAvailableExits(current) + "]");
    }

}