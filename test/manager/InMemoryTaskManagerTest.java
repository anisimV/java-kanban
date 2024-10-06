package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    private Task createTask(String title, String description, TaskStatus status) {
        return new Task(title, description, status);
    }

    private Task createTask(String title, String description, TaskStatus status, LocalDateTime startTime) {
        return new Task(title, description, status, Duration.ofMinutes(30), startTime);
    }

    private Subtask createSubtask(String title, String description, TaskStatus status, int epicId, LocalDateTime startTime) {
        return new Subtask(title, description, status, epicId, Duration.ofMinutes(30), startTime);
    }

    @Test
    @DisplayName("Тест добавления и получения задачи")
    void testAddAndRetrieveTasks() {
        Task task = createTask("Задача 1", "Описание", TaskStatus.NEW);
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
        Subtask subtask = createSubtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId(), LocalDateTime.of(2024, 10, 1, 11, 0));
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(retrievedSubtask, "Подзадача должна быть найдена.");
        assertEquals(subtask, retrievedSubtask, "Добавленная и найденная подзадачи должны совпадать.");
    }

    @Test
    @DisplayName("Тест на уникальность ID задач")
    void testNoIdConflictInManager() {
        Task taskWithGeneratedId = createTask("Задача 1", "Описание", TaskStatus.NEW, LocalDateTime.of(2024, 10, 1, 10, 0));
        Task taskWithGivenId = new Task("Задача 2", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 1, 11, 0));
        taskWithGivenId.setId(999); // Устанавливаем ID для второй задачи

        taskManager.createTask(taskWithGeneratedId);
        taskManager.createTask(taskWithGivenId);

        Task retrievedTaskWithGeneratedId = taskManager.getTask(taskWithGeneratedId.getId());
        Task retrievedTaskWithGivenId = taskManager.getTask(taskWithGivenId.getId());

        assertNotEquals(retrievedTaskWithGeneratedId.getId(), retrievedTaskWithGivenId.getId(), "ID задач не должны конфликтовать.");
        assertEquals(2, taskManager.getAllTasks().size(), "Должно быть две задачи в менеджере.");
    }

    @Test
    @DisplayName("Тест на неизменяемость задачи после добавления")
    void testTaskImmutabilityOnAddition() {
        Task originalTask = createTask("Задача 1", "Описание", TaskStatus.NEW, LocalDateTime.of(2024, 10, 1, 10, 0));
        taskManager.createTask(originalTask);

        Task retrievedTask = taskManager.getTask(originalTask.getId());
        assertNotNull(retrievedTask, "Задача должна быть найдена.");
        assertEquals(originalTask.getTitle(), retrievedTask.getTitle(), "Названия задач должны совпадать.");
        assertEquals(originalTask.getDescription(), retrievedTask.getDescription(), "Описания задач должны совпадать.");
        assertEquals(originalTask.getStatus(), retrievedTask.getStatus(), "Статусы задач должны совпадать.");
    }

    @Test
    @DisplayName("Тест удаления подзадачи")
    void testDeleteSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = createSubtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId(), LocalDateTime.of(2024, 10, 1, 11, 0));
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtask.getId());

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertNull(retrievedSubtask, "Подзадача должна быть удалена.");
        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertFalse(updatedEpic.getSubtaskIds().contains(subtask.getId()), "Эпик не должен содержать удаленные подзадачи.");
    }

    @Test
    @DisplayName("Тест обновления статуса эпика")
    void testUpdateEpicStatus() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask1 = createSubtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId(), LocalDateTime.of(2024, 10, 1, 12, 0));
        Subtask subtask2 = createSubtask("Подзадача 2", "Описание", TaskStatus.DONE, epic.getId(), LocalDateTime.of(2024, 10, 1, 13, 0));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedEpic.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    @DisplayName("Тест удаления ID подзадачи из эпика при удалении подзадачи")
    void testSubtaskIdRemovalOnDeletion() {
        Epic epic = new Epic("Эпик 1", "Описание", TaskStatus.NEW);
        taskManager.createEpic(epic);
        Subtask subtask = createSubtask("Подзадача 1", "Описание", TaskStatus.NEW, epic.getId(), LocalDateTime.of(2024, 10, 1, 11, 0));
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtaskById(subtask.getId());

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertFalse(updatedEpic.getSubtaskIds().contains(subtask.getId()), "ID удаленной подзадачи не должен оставаться в списке подзадач эпика.");
    }

    @Test
    @DisplayName("Тест обновления полей задачи")
    void testTaskFieldUpdate() {
        Task task = createTask("Задача 1", "Описание", TaskStatus.NEW, LocalDateTime.of(2024, 10, 1, 10, 0));
        taskManager.createTask(task);

        task.setTitle("Измененное название");
        task.setDescription("Измененное описание");
        task.setStatus(TaskStatus.DONE);
        task.setStartTime(LocalDateTime.of(2024, 10, 1, 11, 0));
        taskManager.updateTask(task);

        Task updatedTask = taskManager.getTask(task.getId());
        assertEquals("Измененное название", updatedTask.getTitle(), "Название задачи должно быть обновлено.");
        assertEquals("Измененное описание", updatedTask.getDescription(), "Описание задачи должно быть обновлено.");
        assertEquals(TaskStatus.DONE, updatedTask.getStatus(), "Статус задачи должен быть обновлен.");
    }
}
