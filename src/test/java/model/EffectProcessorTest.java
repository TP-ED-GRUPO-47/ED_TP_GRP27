package model;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.GameOverException;
import structures.linear.ArrayUnorderedList;
import structures.queue.LinkedQueue;

/**
 * Unit tests for the {@link EffectProcessor} class.
 * <p>
 * Tests effect processing including damage, healing, swaps, and other game effects.
 * </p>
 *
 * @author Group 27
 * @version 2025/2026
 */
class EffectProcessorTest {

    private EffectProcessor processor;
    private Maze maze;
    private ArrayUnorderedList<Player> allPlayers;
    private LinkedQueue<Player> turnQueue;
    private Player player;
    private Scanner scanner;
    private Player other;
    private Player third;

    @BeforeEach
    void setUp() {
        maze = new Maze();
        allPlayers = new ArrayUnorderedList<>();
        turnQueue = new LinkedQueue<>();
        
        player = new Player("TestPlayer");
        other = new Player("Ally");
        third = new Player("Third");
        allPlayers.addToRear(player);
        allPlayers.addToRear(other);
        allPlayers.addToRear(third);
        
        Room entrance = new Entrance("E1", "Entrance");
        Room standard = new RoomStandard("S1", "Standard Room");
        Room treasure = new Center("C1", "Treasure");
        maze.addRoom(entrance);
        maze.addRoom(standard);
        maze.addRoom(treasure);
        player.setCurrentRoom(entrance);
        other.setCurrentRoom(standard);
        third.setCurrentRoom(treasure);
        
        scanner = new Scanner(System.in);
        processor = new EffectProcessor(scanner, maze, allPlayers, turnQueue);
    }

    /**
     * Tests applying healing effect to a player.
     */
    @Test
    void testApplyHealEffect() {
        player.updatePower(-30);
        assertEquals(70, player.getPower());
        
        RandomEvent healEvent = new RandomEvent("Healing fountain", Effect.HEAL);
        processor.applyEffect(player, healEvent);
        
        assertEquals(90, player.getPower());
    }

    /**
     * Tests applying damage effect to a player.
     */
    @Test
    void testApplyDamageEffect() {
        RandomEvent damageEvent = new RandomEvent("Trap", Effect.DAMAGE);
        processor.applyEffect(player, damageEvent);
        
        assertEquals(75, player.getPower());
    }

    /**
     * Tests that fatal damage throws GameOverException.
     */
    @Test
    void testFatalDamage() {
        player.updatePower(-90);
        
        RandomEvent trapEvent = new RandomEvent("Deadly trap", Effect.TRAP);
        
        assertThrows(GameOverException.class, () -> {
            processor.applyEffect(player, trapEvent);
        });
    }

    /**
     * Tests applying null event does nothing.
     */
    @Test
    void testApplyNullEvent() {
           long initialPower = player.getPower();
        processor.applyEffect(player, null);
        assertEquals(initialPower, player.getPower());
    }

    /**
     * Tests applying event with null effect.
     */
    @Test
    void testApplyEventWithNullEffect() {
           long initialPower = player.getPower();
        RandomEvent nullEffectEvent = new RandomEvent("Empty event", null);
        processor.applyEffect(player, nullEffectEvent);
        assertEquals(initialPower, player.getPower());
    }

    /**
     * Tests setting running state.
     */
    @Test
    void testSetRunning() {
        processor.setRunning(false);
        assertDoesNotThrow(() -> processor.setRunning(true));
    }

    /**
     * Tests applying bonus power effect.
     */
    @Test
    void testApplyBonusPowerEffect() {
        RandomEvent bonusEvent = new RandomEvent("Power boost", Effect.BONUS_POWER);
        processor.applyEffect(player, bonusEvent);
        
        assertEquals(115, player.getPower());
    }

    /**
     * Tests applying trap effect.
     */
    @Test
    void testApplyTrapEffect() {
        RandomEvent trapEvent = new RandomEvent("Spike trap", Effect.TRAP);
        processor.applyEffect(player, trapEvent);
        
        assertEquals(70, player.getPower());
    }

    /**
     * Tests skip turn effect recording.
     */
    @Test
    void testSkipTurnEffect() {
        RandomEvent skipEvent = new RandomEvent("Stun", Effect.SKIP_TURN);
        assertDoesNotThrow(() -> processor.applyEffect(player, skipEvent));
    }

    @Test
    void testPerformSwapHumanChoice() {
        Scanner choiceScanner = new Scanner(new ByteArrayInputStream("1\n".getBytes()));
        EffectProcessor swapProcessor = new EffectProcessor(choiceScanner, maze, allPlayers, turnQueue);

        Room initialPlayerRoom = player.getCurrentRoom();
        Room initialOtherRoom = other.getCurrentRoom();

        swapProcessor.performSwap(player);

        assertEquals(initialOtherRoom, player.getCurrentRoom());
        assertEquals(initialPlayerRoom, other.getCurrentRoom());
    }

    @Test
    void testPerformSwapAllRotatesPositions() {
        Room r1 = player.getCurrentRoom();
        Room r2 = other.getCurrentRoom();
        Room r3 = third.getCurrentRoom();

        processor.performSwapAll();

        assertEquals(r2, player.getCurrentRoom());
        assertEquals(r3, other.getCurrentRoom());
        assertEquals(r1, third.getCurrentRoom());
    }

    @Test
    void testPerformRecedeMovesBackwards() {
        Player p = player;
        p.setCurrentRoom(maze.getRoomById("S1"));
        p.setCurrentRoom(maze.getRoomById("C1"));

        processor.performRecede(p, 2);

        assertEquals("E1", p.getCurrentRoom().getId());
    }

    @Test
    void testApplyEffectExtraTurnQueuesPlayer() {
        RandomEvent extraTurn = new RandomEvent("Extra", Effect.EXTRA_TURN);
        processor.applyEffect(player, extraTurn);
        assertEquals(1, turnQueue.size());
    }

    @Test
    void testApplyEffectSwapAllUsesRotation() {
        RandomEvent swapAll = new RandomEvent("Rotate", Effect.SWAP_ALL);
        processor.applyEffect(player, swapAll);
        assertEquals("S1", player.getCurrentRoom().getId());
    }

    @Test
    void testApplyEffectRecedeBacktracks() {
        player.setCurrentRoom(maze.getRoomById("S1"));
        player.setCurrentRoom(maze.getRoomById("C1"));
        RandomEvent recede = new RandomEvent("Recede", Effect.RECEDE);
        processor.applyEffect(player, recede);
        assertEquals("E1", player.getCurrentRoom().getId());
    }
}
