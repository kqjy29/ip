import java.util.Scanner;
public class Sylveon {
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
            // bye
            if (command.equals("bye")) {
                break;
            }
            // list out tasks
            if (command.equals("list")) {
                System.out.println(line);
                for (int j = 0; j < i; j++) {
                    System.out.println("   " + (j + 1) + "." + tasks[j]);
                }
                System.out.println(line);
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                Task task = tasks[taskNumber - 1];
                task.markAsDone();
                System.out.println(line + "\n   Great! I've marked this task as done <3\n   "
                        + task + "\n" + line);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                Task task = tasks[taskNumber - 1];
                task.markAsNotDone();
                System.out.println(line + "\n   Okay! I've marked this task as not done yet :)\n   "
                        + task + "\n" + line);
            } else {
                //allocation commands to diff classes and adding them into the task array
                if (command.startsWith("todo ")) {
                    String description = command.substring(5);
                    tasks[i] = new Todo(description);
                    i++;
                } else if (command.startsWith("deadline ")) {
                    int idx = command.indexOf(" /by ");
                    String description = command.substring(9, idx);
                    String by = command.substring(idx + 5);
                    tasks[i] = new Deadline(description, by);
                    i++;
                } else if (command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    String description = command.substring(6, fromIndex);
                    String from = command.substring(fromIndex + 7, toIndex);
                    String to = command.substring(toIndex + 5);
                    tasks[i] = new Event(description, from, to);
                    i++;
                }
                System.out.println(line + "\n" + "   " + "Added: " + command + "\n" + line);
            }
        }
        System.out.println(line + "\n" + "Bye bye :) Hope to see you again soon <3\n" + line);

    }
}
