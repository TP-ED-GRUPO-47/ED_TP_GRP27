package model;

import structures.stack.LinkedStack;

/**
 * Representa um jogador no Labirinto da Glória.
 * <p>
 * O jogador possui um nome, uma posição atual no labirinto e um histórico
 * de movimentos realizado durante o jogo.
 * </p>
 *
 * @author Miguel Pereira
 * @version 1.0
 */
public class Player {

    private String name;
    private Room currentRoom;
    private long power;

    /**
     * Histórico de movimentos para o relatório final.
     * Guarda os IDs das salas visitadas.
     */
    private LinkedStack<String> movementHistory;

    /**
     * Construtor do Jogador.
     *
     * @param name Nome do jogador.
     */
    public Player(String name) {
        this.name = name;
        this.power = 100;
        this.movementHistory = new LinkedStack<>();
    }

    /**
     * Define a sala onde o jogador se encontra.
     * Regista automaticamente a sala no histórico.
     *
     * @param room A nova sala atual.
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
        if (room != null) {
            this.movementHistory.push(room.getId());
            room.onEnter();
        }
    }

    /**
     * Obtém a sala atual do jogador.
     * @return A sala onde o jogador está.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Obtém o nome do jogador.
     * @return O nome.
     */
    public String getName() {
        return name;
    }

    /**
     * Método auxiliar para imprimir o histórico (debug).
     */
    public void printHistory() {
        System.out.println("Histórico de " + name + ": " + movementHistory.toString());
    }

    @Override
    public String toString() {
        return "Player: " + name + " [Room: " + (currentRoom != null ? currentRoom.getId() : "None") + "]";
    }
}