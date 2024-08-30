package tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

public class TaskTest {

    @Test
    void taskEqualityById() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.IN_PROGRESS);

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1.getId(), task2.getId(), "Задачи не равны по id.");
    }
}
