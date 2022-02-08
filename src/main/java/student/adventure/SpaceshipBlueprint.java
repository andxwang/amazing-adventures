package student.adventure;

import java.util.ArrayList;

public class SpaceshipBlueprint {

    private String startRoom;
    private String endRoom;
    private Room[] rooms;

    public String getStartRoom() {
        return startRoom;
    }

    public String getEndRoom() {
        return endRoom;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public ArrayList<String> getRoomNames() {
        ArrayList<String> roomNames = new ArrayList<>();
        for (Room room : rooms) {
            roomNames.add(room.getRoomName());
        }
        return roomNames;
    }

    public Room getRoom(String roomName) {
        for (Room room : rooms) {
            if (roomName.equalsIgnoreCase(room.getRoomName()))
                return room;
        }
        return null;
    }

}
