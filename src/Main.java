import manager.TaskManager;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

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

        // Выводим все задачи, эпики и подзадачи
        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nВсе эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nВсе подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        // Обновляем статусы задач и эпиков
        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        subtask3.setStatus(TaskStatus.DONE);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        // Выводим обновленные задачи и эпики
        System.out.println("\nОбновленные задачи и эпики:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        // Удаляем задачи
        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteEpicById(epic1.getId());

        // Выводим задачи после удаления
        System.out.println("\nЗадачи после удаления:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        // Выводим эпики после удаления
        System.out.println("\nЭпики после удаления:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic);
        }
    }
}
