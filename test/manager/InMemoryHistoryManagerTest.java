package manager;

import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private Task createTask(String title, String description, TaskStatus status, Duration duration, LocalDateTime startTime, int id) {
        Task task = new Task(title, description, status, duration, startTime);
        task.setId(id);
        return task;
    }

    @Test
    @DisplayName("Тест пустой истории")
    void testEmptyHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть пустой.");
        assertTrue(history.isEmpty(), "История должна быть пуста.");
    }

    @Test
    @DisplayName("Тест добавления дублирующихся задач")
    void testDuplicateTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = createTask("Задача 1", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 1);

        historyManager.add(task);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать только один экземпляр задачи.");
        assertEquals(task, history.get(0), "В истории должна быть только последняя версия задачи.");
    }

    @Test
    @DisplayName("Тест удаления задачи из начала истории")
    void testRemoveTaskFromBeginning() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = createTask("Задача 1", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        Task task2 = createTask("Задача 2", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу после удаления.");
        assertEquals(task2, history.get(0), "В истории должна остаться задача 2.");
    }

    @Test
    @DisplayName("Тест удаления задачи из середины истории")
    void testRemoveTaskFromMiddle() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = createTask("Задача 1", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        Task task2 = createTask("Задача 2", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 2);
        Task task3 = createTask("Задача 3", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertEquals(task1, history.get(0), "Первая задача в истории должна быть task1.");
        assertEquals(task3, history.get(1), "Вторая задача в истории должна быть task3.");
    }

    @Test
    @DisplayName("Тест удаления задачи с конца истории")
    void testRemoveTaskFromEnd() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = createTask("Задача 1", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 1);
        Task task2 = createTask("Задача 2", "Описание", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now(), 2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу после удаления.");
        assertEquals(task1, history.get(0), "В истории должна остаться задача 1.");
    }
}