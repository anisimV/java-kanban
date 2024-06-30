public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создаем задачи, эпики, подзадачи
        Task task1 = new Task("Проверить почту", "Прочитать и ответить на новые письма", TaskStatus.NEW);
        Task task2 = new Task("Подготовить отчет", "Сделать отчет за неделю", TaskStatus.IN_PROGRESS);

        Epic epic1 = new Epic("Разработка новой функции", "Реализовать новую функцию в приложении", TaskStatus.NEW);
        Subtask subtask1 = new Subtask("Написать код", "Написать основной код функции", TaskStatus.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Протестировать", "Протестировать новую функцию", TaskStatus.NEW, epic1.getId());

        Epic epic2 = new Epic("Исправление ошибок", "Исправить баги в приложении", TaskStatus.NEW);
        Subtask subtask3 = new Subtask("Найти баги", "Проанализировать логи и найти ошибки", TaskStatus.NEW, epic2.getId());

        // Добавяем задачи, эпики, подзадачи в менеджер задач
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(epic1);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        taskManager.createTask(epic2);
        taskManager.createTask(subtask3);

        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nВсе эпики:");
        for (Task task : taskManager.getAllTasks()) {
            if (task instanceof Epic) {
                System.out.println(task);
            }
        }

        System.out.println("\nВсе подзадачи:");
        for (Task task : taskManager.getAllTasks()) {
            if (task instanceof Subtask) {
                System.out.println(task);
            }
        }

        // Обновляем статусы
        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        subtask3.setStatus(TaskStatus.DONE);

        taskManager.updateTask(subtask1);
        taskManager.updateTask(subtask2);
        taskManager.updateTask(subtask3);

        taskManager.updateEpicStatus(epic1);
        taskManager.updateEpicStatus(epic2);

        System.out.println("\nОбновленные задачи и эпики:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }

        // Удаляем задачи
        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteTaskById(epic1.getId());

        System.out.println("\nЗадачи после удаления:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task);
        }
    }
}
