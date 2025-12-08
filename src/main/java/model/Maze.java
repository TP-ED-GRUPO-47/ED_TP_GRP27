package model;

import java.util.Iterator;

import structures.graph.NetworkList;

/**
 * Representa o labirinto (mapa) do jogo.
 * <p>
 * Esta classe encapsula a estrutura de dados em rede (NetworkList) onde
 * os vértices são as salas (Room) e as arestas são os corredores.
 * </p>
 *
 * @author Grupo 47
 * @version 2025/2026
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
     * Cria um corredor entre duas salas, com custo e evento aleatório opcional.
     *
     * @param fromId ID da sala de origem
     * @param toId   ID da sala de destino
     * @param cost   custo/peso do movimento
     * @param event  evento aleatório que ocorre ao atravessar (pode ser null)
     */
    public void addCorridor(String fromId, String toId, double cost, RandomEvent event) {
        Room from = findRoomById(fromId);
        Room to = findRoomById(toId);

        if (from == null || to == null) {
            System.err.println(
                    "ERRO: Não foi possível criar corredor " + fromId + " → " + toId + " (sala não encontrada)");
            return;
        }

        Corridor corridor = new Corridor(from, to, cost, event);
        map.addEdge(from, to, corridor);
    }

    /**
     * Obtém o corredor entre duas salas específicas.
     *
     * @param from sala de origem
     * @param to   sala de destino
     * @return o objeto Corridor ou null se não existir ligação direta
     */
    public Corridor getCorridorBetween(Room from, Room to) {
        return map.getCorridor(from, to);
    }

    /**
     * Procura uma sala pelo seu ID.
     *
     * @param id identificador da sala
     * @return a sala encontrada ou null
     */
    private Room findRoomById(String id) {
        if (id == null) return null;

        Iterator<Room> it = map.iteratorBFS();
        while (it.hasNext()) {
            Room room = it.next();
            if (id.equals(room.getId())) {
                return room;
            }
        }
        return null;
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

    /**
     * Procura no labirinto a sala definida como Entrada.
     * Utiliza o iterador do grafo para percorrer todas as salas.
     *
     * @return A sala de entrada, ou null se não existir.
     */
    public Room getEntrance() {
        if (map.isEmpty())
            return null;

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
        if (currentRoom == null)
            return "Nenhuma";

        Iterator<Room> it = getNeighbors(currentRoom);
        StringBuilder sb = new StringBuilder();

        if (!it.hasNext())
            return "Sem saídas (Beco sem saída)";

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