package tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;
import manager.InMemoryTaskManager;
import java.time.Duration;
import java.time.LocalDateTime;


public class EpicTest {

    @Test
    @DisplayName("Эпик не может быть подзадачей сам себе")
    void testCannotAddSelfAsSubtask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        epic.setId(1);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 1, Duration.ofMinutes(30), LocalDateTime.now());

        Executable executable = () -> taskManager.createSubtask(subtask);

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    @DisplayName("Эпик должен иметь статус NEW, если все подзадачи имеют статус NEW")
    void testEpicStatusAllSubtasksNew() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        epic.setId(1);
        taskManager.createEpic(epic);

        LocalDateTime now = LocalDateTime.now();

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), now);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), now.plusMinutes(31));
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), now.plusMinutes(62));

        subtask1.setId(2);
        subtask2.setId(3);
        subtask3.setId(4);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        TaskStatus epicStatus = taskManager.getEpic(epic.getId()).getStatus();

        assertEquals(TaskStatus.NEW, epicStatus);
    }

    @Test
    @DisplayName("Эпик должен иметь статус DONE, если все подзадачи завершены")
    void testEpicStatusAllSubtasksDone() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        epic.setId(1);
        taskManager.createEpic(epic);

        LocalDateTime now = LocalDateTime.now();
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.DONE, epic.getId(), Duration.ofMinutes(30), now);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, epic.getId(), Duration.ofMinutes(45), now.plusMinutes(31));

        subtask1.setId(2);
        subtask2.setId(3);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        TaskStatus epicStatus = taskManager.getEpic(epic.getId()).getStatus();

        assertEquals(TaskStatus.DONE, epicStatus);
    }


    @Test
    @DisplayName("Эпик должен иметь статус IN_PROGRESS, если подзадачи имеют смешанные статусы NEW и DONE")
    void testEpicStatusNewAndDoneSubtasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        epic.setId(1);
        taskManager.createEpic(epic);

        LocalDateTime now = LocalDateTime.now();
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), now);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, epic.getId(), Duration.ofMinutes(30), now.plusMinutes(31));

        subtask1.setId(2);
        subtask2.setId(3);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        TaskStatus epicStatus = taskManager.getEpic(epic.getId()).getStatus();

        assertEquals(TaskStatus.IN_PROGRESS, epicStatus);
    }

    @Test
    @DisplayName("Эпик должен иметь статус IN_PROGRESS, если все подзадачи имеют статус IN_PROGRESS")
    void testEpicStatusAllSubtasksInProgress() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        epic.setId(1);
        taskManager.createEpic(epic);

        LocalDateTime now = LocalDateTime.now();

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(30), now);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(30), now.plusMinutes(31));
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(30), now.plusMinutes(62));

        subtask1.setId(2);
        subtask2.setId(3);
        subtask3.setId(4);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        TaskStatus epicStatus = taskManager.getEpic(epic.getId()).getStatus();

        assertEquals(TaskStatus.IN_PROGRESS, epicStatus);
    }
}
