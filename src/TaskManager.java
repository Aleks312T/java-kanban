import java.util.*;
import java.util.HashMap;

public class TaskManager
{
    HashMap <Integer, Task> tasks = new HashMap<>();                //Можно было бы и в ArrayList засунуть
    HashMap <Integer, Epic> epics = new HashMap<>();                //Но тогда достается не очень быстро
    HashMap <Integer, SubTask> subTasks = new HashMap<>();

    ArrayList<Integer> newTasks = new ArrayList<>();                //Не уверен, что стоит дублировать
    ArrayList<Integer> inProgressTasks = new ArrayList<>();         //Но, наверно, лучше потратить память
    ArrayList<Integer> doneTasks = new ArrayList<>();               //Чем быстродействие

    public void addTask(String name, String description, String status, String type)
    {
        if(name == null || description == null || status == null || type == null)
            return;
        if(name.equals("") || description.equals("") || status.equals("") || type.equals(""))
            return;

        switch (type) {
            case "Task": {
                Task newTask = new Task(name, description, status);
                int code = newTask.hashCode();
                tasks.put(code, newTask);
                addToList(status, code);
                break;
            }
            case "Epic": {
                Epic newEpic = new Epic(name, description, status);
                int code = newEpic.hashCode();
                epics.put(code, newEpic);
                addToList(status, code);
                break;
            }
            case "SubTask": {
                SubTask newSubTask = new SubTask(name, description, status);
                int code = newSubTask.hashCode();
                subTasks.put(code, newSubTask);
                addToList(status, code);
                break;
            }
        }
    }

    private void addToList(String status, int code)
    {
        switch (status) {
            case "NEW": {
                newTasks.add(code);
                break;
            }
            case "IN_PROGRESS": {
                inProgressTasks.add(code);
                break;
            }
            case "DONE": {
                doneTasks.add(code);
                break;
            }
        }
    }

    public void printNewTasks()
    {
        for(Integer code : newTasks)
        {
            if(tasks.containsKey(code))
                tasks.get(code).toString();
            if(epics.containsKey(code))
                epics.get(code).toString();
            if(subTasks.containsKey(code))
                subTasks.get(code).toString();
        }
    }

    public void printInProgressTasks()
    {
        for(Integer code : inProgressTasks)
        {
            if(tasks.containsKey(code))
                tasks.get(code).toString();
            if(epics.containsKey(code))
                epics.get(code).toString();
            if(subTasks.containsKey(code))
                subTasks.get(code).toString();
        }
    }

    public void printDoneTasks()
    {
        for(Integer code : doneTasks)
        {
            if(tasks.containsKey(code))
                tasks.get(code).toString();
            if(epics.containsKey(code))
                epics.get(code).toString();
            if(subTasks.containsKey(code))
                subTasks.get(code).toString();
        }
    }
}
