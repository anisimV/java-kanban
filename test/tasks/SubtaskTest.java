package tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.Managers;

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

    @Test
    void testSubtaskCannotBeCreatedWithoutEpic() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи", TaskStatus.NEW, 999);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask));
        assertEquals("Эпик с id 999 не существует.", exception.getMessage(), "Подзадача не может быть создана для несуществующего эпика.");
    }
}
