import org.glassfish.grizzly.http.server.HttpServer;
import student.adventure.SpaceshipGame;
import student.server.AdventureResource;
import student.server.AdventureServer;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 1 && args[0].trim().equalsIgnoreCase("console")) {
            try {
                SpaceshipGame game = new SpaceshipGame("src/main/resources/spaceship.json");
                game.play();
            }
            catch (Exception e) {
                throw new Exception("Error in game via console.");
            }
        }
        else {
            try {
                HttpServer server = AdventureServer.createServer(AdventureResource.class);
                server.start();
            }
            catch (Exception e) {
                throw new Exception("Error in game via server API.");
            }
        }
    }
}
