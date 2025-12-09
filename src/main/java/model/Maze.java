package model;

import java.util.Iterator;
import structures.graph.NetworkList;
import structures.linear.ArrayUnorderedList;

/**
 * Represents the game maze (map).
 * <p>
 * This class encapsulates the network data structure (NetworkList) where
 * the vertices are the rooms ({@link Room}) and the edges are the corridors ({@link Corridor}).
 * </p>
 * <p>
 * It maintains auxiliary lists to manage rooms and corridors efficiently,
 * allowing for features like Random Events and Map Loading without graph traversal dependencies.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
public class Maze {

    /**
     * The weighted graph that stores rooms as vertices and travel costs as edge weights.
     */
    private NetworkList<Room> map;

    /**
     * Auxiliary list to store all Corridor objects.
     * Essential for retrieving Random Events associated with connections.
     */
    private ArrayUnorderedList<Corridor> allCorridors;

    /**
     * Auxiliary list to store ALL rooms.
     * Essential for the MapLoader to find rooms by ID before the graph connections are established.
     */
    private ArrayUnorderedList<Room> allRooms;

    /**
     * Constructs a new, empty Maze.
     * Initializes the graph and auxiliary lists.
     */
    public Maze() {
        this.map = new NetworkList<>();
        this.allCorridors = new ArrayUnorderedList<>();
        this.allRooms = new ArrayUnorderedList<>();
    }

    /**
     * Adds a room to the maze.
     * <p>
     * The room is added to both the graph structure and the auxiliary list.
     * </p>
     *
     * @param room The room to add.
     */
    public void addRoom(Room room) {
        if (room != null) {
            this.map.addVertex(room);
            this.allRooms.addToRear(room); // Add to auxiliary list for lookup
        }
    }

    /**
     * Creates a corridor between two rooms with a specific cost and an optional random event.
     *
     * @param fromId ID of the source room.
     * @param toId   ID of the destination room.
     * @param cost   Movement cost/weight.
     * @param event  Random event associated with the corridor (can be null).
     */
    public void addCorridor(String fromId, String toId, double cost, RandomEvent event) {
        Room from = findRoomById(fromId);
        Room to = findRoomById(toId);

        if (from == null || to == null) {
            System.err.println(
                    "ERRO: Sala não encontrada para corredor " + fromId + " -> " + toId);
            return;
        }

        Corridor corridor = new Corridor(from, to, cost, event);

        map.addEdge(from, to, cost);

        allCorridors.addToRear(corridor);
    }

    /**
     * Overloaded method to add a corridor without an event.
     *
     * @param fromId ID of the source room.
     * @param toId   ID of the destination room.
     * @param cost   Movement cost.
     */
    public void addCorridor(String fromId, String toId, double cost) {
        addCorridor(fromId, toId, cost, null);
    }

    /**
     * Retrieves the Corridor object connecting two rooms.
     * <p>
     * This is used to check for events when a player moves between rooms.
     * </p>
     *
     * @param from The source room.
     * @param to   The destination room.
     * @return The Corridor object, or null if no direct connection exists.
     */
    public Corridor getCorridorBetween(Room from, Room to) {
        Iterator<Corridor> it = allCorridors.iterator();
        while (it.hasNext()) {
            Corridor c = it.next();
            boolean direct = c.getSource().equals(from) && c.getTarget().equals(to);
            boolean reverse = c.getSource().equals(to) && c.getTarget().equals(from);

            if (direct || reverse) {
                return c;
            }
        }
        return null;
    }

    /**
     * Finds a room by its unique ID using the auxiliary list.
     * <p>
     * This is robust because it does not rely on graph traversal (BFS),
     * preventing errors during the map loading phase.
     * </p>
     *
     * @param id The identifier of the room.
     * @return The Room object, or null if not found.
     */
    private Room findRoomById(String id) {
        if (id == null) return null;

        Iterator<Room> it = allRooms.iterator();
        while (it.hasNext()) {
            Room room = it.next();
            if (id.equalsIgnoreCase(room.getId())) {
                return room;
            }
        }
        return null;
    }

    /**
     * Creates a secret passage from the given room to a random room in the maze.
     * <p>
     * Used by the LeverRoom functionality. It uses the custom ArrayUnorderedList
     * to pick a random target that is not the current room.
     * </p>
     *
     * @param fromRoom The room where the passage starts.
     * @return The ID of the target room, or null if creation fails.
     */
    public String createSecretPassage(Room fromRoom) {
        if (map.size() < 2) return null;

        ArrayUnorderedList<Room> candidates = new ArrayUnorderedList<>();
        Iterator<Room> it = allRooms.iterator();

        while (it.hasNext()) {
            Room r = it.next();
            if (!r.getId().equals(fromRoom.getId())) {
                candidates.addToRear(r);
            }
        }

        if (candidates.isEmpty()) return null;

        java.util.Random rnd = new java.util.Random();
        int randomIndex = rnd.nextInt(candidates.size());

        Iterator<Room> candidateIt = candidates.iterator();
        Room target = null;
        for (int i = 0; i <= randomIndex; i++) {
            if (candidateIt.hasNext()) target = candidateIt.next();
        }

        if (target != null) {
            addCorridor(fromRoom.getId(), target.getId(), 0.5);
            return target.getId();
        }
        return null;
    }

    /**
     * Gets an iterator representing the shortest path between two rooms.
     *
     * @param start The starting room.
     * @param end   The destination room.
     * @return An iterator of rooms comprising the path.
     */
    public Iterator<Room> getShortestPath(Room start, Room end) {
        return map.iteratorShortestPath(start, end);
    }

    /**
     * Gets the neighboring rooms of a specific room.
     *
     * @param currentRoom The room to check.
     * @return An iterator of adjacent rooms.
     */
    public Iterator<Room> getNeighbors(Room currentRoom) {
        return map.getNeighbors(currentRoom);
    }

    /**
     * Finds the Entrance room of the maze.
     *
     * @return The Entrance room, or null if none exists.
     */
    public Room getEntrance() {
        if (map.isEmpty()) return null;

        Iterator<Room> it = allRooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r instanceof Entrance) {
                return r;
            }
        }
        return null;
    }

    /**
     * Finds the Treasure room (Center) of the maze.
     * Useful for Bots to determine their target.
     *
     * @return The Center room, or null if none exists.
     */
    public Room getTreasureRoom() {
        if (map.isEmpty()) return null;

        Iterator<Room> it = allRooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r instanceof Center) {
                return r;
            }
        }
        return null;
    }

    /**
     * Returns a formatted string of available exits for the console UI.
     *
     * @param currentRoom The room to check exits for.
     * @return A string listing neighbor IDs (in Portuguese).
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
     * Returns the string representation of the maze structure.
     *
     * @return String describing the graph.
     */
    @Override
    public String toString() {
        return "Maze Structure:\n" + map.toString();
    }
}