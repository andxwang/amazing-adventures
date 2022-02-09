import student.adventure.SpaceshipGame;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SpaceshipGame game = new SpaceshipGame("src/main/resources/spaceship.json");
        game.play();
    }
}
