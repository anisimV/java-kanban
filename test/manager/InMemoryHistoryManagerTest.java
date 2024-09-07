package manager;

import tasks.Task;
import tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    @Test
    void testAddTaskToHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Задача 1", "Описание", TaskStatus.NEW);
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не должна быть пустой.");
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task, history.get(0), "Задача в истории не совпадает с добавленной задачей.");
    }

    @Test
    void testAddTaskTwiceKeepsOnlyLast() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Задача 1", "Описание", TaskStatus.NEW);
        task.setId(1);

        historyManager.add(task);
        historyManager.add(task); // Добавляем снова

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "История должна содержать только один экземпляр задачи.");
        assertEquals(task, history.get(0), "В истории должна быть только последняя версия задачи.");
    }

    @Test
    void testRemoveTaskFromHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание", TaskStatus.NEW);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();

        assertFalse(history.contains(task1), "История не должна содержать удаленную задачу.");
        assertTrue(history.contains(task2), "История должна содержать задачу, которая не была удалена.");
    }

    @Test
    void testHistoryMaintainsOrder() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание", TaskStatus.NEW);
        task2.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();

        assertEquals(task1, history.get(0), "Первая задача в истории должна быть task1.");
        assertEquals(task2, history.get(1), "Вторая задача в истории должна быть task2.");
    }

    @Test
    void testRemoveNodeFromHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание", TaskStatus.NEW);
        task2.setId(2);
        Task task3 = new Task("Задача 3", "Описание", TaskStatus.NEW);
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать две задачи после удаления.");
        assertEquals(task1, history.get(0), "Первая задача в истории должна быть task1.");
        assertEquals(task3, history.get(1), "Вторая задача в истории должна быть task3.");
    }
}
