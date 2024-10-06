package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import exceptions.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("task_manager_test", ".csv");
        tempFile.deleteOnExit();
        taskManager = createTaskManager();
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(tempFile);
    }

    @Test
    @DisplayName("Тест сохранения и загрузки пустого файла")
    public void testSaveAndLoadEmptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    @DisplayName("Тест сохранения нескольких задач")
    public void testSaveMultipleTasks() {
        LocalDateTime now = LocalDateTime.now();

        Task task1 = new Task("Задача 1", "Описание  1", TaskStatus.NEW, Duration.ofMinutes(30), now);
        Task task2 = new Task("Задача 2", "Описание  2", TaskStatus.NEW, Duration.ofMinutes(30), now.plusMinutes(40));
        Task task3 = new Task("Задача 3", "Описание  3", TaskStatus.NEW, Duration.ofMinutes(30), now.plusMinutes(80));

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(3, loadedManager.getAllTasks().size());
        assertEquals(task1.getTitle(), loadedManager.getAllTasks().get(0).getTitle());
        assertEquals(task2.getTitle(), loadedManager.getAllTasks().get(1).getTitle());
        assertEquals(task3.getTitle(), loadedManager.getAllTasks().get(2).getTitle());
    }

    @Test
    @DisplayName("Тест загрузки нескольких задач")
    public void testLoadMultipleTasks() {
        LocalDateTime now = LocalDateTime.now();

        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW, Duration.ofMinutes(30), now);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW, Duration.ofMinutes(45), now.plusMinutes(40));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getAllTasks().size());

        Task loadedTask1 = loadedManager.getAllTasks().get(0);
        Task loadedTask2 = loadedManager.getAllTasks().get(1);

        assertEquals(task1.getTitle(), loadedTask1.getTitle());
        assertEquals(task1.getDescription(), loadedTask1.getDescription());
        assertEquals(task1.getStatus(), loadedTask1.getStatus());
        assertEquals(task1.getDuration(), loadedTask1.getDuration());
        assertEquals(task1.getStartTime(), loadedTask1.getStartTime());

        assertEquals(task2.getTitle(), loadedTask2.getTitle());
        assertEquals(task2.getDescription(), loadedTask2.getDescription());
        assertEquals(task2.getStatus(), loadedTask2.getStatus());
        assertEquals(task2.getDuration(), loadedTask2.getDuration());
        assertEquals(task2.getStartTime(), loadedTask2.getStartTime());
    }

    @Test
    @DisplayName("Тест выброса исключения при сохранении в недоступный файл")
    public void testSaveException() {
        File readOnlyFile = new File("/path/to/read-only/file.csv");

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager = new FileBackedTaskManager(readOnlyFile);
            manager.createTask(new Task("Задача  1", "Описание  1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        }, "При сохранении в недоступный файл должно возникнуть исключение ManagerSaveException");
    }

    @Test
    @DisplayName("Тест отсутствия исключения при сохранении в доступный файл")
    public void testNoException() {
        assertDoesNotThrow(() -> {
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
            manager.createTask(new Task("Задача 1", "Описание  1", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        }, "Создание задачи в рабочем менеджере не должно приводить к исключению");
    }
}
