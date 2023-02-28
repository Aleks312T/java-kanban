package tasks;

import main.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task
{
    public SubTask(String name, String description, Status status, int parent)
    {
        super(name, description, status);
        this.taskType = TaskTypes.SUBTASK;
        this.parent = parent;
    }

    public SubTask(String name, String description, Status status, LocalDateTime localDateTime, int parent)
    {
        super(name, description, status, localDateTime);
        this.taskType = TaskTypes.SUBTASK;
        this.parent = parent;
    }

    public SubTask(String name, String description, Status status,
                   LocalDateTime localDateTime, Duration duration, int parent)
    {
        super(name, description, status, localDateTime, duration);
        this.taskType = TaskTypes.SUBTASK;
        this.parent = parent;
    }

    @Override
    public String toString() {
        long s = duration.getSeconds();
        String durationOutput = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", taskType=" + taskType +
                ", parent=" + parent +
                ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", endTime=" + endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + durationOutput +
                ", parent id=" + parent +
                '}';
    }
}
