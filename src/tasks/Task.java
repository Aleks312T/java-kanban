package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import main.Status;

public class Task {
    private String name;
    private String description;
    private int id;                                           //Пускай это будет и id задачи, и хэш-кодом

    protected Status status;
    protected TaskTypes taskType;
    protected int parent;

    protected static LocalDateTime startTime;                  //Время начала выполнения задачи
    protected static LocalDateTime endTime;                    //Время окончания выполнения задачи
    protected static Duration duration;                        //Длительность выполнения задачи

    public Task(String name, String description, Status status)
    {
        if(name != null && description != null && status != null && status != Status.NONE)
        {
            this.name = name;
            this.description = description;
            this.status = status;
            this.taskType = TaskTypes.TASK;
            startTime = LocalDateTime.now();
            duration = Duration.ofHours(1);
            endTime = startTime.plus(duration);
            //В учет HashCode / ID идет имя, описание, время начала и длительность.
            this.id = 17 * 923521 + name.hashCode() * 29791 + description.hashCode() * 961 +
                    startTime.hashCode() * 31 + duration.hashCode();
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

    public static LocalDateTime getStartTime() {
        return startTime;
    }

    public static LocalDateTime getEndTime() {
        return endTime;
    }

    public static Duration getDuration() {
        return duration;
    }

    public int getParent() {
        return parent;
    }

    @Override
    public String toString() {

        return "tasks.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
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
