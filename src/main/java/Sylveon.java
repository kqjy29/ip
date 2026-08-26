import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Sylveon {
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

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage("data/sylveon.txt");
        TaskList tasks = new TaskList(storage.load());

        //chat loop
        while (true) {
            String command = ui.readCommand();
            Parser parser = new Parser();
            String commandWord = parser.getCommandWord(command);
            String arguments = parser.getArguments(command);
            // bye
            if (commandWord.equals("bye")) {
                break;
            }
            try {
            // list out tasks
            if (commandWord.equals("list")) {
                ui.showList(tasks.getTasks());
            } else if (commandWord.equals("mark")) {
                int taskNumber = parseTaskNumber(arguments, "mark", tasks.size());
                Task task = tasks.get(taskNumber - 1);
                task.markAsDone();
                storage.save(tasks.getTasks());
                ui.showMarked(task);
            } else if (commandWord.equals("unmark")) {
                int taskNumber = parseTaskNumber(arguments, "unmark", tasks.size());
                Task task = tasks.get(taskNumber - 1);
                task.markAsNotDone();
                storage.save(tasks.getTasks());
                ui.showUnmarked(task);
            } else if (commandWord.equals("delete")) {
                int taskNumber = parseTaskNumber(arguments, "delete", tasks.size());
                Task deletedTask = tasks.delete(taskNumber - 1);
                storage.save(tasks.getTasks());
                ui.showDeleted(deletedTask, tasks.size());
            } else {
                // allocating commands to diff classes and adding them into the task array
                if (commandWord.equals("todo")) {
                    String description = arguments;
                    if (description.isEmpty()) {
                        throw new SylveonException("Error! Remember to add a description :)");
                    }
                    tasks.add(new Todo(description));
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
                                "Error! An event must be written like this: event <description> /from <start> /to <end>"
                        );
                    }
                    String description = arguments.substring(0, fromIndex).trim();
                    String from = arguments.substring(fromIndex + 7, toIndex).trim();
                    String to = arguments.substring(toIndex + 5).trim();
                    if (description.isEmpty()) {
                        throw new SylveonException("Error! Remember to add a description :)");
                    }
                    if (from.isEmpty()) {
                        throw new SylveonException("Error! Please add a starting time after /from :)");
                    }
                    if (to.isEmpty()) {
                        throw new SylveonException("Please add an ending time after /to :)");
                    }
                    tasks.add(new Event(description, from, to));
                    storage.save(tasks.getTasks());
                } else {
                    //unknown command
                    throw new SylveonException("Oh no, could you try something else? I do not recognise this command :(");
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
