import java.util.*;
import java.util.HashMap;

public class Epic extends Task
{
    public Epic(String name, String description, String status)
    {
        super(name, description, status);
    }

    HashMap <Integer, SubTask> subTasks = new HashMap<>();

    public void changeStatus()
    {
        int newTasks = 0;
        int doneTasks = 0;
        int inProgressTasks = 0;
        for(SubTask subTask : subTasks.values())
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
}
