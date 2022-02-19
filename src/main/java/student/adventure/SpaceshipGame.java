package student.adventure;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Game class to handle game input and functions
 */
public class SpaceshipGame {

    public SpaceshipBlueprint blueprint;
    private String currentRoom;
    private ArrayList<String> inventory;
    private ArrayList<String> roomHistory;
    private Scanner input;
    private final String introMessage = "You are aboard the Rocinante, a speedy, slick space frigate!\n" +
                                        "You've finished your duties scouting the dark depths of our solar system for precious materials.\n" +
                                        "Now it is time for you to head back to Earth via transport pod.\n" +
                                        "You need to make your way to the Docking Room!\n";
    private final String helpMessage = "To see where you currently are, type examine.\n" +
                                        "To go to another room, type go [direction].\n" +
                                        "To pick up an item, type take [item]. The room may not have an item.\n" +
                                        "To drop an item, type drop [item].\n" +
                                        "To see your inventory, type inventory.\n" +
                                        "To see the history of rooms you've visited, type history.\n" +
                                        "To exit the game, type quit.\n" +
                                        "To see this message again, type help.";
    private final String doneMessage = "================================================================\n" +
                                        "You've reached the docking room! Have a safe trip back to Earth!\n" +
                                        "================================================================";
    private final String[] directions = {"up", "left", "down", "right"};

    public SpaceshipGame(String fileName) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        Gson gson = new Gson();
        blueprint = gson.fromJson(reader, SpaceshipBlueprint.class);
        currentRoom = blueprint.getStartRoom();
        inventory = new ArrayList<>();
        roomHistory = new ArrayList<>();
        roomHistory.add(currentRoom);
        input = new Scanner(System.in);
    }

    /**
     * Main driver method for game.
     */
    public void play() {
        printIntro();
        System.out.println();
        printHelp();

        System.out.print("> ");
        String inText = input.nextLine();
        inText = inText.trim();
        Command cmd = new Command(inText);
        CommandType action = cmd.action();

        while (action != CommandType.QUIT) {

            switch (action) {
                case HELP:
                    printHelp();
                    break;
                case EXAMINE:
                    examine();
                    break;
                case GO:
                    String direction = inText.substring(3).trim();
                    go(direction);
                    break;
                case TAKE:
                    String itemWanted = inText.substring(5).trim();
                    take(itemWanted);
                    break;
                case DROP:
                    String itemToDrop = inText.substring(5).trim();
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

            if (reachedEndRoom()) {
                System.out.println(doneMessage);
                return;
            }

            System.out.print("> ");
            inText = input.nextLine();
            cmd.setCommand(inText);
            action = cmd.action();
        }

    }

    /**
     * Print introduction paragraph for game.
     */
    public void printIntro() {
        System.out.println(introMessage);
    }

    /**
     * Print help message for game. Can be repeatedly called via user input.
     */
    public void printHelp() {
        System.out.println(helpMessage);
    }

    /**
     * Move to room in direction.
     * If the direction is invalid or there is no room in that direction,
     * print error message and re prompt user.
     * @param direction a String representing a direction
     */
    public void go(String direction) {
        boolean isValidDirection = false;
        for (String dir : directions) {
            if (dir.equalsIgnoreCase(direction)) {
                isValidDirection = true;
                break;
            }
        }
        if (!isValidDirection) {
            System.out.println("Invalid direction. Try again.");
            return;
        }

        for (LeaveDirection exitDir : blueprint.getRoom(currentRoom).getLeaveDirections()) {
            if (exitDir.getDirectionName().equalsIgnoreCase(direction)) {
                this.currentRoom = exitDir.getRoomName();
                examine();
                roomHistory.add(currentRoom);
                return;
            }
        }
        System.out.println("Invalid direction. Try again.");
    }

    /**
     * Get current room name, description, and items.
     */
    public void examine() {
        System.out.println("You are currently in " + currentRoom + ".");
        Room current = blueprint.getRoom(currentRoom);
        if (current == null) {
            // Game should never reach this state, but implemented for failsafe purposes
            System.out.println("Invalid room!");
            return;
        }
        current.printDescription();
        current.printItems();
        current.printWhereFrom();
    }

    /**
     * Move an item into the user's inventory and remove it from the current room.
     * If there is no such item in the room, print an error message and do nothing.
     * @param itemWanted a String representing the desired item to take
     */
    public void take(String itemWanted) {
        Room current = blueprint.getRoom(currentRoom);
        if (!current.hasItem(itemWanted)) {
            System.out.println("There's no " + itemWanted + " in this room!");
            return;
        }
        current.takeItem(itemWanted);
        inventory.add(itemWanted);
    }

    /**
     * Remove an item from the user's inventory and place it in the current room.
     * If there is no such item in the inventory, print an error message and do nothing.
     * @param itemToDrop a String representing the desired item to drop
     */
    public void drop(String itemToDrop) {
        Room current = blueprint.getRoom(currentRoom);
        if (!inventory.contains(itemToDrop)) {
            System.out.println("There's no " + itemToDrop + " in your inventory!");
            return;
        }
        current.placeItem(itemToDrop);
        inventory.remove(itemToDrop);
    }

    /**
     * Print inventory.
     */
    public void printInventory() {
        System.out.println("Your inventory: " + inventory);
    }

    public boolean reachedEndRoom() {
        return currentRoom.equalsIgnoreCase(blueprint.getEndRoom());
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
                printHelp();
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
