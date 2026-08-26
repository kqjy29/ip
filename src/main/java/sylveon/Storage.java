package sylveon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    public void save(ArrayList<Task> tasks) {
        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            String line;

            if (task instanceof Todo) {
                line = "T | " + (task.isDone() ? "1" : "0")
                        + " | " + task.getDescription();
            } else if (task instanceof Deadline deadline) {
                line = "D | " + (task.isDone() ? "1" : "0")
                        + " | " + task.getDescription()
                        + " | " + deadline.getBy();
            } else if (task instanceof Event event) {
                line = "E | " + (task.isDone() ? "1" : "0")
                        + " | " + task.getDescription()
                        + " | " + event.getFrom()
                        + " | " + event.getTo();
            } else {
                continue;
            }

            lines.add(line);
        }

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Error: Could not save tasks.");
        }
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                String[] parts = line.split(" \\| ");

                Task task;

                if (parts[0].equals("T")) {
                    task = new Todo(parts[2]);
                } else if (parts[0].equals("D")) {
                    task = new Deadline(parts[2], LocalDate.parse(parts[3]));
                } else if (parts[0].equals("E")) {
                    task = new Event(parts[2], parts[3], parts[4]);
                } else {
                    continue;
                }

                if (parts[1].equals("1")) {
                    task.markAsDone();
                }

                tasks.add(task);
            }

        } catch (IOException e) {
            System.out.println("Error: Could not load tasks.");
        }

        return tasks;
    }
}
