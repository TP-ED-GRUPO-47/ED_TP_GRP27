package model;

import structures.graph.NetworkList;
import java.util.Iterator;

/**
 * Representa o labirinto (mapa) do jogo.
 * <p>
 * Esta classe encapsula a estrutura de dados em rede (NetworkList) onde
 * os vértices são as salas (Room) e as arestas são os corredores.
 * </p>
 *
 * @author Miguel Pereira
 * @version 1.0
 */
public class Maze {

    /**
     * O grafo pesado que armazena as salas e os custos de movimento.
     */
    private NetworkList<Room> map;

    /**
     * Construtor do Maze.
     * Inicializa a rede vazia.
     */
    public Maze() {
        this.map = new NetworkList<>();
    }

    /**
     * Adiciona uma sala ao labirinto.
     *
     * @param room A sala a adicionar.
     */
    public void addRoom(Room room) {
        if (room != null) {
            this.map.addVertex(room);
        }
    }

    /**
     * Cria um corredor entre duas salas com um determinado custo (peso).
     *
     * @param fromId ID da sala de origem.
     * @param toId   ID da sala de destino.
     * @param cost   O custo/peso para atravessar o corredor (ex: distância ou dificuldade).
     */
    public void addCorridor(String fromId, String toId, double cost) {
        Room from = new RoomStandard(fromId, "");
        Room to = new RoomStandard(toId, "");

        this.map.addEdge(from, to, cost);
    }

    /**
     * Obtém o iterador para o caminho mais curto entre duas salas.
     *
     * @param start A sala de início.
     * @param end   A sala de destino.
     * @return Um iterador com a sequência de salas do caminho mais curto.
     */
    public Iterator<Room> getShortestPath(Room start, Room end) {
        return map.iteratorShortestPath(start, end);
    }

    /**
     * Devolve um iterador com as salas vizinhas (para onde o jogador pode ir).
     *
     * @param currentRoom A sala atual.
     * @return Iterator<Room> com as salas acessíveis.
     */
    public Iterator<Room> getNeighbors(Room currentRoom) {
        // Delega a chamada para a NetworkList que alteraste agora
        return map.getNeighbors(currentRoom);
    }

    // ... (outros métodos) ...

    /**
     * Procura no labirinto a sala definida como Entrada.
     * Utiliza o iterador do grafo para percorrer todas as salas.
     *
     * @return A sala de entrada, ou null se não existir.
     */
    public Room getEntrance() {
        if (map.isEmpty()) return null;

        Iterator<Room> it = map.iteratorBFS(0);

        while (it.hasNext()) {
            Room r = it.next();
            if (r instanceof Entrance) {
                return r;
            }
        }
        return null;
    }

    /**
     * Devolve uma String bonita com as saídas para mostrar na consola.
     */
    public String getAvailableExits(Room currentRoom) {
        if (currentRoom == null) return "Nenhuma";

        Iterator<Room> it = getNeighbors(currentRoom);
        StringBuilder sb = new StringBuilder();

        if (!it.hasNext()) return "Sem saídas (Beco sem saída)";

        while (it.hasNext()) {
            Room neighbor = it.next();
            sb.append(neighbor.getId()).append(" | ");
        }

        return sb.toString();
    }

    /**
     * Retorna a representação textual do grafo do labirinto.
     *
     * @return String descrevendo as conexões.
     */
    @Override
    public String toString() {
        return "Maze Structure:\n" + map.toString();
    }
}