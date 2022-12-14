import java.util.*;
import java.util.HashMap;

public class Epic extends Task
{
    public static final String type = "Epic";
    public Epic(String name, String description, String status)
    {
        super(name, description, status);
    }

    ArrayList <SubTask> subTasks = new ArrayList<>();

    public void changeStatus()
    {
        int newTasks = 0;
        int doneTasks = 0;
        int inProgressTasks = 0;
        for(SubTask subTask : subTasks)
        {
            if(subTask.status.equals("IN_PROGRESS"))
            {
                inProgressTasks++;
                break;
            }
            if(subTask.status.equals("NEW"))
                newTasks++;
            if(subTask.status.equals("DONE"))
                doneTasks++;
        }
        if(inProgressTasks > 0)                                             //Если есть IN_PROGRESS, то и эпик тоже
            this.status = "IN_PROGRESS";
        else if(doneTasks == 0 && inProgressTasks == 0)                     //Условие учитывает отсутствие подзадач
            this.status = "NEW";
        else if(newTasks == 0 && doneTasks > 0 && inProgressTasks == 0)     //Условие для DONE
            this.status = "DONE";
        else
            this.status = "IN_PROGRESS";                                    //Ситуация, когда только NEW и DONE
    }

    @Override
    public String toString() {

        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + this.hashCode() +
                ", status='" + status + '\'' +
                '}';
    }
}
