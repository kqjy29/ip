package sylveon;

/**
 * Represents one task in sylveon.Sylveon's task list.
 */
public class Task {
    private final String description;
    private boolean done;

    /** Creates a task that is initially not done. */
    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        done = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        done = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    /** Returns the task's display status and description. */
    @Override
    public String toString() {
        return "[" + (done ? "X" : " ") + "] " + description;
    }
}
