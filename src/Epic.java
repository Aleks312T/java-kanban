import java.util.*;
import java.util.HashMap;

public class Epic extends Task
{
    public Epic(String name, String description, Status status)
    {
        super(name, description, status);
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
        else if(doneTasks == 0 && inProgressTasks == 0)                     //Условие учитывает отсутствие подзадач
            this.status = Status.NEW;
        else if(newTasks == 0 && doneTasks > 0 && inProgressTasks == 0)     //Условие для DONE
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

        return "Epic{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.hashCode() +
                ", status='" + status + '\'' +
                '}';
    }
}
