package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import main.Status;

public class Epic extends Task
{
    public Epic(String name, String description, Status status)
    {
        super(name, description, status);
        this.taskType = TaskTypes.EPIC;
    }

    public Epic(String name, String description, Status status, LocalDateTime localDateTime)
    {
        super(name, description, status, localDateTime);
        this.taskType = TaskTypes.EPIC;
    }

    public Epic(String name, String description, Status status, LocalDateTime localDateTime, Duration duration)
    {
        super(name, description, status, localDateTime, duration);
        this.taskType = TaskTypes.EPIC;
    }

    protected ArrayList <SubTask> subTasks = new ArrayList<>();

    public void changeStatus()
    {
        int newTasks = 0;
        int doneTasks = 0;
        int inProgressTasks = 0;
        for(SubTask subTask : subTasks)
        {
            if(subTask.status == Status.IN_PROGRESS)
            {
                inProgressTasks++;
                break;
            }
            if(subTask.status == Status.NEW)
                newTasks++;
            if(subTask.status == Status.DONE)
                doneTasks++;
        }
        if(inProgressTasks > 0)                                             //Если есть IN_PROGRESS, то и эпик тоже
            this.status = Status.IN_PROGRESS;
        else if(doneTasks == 0)                     //Условие учитывает отсутствие подзадач
            this.status = Status.NEW;
        else if(newTasks == 0 && doneTasks > 0)     //Условие для DONE
            this.status = Status.DONE;
        else
            this.status = Status.IN_PROGRESS;                                    //Ситуация, когда только NEW и DONE
    }

    public void addSubTask(SubTask subTask)
    {
        if(subTask != null)
        {
            subTasks.add(subTask);
            this.changeStatus();
        }
    }

    public ArrayList <SubTask> getSubTasks()
    {
        return subTasks;
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
                '}';
    }
}
