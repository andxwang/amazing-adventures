package student.adventure;

import org.junit.Before;
import org.junit.Test;
import student.server.AdventureState;
import student.server.GameStatus;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EngineInteractionTest {

    private SpaceshipGame game;

    @Before
    public void setUpGame() throws FileNotFoundException {
        try {
            game = new SpaceshipGame("src/main/resources/spaceship.json");
        }
        catch (Exception e) {
            throw new FileNotFoundException("Invalid file path!");
        }
    }

    @Test
    public void testCommandOptions() {
        List<String> singletonList = new ArrayList<>();
        singletonList.add("");
        assertEquals(Arrays.asList("up", "left", "down", "right"), game.getCommandOptions().get("go"));
        assertEquals(singletonList, game.getCommandOptions().get("help"));
        assertEquals(singletonList, game.getCommandOptions().get("examine"));
        assertEquals(singletonList, game.getCommandOptions().get("inventory"));
        assertEquals(singletonList, game.getCommandOptions().get("history"));
        assertEquals(Arrays.asList("master key"), game.getCommandOptions().get("take"));
        assertTrue(game.getCommandOptions().get("drop").isEmpty());
    }

    @Test
    public void testProcessCommand() throws FileNotFoundException {
        // All LHS expected strings are correct, as determined by tests in EngineGameplayTest
        assertEquals(game.getHelpMessage(), game.processCommand("help", "anything"));
        assertEquals(game.examine(), game.processCommand(" exAmiNe  ", "any"));
        SpaceshipGame game2 = new SpaceshipGame("src/main/resources/spaceship.json");
        game2.go("down");
        assertEquals(game2.examine(), game.processCommand("go", "DOWN"));
        assertEquals(game.take("none"), game.processCommand("take", "none"));
        assertEquals(game.take("master key"), game.processCommand("take", "master key"));
        assertEquals(game.drop("master key"), game.processCommand("drop", "master key"));
        assertEquals(game.getInventoryAsString(), game.processCommand("inventory", ""));
        assertEquals(game.getHistoryAsString(), game.processCommand("hisTorY", ""));
        assertEquals("Invalid command. Try again.", game.processCommand("qwe", "rty"));
        assertEquals("Invalid command. Try again.", game.processCommand("goo", "left"));
    }

    @Test
    public void testExecuteCommand1() {
        // test going down from start room to Operations
        GameStatus parameterStatus = new GameStatus(false, 0, game.examine(),
                game.getImageURL(), game.getVideoURL(),
                new AdventureState("FlightDeck"), game.getCommandOptions());
        GameStatus resultStatus = game.executeCommand(parameterStatus, new student.server.Command("go", "down"));

        assertFalse(resultStatus.isError());
        assertEquals(game.examine(), resultStatus.getMessage());
        assertEquals(0, resultStatus.getId());
        assertEquals(game.getImageURL(), resultStatus.getImageUrl());
        assertEquals(game.getVideoURL(), resultStatus.getVideoUrl());
        assertEquals(new AdventureState("Operations").getCurrentRoom(), resultStatus.getState().getCurrentRoom());
        assertEquals(game.getCommandOptions(), resultStatus.getCommandOptions());
    }

    @Test
    public void testExecuteCommand2() {
        // test going right from Dining to MedBay
        game.go("down");
        game.go("down");
        game.go("down");
        // now the player is in Dining
        GameStatus parameterStatus = new GameStatus(false, 0, game.examine(),
                game.getImageURL(), game.getVideoURL(),
                new AdventureState("Dining"), game.getCommandOptions());
        GameStatus resultStatus = game.executeCommand(parameterStatus, new student.server.Command("go", "right"));

        assertFalse(resultStatus.isError());
        assertEquals(game.examine(), resultStatus.getMessage());
        assertEquals(0, resultStatus.getId());
        assertEquals(game.getImageURL(), resultStatus.getImageUrl());
        assertEquals(game.getVideoURL(), resultStatus.getVideoUrl());
        assertEquals(new AdventureState("MedBay").getCurrentRoom(), resultStatus.getState().getCurrentRoom());
        assertEquals(game.getCommandOptions(), resultStatus.getCommandOptions());
    }
}
