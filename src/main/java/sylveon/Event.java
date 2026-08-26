package sylveon;

/** Represents a task that takes place during a specified time period. */
public class Event extends Task {
    private String from;
    private String to;

    /** Creates an event task. */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the starting time of the event. */
    public String getFrom() {
        return from;
    }

    /** Returns the ending time of the event. */
    public String getTo() {
        return to;
    }

    /** Returns the display representation of this event task. */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                    + " (from: " + from + " to: " + to + ")";
    }
}
