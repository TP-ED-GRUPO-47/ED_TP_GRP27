package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.InvalidMoveException;
import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;

/**
 * Unit tests for the {@link TurnManager} class.
 * <p>
 * Tests turn management, player movement, and user interaction.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class TurnManagerTest {

    private TurnManager turnManager;
    private Maze maze;
    private RoomEventHandler eventHandler;
    private EffectProcessor effectProcessor;
    private Player player;
    private Bot bot;
    private Scanner scanner;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        
        maze = new Maze();
        ArrayUnorderedList<Player> allPlayers = new ArrayUnorderedList<>();
        LinkedQueue<Player> turnQueue = new LinkedQueue<>();
        
        player = new Player("TestPlayer");
        bot = new Bot("TestBot");
        allPlayers.addToRear(player);
        allPlayers.addToRear(bot);
        
        Room entrance = new Entrance("E1", "Entrance");
        Room standard = new RoomStandard("S1", "Standard Room");
        Room treasure = new Center("C1", "Treasure");
        
        maze.addRoom(entrance);
        maze.addRoom(standard);
        maze.addRoom(treasure);
        
            maze.addCorridor("E1", "S1", 1.0, null);
            maze.addCorridor("S1", "C1", 1.0, null);
        
        player.setCurrentRoom(entrance);
        bot.setCurrentRoom(entrance);
        
        scanner = new Scanner(System.in);
        eventHandler = new RoomEventHandler(scanner, maze);
        effectProcessor = new EffectProcessor(scanner, maze, allPlayers, turnQueue);
        turnManager = new TurnManager(scanner, maze, eventHandler, effectProcessor);
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
    /**
     * Tests playing bot turn with valid move.
     */
    @Test
    void testPlayBotTurnWithMove() {
        assertDoesNotThrow(() -> turnManager.playBotTurn(bot));
    }

    @Test
    void testPlayHumanTurnChoosesExit() {
        Scanner choiceScanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        TurnManager manager = new TurnManager(choiceScanner, maze, eventHandler, effectProcessor);

        manager.playHumanTurn(player);

        assertEquals("S1", player.getCurrentRoom().getId());
    }

    /**
     * Tests movePlayer functionality directly.
     */
    @Test
    void testMovePlayer() {
        Room current = player.getCurrentRoom();
        assertNotNull(current);
        
        assertDoesNotThrow(() -> turnManager.movePlayer(player, "S1"));
    }

    @Test
    void testMovePlayerInvalidTargetThrows() {
        assertThrows(InvalidMoveException.class, () -> turnManager.movePlayer(player, "Z9"));
    }

    @Test
    void testMovePlayerBlockedByRiddle() {
        RiddleRoom riddle = new RiddleRoom("R1", "Enigma", null);
        maze.addRoom(riddle);
        maze.addCorridor("R1", "S1", 1.0, null);
        player.setCurrentRoom(riddle);

        turnManager.movePlayer(player, "S1");

        assertEquals("R1", player.getCurrentRoom().getId());
    }

    @Test
    void testGetRoomByIndexAndIsRoomIdAbsentViaReflection() throws Exception {
        ArrayUnorderedList<Room> rooms = new ArrayUnorderedList<>();
        Room r1 = maze.getRoomById("E1");
        Room r2 = maze.getRoomById("S1");
        rooms.addToRear(r1);
        rooms.addToRear(r2);

        Method getRoomByIndex = TurnManager.class.getDeclaredMethod("getRoomByIndex", ArrayUnorderedList.class, int.class);
        getRoomByIndex.setAccessible(true);
        Room result = (Room) getRoomByIndex.invoke(turnManager, rooms, 2);
        assertEquals(r2, result);

        Method isRoomIdAbsent = TurnManager.class.getDeclaredMethod("isRoomIdAbsent", ArrayUnorderedList.class, String.class);
        isRoomIdAbsent.setAccessible(true);
        boolean absent = (boolean) isRoomIdAbsent.invoke(turnManager, rooms, "E1");
        boolean absentNew = (boolean) isRoomIdAbsent.invoke(turnManager, rooms, "Z9");
        assertEquals(false, absent);
        assertEquals(true, absentNew);
    }

    @Test
    void testPrintStatusOutputsInfo() throws Exception {
        Method printStatus = TurnManager.class.getDeclaredMethod("printStatus", Player.class);
        printStatus.setAccessible(true);

        printStatus.invoke(turnManager, player);

        String output = outContent.toString();
        assertTrue(output.contains("Localiza"));
        assertTrue(output.contains(player.getCurrentRoom().getId()));
    }
}
