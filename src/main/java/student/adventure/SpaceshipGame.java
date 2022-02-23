package student.adventure;

import com.google.gson.Gson;
import student.server.AdventureState;
import student.server.GameStatus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Game class to handle game input and functions
 */
public class SpaceshipGame {

    public SpaceshipBlueprint blueprint;
    private String currentRoom;
    private ArrayList<String> inventory;
    private ArrayList<String> roomHistory;
    private Scanner input;
    private AdventureState adventureState;
    private final String introMessage = "You are aboard the Rocinante, a speedy, slick space frigate!\n" +
                                        "You've finished your duties scouting the dark depths of our solar system for precious materials.\n" +
                                        "Now it is time for you to head back to Earth via transport pod.\n" +
                                        "You need to make your way to the Docking Room!\n";
    private final String helpMessage = "To see where you currently are, examine.\n" +
                                        "To go to another room, go [direction].\n" +
                                        "To pick up an item, take [item]. The room may not have an item.\n" +
                                        "To drop an item, drop [item].\n" +
                                        "To see your inventory, select inventory.\n" +
                                        "To see the history of rooms you've visited, select history.\n" +
                                        "To see this message again, select help.";
    private final String doneMessage = "================================================================\n" +
                                        "You've reached the docking room! Have a safe trip back to Earth!\n" +
                                        "================================================================";
    private final String[] directions = {"up", "left", "down", "right"};

    public SpaceshipGame(String fileName) throws FileNotFoundException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            Gson gson = new Gson();
            blueprint = gson.fromJson(reader, SpaceshipBlueprint.class);
        }
        catch (FileNotFoundException e) {
            throw new FileNotFoundException("Invalid file!");
        }
        currentRoom = blueprint.getStartRoom();
        inventory = new ArrayList<>();
        roomHistory = new ArrayList<>();
        roomHistory.add(currentRoom);
        input = new Scanner(System.in);
        adventureState = new AdventureState(currentRoom);
    }

    /**
     * Main driver method for game when played via console.
     */
    public void play() {
        System.out.println(getIntroMessage());
        System.out.println();
        System.out.println(getHelpMessage());

        System.out.print("> ");
        String inText = input.nextLine();
        inText = inText.trim();
        Command cmd = new Command(inText);
        CommandType action = cmd.action();

        while (action != CommandType.QUIT) {

            switch (action) {
                case HELP:
                    System.out.println(getHelpMessage());
                    break;
                case EXAMINE:
                    System.out.println(examine());
                    break;
                case GO:
                    String direction = inText.substring(3).trim();
                    System.out.println(go(direction));
                    break;
                case TAKE:
                    String itemWanted = inText.substring(5).trim();
                    System.out.println(take(itemWanted.trim()));
                    break;
                case DROP:
                    String itemToDrop = inText.substring(5).trim();
                    System.out.println(drop(itemToDrop.trim()));
                    break;
                case INVENTORY:
                    System.out.println(getInventoryAsString());
                    break;
                case HISTORY:
                    System.out.println(getHistoryAsString());
                    break;
                case INVALID:
                    System.out.println("Invalid command. Try again.");
                    break;
                default:
                    break;
            }

            if (reachedEndRoom()) {
                System.out.println(doneMessage);
                return;
            }

            System.out.print("> ");
            inText = input.nextLine();
            cmd.setCommand(inText.trim());
            action = cmd.action();
        }

    }

    /**
     * Get the current room.
     * Should only be used for testing purposes.
     * @return the current room's name
     */
    public String getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Get the game introduction message.
     * @return a String for the intro
     */
    public String getIntroMessage() {
        return introMessage;
    }

    /**
     * Get the game help message.
     * @return a String for the help message
     */
    public String getHelpMessage() {
        return helpMessage;
    }

    public AdventureState getAdventureState() {
        return adventureState;
    }

    /**
     * Move to room in direction.
     * If the direction is invalid or there is no room in that direction,
     * print error message and re prompt user.
     * @param direction a String representing a direction
     */
    public String go(String direction) {
        boolean isValidDirection = false;
        for (String dir : directions) {
            if (dir.trim().equalsIgnoreCase(direction.trim())) {
                isValidDirection = true;
                break;
            }
        }
        if (!isValidDirection) {
            return "Invalid direction. Try again.";
        }

        String message = "";
        for (LeaveDirection exitDir : blueprint.getRoom(currentRoom).getLeaveDirections()) {
            if (exitDir.getDirectionName().equalsIgnoreCase(direction.trim())) {
                this.currentRoom = exitDir.getRoomName();
                message += examine();
                roomHistory.add(currentRoom);
                return message;
            }
        }
        return "Invalid direction. Try again.";
    }

    /**
     * Get current room name, description, and items.
     */
    public String examine() {
        String message = "";
        message += "You are currently in " + currentRoom + ".\n";
        Room current = blueprint.getRoom(currentRoom);
        if (current == null) {
            // Game should never reach this state, but implemented for failsafe purposes
            return "Invalid room!\n";
        }
        message += current.getDescription() + "\n";
        message += "Items in this room: " + current.getItemsAsString() + "\n";
        message += current.getDirectionsToRooms();
        return message;
    }

    /**
     * Move an item into the user's inventory and remove it from the current room.
     * If there is no such item in the room, print an error message and do nothing.
     * @param itemWanted a String representing the desired item to take
     */
    public String take(String itemWanted) {
        Room current = blueprint.getRoom(currentRoom);
        if (!current.hasItem(itemWanted)) {
            return "There's no " + itemWanted + " in this room!";
        }
        current.takeItem(itemWanted);
        inventory.add(itemWanted);
        return "";
    }

    /**
     * Remove an item from the user's inventory and place it in the current room.
     * If there is no such item in the inventory, print an error message and do nothing.
     * @param itemToDrop a String representing the desired item to drop
     */
    public String drop(String itemToDrop) {
        Room current = blueprint.getRoom(currentRoom);
        if (!inventory.contains(itemToDrop)) {
            return "There's no " + itemToDrop + " in your inventory!";
        }
        current.placeItem(itemToDrop);
        inventory.remove(itemToDrop);
        return "";
    }

    /**
     * Print inventory.
     */
    public void printInventory() {
        System.out.println("Your inventory: " + inventory);
    }

    /**
     * Gets the player's current inventory.
     * @return a String showing the inventory
     */
    public String getInventoryAsString() {
        return "Your inventory: " + inventory.toString();
    }

    /**
     * Get the history of rooms the player has travelled to.
     * @return a String showing the list of rooms
     */
    public String getHistoryAsString() {
        return "Travel history: " + roomHistory.toString();
    }

    /**
     * Check if the player has reached the goal room.
     * @return true if the player has reached the goal room, false otherwise
     */
    public boolean reachedEndRoom() {
        return currentRoom.equalsIgnoreCase(blueprint.getEndRoom());
    }

    /**
     * Get the current room's image URL
     * @return the URL
     */
    public String getImageURL() {
        return blueprint.getRoom(currentRoom).getImageURL();
    }

    /**
     * Get the current room's video/sound URL
     * @return the URL
     */
    public String getVideoURL() {
        return blueprint.getRoom(currentRoom).getVideoURL();
    }

    /**
     * Process passed in command and value (verb + noun).
     * @param command a String representing the action to take, e.g. "go"
     * @param value a String representing the value of the action, e.g. "left"
     * @return the taken action as a String
     */
    public String processCommand(String command, String value) {
        // reusing code from play() and own Command class + enum
        Command cmd = new Command(command.trim());
        CommandType action = cmd.action();

        switch (action) {
            case HELP:
                return getHelpMessage();
            case EXAMINE:
                return examine();
            case GO:
                String direction = value.trim();
                return go(direction);
            case TAKE:
                String itemWanted = value.trim();
                return take(itemWanted);
            case DROP:
                String itemToDrop = value.trim();
                return drop(itemToDrop);
            case INVENTORY:
                return getInventoryAsString();
            case HISTORY:
                return getHistoryAsString();
            case INVALID:
            default:
                break;
        }
        return "Invalid command. Try again.";
    }

    /**
     * Gets a mapping of commands to possible arguments for those commands.
     * E.g. "go" -> ["up", "left", "down", "right"]
     *      "examine" -> []
     * @return a Map of commands to arguments
     */
    public Map<String, List<String>> getCommandOptions() {
        Map<String, List<String>> commandMap = new HashMap<>();
        // need a "placeholder" list so the buttons with no arguments will appear
        List<String> placeholderList = new ArrayList<>(Collections.singletonList(""));
        commandMap.put("go", Arrays.asList(directions));
        commandMap.put("help", placeholderList);
        commandMap.put("examine", placeholderList);
        commandMap.put("inventory", placeholderList);
        commandMap.put("history", placeholderList);
        commandMap.put("take", blueprint.getRoom(currentRoom).getItems());
        commandMap.put("drop", inventory);
        return commandMap;
    }

    /**
     * Execute a command for the API.
     * @param status the current GameStatus
     * @param cmd a String representing the server command.
     * @return the updated GameStatus
     */
    public GameStatus executeCommand(GameStatus status, student.server.Command cmd) {
        String message = processCommand(cmd.getCommandName(), cmd.getCommandValue());
        adventureState.setCurrentRoom(currentRoom);
        if (reachedEndRoom())
            message += "\n" + doneMessage;
        return new GameStatus(false, status.getId(), message, getImageURL(), getVideoURL(),
                adventureState, getCommandOptions());
    }

    /**
     * For testing purposes.
     * <b>Not to be used by player.</b>
     * Same code as in play() verbatim, except without while loop.
     */
    public void testCommand(String input) {
        input = input.trim();
        Command cmd = new Command(input);
        CommandType action = cmd.action();

        switch (action) {
            case HELP:
                System.out.println(getHelpMessage());
                break;
            case EXAMINE:
                examine();
                break;
            case GO:
                String direction = input.substring(3).trim();
                go(direction);
                break;
            case TAKE:
                String itemWanted = input.substring(5).trim();
                take(itemWanted);
                break;
            case DROP:
                String itemToDrop = input.substring(5).trim();
                drop(itemToDrop);
                break;
            case INVENTORY:
                printInventory();
                break;
            case HISTORY:
                System.out.println(roomHistory);
                break;
            case INVALID:
                System.out.println("Invalid command. Try again.");
                break;
            default:
                break;
        }

    }
}
