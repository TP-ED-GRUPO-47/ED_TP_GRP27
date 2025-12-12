package model;

import java.util.Iterator;
import java.util.Scanner;

import exceptions.EmptyCollectionException;
import io.MapLoader;
import io.ReportExporter;
import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;

/**
 * Core engine of the "Labirinto da Glória" game.
 * <p>
 * Refactored to delegate responsibilities to specialized handlers:
 * - {@link RoomEventHandler} for riddles and levers
 * - {@link EffectProcessor} for game effects (SWAP, RECEDE, DAMAGE)
 * - {@link TurnManager} for turn execution and movement
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class GameEngine {
    private Maze maze;
    private LinkedQueue<Player> turnQueue;
    private ArrayUnorderedList<Player> allPlayers;
    private boolean isRunning;
    private Scanner scanner;
    private ArrayUnorderedList<Room> availableEntrances;
    private int entranceIndex;

    private RoomEventHandler eventHandler;
    private EffectProcessor effectProcessor;
    private TurnManager turnManager;
    private Player winner;

    /**
     * Initializes the Game Engine with an empty queue and scanner.
     */
    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.turnQueue = new LinkedQueue<>();
        this.allPlayers = new ArrayUnorderedList<>();
        this.isRunning = false;
        this.availableEntrances = new ArrayUnorderedList<>();
        this.entranceIndex = 0;
        this.winner = null;
    }

    /**
     * Initializes helper classes after maze is loaded.
     */
    private void initHelpers() {
        this.eventHandler = new RoomEventHandler(scanner, maze);
        this.effectProcessor = new EffectProcessor(scanner, maze, allPlayers, turnQueue);
        this.turnManager = new TurnManager(scanner, maze, eventHandler, effectProcessor);
    }

    /**
     * Sets up the game by loading the map and registering players.
     *
     * @param mapFile The filename of the JSON map to load.
     */
    public void initGame(String mapFile) {
        System.out.println("\nA carregar o Labirinto...\n");
        this.maze = MapLoader.loadMaze(mapFile);

        if (this.maze == null || this.maze.toString().isEmpty()) {
            System.err.println("Erro ao carregar o mapa.");
            return;
        }
        if (!maze.isConnected()) {
            System.err.println("O mapa carregado não é conexo (existem salas inalcançáveis).");
            return;
        }

        this.availableEntrances = maze.getAllEntrances();
        this.entranceIndex = 0;

        if (availableEntrances.isEmpty()) {
            System.err.println("Nenhuma entrada encontrada no mapa.");
            return;
        }

        initHelpers();

        System.out.println("\nEntradas disponíveis no labirinto:\n");
        Iterator<Room> entranceIt = availableEntrances.iterator();
        int entranceNum = 1;
        while (entranceIt.hasNext()) {
            Room entrance = entranceIt.next();
            System.out.println(entranceNum + ". " + entrance.getId() + " - " + entrance.getDescription());
            entranceNum++;
        }

        System.out.print("\nQuantos jogadores humanos? ");
        int numHumans = readIntSafe();

        boolean manualEntrance = false;
        if (numHumans > 0) {
            System.out.print("Permitir escolha de entrada para jogadores humanos? (S/N): ");
            manualEntrance = scanner.nextLine().trim().toLowerCase().startsWith("s");
        }

        for (int i = 1; i <= numHumans; i++) {
            System.out.print("\nNome do Jogador " + i + ": ");
            String name = scanner.nextLine();
            Player player = new Player(name);
            
            if (manualEntrance && availableEntrances.size() > 1) {
                addPlayerWithChoice(player);
            } else {
                addPlayerToGame(player);
            }
        }

        System.out.print("\nQuantos Bots? ");
        int numBots = readIntSafe();

        for (int i = 1; i <= numBots; i++) {
            addPlayerToGame(new Bot("Bot_" + i));
        }

        if (turnQueue.isEmpty()) {
            System.out.println("Sem jogadores, o jogo não pode começar.");
            return;
        }

        System.out.println("\nO jogo vai começar...");
        this.isRunning = true;
    }

    /**
     * Helper to place a player at an entrance and add them to the turn queue.
     * @param p The player to add.
     */
    private void addPlayerToGame(Player p) {
        if (availableEntrances.isEmpty()) {
            System.err.println("Não há entradas disponíveis no mapa.");
            return;
        }

        Iterator<Room> it = availableEntrances.iterator();
        int count = 0;
        Room entrada = null;
        while (it.hasNext() && count <= entranceIndex) {
            entrada = it.next();
            count++;
        }

        if (entrada == null || count <= entranceIndex) {
            it = availableEntrances.iterator();
            entrada = it.hasNext() ? it.next() : null;
            entranceIndex = 0;
        }

        if (entrada != null) {
            p.setCurrentRoom(entrada);
            turnQueue.enqueue(p);
            allPlayers.addToRear(p);
            entranceIndex++;
        } else {
            System.err.println("Não há entrada definida no mapa.");
        }
    }

    /**
     * Allows a player to manually choose their starting entrance.
     * @param p The player to add.
     */
    private void addPlayerWithChoice(Player p) {
        if (availableEntrances.isEmpty()) {
            System.err.println("Não há entradas disponíveis no mapa.");
            return;
        }

        System.out.println("\n" + p.getName() + ", escolhe a tua entrada:\n");
        Iterator<Room> it = availableEntrances.iterator();
        int index = 1;
        while (it.hasNext()) {
            Room entrance = it.next();
            System.out.println(+ index + ". " + entrance.getId() + " - " + entrance.getDescription());
            index++;
        }

        System.out.print("\nEscolha (1-" + (index - 1) + "): ");
        int choice = readIntSafe();

        it = availableEntrances.iterator();
        Room entrada = null;
        for (int i = 1; i <= choice && it.hasNext(); i++) {
            entrada = it.next();
        }

        if (entrada != null) {
            p.setCurrentRoom(entrada);
            turnQueue.enqueue(p);
            allPlayers.addToRear(p);
        } else {
            System.out.println("Entrada inválida. A usar entrada padrão.");
            addPlayerToGame(p);
        }
    }

    /**
     * Reads an integer from input, consuming invalid tokens until a number is provided.
     */
    private int readIntSafe() {
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, insere um número válido.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    /**
     * Starts the main game loop.
     * Delegates turn execution to TurnManager.
     */
    public void start() {
        System.out.println("\nO jogo começou! Boa sorte a todos!");

        while (isRunning && !turnQueue.isEmpty()) {
            try {
                Player currentPlayer = turnQueue.dequeue();

                System.out.println("\nTurno de: " + currentPlayer.getName());

                if (currentPlayer.skipsTurn()) {
                    System.out.println(currentPlayer.getName() + " perdeu a vez devido a um efeito anterior!");
                    currentPlayer.setSkipNextTurn(false);
                    turnQueue.enqueue(currentPlayer);
                    continue;
                }

                if (currentPlayer instanceof Bot) {
                    turnManager.playBotTurn((Bot) currentPlayer);
                } else {
                    turnManager.playHumanTurn(currentPlayer);
                }
                
                effectProcessor.setRunning(isRunning);

                if (currentPlayer.getCurrentRoom() instanceof Center) {
                    System.out.println("\nVitória! " + currentPlayer.getName() + " encontrou o tesouro!");
                    System.out.println("\nA gerar relatório final...");
                    ReportExporter.exportMissionReport(currentPlayer);
                    winner = currentPlayer;
                    isRunning = false;
                    break;
                }

                if (isRunning) {
                    turnQueue.enqueue(currentPlayer);
                    if (currentPlayer instanceof Bot) {
                        try { Thread.sleep(1000); } catch (InterruptedException e) { }
                    }
                }

            } catch (exceptions.GameOverException e) {
                isRunning = false;
                break;
            } catch (EmptyCollectionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nJogo terminado.\n");

        try {
            ReportExporter.exportMatchSummary(allPlayers, winner);
        } catch (Exception e) {
            System.err.println("Falha ao gerar relatório global: " + e.getMessage());
        }
    }
}
