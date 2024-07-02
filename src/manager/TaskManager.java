package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int nextId = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    // Создание задачи
    public void createTask(Task task) {
        task.setId(nextId++);
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            if (epics.containsKey(subtask.getEpicId())) {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
                updateEpicStatus(epic);
            } else {
                throw new IllegalArgumentException("Эпик с id " + subtask.getEpicId() + " не существует.");
            }
        } else {
            tasks.put(task.getId(), task);
        }
    }

    // Обновление задачи
    public void updateTask(Task updatedTask) {
        if (updatedTask instanceof Epic && epics.containsKey(updatedTask.getId())) {
            epics.put(updatedTask.getId(), (Epic) updatedTask);
        } else if (updatedTask instanceof Subtask && subtasks.containsKey(updatedTask.getId())) {
            Subtask subtask = (Subtask) updatedTask;
            if (epics.containsKey(subtask.getEpicId())) {
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                updateEpicStatus(epic);
            } else {
                throw new IllegalArgumentException("Эпик с id " + subtask.getEpicId() + " не существует.");
            }
        } else if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    // Удаление задачи по идентификатору
    public void deleteTaskById(int id) {
        if (epics.containsKey(id)) {
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskIds().remove(Integer.valueOf(id));
            updateEpicStatus(epic);
            subtasks.remove(id);
        } else if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    // Получение всех задач
    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        allTasks.addAll(epics.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    // Получение всех эпиков
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение всех подзадач
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Получение всех подзадач для заданного эпика
    public List<Subtask> getAllSubtasksForEpic(Epic epic) {
        List<Subtask> subtasksForEpic = new ArrayList<>();
        for (int subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                subtasksForEpic.add(subtask);
            }
        }
        return subtasksForEpic;
    }

    // Приватный метод для обновления статуса эпика
    private void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;

        for (int subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                TaskStatus status = subtask.getStatus();
                if (status != TaskStatus.DONE) {
                    allDone = false;
                }
                if (status != TaskStatus.NEW) {
                    allNew = false;
                }
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

    // Методы для создания, изменения и удаления задач разных типов
    public void createEpic(Epic epic) {
        createTask(epic);
    }

    public void updateEpic(Epic epic) {
        updateTask(epic);
    }

    public void deleteEpicById(int id) {
        deleteTaskById(id);
    }

    public void createSubtask(Subtask subtask) {
        createTask(subtask);
    }

    public void updateSubtask(Subtask subtask) {
        updateTask(subtask);
    }

    public void deleteSubtaskById(int id) {
        deleteTaskById(id);
    }
}
