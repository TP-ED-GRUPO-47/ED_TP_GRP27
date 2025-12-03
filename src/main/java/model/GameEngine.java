package model;

import io.MapLoader;
import structures.queue.LinkedQueue;
import exceptions.EmptyCollectionException;

import java.util.Iterator;
import java.util.Scanner;

public class GameEngine {
    private Maze maze;
    private LinkedQueue<Player> turnQueue;
    private boolean isRunning;
    private Scanner scanner;

    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.turnQueue = new LinkedQueue<>();
        this.isRunning = false;
    }

    public void initGame(String mapFile) {
        System.out.println(">>> A carregar o Labirinto...");
        this.maze = MapLoader.loadMaze(mapFile);

        if (this.maze == null || this.maze.toString().isEmpty()) {
            System.err.println("CRÍTICO: Erro ao carregar mapa.");
            return;
        }

        System.out.print("Quantos jogadores humanos? ");
        int numHumans = 0;
        if (scanner.hasNextInt()) {
            numHumans = scanner.nextInt();
            scanner.nextLine(); // limpar buffer
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

    private void addPlayerToGame(Player p) {
        Room entrada = maze.getEntrance();
        if (entrada != null) {
            p.setCurrentRoom(entrada);
            turnQueue.enqueue(p); // Adiciona o jogador à fila
            System.out.println(">> " + p.getName() + " entrou no jogo em: " + entrada.getId());
        } else {
            System.err.println("Erro: Não há entrada definida no mapa.");
        }
    }

    public void start() {
        System.out.println("\n=== O JOGO COMEÇOU! ===");

        while (isRunning && !turnQueue.isEmpty()) {
            try {
                Player currentPlayer = turnQueue.dequeue();

                System.out.println("\n>>> Turno de: " + currentPlayer.getName() + " <<<");

                if (currentPlayer instanceof Bot) {
                    playBotTurn((Bot) currentPlayer);
                } else {
                    playHumanTurn(currentPlayer);
                }

                if (currentPlayer.getCurrentRoom() instanceof Center) {
                    System.out.println("\n🎉 VITORIA! " + currentPlayer.getName() + " encontrou o tesouro!");
                    isRunning = false; // Jogo acaba
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

    private void playHumanTurn(Player player) {
        printStatus(player);

        System.out.println("O que queres fazer? (move <ID> | look | exit)");
        System.out.print("> ");

        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            processCommand(player, input);
        }
    }

    private void playBotTurn(Bot bot) {
        String targetId = bot.decideMove(maze);

        if (targetId != null) {
            System.out.println(bot.getName() + " decide mover-se para: " + targetId);
            movePlayer(bot, targetId);
        } else {
            System.out.println(bot.getName() + " está confuso e passa a vez.");
        }
    }

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
            p.setCurrentRoom(targetRoom);
            System.out.println(">> " + p.getName() + " entrou em: " + targetRoom.getId());

            if (targetRoom instanceof RiddleRoom && !(p instanceof Bot)) {
                handleRiddle((RiddleRoom) targetRoom);
            }
        } else {
            System.out.println("(!) Movimento inválido: " + targetId);
        }
    }

    private void handleRiddle(RiddleRoom room) {
        if (room.isSolved()) {
            System.out.println("Este enigma já foi resolvido anteriormente.");
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

        System.out.print("Resposta: ");
        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (r.checkAnswer(choice - 1)) {
                System.out.println("✅ Correto! Podes continuar.");
                room.setSolved(true);
            } else {
                System.out.println("❌ Errado! Perdeste tempo.");
            }
        }
    }

    private void printStatus(Player p) {
        Room current = p.getCurrentRoom();
        System.out.println("\n--- Estado de " + p.getName() + " ---");
        System.out.println("Local: " + current.getId() + " (" + current.getDescription() + ")");
        System.out.println("Saídas: [" + maze.getAvailableExits(current) + "]");
    }
}