package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddAndRetrieveTasks() {
        Task task = new Task("Задача 1", "Описание", TaskStatus.NEW);
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        assertNotNull(retrievedTask, "Задача должна быть найдена.");
        assertEquals(task, retrievedTask, "Добавленная и найденная задачи должны совпадать.");
    }

    @Test
    void testAddAndRetrieveEpics() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);

        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(retrievedEpic, "Эпик должен быть найден.");
        assertEquals(epic, retrievedEpic, "Добавленный и найденный эпики должны совпадать.");
    }

    @Test
    void testAddAndRetrieveSubtasks() {
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
        Task originalTask = new Task("Задача 1", "Описание", TaskStatus.NEW);
        taskManager.createTask(originalTask);

        Task retrievedTask = taskManager.getTask(originalTask.getId());
        assertNotNull(retrievedTask, "Задача должна быть найдена.");
        assertEquals(originalTask.getTitle(), retrievedTask.getTitle(), "Названия задач должны совпадать.");
        assertEquals(originalTask.getDescription(), retrievedTask.getDescription(), "Описания задач должны совпадать.");
        assertEquals(originalTask.getStatus(), retrievedTask.getStatus(), "Статусы задач должны совпадать.");
    }

    @Test
    void testDeleteSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtask.getId());

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertNull(retrievedSubtask, "Подзадача должна быть удалена.");
        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertTrue(updatedEpic.getSubtaskIds().isEmpty(), "Эпик не должен содержать удаленные подзадачи.");
    }

    @Test
    void testUpdateEpicStatus() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", TaskStatus.DONE, epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void testSubtaskIdRemovalOnDeletion() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtask.getId());

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertFalse(updatedEpic.getSubtaskIds().contains(subtask.getId()), "ID удаленной подзадачи не должен оставаться в списке подзадач эпика.");
    }

    @Test
    void testTaskFieldUpdate() {
        Task task = new Task("Задача 1", "Описание", TaskStatus.NEW);
        taskManager.createTask(task);
        task.setTitle("Измененное название");
        task.setDescription("Измененное описание");
        task.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTask(task.getId());
        assertEquals("Измененное название", updatedTask.getTitle(), "Название задачи должно быть обновлено.");
        assertEquals("Измененное описание", updatedTask.getDescription(), "Описание задачи должно быть обновлено.");
        assertEquals(TaskStatus.DONE, updatedTask.getStatus(), "Статус задачи должен быть обновлен.");
    }
}
