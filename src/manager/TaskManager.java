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
        tasks.put(task.getId(), task);
    }

    // Создание эпика
    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    // Создание подзадачи
    public void createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        if (epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        } else {
            throw new IllegalArgumentException("Эпик с id " + subtask.getEpicId() + " не существует.");
        }
    }

    // Обновление задачи
    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    // Обновление эпика
    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            Epic existingEpic = epics.get(updatedEpic.getId());
            existingEpic.setTitle(updatedEpic.getTitle());
            existingEpic.setDescription(updatedEpic.getDescription());
        }
    }

    // Обновление подзадачи
    public void updateSubtask(Subtask updatedSubtask) {
        if (subtasks.containsKey(updatedSubtask.getId())) {
            Subtask subtask = subtasks.get(updatedSubtask.getId());
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
        } else {
            throw new IllegalArgumentException("Эпик с id " + updatedSubtask.getEpicId() + " не существует.");
        }
    }

    // Удаление задачи по идентификатору
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    // Удаление эпика по идентификатору
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    // Удаление подзадачи по идентификатору
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskIds().remove(Integer.valueOf(id));
            updateEpicStatus(epic);
        }
    }

    // Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Удаление всех эпиков и связанных подзадач
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    // Удаление всех подзадач
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
        }
        subtasks.clear();
    }

    // Получение всех задач
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
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
    }
}
