package student.adventure;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class EngineGameplayTest {

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

    @Test(expected = FileNotFoundException.class)
    public void testFilePath() throws FileNotFoundException {
        SpaceshipGame game = new SpaceshipGame("/qwerty");
    }

    @Test
    public void testGo() {
        assertEquals("FlightDeck", game.getCurrentRoom());
        game.go("down");
        assertEquals("Operations", game.getCurrentRoom());
        game.go("left");
        assertEquals("Quarters", game.getCurrentRoom());
    }

    @Test
    public void testGoWithFormat() {
        assertEquals("FlightDeck", game.getCurrentRoom());
        game.go("    down       ");
        assertEquals("Operations", game.getCurrentRoom());
        game.go("LeFt");
        assertEquals("Quarters", game.getCurrentRoom());
        game.go("downn"); // invalid direction
        assertEquals("Quarters", game.getCurrentRoom());
    }

    @Test
    public void testTake() {
        assertEquals("There's no nonexistent item?! in this room!",
                game.take("nonexistent item?!"));
        game.take("master key");
        assertEquals("Your inventory: [master key]", game.getInventoryAsString());
    }

    @Test
    public void testDrop() {
        assertEquals("There's no xyz in your inventory!", game.drop("xyz"));
        game.take("master key");
        game.go("down");
        game.drop("master key");
        assertEquals("Your inventory: []", game.getInventoryAsString());
    }

    @Test
    public void testGetImageURL() {
        // Line length >= 100 can't be helped because this is a link
        assertEquals("https://images.squarespace-cdn.com/content/v1/5457c038e4b0c813a205dbe2/1495550975513-04KIC1VZW4ZEH0MQ3LM8/EX2_Roci-Ops7.jpg?format=1000w",
                game.getImageURL());
        // move to Operations room
        game.go("down");
        assertEquals("https://images.squarespace-cdn.com/content/v1/5457c038e4b0c813a205dbe2/1495493196497-RWHY7ZAACO4PYZ2PZWD6/EX2_Arbogast1.jpg?format=1000w",
                game.getImageURL());
    }

    @Test
    public void testGetVideoURL() {
        assertEquals("https://youtu.be/YLMQnoO5Uik", game.getVideoURL());
        // move to Operations room
        game.go("down");
        assertEquals("https://youtu.be/NuFj97KhHFE", game.getVideoURL());
    }
}
