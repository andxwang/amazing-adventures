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

    public void play() {
        printIntro();
        System.out.println();
        printHelp();

        System.out.print("> ");
        String inText = input.nextLine();

        while (!inText.trim().equalsIgnoreCase("quit")) {
            inText = inText.trim();

            // help
            if (inText.trim().equalsIgnoreCase("help")) {
                printHelp();
            }

            // examine
            else if (inText.trim().equalsIgnoreCase("examine")) {
                examine();
            }

            // go [direction]
            else if (inText.length() >= 2 && inText.substring(0, 2).equalsIgnoreCase("go")) {
                String direction = inText.substring(3).trim();
                go(direction);
            }

            // take [item]
            else if (inText.length() >= 4 && inText.substring(0, 4).equalsIgnoreCase("take")) {
                String itemWanted = inText.substring(5).trim();
                take(itemWanted);
            }

            // drop [item]
            else if (inText.length() >= 4 && inText.substring(0, 4).equalsIgnoreCase("drop")) {
                String itemToDrop = inText.substring(5).trim();
                drop(itemToDrop);
            }

            // inventory
            else if (inText.equalsIgnoreCase("inventory")) {
                System.out.println("Your inventory: " + inventory);
            }

            else {
                System.out.println("Invalid command. Try again.");
            }

            if (currentRoom.equalsIgnoreCase(blueprint.getEndRoom())) {
                System.out.println("================================================================\n" +
                                   "You've reached the docking room! Have a safe trip back to Earth!\n" +
                                   "================================================================");
                break;
            }

            System.out.print("> ");
            inText = input.nextLine();
        }
    }

    public void printIntro() {
        System.out.println("You are aboard the Rocinante, a speedy, slick space frigate!");
        System.out.println("You've finished your duties scouting the dark depths of our solar system " +
                "for precious materials.");
        System.out.println("Now it is time for you to head back to Earth via transport pod.");
        System.out.println("You need to make your way to the Docking Room!\n");
    }

    public void printHelp() {
        System.out.println("To see where you currently are, type examine.");
        System.out.println("To go to another room, type go [direction].");
        System.out.println("To pick up an item, type take [item]. The room may not have an item.");
        System.out.println("To drop an item, type drop [item].");
        System.out.println("To see your inventory, type inventory.");
        System.out.println("To exit the game, type quit.");
        System.out.println("To see this message again, type help.");
    }

    public void go(String direction) {
        // TODO: add isValidDirection() somewhere
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

    public void take(String itemWanted) {
        Room current = blueprint.getRoom(currentRoom);
        if (!current.hasItem(itemWanted)) {
            System.out.println("There's no " + itemWanted + " in this room!");
            return;
        }
        current.takeItem(itemWanted);
        inventory.add(itemWanted);
    }

    public void drop(String itemToDrop) {
        Room current = blueprint.getRoom(currentRoom);
        if (!inventory.contains(itemToDrop)) {
            System.out.println("There's no " + itemToDrop + " in your inventory!");
            return;
        }
        current.placeItem(itemToDrop);
        inventory.remove(itemToDrop);
    }
}
