package manager;

import tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    abstract void setUp() throws IOException;

    protected abstract T createTaskManager();

    @Test
    @DisplayName("Тест добавления и получения задачи")
    void testAddAndRetrieveTasks() {
        Task task = new Task("Задача 1", "Описание", TaskStatus.NEW);
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        assertNotNull(retrievedTask, "Задача должна быть найдена.");
        assertEquals(task, retrievedTask, "Добавленная и найденная задачи должны совпадать.");
    }

    @Test
    @DisplayName("Тест добавления и получения эпика")
    void testAddAndRetrieveEpics() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);

        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(retrievedEpic, "Эпик должен быть найден.");
        assertEquals(epic, retrievedEpic, "Добавленный и найденный эпики должны совпадать.");
    }

    @Test
    @DisplayName("Тест добавления и получения подзадачи")
    void testAddAndRetrieveSubtasks() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(retrievedSubtask, "Подзадача должна быть найдена.");
        assertEquals(subtask, retrievedSubtask, "Добавленная и найденная подзадачи должны совпадать.");
    }

    @Test
    @DisplayName("Тест на уникальность ID задач")
    void testNoIdConflictInManager() {
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task("Задача 1", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), now);
        Task task2 = new Task("Задача 2", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), now.plusMinutes(40));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertNotEquals(task1.getId(), task2.getId(), "ID задач не должны конфликтовать.");
    }

    @Test
    @DisplayName("Тест на проверку наличия эпика у подзадачи")
    void testSubtaskMustHaveEpic() {
        Subtask subtask = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW, 999, Duration.ofMinutes(30), LocalDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask), "Подзадача должна иметь существующий эпик.");
    }

    @Test
    @DisplayName("Тест вычисления статуса эпика")
    void testEpicStatusCalculation() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", TaskStatus.DONE, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now());

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    @DisplayName("Тест на пересечение интервалов задач")
    void testTaskIntervalOverlap() {
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task("Задача 1", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), now);
        Task task2 = new Task("Задача 2", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), now.plusMinutes(15));

        taskManager.createTask(task1);
        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2), "Задачи не должны пересекаться по времени.");
    }
}
