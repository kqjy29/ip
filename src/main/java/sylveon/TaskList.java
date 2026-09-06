package sylveon;

import java.util.ArrayList;

/** Manages the collection of tasks in sylveon.Sylveon. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates a task list containing the supplied tasks. */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Task list must not be null";
        this.tasks = tasks;
    }

    /** Adds a task to the list. */
    public void add(Task task) {
        assert task != null : "Cannot add a null task";
        tasks.add(task);
    }

    /** Returns the task at the given zero-based index. */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Task index must be within the list";
        return tasks.get(index);
    }

    /** Removes and returns the task at the given zero-based index. */
    public Task delete(int index) {
        assert index >= 0 && index < tasks.size() : "Task index must be within the list";
        return tasks.remove(index);
    }

    /** Returns the number of tasks. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns all tasks whose descriptions contain the specified keyword.
     *
     * @param keyword keyword to search for
     * @return tasks whose descriptions contain the keyword
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            assert task != null : "Task list must not contain null tasks";
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    /** Returns whether there are no tasks. */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the tasks for display and storage operations.
     *
     * @return the task collection
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
