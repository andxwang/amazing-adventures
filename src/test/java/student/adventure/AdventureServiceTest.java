package student.adventure;

import org.junit.Before;
import org.junit.Test;
import student.server.AdventureState;
import student.server.Command;
import student.server.GameStatus;
import student.server.SpaceshipAdventureService;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class AdventureServiceTest {

    private SpaceshipGame game;
    private SpaceshipAdventureService advService;

    @Before
    public void setUpGame() throws Exception {
        try {
            game = new SpaceshipGame("src/main/resources/spaceship.json");
        }
        catch (Exception e) {
            throw new FileNotFoundException("Invalid file path!");
        }
        advService = new SpaceshipAdventureService();
        advService.newGame();
    }

    @Test
    public void testReset() {
        advService.reset();
        assertEquals(new HashMap<>(), advService.getGameList());
        assertEquals(new HashMap<>(), advService.getEngineList());
    }

    @Test
    public void testNewGame() {
        assertEquals(1, advService.getGameList().size());
        assertEquals(1, advService.getEngineList().size());
    }

    @Test
    public void testGetGame() {
        GameStatus testStatus = advService.getGame(0);
        assertFalse(testStatus.isError());
        assertEquals(0, testStatus.getId());
        assertEquals("FlightDeck", testStatus.getState().getCurrentRoom());
    }

    @Test
    public void testDestroyGame() throws Exception {
        advService.newGame();
        advService.destroyGame(0);
        // game id 1 should still be in the map
        assertTrue(advService.getGameList().containsKey(1));
        assertTrue(advService.getEngineList().containsKey(1));
    }

    @Test
    public void testExecuteCommand() {
        advService.executeCommand(0, new Command("go", "down"));
        GameStatus testStatus = advService.getGame(0);
        assertEquals(0, testStatus.getId());
        assertEquals("Operations", testStatus.getState().getCurrentRoom());
    }
}
