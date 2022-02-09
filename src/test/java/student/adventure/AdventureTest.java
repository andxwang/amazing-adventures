package student.adventure;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;


public class AdventureTest {

    /*
    References:
    https://www.baeldung.com/java-testing-system-out-println
    https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
     */

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private SpaceshipGame game;

    @Before
    public void setUp() throws FileNotFoundException {
        System.setOut(new PrintStream(outputStreamCaptor));
        game = new SpaceshipGame("src/main/resources/spaceship.json");
    }

    @Test(expected = FileNotFoundException.class)
    public void badFilePath() throws FileNotFoundException {
        game = new SpaceshipGame("/nonexistent");
    }

    @Test
    public void testInvalidCommand() {
        game.testCommand("qwerty");
        String expected = "Invalid command. Try again.";
        String actual1  = outputStreamCaptor.toString().trim();
        assertEquals(expected, actual1);

        outputStreamCaptor.reset();
        game.testCommand("");
        String actual2  = outputStreamCaptor.toString().trim();
        assertEquals(expected, actual2);
    }

    @Test
    public void testHelp() {
        game.testCommand("help");
        String expected = "To see where you currently are, type examine.";
        String actual   = outputStreamCaptor.toString().substring(0, 46).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testExtraWhitespace() {
        game.testCommand("   help         ");
        String expected = "To see where you currently are, type examine.";
        String actual   = outputStreamCaptor.toString().substring(0, 46).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testCapitalization() {
        game.testCommand("hElp");
        String expected = "To see where you currently are, type examine.";
        String actual   = outputStreamCaptor.toString().substring(0, 46).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testExamine() {
        // start in FlightDeck
        game.testCommand("examine");
        String expected = "You are currently in FlightDeck.";
        String actual   = outputStreamCaptor.toString().substring(0, 32).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testGoInvalidDir() {
        game.testCommand("go zzz");
        String expected = "Invalid direction. Try again.";
        String actual   = outputStreamCaptor.toString().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testGoWall() {
        game.testCommand("go down");
        game.testCommand("go right");
        outputStreamCaptor.reset();
        game.testCommand("go up"); // can't go up from Barracks
        String expected = "Invalid direction. Try again.";
        String actual   = outputStreamCaptor.toString().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testGoDown() {
        // goes to Operations
        game.testCommand("go DOWN");
        String expected = "You are currently in Operations.";
        String actual   = outputStreamCaptor.toString().substring(0, 33).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testTakeItem() {
        game.testCommand("take master key");
        game.testCommand("inventory");
        String expected = "Your inventory: [master key]";
        String actual   = outputStreamCaptor.toString().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void testDropItem() {
        game.testCommand("take master key");
        game.testCommand("go down");
        outputStreamCaptor.reset();
        game.testCommand("drop master key");
        game.testCommand("inventory");
        String expInventory = "Your inventory: []";
        String actualInventory = outputStreamCaptor.toString().trim();
        assertEquals(expInventory, actualInventory);

        outputStreamCaptor.reset();
        game.testCommand("examine");
        String expRoomItems = "Items in this room: [Martian pen, holograph handheld, master key]";
        String actualRoomItems = outputStreamCaptor.toString().substring(144, 144+65+1).trim();
        assertEquals(expRoomItems, actualRoomItems);
    }

    @Test
    public void testQuit() {
        game.testCommand("QUIT");
        assertEquals("", outputStreamCaptor.toString().trim());
    }

    @After
    public void clearOut() {
        System.setOut(standardOut);
    }
}