import java.util.Scanner;
public class Sylveon {
    private static int parseTaskNumber(String arguments, String command, int taskCount)
            throws SylveonException {
        if (arguments.isEmpty()) {
            throw new SylveonException("Please provide a task number after " + command + ".");
        }
        try {
            int taskNumber = Integer.parseInt(arguments);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new SylveonException("That task number does not exist.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new SylveonException("The task number must be a whole number.");
        }
    }

    public static void main(String[] args) {
        String banner = "  ____          _                              \n"
                + " / ___| _   _  | | __   __  ___   ___   _ __  \n"
                + " \\___ \\| | | | | | \\ \\ / / / _ \\ / _ \\ | '_ \\ \n"
                + "  ___) | |_| | | |  \\ V / |  __/| (_) || | | |\n"
                + " |____/ \\__, | |_|   \\_/   \\___| \\___/ |_| |_|\n"
                + "        |___/                                 ";
        String line = "----------<3----------<3----------<3----------";
        System.out.println(line);
        System.out.println(banner);
        System.out.println("Hi!! I'm Sylveon <3\nWhat can I do for you?\n" + line);
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int i = 0;
        while (true) {
            System.out.print("Enter your text: ");
            String command = scanner.nextLine();
            String[] words = command.trim().split("\\s+", 2);
            String commandWord = words[0];
            String arguments = words.length > 1 ? words[1].trim() : "";
            // bye
            if (commandWord.equals("bye")) {
                break;
            }
            try {
            // list out tasks
            if (commandWord.equals("list")) {
                System.out.println(line);
                for (int j = 0; j < i; j++) {
                    System.out.println("   " + (j + 1) + "." + tasks[j]);
                }
                System.out.println(line);
            } else if (commandWord.equals("mark")) {
                int taskNumber = parseTaskNumber(arguments, "mark", i);
                Task task = tasks[taskNumber - 1];
                task.markAsDone();
                System.out.println(line + "\n   Great! I've marked this task as done <3\n   "
                        + task + "\n" + line);
            } else if (commandWord.equals("unmark")) {
                int taskNumber = parseTaskNumber(arguments, "unmark", i);
                Task task = tasks[taskNumber - 1];
                task.markAsNotDone();
                System.out.println(line + "\n   Okay! I've marked this task as not done yet :)\n   "
                        + task + "\n" + line);
            } else {
                // allocating commands to diff classes and adding them into the task array
                if (commandWord.equals("todo")) {
                    String description = arguments;
                    if (description.isEmpty()) {
                        throw new SylveonException("Error! Remember to add a description :)");
                    }
                    tasks[i] = new Todo(description);
                    i++;
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
                    tasks[i] = new Deadline(description, by);
                    i++;
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
                    tasks[i] = new Event(description, from, to);
                    i++;
                } else {
                    //unknown command
                    throw new SylveonException("Oh no, could you try something else? I do not recognise this command :(");
                }
                System.out.println(line + "\n" + "   " + "Added: " + command + "\n" + line);
            }
            } catch (SylveonException e) {
                System.out.println(line + "\n   " + e.getMessage() + "\n" + line);
            }
        }
        System.out.println(line + "\n" + "Bye bye :) Hope to see you again soon <3\n" + line);

    }
}
