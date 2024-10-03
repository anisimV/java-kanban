package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import manager.Managers;

public class Epic extends Task {
    private final List<Integer> subtaskIds;
    private LocalDateTime endTime;

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status, Duration.ZERO, LocalDateTime.now());
        this.subtaskIds = new ArrayList<>();
        this.endTime = LocalDateTime.now();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
        updateEpicTime();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subtaskIds.isEmpty()) {
            return LocalDateTime.now();
        }
        LocalDateTime earliestStart = null;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = (Subtask) Managers.getDefault().getSubtask(subtaskId);
            if (subtask != null) {
                LocalDateTime subtaskStart = subtask.getStartTime();
                if (earliestStart == null || (subtaskStart != null && subtaskStart.isBefore(earliestStart))) {
                    earliestStart = subtaskStart;
                }
            }
        }
        return earliestStart;
    }

    @Override
    public Duration getDuration() {
        Duration totalDuration = Duration.ZERO;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = (Subtask) Managers.getDefault().getSubtask(subtaskId);
            if (subtask != null) {
                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }
        return totalDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtaskIds.isEmpty()) {
            return LocalDateTime.now();
        }
        LocalDateTime latestEnd = null;

        for (int subtaskId : subtaskIds) {
            Subtask subtask = (Subtask) Managers.getDefault().getSubtask(subtaskId);
            if (subtask != null) {
                LocalDateTime subtaskEnd = subtask.getEndTime();
                if (latestEnd == null || (subtaskEnd != null && subtaskEnd.isAfter(latestEnd))) {
                    latestEnd = subtaskEnd;
                }
            }
        }
        return latestEnd;
    }

    private void updateEpicTime() {
        setDuration(getDuration());
        setStartTime(getStartTime());
        endTime = getEndTime();
    }
}
