package tasks;

import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubtaskTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void subtaskCannotBeItsOwnEpic() {
        Subtask subtask = new Subtask("Задача 1", "Описание подзадачи", TaskStatus.NEW, 1);

        try {
            taskManager.createSubtask(subtask);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Эпик с id 1 не существует.", e.getMessage());
        }
    }
}
