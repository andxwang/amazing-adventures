package student.adventure;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SpaceshipGame {

    public SpaceshipBlueprint blueprint;
    private String currentRoom;
    private ArrayList<String> inventory;
    Scanner input;

    public SpaceshipGame(String fileName) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        Gson gson = new Gson();
        blueprint = gson.fromJson(reader, SpaceshipBlueprint.class);
        currentRoom = blueprint.getStartRoom();
        inventory = new ArrayList<>();
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
                case INVALID:
                    System.out.println("Invalid command. Try again.");
                    break;
                default:
                    break;
            }

            if (currentRoom.equalsIgnoreCase(blueprint.getEndRoom())) {
                System.out.println("================================================================\n" +
                                    "You've reached the docking room! Have a safe trip back to Earth!\n" +
                                    "================================================================");
                break;
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
        System.out.println("You are aboard the Rocinante, a speedy, slick space frigate!");
        System.out.println("You've finished your duties scouting the dark depths of our solar system " +
                "for precious materials.");
        System.out.println("Now it is time for you to head back to Earth via transport pod.");
        System.out.println("You need to make your way to the Docking Room!\n");
    }

    /**
     * Print help message for game. Can be repeatedly called via user input.
     */
    public void printHelp() {
        System.out.println("To see where you currently are, type examine.");
        System.out.println("To go to another room, type go [direction].");
        System.out.println("To pick up an item, type take [item]. The room may not have an item.");
        System.out.println("To drop an item, type drop [item].");
        System.out.println("To see your inventory, type inventory.");
        System.out.println("To exit the game, type quit.");
        System.out.println("To see this message again, type help.\n");
    }

    /**
     * Move to room in direction.
     * If the direction is invalid or there is no room in that direction,
     * print error message and re prompt user.
     * @param direction a String representing a direction
     */
    public void go(String direction) {
        if (!(direction.equalsIgnoreCase("Left") ||
                direction.equalsIgnoreCase("Up") ||
                direction.equalsIgnoreCase("Right") ||
                direction.equalsIgnoreCase("Down"))) {
            System.out.println("Invalid direction. Try again.");
            return;
        }

        for (LeaveDirection exitDir : blueprint.getRoom(currentRoom).getLeaveDirections()) {
            if (exitDir.getDirectionName().equalsIgnoreCase(direction)) {
                this.currentRoom = exitDir.getRoomName();
                examine();
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
}
