package sylveon;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/** Runs the Sylveon chatbot application. */
public class Sylveon {
    private static final String DEADLINE_SEPARATOR = " /by ";
    private static final String EVENT_START_SEPARATOR = " /from ";
    private static final String EVENT_END_SEPARATOR = " /to ";

    private final Storage storage;
    private final TaskList tasks;
    private ArrayList<Task> displayedTasks;

    /** Creates Sylveon and loads its saved tasks. */
    public Sylveon() {
        this("data/sylveon.txt");
    }

    /** Creates Sylveon using the specified storage file. */
    Sylveon(String filePath) {
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
        displayedTasks = new ArrayList<>(tasks.getTasks());
    }

    /**
     * Generates a response for a user's GUI message.
     *
     * @param input the user's message
     * @return Sylveon's response
     */
    public String getResponse(String input) {
        try {
            return processCommand(input);
        } catch (SylveonException e) {
            return e.getMessage();
        }
    }

    private String processCommand(String input) throws SylveonException {
        Parser parser = new Parser();
        String commandWord = parser.getCommandWord(input);
        String arguments = parser.getArguments(input);

        switch (commandWord) {
        case "list":
            return formatTaskList();
        case "sort":
            return sortTasks(arguments);
        case "mark":
        case "unmark":
            return changeTaskStatus(commandWord, arguments);
        case "delete":
            return deleteTask(arguments);
        case "find":
            return findTasks(arguments);
        case "todo":
            return addTask(new Todo(arguments), input);
        case "deadline":
            return addDeadline(arguments, input);
        case "event":
            return addEvent(arguments, input);
        case "bye":
            return "Bye bye :) Hope to see you again soon <3";
        default:
            throw new SylveonException("Oh no, could you try something else? "
                    + "I do not recognise this command :(");
        }
    }

    private String formatTaskList() {
        displayedTasks = new ArrayList<>(tasks.getTasks());
        if (tasks.isEmpty()) {
            return "Yay! Your task list is empty :)";
        }
        StringBuilder result = new StringBuilder("Here are your tasks:\n");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("   ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return result.toString().trim();
    }

    private String changeTaskStatus(String command, String arguments) throws SylveonException {
        int taskNumber = parseTaskNumber(arguments, command, displayedTasks.size());
        Task task = displayedTasks.get(taskNumber - 1);
        assert task != null : "A valid task number must return a task";
        if (command.equals("mark")) {
            task.markAsDone();
            storage.save(tasks.getTasks());
            return "Great! I've marked this task as done <3\n" + task;
        }
        task.markAsNotDone();
        storage.save(tasks.getTasks());
        return "Okay! I've marked this task as not done yet :)\n" + task;
    }

    private String deleteTask(String arguments) throws SylveonException {
        int taskNumber = parseTaskNumber(arguments, "delete", displayedTasks.size());
        Task deletedTask = displayedTasks.get(taskNumber - 1);
        tasks.delete(deletedTask);
        displayedTasks.remove(deletedTask);
        assert deletedTask != null : "Deleting a valid task must return a task";
        storage.save(tasks.getTasks());
        return "Sure! I've removed this task:\n" + deletedTask
                + "\nNow you have " + tasks.size() + " tasks left!";
    }

    private String findTasks(String arguments) throws SylveonException {
        if (arguments.isEmpty()) {
            throw new SylveonException("Error! Please provide a keyword after find.");
        }
        ArrayList<Task> matchingTasks = tasks.find(arguments);
        if (matchingTasks.isEmpty()) {
            return "There are no matching tasks.";
        }
        StringBuilder result = new StringBuilder("Here are the matching tasks:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            result.append("   ").append(i + 1).append(". ").append(matchingTasks.get(i)).append("\n");
        }
        return result.toString().trim();
    }

    private String addTask(Task task, String input) throws SylveonException {
        if (task.getDescription().isEmpty()) {
            throw new SylveonException("Error! Remember to add a description :)");
        }
        tasks.add(task);
        displayedTasks = new ArrayList<>(tasks.getTasks());
        assert tasks.get(tasks.size() - 1) == task : "Added task must be stored at the end of the list";
        storage.save(tasks.getTasks());
        return "Added: " + input;
    }

    private String sortTasks(String arguments) throws SylveonException {
        if (arguments.isEmpty()) {
            displayedTasks = tasks.sortAlphabetically();
            storage.save(tasks.getTasks());
            return formatSortedTasks("Sorted tasks alphabetically:");
        }
        if (arguments.contains(" ")) {
            throw new SylveonException("Error! Sort commands accept exactly one task type.");
        }
        if (!arguments.equals("deadline") && !arguments.equals("event") && !arguments.equals("todo")) {
            throw new SylveonException("Error! Invalid sort type. Use deadline, event, or todo.");
        }

        displayedTasks = tasks.sortByType(arguments);
        storage.save(tasks.getTasks());
        String order = arguments.equals("todo") ? "alphabetically" : "by date (earliest first)";
        return formatSortedTasks("Sorted " + arguments + "s " + order + ":");
    }

    private String formatSortedTasks(String header) {
        StringBuilder result = new StringBuilder(header);
        if (displayedTasks.isEmpty()) {
            return result.append("\nThere are no matching tasks.").toString();
        }
        for (int i = 0; i < displayedTasks.size(); i++) {
            result.append("\n   ").append(i + 1).append(". ").append(displayedTasks.get(i));
        }
        return result.toString();
    }

    private String addDeadline(String arguments, String input) throws SylveonException {
        int index = arguments.indexOf(DEADLINE_SEPARATOR);
        if (index == -1) {
            throw new SylveonException("Error! A deadline has to be written like this: "
                    + "deadline <description> /by <date>");
        }
        String description = arguments.substring(0, index).trim();
        String dateText = arguments.substring(index + DEADLINE_SEPARATOR.length()).trim();
        if (description.isEmpty() || dateText.isEmpty()) {
            throw new SylveonException("Error! A deadline needs a description and date :)");
        }
        try {
            return addTask(new Deadline(description, LocalDate.parse(dateText)), input);
        } catch (DateTimeParseException e) {
            throw new SylveonException("Error! Please use the date format yyyy-mm-dd :)");
        }
    }

    private String addEvent(String arguments, String input) throws SylveonException {
        int fromIndex = arguments.indexOf(EVENT_START_SEPARATOR);
        int toIndex = arguments.indexOf(EVENT_END_SEPARATOR);
        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new SylveonException("Error! An event must be written like this: "
                    + "event <description> /from <yyyy-mm-dd> /to <yyyy-mm-dd>");
        }
        String description = arguments.substring(0, fromIndex).trim();
        String fromText = arguments.substring(fromIndex + EVENT_START_SEPARATOR.length(), toIndex).trim();
        String toText = arguments.substring(toIndex + EVENT_END_SEPARATOR.length()).trim();
        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new SylveonException("Error! An event needs a description and dates in yyyy-mm-dd format :)");
        }
        try {
            LocalDate from = LocalDate.parse(fromText);
            LocalDate to = LocalDate.parse(toText);
            return addTask(new Event(description, from, to), input);
        } catch (DateTimeParseException e) {
            throw new SylveonException("Error! Event dates must use the format yyyy-mm-dd :)");
        }
    }

    /** Converts a user-provided task number into a zero-based list position. */
    private static int parseTaskNumber(String arguments, String command, int taskCount)
            throws SylveonException {
        if (arguments.isEmpty()) {
            throw new SylveonException("Error! Please provide a task number after " + command + " :)");
        }
        try {
            int taskNumber = Integer.parseInt(arguments);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new SylveonException("Uh oh.. That task number does not exist :(");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new SylveonException("Error! The task number has to be a whole number!");
        }
    }

    /** Starts the chatbot and processes commands until the user exits. */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage("data/sylveon.txt");
        TaskList tasks = new TaskList(storage.load());
        ArrayList<Task> displayedTasks = new ArrayList<>(tasks.getTasks());

        // Process commands until the user exits.
        while (true) {
            String command = ui.readCommand();
            Parser parser = new Parser();
            String commandWord = parser.getCommandWord(command);
            String arguments = parser.getArguments(command);
            // Exit the application.
            if (commandWord.equals("bye")) {
                break;
            }
            try {
            // Display the task list.
            if (commandWord.equals("list")) {
                displayedTasks = new ArrayList<>(tasks.getTasks());
                ui.showList(displayedTasks);
            } else if (commandWord.equals("sort")) {
                if (arguments.isEmpty()) {
                    displayedTasks = tasks.sortAlphabetically();
                    storage.save(tasks.getTasks());
                    ui.showSorted("all", displayedTasks);
                } else if (arguments.contains(" ")) {
                    throw new SylveonException("Error! Sort commands accept exactly one task type.");
                } else if (!arguments.equals("deadline") && !arguments.equals("event")
                        && !arguments.equals("todo")) {
                    throw new SylveonException("Error! Invalid sort type. Use deadline, event, or todo.");
                } else {
                    displayedTasks = tasks.sortByType(arguments);
                    storage.save(tasks.getTasks());
                    ui.showSorted(arguments, displayedTasks);
                }
            } else if (commandWord.equals("mark")) {
                int taskNumber = parseTaskNumber(arguments, "mark", displayedTasks.size());
                Task task = displayedTasks.get(taskNumber - 1);
                task.markAsDone();
                storage.save(tasks.getTasks());
                ui.showMarked(task);
            } else if (commandWord.equals("unmark")) {
                int taskNumber = parseTaskNumber(arguments, "unmark", displayedTasks.size());
                Task task = displayedTasks.get(taskNumber - 1);
                task.markAsNotDone();
                storage.save(tasks.getTasks());
                ui.showUnmarked(task);
            } else if (commandWord.equals("delete")) {
                int taskNumber = parseTaskNumber(arguments, "delete", displayedTasks.size());
                Task deletedTask = displayedTasks.get(taskNumber - 1);
                tasks.delete(deletedTask);
                displayedTasks.remove(deletedTask);
                storage.save(tasks.getTasks());
                ui.showDeleted(deletedTask, tasks.size());
            } else if (commandWord.equals("find")) {
                if (arguments.isEmpty()) {
                    throw new SylveonException(
                            "Error! Please provide a keyword after find."
                    );
                }

                ArrayList<Task> matchingTasks = tasks.find(arguments);
                ui.showMatchingTasks(matchingTasks);

            } else {
                // Create and add a new task.
                if (commandWord.equals("todo")) {
                    String description = arguments;
                    if (description.isEmpty()) {
                        throw new SylveonException("Error! Remember to add a description :)");
                    }
                    tasks.add(new Todo(description));
                    displayedTasks = new ArrayList<>(tasks.getTasks());
                    storage.save(tasks.getTasks());
                } else if (commandWord.equals("deadline")) {
                    int idx = arguments.indexOf(" /by ");
                    if (idx == -1) {
                        throw new SylveonException(
                                "Error! A deadline has to be written like this: deadline <description> /by <date>"
                        );
                    }
                    String description = arguments.substring(0, idx).trim();
                    String by = arguments.substring(idx + 5).trim();
                    if (description.isEmpty()) {
                        throw new SylveonException("Error! Remember to add a description :)");
                    }
                    if (by.isEmpty()) {
                        throw new SylveonException("Error! Please provide a date after /by :)");
                    }
                    try {
                        LocalDate date = LocalDate.parse(by);
                        tasks.add(new Deadline(description, date));
                        displayedTasks = new ArrayList<>(tasks.getTasks());
                        storage.save(tasks.getTasks());
                    } catch (DateTimeParseException e) {
                        throw new SylveonException(
                                "Error! Please use the date format yyyy-mm-dd :)"
                        );
                    }

                } else if (commandWord.equals("event")) {
                    int fromIndex = arguments.indexOf(" /from ");
                    int toIndex = arguments.indexOf(" /to ");
                    if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                        throw new SylveonException(
                                "Error! An event must be written like this: event <description> "
                                        + "/from <yyyy-mm-dd> /to <yyyy-mm-dd>"
                        );
                    }
                    String description = arguments.substring(0, fromIndex).trim();
                    String fromText = arguments.substring(fromIndex + 7, toIndex).trim();
                    String toText = arguments.substring(toIndex + 5).trim();
                    if (description.isEmpty()) {
                        throw new SylveonException("Error! Remember to add a description :)");
                    }
                    if (fromText.isEmpty()) {
                        throw new SylveonException(
                                "Error! Please add a starting date in yyyy-mm-dd format after /from :)");
                    }
                    if (toText.isEmpty()) {
                        throw new SylveonException(
                                "Error! Please add an ending date in yyyy-mm-dd format after /to :)");
                    }
                    try {
                        LocalDate from = LocalDate.parse(fromText);
                        LocalDate to = LocalDate.parse(toText);
                        tasks.add(new Event(description, from, to));
                        displayedTasks = new ArrayList<>(tasks.getTasks());
                    } catch (DateTimeParseException e) {
                        throw new SylveonException("Error! Event dates must use the format yyyy-mm-dd :)");
                    }
                    storage.save(tasks.getTasks());
                } else {
                    // Reject unrecognised commands.
                    throw new SylveonException(
                            "Oh no, could you try something else? I do not recognise this command :(");
                }
                ui.showAdded(command);
            }
            } catch (SylveonException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showBye();

    }
}
