package manager;

import tasks.*;

import java.util.Comparator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void createTask(Task task) {
        validateTaskTime(task);
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        validateTaskTime(subtask);
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

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateTask(Task updatedTask) {
        validateTaskTime(updatedTask);
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            Epic existingEpic = epics.get(updatedEpic.getId());
            existingEpic.setTitle(updatedEpic.getTitle());
            existingEpic.setDescription(updatedEpic.getDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        validateTaskTime(updatedSubtask);
        if (subtasks.containsKey(updatedSubtask.getId())) {
            Subtask subtask = subtasks.get(updatedSubtask.getId());
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
        } else {
            throw new IllegalArgumentException("Эпик с id " + updatedSubtask.getEpicId() + " не существует.");
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            epic.getSubtaskIds().forEach(subtaskId -> {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            });
            historyManager.remove(id);
        }
    }


    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskIds().remove(Integer.valueOf(id));
                updateEpicStatus(epic);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasksForEpic(Epic epic) {
        return epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .toList();
    }

    @Override
    public void deleteAllTasks() {
        List<Integer> taskIds = new ArrayList<>(tasks.keySet());
        for (Integer taskId : taskIds) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        List<Integer> epicIds = new ArrayList<>(epics.keySet());
        for (Integer epicId : epicIds) {
            Epic epic = epics.get(epicId);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            historyManager.remove(epicId);
        }
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().stream()
                .forEach(id -> {
                    Subtask subtask = subtasks.remove(id);
                    if (subtask != null) {
                        Epic epic = epics.get(subtask.getEpicId());
                        epic.getSubtaskIds().remove(Integer.valueOf(id));
                        historyManager.remove(id);
                    }
                });
        epics.values().forEach(epic -> epic.setStatus(TaskStatus.NEW));
    }


    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasksForEpic = getAllSubtasksForEpic(epic);
        if (subtasksForEpic.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (Subtask subtask : subtasksForEpic) {
            if (subtask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
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


    private boolean isOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = start1.plus(Duration.ofMinutes(task1.getDuration().toMinutes()));
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = start2.plus(Duration.ofMinutes(task2.getDuration().toMinutes()));

        return (start1.isBefore(end2) && end1.isAfter(start2));
    }

    private void validateTaskTime(Task task) {
        LocalDateTime newStartTime = task.getStartTime();
        Duration newDuration = task.getDuration();
        LocalDateTime newEndTime = newStartTime.plus(newDuration);

        for (Task existingTask : getAllTasks()) {
            if (existingTask.getId() != task.getId()) {
                LocalDateTime existingStartTime = existingTask.getStartTime();
                LocalDateTime existingEndTime = existingStartTime.plus(existingTask.getDuration());

                if ((newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime))) {
                    throw new IllegalArgumentException("Задача пересекается с другой задачей по времени.");
                }
            }
        }
    }


    public List<Task> getPrioritizedTasks() {
        List<Task> prioritizedTasks = new ArrayList<>();

        prioritizedTasks.addAll(getAllTasks());
        prioritizedTasks.addAll(getAllSubtasks());

        prioritizedTasks.removeIf(task -> task.getStartTime() == null);

        prioritizedTasks.sort(Comparator.comparing(Task::getStartTime));

        return prioritizedTasks;
    }
}

