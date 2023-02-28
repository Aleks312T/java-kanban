package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import main.Status;

public class Task {
    protected String name;
    protected String description;
    protected int id;                                           //Пускай это будет и id задачи, и хэш-кодом

    protected Status status;
    protected TaskTypes taskType;
    protected int parent;

    protected LocalDateTime startTime;                  //Время начала выполнения задачи
    protected LocalDateTime endTime;                    //Время окончания выполнения задачи
    protected Duration duration;                        //Длительность выполнения задачи

    public Task(String name, String description, Status status)
    {
        if(name != null && description != null && status != null && status != Status.NONE)
        {
            this.name = name;
            this.description = description;
            this.status = status;
            this.taskType = TaskTypes.TASK;
            this.startTime = LocalDateTime.now();
            this.startTime = this.startTime.minusNanos(this.startTime.getNano());
            this.duration = Duration.ofHours(1);
            this.endTime = startTime.plus(duration);
            //В учет HashCode / ID идет имя, описание, время начала и длительность.
            this.id = 17 * 961 + name.hashCode() * 31 + description.hashCode();
            this.parent = this.id;
        }
    }

    public Task(String name, String description, Status status, LocalDateTime localDateTime)
    {
        if(name != null && description != null && status != null && status != Status.NONE)
        {
            this.name = name;
            this.description = description;
            this.status = status;
            this.taskType = TaskTypes.TASK;
            this.startTime = localDateTime;
            this.startTime = this.startTime.minusNanos(this.startTime.getNano());
            this.duration = Duration.ofHours(1);
            this.endTime = startTime.plus(duration);
            //В учет HashCode / ID идет имя, описание, время начала и длительность.
            this.id = 17 * 961 + name.hashCode() * 31 + description.hashCode();
            this.parent = this.id;
        }
    }

    public Task(String name, String description, Status status, LocalDateTime localDateTime, Duration duration)
    {
        if(name != null && description != null && status != null && status != Status.NONE)
        {
            this.name = name;
            this.description = description;
            this.status = status;
            this.taskType = TaskTypes.TASK;
            this.startTime = localDateTime;
            this.startTime = this.startTime.minusNanos(this.startTime.getNano());
            this.duration = duration;
            this.endTime = startTime.plus(duration);
            //В учет HashCode / ID идет имя, описание
            this.id = 17 * 961 + name.hashCode() * 31 + description.hashCode();
            this.parent = this.id;
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if(status != null)
            this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;                                             // возвращаем хеш
    }

    public TaskTypes getTaskType() {
        return taskType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getParent() {
        return parent;
    }

    @Override
    public String toString() {
        long s = duration.getSeconds();
        String durationOutput = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", taskType=" + taskType +
                ", parent=" + parent +
                ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", endTime=" + endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + durationOutput +
                '}';
    }

    @Override
    public int hashCode() {
        return id;                                              // возвращаем хеш
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name)
                && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }
}
