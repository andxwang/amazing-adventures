package student.adventure;

public class Command {

    String text;

    public Command(String inText) {
        text = inText.trim();
    }

    /**
     * Process the input text and choose an action.
     * If the command does match one of the given commands,
     * return an invalid enum type.
     * @return A CommandType enum action.
     */
    public CommandType action() {
        if (text.equalsIgnoreCase("quit"))
            return CommandType.QUIT;

        if (text.equalsIgnoreCase("help"))
            return CommandType.HELP;

        if (text.equalsIgnoreCase("examine"))
            return CommandType.EXAMINE;

        if (text.length() >= 2 && text.substring(0, 2).equalsIgnoreCase("go"))
            return CommandType.GO;

        if (text.length() >= 4 && text.substring(0, 4).equalsIgnoreCase("take"))
            return CommandType.TAKE;

        if (text.length() >= 4 && text.substring(0, 4).equalsIgnoreCase("drop"))
            return CommandType.DROP;

        if (text.equalsIgnoreCase("inventory"))
            return CommandType.INVENTORY;

        return CommandType.INVALID;
    }

    public void setCommand(String cmd) {
        text = cmd;
    }
}
