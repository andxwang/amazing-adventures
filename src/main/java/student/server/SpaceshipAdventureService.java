package student.server;

import student.adventure.SpaceshipGame;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class SpaceshipAdventureService implements AdventureService {

    private Map<Integer, GameStatus> gameList;
    private Map<Integer, SpaceshipGame> engineList;
    private int numGames; // tracks the game ID
    private final String jsonFile = "src/main/resources/spaceship.json";

    public SpaceshipAdventureService() throws FileNotFoundException {
        gameList = new HashMap<>();
        engineList = new HashMap<>();
    }

    /**
     * Resets the service to its initial state.
     */
    @Override
    public void reset() {
        numGames = 0;
        gameList.clear();
        engineList.clear();
    }

    /**
     * Creates a new Adventure game and stores it.
     *
     * @return the id of the game.
     */
    @Override
    public int newGame() throws AdventureException {
        try {
            SpaceshipGame gameEngine = new SpaceshipGame(jsonFile);
            GameStatus gameStatus = new GameStatus(false, numGames,
                    gameEngine.getIntroMessage() + "\n" + gameEngine.getHelpMessage(),
                    gameEngine.getImageURL(), gameEngine.getVideoURL(),
                    gameEngine.getAdventureState(), gameEngine.getCommandOptions());
            gameList.put(numGames, gameStatus);
            engineList.put(numGames, gameEngine);
        }
        catch (Exception e) {
            throw new AdventureException("Exception thrown while creating new game!");
        }
        return numGames++;
    }

    /**
     * Returns the state of the game instance associated with the given ID.
     *
     * @param id the instance id
     * @return the current state of the game
     */
    @Override
    public GameStatus getGame(int id) {
        return gameList.get(id);
    }

    /**
     * Removes & destroys a game instance with the given ID.
     *
     * @param id the instance id
     * @return false if the instance could not be found and/or was not deleted
     */
    @Override
    public boolean destroyGame(int id) {
        if (gameList.containsKey(id)) {
            gameList.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Executes a command on the game instance with the given id, changing the game state if applicable.
     *
     * @param id      the instance id
     * @param command the issued command
     */
    @Override
    public void executeCommand(int id, Command command) {
        // check if the game id is valid
        if (gameList.containsKey(id) && engineList.containsKey(id)) {
            SpaceshipGame thisGameEngine = engineList.get(id);
            gameList.replace(id, thisGameEngine.executeCommand(gameList.get(id), command));
        }
    }

    /**
     * Returns a sorted leaderboard of player "high" scores.
     *
     * @return a sorted map of player names to scores
     */
    @Override
    public SortedMap<String, Integer> fetchLeaderboard() {
        return null;
    }

    public Map<Integer, GameStatus> getGameList() {
        return gameList;
    }

    public Map<Integer, SpaceshipGame> getEngineList() {
        return engineList;
    }

    public int getNumGames() {
        return numGames;
    }
}
