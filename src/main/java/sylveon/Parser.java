package sylveon;

public class Parser {
    public String getCommandWord(String command) {
        return command.trim().split("\\s+", 2)[0];
    }

    public String getArguments(String command) {
        String[] words = command.trim().split("\\s+", 2);
        return words.length > 1 ? words[1].trim() : "";
    }
}
