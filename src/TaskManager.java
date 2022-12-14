import java.util.*;
import java.util.HashMap;

public class TaskManager
{
    protected HashMap <Integer, Task> tasks = new HashMap<>();                //Можно было бы и в ArrayList засунуть
    protected HashMap <Integer, Epic> epics = new HashMap<>();                //Но тогда достается не очень быстро
    protected HashMap <Integer, SubTask> subTasks = new HashMap<>();

    protected ArrayList<Integer> newTasks = new ArrayList<>();                //Не уверен, что стоит дублировать
    protected ArrayList<Integer> inProgressTasks = new ArrayList<>();         //Но, наверно, лучше потратить память
    protected ArrayList<Integer> doneTasks = new ArrayList<>();               //Чем быстродействие

    public void addTask(Task newTask)
    {
        if(newTask.name == null || newTask.description == null || newTask.status == null)
            return;
        if(newTask.name.equals("") || newTask.status.equals(""))
            return;

        if(!tasks.containsKey(newTask.hashCode()))
        {
            tasks.put(newTask.hashCode(), newTask);
            addToList(newTask.status, newTask.hashCode());
        }
        else
        {
            Task oldTask = tasks.get(newTask.hashCode());
            if(!oldTask.status.equals(newTask.status))                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask.status, oldTask.hashCode());
                addToList(newTask.status, newTask.hashCode());
            }
            tasks.replace(newTask.hashCode(), newTask);
        }
    }

    public void addTask(Epic newTask)                                //Решил вынести отдельную версию с Epic
    {
        if(newTask.name == null || newTask.description == null || newTask.status == null)
            return;
        if(newTask.name.equals("") || newTask.status.equals(""))
            return;

        System.out.print("Добавляю Epic");
        Epic newEpic = new Epic(newTask.name, newTask.description, newTask.status);
        if(!epics.containsKey(newTask.hashCode()))
        {
            epics.put(newTask.hashCode(), newEpic);
            addToList(newTask.status, newTask.hashCode());
        }
        else
        {
            Task oldTask = epics.get(newTask.hashCode());
            if(!oldTask.status.equals(newTask.status))                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask.status, oldTask.hashCode());
                addToList(newTask.status, newTask.hashCode());
            }
            epics.replace(newTask.hashCode(), newEpic);
        }
    }

    public void addTask(SubTask newTask)                                //Решил вынести отдельную версию с SubTask
    {
        if(newTask.name == null || newTask.description == null || newTask.status == null)
            return;
        if(newTask.name.equals("") || newTask.status.equals(""))
            return;

        //Добавляю в эпик подзадачу
        String epicOldStatus = "";
        String epicNewStatus = "";
        int epicCode = 0;                                               //Чтобы компилятор дальше не ругался
        for(Integer code : epics.keySet())
        {
            if(code == newTask.parent)
            {
                epicOldStatus = epics.get(code).status;
                epics.get(code).addSubTask(newTask);
                epicNewStatus = epics.get(code).status;
                epicCode = code;
                break;
            }
        }

        if(!epicOldStatus.equals(epicNewStatus))                        //Изменение статуса Epic в самом taskManager
        {
            removeFromList(epicOldStatus, epicCode);
            addToList(epicNewStatus, epicCode);
        }

        if(!subTasks.containsKey(newTask.hashCode()))
        {
            subTasks.put(newTask.hashCode(), newTask);
            addToList(newTask.status, newTask.hashCode());
        }
        else
        {
            Task oldTask = subTasks.get(newTask.hashCode());
            if(!oldTask.status.equals(newTask.status))                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask.status, oldTask.hashCode());
                addToList(newTask.status, newTask.hashCode());
            }
            subTasks.replace(newTask.hashCode(), newTask);
        }
    }

    public Task returnTask(int id)
    {
        Task result = new Task("", "", "");
        if(tasks.containsKey(id))
        {
            result = tasks.get(id);
            return result;
        } else
        if(epics.containsKey(id))
        {
            result = epics.get(id);
            return result;
        } else
        if(subTasks.containsKey(id))
        {
            result = subTasks.get(id);
            return result;
        }
        return result;                                              //Этот return не должен сработать, но нужна заглушка
    }

    public void deleteTask(int id)
    {
        if(tasks.containsKey(id))
        {
            removeFromList(tasks.get(id).status, tasks.get(id).hashCode());
            tasks.remove(id);
        } else
        if(epics.containsKey(id))
        {
            removeFromList(epics.get(id).status, epics.get(id).hashCode());
            epics.remove(id);
        } else
        if(subTasks.containsKey(id))
        {
            removeFromList(subTasks.get(id).status, subTasks.get(id).hashCode());
            subTasks.remove(id);
        }
    }

    private void addToList(String status, int code)                 //Добавление в списки new, inProgress и done -Tasks
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

    private void removeFromList(String status, int code)            //Удаление из списков new, inProgress и done -Tasks
    {
        switch (status) {
            case "NEW": {
                for(int i = 0; i < newTasks.size(); ++i)
                    if(newTasks.get(i) == code)
                    {
                        newTasks.remove(i);
                        break;
                    }
                break;
            }
            case "IN_PROGRESS": {
                for(int i = 0; i < inProgressTasks.size(); ++i)
                    if(inProgressTasks.get(i) == code)
                    {
                        inProgressTasks.remove(i);
                        break;
                    }
                break;
            }
            case "DONE": {
                for(int i = 0; i < doneTasks.size(); ++i)
                    if(doneTasks.get(i) == code)
                    {
                        doneTasks.remove(i);
                        break;
                    }
                break;
            }
        }
    }

    public int countEpics()
    {
        return epics.size();
    }

    public boolean containEpic(int code)
    {
        return epics.containsKey(code);
    }

    public ArrayList <SubTask> findSubTasks(int code)
    {
        Epic parentEpic = epics.get(code);
        return parentEpic.getSubTasks();
    }

    public void printNewTasks()                                                 //Вывод списка newTasks
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

    public void printInProgressTasks()                                          //Вывод списка inProgressTasks
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

    public void printDoneTasks()                                                //Вывод списка doneTasks
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

    public void deleteAllTasks()                                                //Удаления ВСЕХ задач
    {
        tasks.clear();
        epics.clear();
        subTasks.clear();

        newTasks.clear();
        inProgressTasks.clear();
        doneTasks.clear();
    }
}
