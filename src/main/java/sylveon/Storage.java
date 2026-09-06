package sylveon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** Handles saving and loading tasks from a file. */
public class Storage {
    private static final String TODO_CODE = "T";
    private static final String DEADLINE_CODE = "D";
    private static final String EVENT_CODE = "E";
    private static final String DONE_CODE = "1";
    private static final String NOT_DONE_CODE = "0";
    private static final String STORAGE_SEPARATOR = " | ";

    private final Path filePath;

    /** Creates a storage object for the specified file. */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /** Saves the supplied tasks to the storage file. */
    public void save(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks to save must not be null";
        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            String line;

            if (task instanceof Todo) {
                line = TODO_CODE + STORAGE_SEPARATOR + (task.isDone() ? DONE_CODE : NOT_DONE_CODE)
                        + STORAGE_SEPARATOR + task.getDescription();
            } else if (task instanceof Deadline deadline) {
                line = DEADLINE_CODE + STORAGE_SEPARATOR + (task.isDone() ? DONE_CODE : NOT_DONE_CODE)
                        + STORAGE_SEPARATOR + task.getDescription()
                        + STORAGE_SEPARATOR + deadline.getBy();
            } else if (task instanceof Event event) {
                line = EVENT_CODE + STORAGE_SEPARATOR + (task.isDone() ? DONE_CODE : NOT_DONE_CODE)
                        + STORAGE_SEPARATOR + task.getDescription()
                        + STORAGE_SEPARATOR + event.getFrom()
                        + STORAGE_SEPARATOR + event.getTo();
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

    /** Loads tasks from the storage file. */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                String[] fields = line.split(Pattern.quote(STORAGE_SEPARATOR));

                Task task;

                String taskType = fields[0];
                String completionStatus = fields[1];
                String description = fields[2];

                if (taskType.equals(TODO_CODE)) {
                    task = new Todo(description);
                } else if (taskType.equals(DEADLINE_CODE)) {
                    task = new Deadline(description, LocalDate.parse(fields[3]));
                } else if (taskType.equals(EVENT_CODE)) {
                    task = new Event(description, fields[3], fields[4]);
                } else {
                    continue;
                }

                if (completionStatus.equals(DONE_CODE)) {
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
