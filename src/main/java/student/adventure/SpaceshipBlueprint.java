package student.adventure;

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

    /**
     * Helper method to get Room object corresponding to given room name.
     * @param roomName a String represent the desired room name
     * @return a Room object
     */
    public Room getRoom(String roomName) {
        for (Room room : rooms) {
            if (roomName.equalsIgnoreCase(room.getRoomName()))
                return room;
        }
        return null;
    }

}
