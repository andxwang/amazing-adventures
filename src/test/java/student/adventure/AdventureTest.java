package student.adventure;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintStream;


public class AdventureTest {

    @Before
    public void setUp() throws FileNotFoundException {
        // This is run before every test.
        SpaceshipGame game = new SpaceshipGame("src/main/resources/spaceship.json");
    }


}