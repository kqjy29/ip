package sylveon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests Sylveon's command processing and sorted-task numbering. */
public class SylveonTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    public void sortDeadline_mixedTasks_displaysOnlyDeadlinesInDateOrder() {
        Sylveon sylveon = createSylveon();
        sylveon.getResponse("todo unrelated task");
        sylveon.getResponse("deadline later /by 2026-09-20");
        sylveon.getResponse("deadline earlier /by 2026-09-10");

        String response = sylveon.getResponse("sort deadline");

        assertEquals("Sorted deadlines by date (earliest first):\n"
                + "   1. [D][ ] earlier (by: Sept 10 2026)\n"
                + "   2. [D][ ] later (by: Sept 20 2026)", response);
    }

    @Test
    public void markAfterSortDeadline_marksTheDisplayedDeadline() {
        Sylveon sylveon = createSylveon();
        sylveon.getResponse("todo unrelated task");
        sylveon.getResponse("deadline later /by 2026-09-20");
        sylveon.getResponse("deadline earlier /by 2026-09-10");
        sylveon.getResponse("sort deadline");

        String response = sylveon.getResponse("mark 1");

        assertEquals("Great! I've marked this task as done <3\n"
                + "[D][X] earlier (by: Sept 10 2026)", response);
    }

    @Test
    public void eventWithNonIsoDate_returnsDateFormatError() {
        Sylveon sylveon = createSylveon();

        String response = sylveon.getResponse("event meeting /from tomorrow /to 2026-09-20");

        assertEquals("Error! Event dates must use the format yyyy-mm-dd :)", response);
    }

    private Sylveon createSylveon() {
        return new Sylveon(temporaryDirectory.resolve("tasks.txt").toString());
    }
}
