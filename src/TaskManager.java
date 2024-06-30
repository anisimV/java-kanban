import java.util.*;

class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();

    // Получение всех задач
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Очистка всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Получение задачи по id
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    // Создание задачи или эпика
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            Epic epic = (Epic) tasks.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtaskId(subtask.getId());
            }
        }
    }

    // Обновление задачи или эпика
    public void updateTask(Task updatedTask) {
        tasks.put(updatedTask.getId(), updatedTask);
    }

    // Удаление задачи по идентификатору
    public void deleteTaskById(int id) {
        Task task = tasks.get(id);
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            for (int subtaskId : epic.getSubtaskIds()) {
                tasks.remove(subtaskId);
            }
        }
        tasks.remove(id);
    }

    // Получение всех подзадач для заданного эпика
    public List<Subtask> getAllSubtasksForEpic(Epic epic) {
        List<Subtask> subtasks = new ArrayList<>();
        for (int subtaskId : epic.getSubtaskIds()) {
            Task task = tasks.get(subtaskId);
            if (task instanceof Subtask) {
                subtasks.add((Subtask) task);
            }
        }
        return subtasks;
    }

    // Обновление статуса эпика на основе статусов его подзадач
    public void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;

        for (int subtaskId : epic.getSubtaskIds()) {
            TaskStatus status = tasks.get(subtaskId).getStatus();
            if (status != TaskStatus.DONE) {
                allDone = false;
            }
            if (status != TaskStatus.NEW) {
                allNew = false;
            }
        }

        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }

        updateTask(epic);
    }
}
