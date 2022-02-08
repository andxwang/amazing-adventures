import student.adventure.SpaceshipGame;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // Wishing you good luck on your Adventure!
        // use scanner
        SpaceshipGame game = new SpaceshipGame("src/main/resources/spaceship.json");

        game.play();

    }
}
