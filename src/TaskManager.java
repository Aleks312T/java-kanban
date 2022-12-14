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

    public void addTask(Task newTask)
    {
        if(newTask.name == null || newTask.description == null || newTask.status == null)
            return;
        //if(name.equals("") || description.equals("") || status.equals("") || type.equals(""))            return;

        switch (newTask.type) {
            case "Task": {
                if(!tasks.containsKey(newTask.hashCode()))
                {
                    tasks.put(newTask.hashCode(), newTask);
                    addToList(newTask.status, newTask.hashCode());
                }
                else
                {
                    Task oldTask = tasks.get(newTask.hashCode());
                    if(!oldTask.status.equals(newTask.status))      //Проверка на изменение статуса
                    {
                        //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                        removeFromList(oldTask.status, oldTask.hashCode());
                        addToList(newTask.status, newTask.hashCode());
                    }
                    tasks.replace(newTask.hashCode(), newTask);
                }
                break;
            }
            case "Epic": {
                //Что-то мне подсказывает, что тут можно лучше
                Epic newEpic = new Epic(newTask.name, newTask.description, newTask.status);
                if(!epics.containsKey(newTask.hashCode()))
                {
                    epics.put(newTask.hashCode(), newEpic);
                    addToList(newTask.status, newTask.hashCode());
                }
                else
                {
                    Task oldTask = epics.get(newTask.hashCode());
                    if(!oldTask.status.equals(newTask.status))      //Проверка на изменение статуса
                    {
                        //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                        removeFromList(oldTask.status, oldTask.hashCode());
                        addToList(newTask.status, newTask.hashCode());
                    }
                    epics.replace(newTask.hashCode(), newEpic);
                }
                break;
            }
            case "SubTask": {
                //Что-то мне подсказывает, что тут можно лучше
                SubTask newSubTask = new SubTask(newTask.name, newTask.description, newTask.status);
                if(!subTasks.containsKey(newTask.hashCode()))
                {
                    subTasks.put(newTask.hashCode(), newSubTask);
                    addToList(newTask.status, newTask.hashCode());
                }
                else
                {
                    Task oldTask = subTasks.get(newTask.hashCode());
                    if(!oldTask.status.equals(newTask.status))      //Проверка на изменение статуса
                    {
                        //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                        removeFromList(oldTask.status, oldTask.hashCode());
                        addToList(newTask.status, newTask.hashCode());
                    }
                    subTasks.replace(newTask.hashCode(), newSubTask);
                }
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

    private void removeFromList(String status, int code)
    {
        switch (status) {
            case "NEW": {
                for(int i = 0; i < newTasks.size(); ++i)
                    if(newTasks.get(i) == code)
                        newTasks.remove(i);
                break;
            }
            case "IN_PROGRESS": {
                for(int i = 0; i < inProgressTasks.size(); ++i)
                    if(inProgressTasks.get(i) == code)
                        inProgressTasks.remove(i);
                break;
            }
            case "DONE": {
                for(int i = 0; i < doneTasks.size(); ++i)
                    if(doneTasks.get(i) == code)
                        doneTasks.remove(i);
                break;
            }
        }
    }

    public void printNewTasks()
    {
        for(Integer code : newTasks)
        {
            if(tasks.containsKey(code))
                System.out.println(tasks.get(code).toString());
            if(epics.containsKey(code))
                System.out.println(epics.get(code).toString());
            if(subTasks.containsKey(code))
                System.out.println(subTasks.get(code).toString());
        }
    }

    public void printInProgressTasks()
    {
        for(Integer code : inProgressTasks)
        {
            if(tasks.containsKey(code))
                System.out.println(tasks.get(code).toString());
            if(epics.containsKey(code))
                System.out.println(epics.get(code).toString());
            if(subTasks.containsKey(code))
                System.out.println(subTasks.get(code).toString());
        }
    }

    public void printDoneTasks()
    {
        for(Integer code : doneTasks)
        {
            if(tasks.containsKey(code))
                System.out.println(tasks.get(code).toString());
            if(epics.containsKey(code))
                System.out.println(epics.get(code).toString());
            if(subTasks.containsKey(code))
                System.out.println(subTasks.get(code).toString());
        }
    }
}
