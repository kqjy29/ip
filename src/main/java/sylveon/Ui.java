package sylveon;

import java.util.ArrayList;
import java.util.Scanner;

/** Handles displaying messages and reading input for the chatbot. */
public class Ui {
    private static final String LINE =
            "----------<3----------<3----------<3----------";

    private static final String BANNER =
            "  ____          _                              \n"
                    + " / ___| _   _  | | __   __  ___   ___   _ __  \n"
                    + " \\___ \\| | | | | | \\ \\ / / / _ \\ / _ \\ | '_ \\ \n"
                    + "  ___) | |_| | | |  \\ V / |  __/| (_) || | | |\n"
                    + " |____/ \\__, | |_|   \\_/   \\___| \\___/ |_| |_|\n"
                    + "        |___/                                 ";

    private final Scanner scanner = new Scanner(System.in);

    /** Displays the chatbot welcome message. */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hi!! I'm sylveon.Sylveon <3\nWhat can I do for you?\n" + LINE);
    }

    /** Reads a command from the user. */
    public String readCommand() {
        System.out.print("Enter your text: ");
        return scanner.nextLine();
    }

    /** Displays the chatbot goodbye message. */
    public void showBye() {
        System.out.println(LINE
                + "\nBye bye :) Hope to see you again soon <3\n"
                + LINE);
    }

    /** Displays all tasks in the task list. */
    public void showList(ArrayList<Task> tasks) {
        System.out.println(LINE);

        if (tasks.isEmpty()) {
            System.out.println("Yay! Your task list is empty :)");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("   " + (i + 1) + "." + tasks.get(i));
            }
        }

        System.out.println(LINE);
    }

    /** Displays a confirmation that a task was marked as done. */
    public void showMarked(Task task) {
        System.out.println(LINE
                + "\n   Great! I've marked this task as done <3\n   "
                + task + "\n"
                + LINE);
    }

    /** Displays a confirmation that a task was marked as not done. */
    public void showUnmarked(Task task) {
        System.out.println(LINE
                + "\n   Okay! I've marked this task as not done yet :)\n   "
                + task + "\n"
                + LINE);
    }

    /** Displays a confirmation that a task was deleted. */
    public void showDeleted(Task task, int remainingTasks) {
        System.out.println(LINE
                + "\n   Sure! I've removed this task:\n     "
                + task
                + "\n   Now you have "
                + remainingTasks
                + " tasks left! Well done <3\n"
                + LINE);
    }

    /** Displays a confirmation that a task was added. */
    public void showAdded(String command) {
        System.out.println(LINE
                + "\n   Added: " + command
                + "\n"
                + LINE);
    }

    /** Displays an error message. */
    public void showError(String message) {
        System.out.println(LINE
                + "\n   " + message
                + "\n"
                + LINE);
    }
}
