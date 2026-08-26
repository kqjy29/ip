import java.util.Scanner;
import java.util.ArrayList;

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

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hi!! I'm Sylveon <3\nWhat can I do for you?\n" + LINE);
    }

    public String readCommand() {
        System.out.print("Enter your text: ");
        return scanner.nextLine();
    }

    public void showBye() {
        System.out.println(LINE
                + "\nBye bye :) Hope to see you again soon <3\n"
                + LINE);
    }

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

    public void showMarked(Task task) {
        System.out.println(LINE
                + "\n   Great! I've marked this task as done <3\n   "
                + task + "\n"
                + LINE);
    }

    public void showUnmarked(Task task) {
        System.out.println(LINE
                + "\n   Okay! I've marked this task as not done yet :)\n   "
                + task + "\n"
                + LINE);
    }

    public void showDeleted(Task task, int remainingTasks) {
        System.out.println(LINE
                + "\n   Sure! I've removed this task:\n     "
                + task
                + "\n   Now you have "
                + remainingTasks
                + " tasks left! Well done <3\n"
                + LINE);
    }

    public void showAdded(String command) {
        System.out.println(LINE
                + "\n   Added: " + command
                + "\n"
                + LINE);
    }

    public void showError(String message) {
        System.out.println(LINE
                + "\n   " + message
                + "\n"
                + LINE);
    }
}
