package student.adventure;

/**
 * Class to represent the directions one can take to leave the current room
 */
public class LeaveDirection {

    private String directionName;
    private String room;

    public String getDirectionName() {
        return directionName;
    }

    public String getRoomName() {
        return room;
    }

    @Override
    public String toString() {
        return "LeaveDirection{" +
                "directionName='" + directionName + '\'' +
                ", room='" + room + '\'' +
                '}';
    }
}
