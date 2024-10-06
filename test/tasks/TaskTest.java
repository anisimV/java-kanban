package tasks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;

public class TaskTest {

    @Test
    @DisplayName("Тест на проверку равенства задачи по id")
    void taskEqualityById() {
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(60);

        Task task = new Task("Задача", "Описание", TaskStatus.NEW, duration, startTime);
        task.setId(1);

        assertEquals(task.getId(), task.getId(), "Задача не равна сама себе.");

        Task sameIdTask = new Task("Другая задача", "Описание", TaskStatus.NEW, duration, startTime);
        sameIdTask.setId(1);

        assertEquals(sameIdTask.getId(), task.getId(), "Задачи с одинаковыми id не равны.");

        Task differentIdTask = new Task("Задача", "Описание", TaskStatus.NEW, duration, startTime);
        differentIdTask.setId(2);

        assertNotEquals(differentIdTask.getId(), task.getId(), "Задачи с разными id должны быть не равны.");
    }
}
