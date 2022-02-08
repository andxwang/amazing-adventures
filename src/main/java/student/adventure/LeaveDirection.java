package student.adventure;

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
