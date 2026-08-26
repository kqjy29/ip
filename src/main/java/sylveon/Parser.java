package sylveon;

/** Parses user commands into command words and arguments. */
public class Parser {
    /** Extracts the first word from a command. */
    public String getCommandWord(String command) {
        return command.trim().split("\\s+", 2)[0];
    }

    /** Extracts the arguments following the command word. */
    public String getArguments(String command) {
        String[] words = command.trim().split("\\s+", 2);
        return words.length > 1 ? words[1].trim() : "";
    }
}
