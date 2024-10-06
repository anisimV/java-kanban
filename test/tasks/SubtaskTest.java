package tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import manager.TaskManager;
import manager.Managers;
import java.time.Duration;
import java.time.LocalDateTime;


public class SubtaskTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("Подзадача не может быть своим собственным эпиком")
    void subtaskCannotBeItsOwnEpic() {
        Subtask subtask = new Subtask("Задача 1", "Описание подзадачи", TaskStatus.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());

        try {
            taskManager.createSubtask(subtask);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Эпик с id 1 не существует.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Подзадача не может быть создана без существующего эпика")
    void testSubtaskCannotBeCreatedWithoutEpic() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи", TaskStatus.NEW, 999, Duration.ofMinutes(30), LocalDateTime.now());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask));
        assertEquals("Эпик с id 999 не существует.", exception.getMessage(), "Подзадача не может быть создана для несуществующего эпика.");
    }
}
