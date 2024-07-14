package manager;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    @Test
    void testAddAndRetrieveTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Задача 1", "Описание", TaskStatus.NEW);
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        assertNotNull(retrievedTask, "Задача должна быть найдена.");
        assertEquals(task, retrievedTask, "Добавленная и найденная задачи должны совпадать.");
    }

    @Test
    void testAddAndRetrieveEpics() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);

        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(retrievedEpic, "Эпик должен быть найден.");
        assertEquals(epic, retrievedEpic, "Добавленный и найденный эпики должны совпадать.");
    }

    @Test
    void testAddAndRetrieveSubtasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(retrievedSubtask, "Подзадача должна быть найдена.");
        assertEquals(subtask, retrievedSubtask, "Добавленная и найденная подзадачи должны совпадать.");
    }

    @Test
    void testNoIdConflictInManager() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task taskWithGeneratedId = new Task("Задача 1", "Описание", TaskStatus.NEW);
        Task taskWithGivenId = new Task("Задача 2", "Описание", TaskStatus.NEW);
        taskWithGivenId.setId(999);

        taskManager.createTask(taskWithGeneratedId);
        taskManager.createTask(taskWithGivenId);

        Task retrievedTaskWithGeneratedId = taskManager.getTask(taskWithGeneratedId.getId());
        Task retrievedTaskWithGivenId = taskManager.getTask(taskWithGivenId.getId());

        assertNotEquals(retrievedTaskWithGeneratedId.getId(), retrievedTaskWithGivenId.getId(), "ID задач не должны конфликтовать.");
        assertEquals(2, taskManager.getAllTasks().size(), "Должно быть две задачи в менеджере.");
    }

    @Test
    void testTaskImmutabilityOnAddition() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task originalTask = new Task("Задача 1", "Описание", TaskStatus.NEW);
        taskManager.createTask(originalTask);

        Task retrievedTask = taskManager.getTask(originalTask.getId());
        assertNotNull(retrievedTask, "Задача должна быть найдена.");
        assertEquals(originalTask.getTitle(), retrievedTask.getTitle(), "Названия задач должны совпадать.");
        assertEquals(originalTask.getDescription(), retrievedTask.getDescription(), "Описания задач должны совпадать.");
        assertEquals(originalTask.getStatus(), retrievedTask.getStatus(), "Статусы задач должны совпадать.");
    }
}
