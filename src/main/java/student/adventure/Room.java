package student.adventure;

import java.util.ArrayList;
import java.util.Map;

public class Room {

    private String roomName;
    private String description;
    private ArrayList<String> items;
    private LeaveDirection[] leaveDirections;

    public String getRoomName() {
        return roomName;
    }

    public String getDescription() {
        return description;
    }

    public void printDescription() {
        System.out.println(description);
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void printItems() {
        System.out.print("Items in this room: ");
        System.out.println(items);
    }

    public LeaveDirection[] getLeaveDirections() {
        return leaveDirections;
    }

    public void printWhereFrom() {
        System.out.println("You can go: ");
        for (LeaveDirection dir : leaveDirections) {
            System.out.println("  - " + dir.getDirectionName() + " to " + dir.getRoomName());
        }
    }

    public boolean hasItem(String itemName) {
        return items.contains(itemName);
    }

    public void takeItem(String itemName) {
        items.remove(itemName);
    }

    public void placeItem(String itemName) {
        items.add(itemName);
    }

}
