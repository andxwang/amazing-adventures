package student.adventure;

import java.util.ArrayList;

/**
 * Class to represent a room in the map
 */
public class Room {

    private String roomName;
    private String description;
    private String imageURL;
    private String videoURL;
    private ArrayList<String> items;
    private LeaveDirection[] leaveDirections;

    public String getRoomName() {
        return roomName;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void printDescription() {
        System.out.println(description);
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public String getItemsAsString() {
        return items.toString();
    }

    /**
     * Prints items in this room.
     */
    public void printItems() {
        System.out.print("Items in this room: ");
        System.out.println(items);
    }

    public LeaveDirection[] getLeaveDirections() {
        return leaveDirections;
    }

    /**
     * Prints out which rooms you can go do and in which direction.
     */
    public void printWhereFrom() {
        System.out.println("You can go: ");
        for (LeaveDirection dir : leaveDirections) {
            System.out.println("  - " + dir.getDirectionName() + " to " + dir.getRoomName());
        }
    }

    public String getDirectionsToRooms() {
        StringBuilder message = new StringBuilder("You can go: \n");
        for (LeaveDirection dir : leaveDirections) {
            message.append("  - ").append(dir.getDirectionName()).append(" to ").append(dir.getRoomName()).append("\n");
        }
        return message.toString();
    }

    /**
     * Check if this room has a given item.
     * @param itemName a String representing the item
     * @return true if the item is in this room, false otherwise
     */
    public boolean hasItem(String itemName) {
        return items.contains(itemName);
    }

    /**
     * Takes an item and removes from this room's items ArrayList.
     * @param itemName a String representing the item
     */
    public void takeItem(String itemName) {
        if (hasItem(itemName)) items.remove(itemName);
    }

    /**
     * Place an item into this room and add it to this room's items ArrayList
     * @param itemName a String representing the item
     */
    public void placeItem(String itemName) {
        items.add(itemName);
    }

}
