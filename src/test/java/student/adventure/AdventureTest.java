package student.adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;


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
    public void doSth() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.out.println("???");
        assertEquals("???!", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testHelp() {
        String input = "help";
        game.printHelp();
        String textExpected = "To see where you currently are, type examine.";
        String textActual   = outputStreamCaptor.toString().substring(0, 46).trim();
        assertEquals(textExpected, textActual);
    }

    @Test
    public void testInvalidCommand() {

    }

    @Test
    public void testHelpWhitespace() {
        String input = "   help         ";
        game.printHelp();
        String textExpected = "To see where you currently are, type examine.";
        String textActual   = outputStreamCaptor.toString().substring(0, 46).trim();
        assertEquals(textExpected, textActual);
    }

    @Test
    public void testHelpCapitalization() {
        String input = "hElp";
        game.printHelp();
        String textExpected = "To see where you currently are, type examine.";
        String textActual   = outputStreamCaptor.toString().substring(0, 46).trim();
        assertEquals(textExpected, textActual);
    }

    @Test
    public void basicIO() {
        InputStream sysIn = System.in; // backup System.in to restore it later
        ByteArrayInputStream in = new ByteArrayInputStream("help".getBytes());
        System.setIn(in);

        System.setOut(new PrintStream(outputStreamCaptor));
        Scanner inp = new Scanner(System.in);
        if (inp.next().trim().equalsIgnoreCase("help")) {
            System.out.println("received");
        }
        else {
            System.out.println("NOT received");
        }

        // passes
        assertEquals("received", outputStreamCaptor.toString().trim());

        System.setIn(sysIn);
    }

    @Test
    public void basicGame() throws FileNotFoundException {
        SpaceshipGame game = new SpaceshipGame("src/main/resources/spaceship.json");
        InputStream sysIn = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("quit".getBytes());
        System.setIn(in);
//        System.setOut(new PrintStream(outputStreamCaptor));
        game.play();
//        assertEquals("received", outputStreamCaptor.toString().trim());
    }

    @Test
    public void generalTest() throws FileNotFoundException {

        SpaceshipGame game = new SpaceshipGame("src/main/resources/spaceship.json");

        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream("go left".getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;
        System.setOut(ps);

        System.setIn(stdin);
        System.setOut(stdout);
        game.play();


        String outputText = byteArrayOutputStream.toString();
//        String key = "output:";
//        String output = outputText.substring(outputText.indexOf(key) + key.length()).trim();
        Assert.assertEquals(outputText, "7");
    }

//    @Before
//    public void setUp() throws FileNotFoundException {
//        // This is run before every test.
//        SpaceshipGame game = new SpaceshipGame("src/main/resources/spaceship.json");
//        System.setOut(new PrintStream(outputStreamCaptor));
//        game.play();
//    }

    @Test
    public void helpMessage() {
        ByteArrayInputStream in = new ByteArrayInputStream("help".getBytes());
        System.setIn(in);
//        assertEquals("sumthin", outputStreamCaptor.toString().trim());
    }

    @After
    public void restore() {
//        System.setOut(standardOut);
//        System.setIn(sysIn);
    }

}