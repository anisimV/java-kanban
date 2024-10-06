import manager.*;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Проверка функционала",
                "Проверить основные функции приложения",
                TaskStatus.NEW,
                Duration.ofMinutes(60),
                LocalDateTime.now());
        Task task2 = new Task("Рефакторинг кода",
                "Улучшить архитектуру и чистоту кода",
                TaskStatus.IN_PROGRESS,
                Duration.ofMinutes(120),
                LocalDateTime.now().plusHours(1));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Разработка новой функциональности",
                "Добавить возможность загрузки файлов",
                TaskStatus.NEW);
        Epic epic2 = new Epic("Исправление ошибок",
                "Исправить выявленные баги",
                TaskStatus.NEW);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Написать модуль загрузки",
                "Написать основной код для загрузки файлов",
                TaskStatus.NEW,
                epic1.getId(),
                Duration.ofMinutes(30),
                LocalDateTime.now().plusHours(4));
        Subtask subtask2 = new Subtask("Протестировать загрузку",
                "Провести тестирование загрузки файлов",
                TaskStatus.NEW,
                epic1.getId(),
                Duration.ofMinutes(45),
                LocalDateTime.now().plusHours(5));

        Subtask subtask3 = new Subtask("Анализировать логи",
                "Провести анализ логов для поиска ошибок",
                TaskStatus.NEW,
                epic2.getId(),
                Duration.ofMinutes(90),
                LocalDateTime.now().plusHours(6));

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask3.getId());

        System.out.println("\nИстория просмотров:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getTitle());
        }

        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteEpicById(epic1.getId());

        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic2.getId());

        System.out.println("\nОбновленная история просмотров:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getTitle());
        }
    }
}
