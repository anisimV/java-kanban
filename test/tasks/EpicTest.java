package tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.function.Executable;

public class EpicTest {

    @Test
    void testCannotAddSelfAsSubtask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        epic.setId(1);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 1);

        Executable executable = () -> taskManager.createSubtask(subtask);

        assertThrows(IllegalArgumentException.class, executable);
    }
}
