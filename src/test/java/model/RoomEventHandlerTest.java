package model;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import structures.linear.ArrayUnorderedList;

/**
 * Unit tests for the {@link RoomEventHandler} class.
 * <p>
 * Tests handling of riddles, levers, and other room-specific events.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class RoomEventHandlerTest {

    private RoomEventHandler handler;
    private Maze maze;
    private Scanner scanner;
    private Player player;
    private Bot bot;

    @BeforeEach
    void setUp() {
        maze = new Maze();
        player = new Player("TestPlayer");
        bot = new Bot("TestBot");
        
        Room entrance = new Entrance("E1", "Entrance");
        maze.addRoom(entrance);
        player.setCurrentRoom(entrance);
    }

    /**
     * Tests handling riddle with correct answer.
     */
    @Test
    void testHandleRiddleCorrectAnswer() {
        String input = "2\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        handler = new RoomEventHandler(scanner, maze);
        
        ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
        options.addToRear("Wrong");
        options.addToRear("Correct");
        options.addToRear("Wrong2");
        
        Riddle riddle = new Riddle("What is 1+1?", options, 1);
        RiddleRoom room = new RiddleRoom("R1", "Riddle Room", riddle);
        
        assertFalse(room.isSolved());
        handler.handleRiddle(room, player);
        assertTrue(room.isSolved());
    }

    /**
     * Tests handling riddle with wrong answer.
     */
    @Test
    void testHandleRiddleWrongAnswer() {
        String input = "1\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        handler = new RoomEventHandler(scanner, maze);
        
        ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
        options.addToRear("Wrong");
        options.addToRear("Correct");
        
        Riddle riddle = new Riddle("Test?", options, 1);
        RiddleRoom room = new RiddleRoom("R1", "Riddle Room", riddle);
        
        handler.handleRiddle(room, player);
        assertFalse(room.isSolved());
    }

    /**
     * Tests handling already solved riddle.
     */
    @Test
    void testHandleAlreadySolvedRiddle() {
        scanner = new Scanner(System.in);
        handler = new RoomEventHandler(scanner, maze);
        
        ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
        options.addToRear("Option");
        
        Riddle riddle = new Riddle("Test?", options, 0);
        RiddleRoom room = new RiddleRoom("R1", "Riddle Room", riddle);
        room.setSolved(true);
        
        assertDoesNotThrow(() -> handler.handleRiddle(room, player));
        assertTrue(room.isSolved());
    }

    /**
     * Tests bot handling riddle.
     */
    @Test
    void testHandleBotRiddle() {
        scanner = new Scanner(System.in);
        handler = new RoomEventHandler(scanner, maze);
        
        ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
        options.addToRear("A");
        options.addToRear("B");
        
        Riddle riddle = new Riddle("Bot test?", options, 0);
        RiddleRoom room = new RiddleRoom("R1", "Bot Riddle", riddle);
        
        assertDoesNotThrow(() -> handler.handleBotRiddle(room, bot));
    }

    /**
     * Tests handling lever interaction with correct choice.
     */
    @Test
    void testHandleLever() {
        scanner = new Scanner(System.in);
        handler = new RoomEventHandler(scanner, maze);
        
        LeverRoom leverRoom = new LeverRoom("L1", "Lever Room");
        
        assertDoesNotThrow(() -> handler.handleLever(player, leverRoom));
    }

    /**
     * Tests handling riddle with non-integer input.
     */
    @Test
    void testHandleRiddleInvalidInput() {
        String input = "invalid\n";
        scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        handler = new RoomEventHandler(scanner, maze);
        
        ArrayUnorderedList<String> options = new ArrayUnorderedList<>();
        options.addToRear("Option1");
        
        Riddle riddle = new Riddle("Test?", options, 0);
        RiddleRoom room = new RiddleRoom("R1", "Riddle", riddle);
        
        assertDoesNotThrow(() -> handler.handleRiddle(room, player));
        assertFalse(room.isSolved());
    }
}
