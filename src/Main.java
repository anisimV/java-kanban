import manager.*;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // Создаем задачи, эпики, подзадачи
        Task task1 = new Task("Проверка функционала", "Проверить основные функции приложения", TaskStatus.NEW);
        Task task2 = new Task("Рефакторинг кода", "Улучшить архитектуру и чистоту кода", TaskStatus.IN_PROGRESS);

        // Добавляем задачи в менеджер задач
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Создаем эпики
        Epic epic1 = new Epic("Разработка новой функциональности", "Добавить возможность загрузки файлов", TaskStatus.NEW);
        Epic epic2 = new Epic("Исправление ошибок", "Исправить выявленные баги", TaskStatus.NEW);

        // Добавляем эпики в менеджер задач
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        // Создаем подзадачи для эпика epic1
        Subtask subtask1 = new Subtask("Написать модуль загрузки", "Написать основной код для загрузки файлов", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Протестировать загрузку", "Провести тестирование загрузки файлов", TaskStatus.NEW, epic1.getId());

        // Создаем подзадачу для эпика epic2
        Subtask subtask3 = new Subtask("Анализировать логи", "Провести анализ логов для поиска ошибок", TaskStatus.NEW, epic2.getId());

        // Добавляем подзадачи в менеджер задач
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        // Получаем задачи, эпики и подзадачи, чтобы обновить историю
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask3.getId());

        // Выводим историю просмотров
        System.out.println("\nИстория просмотров:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getTitle());
        }

        // Удаляем задачи и эпики
        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteEpicById(epic1.getId());

        // Повторно получаем задачи и эпики, чтобы обновить историю
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic2.getId());

        // Выводим обновленную историю просмотров
        System.out.println("\nОбновленная история просмотров:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task.getTitle());
        }
    }
}
