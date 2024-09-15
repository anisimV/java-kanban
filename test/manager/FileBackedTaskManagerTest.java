package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("taskManagerTest", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void shouldSaveAndLoadEmptyFile() throws IOException {
        manager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(manager.getAllTasks().isEmpty(), "Список задач должен быть пустым");
        assertTrue(manager.getAllEpics().isEmpty(), "Список эпиков должен быть пустым");
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым");
    }

    @Test
    void shouldSaveAndLoadMultipleTasks() throws IOException {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        manager.createTask(task1); // Добавляем задачу в менеджер, чтобы присвоить ей ID

        Epic epic1 = new Epic("Epic 1", "Description 1", TaskStatus.NEW);
        manager.createEpic(epic1); // Создаём эпик, чтобы у него появился ID

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subtask1); // Создаём подзадачу с корректным ID эпика

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(4, lines.size(), "Файл должен содержать 4 строки: заголовок и 3 задачи");

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> loadedTasks = loadedManager.getAllTasks();
        List<Epic> loadedEpics = loadedManager.getAllEpics();
        List<Subtask> loadedSubtasks = loadedManager.getAllSubtasks();

        assertEquals(1, loadedTasks.size(), "Должна быть одна задача");
        assertEquals("Task 1", loadedTasks.get(0).getTitle());

        assertEquals(1, loadedEpics.size(), "Должен быть один эпик");
        assertEquals("Epic 1", loadedEpics.get(0).getTitle());

        assertEquals(1, loadedSubtasks.size(), "Должна быть одна подзадача");
        assertEquals("Subtask 1", loadedSubtasks.get(0).getTitle());
        assertEquals(epic1.getId(), loadedSubtasks.get(0).getEpicId(), "ID эпика в подзадаче должен совпадать");
    }


    @Test
    void shouldLoadMultipleTasksFromFile() throws IOException {
        // Создаём задачи
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW);
        manager.createTask(task1); // Добавляем задачу в менеджер, чтобы присвоить ей ID

        Epic epic1 = new Epic("Epic 1", "Description 1", TaskStatus.NEW);
        manager.createEpic(epic1); // Создаём эпик, чтобы получить его ID

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW, epic1.getId());
        manager.createSubtask(subtask1); // Создаём подзадачу с корректным ID эпика

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getAllTasks().size(), "Должна быть одна задача");
        assertEquals(1, loadedManager.getAllEpics().size(), "Должен быть один эпик");
        assertEquals(1, loadedManager.getAllSubtasks().size(), "Должна быть одна подзадача");
        assertEquals(epic1.getId(), loadedManager.getAllSubtasks().get(0).getEpicId(), "ID эпика в подзадаче должен совпадать");
    }
}
