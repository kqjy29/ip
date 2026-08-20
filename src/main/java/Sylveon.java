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
        while (true) {
            System.out.print("Enter your text: ");
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }
            System.out.println(line + "\n" + command + "\n" + line);
        }
        System.out.println(line + "\n" + "Bye bye :) Hope to see you again soon <3\n" + line);

    }
}
