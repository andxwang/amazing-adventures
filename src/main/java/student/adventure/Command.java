package student.adventure;

/**
 * Utility class to handle commands in SpaceshipGame.play().
 * Takes in text input and processes into CommandType enum type.
 */
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
        /* I am not storing these strings as private final variables as suggested in feedback
           because it would be redundant and less readable. E.g. no point in checking if
           text.equals(quit) where quit is just a String for "quit".
         */

        if (text.equalsIgnoreCase("quit"))
            return CommandType.QUIT;

        if (text.equalsIgnoreCase("help"))
            return CommandType.HELP;

        if (text.equalsIgnoreCase("examine"))
            return CommandType.EXAMINE;

        if (text.equalsIgnoreCase("inventory"))
            return CommandType.INVENTORY;

        if (text.equalsIgnoreCase("history"))
            return CommandType.HISTORY;

        String[] split = text.split(" ");

        if (split[0].equalsIgnoreCase("go"))
            return CommandType.GO;

        if (split[0].equalsIgnoreCase("take"))
            return CommandType.TAKE;

        if (split[0].equalsIgnoreCase("drop"))
            return CommandType.DROP;

        return CommandType.INVALID;
    }

    public void setCommand(String cmd) {
        text = cmd;
    }
}
