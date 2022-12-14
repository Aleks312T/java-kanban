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
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.status == null)
            return;
        if(newTask.getName().equals("") || newTask.status.equals(""))
            return;

        if(!tasks.containsKey(newTask.hashCode()))
        {
            tasks.put(newTask.hashCode(), newTask);
            addToList(newTask);
        }
        else
        {
            Task oldTask = tasks.get(newTask.hashCode());
            if(!oldTask.status.equals(newTask.status))                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask);
                addToList(newTask);
            }
            tasks.replace(newTask.hashCode(), newTask);
        }
    }

    public void addTask(Epic newTask)                                //Решил вынести отдельную версию с Epic
    {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.status == null)
            return;
        if(newTask.getName().equals("") || newTask.status.equals(""))
            return;

        Epic newEpic = new Epic(newTask.getName(), newTask.getDescription(), newTask.status);
        if(!epics.containsKey(newTask.hashCode()))
        {
            epics.put(newTask.hashCode(), newEpic);
            addToList(newTask);
        }
        else
        {
            Task oldTask = epics.get(newTask.hashCode());
            if(!oldTask.status.equals(newTask.status))                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask);
                addToList(newTask);
            }
            epics.replace(newTask.hashCode(), newEpic);
        }
    }

    public void addTask(SubTask newTask)                                //Решил вынести отдельную версию с SubTask
    {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.status == null)
            return;
        if(newTask.getName().equals("") || newTask.status.equals(""))
            return;

        //Добавляю в эпик подзадачу
        String epicOldStatus = "";
        String epicNewStatus = "";
        int epicCode = 0;

        Epic oldEpic = new Epic("", "", "");       //Чтобы компилятор дальше не ругался
        Epic newEpic = new Epic("", "", "");

        for(Integer code : epics.keySet())
        {
            if(code == newTask.parent)
            {
                oldEpic = epics.get(code);
                removeFromList(oldEpic);

                epics.get(code).addSubTask(newTask);                    //Здесь в том числе происходит смена типа эпика

                newEpic = epics.get(code);
                addToList(newEpic);
                break;
            }
        }


        if(!subTasks.containsKey(newTask.hashCode()))
        {
            subTasks.put(newTask.hashCode(), newTask);
            addToList(newTask);
        }
        else
        {
            Task oldTask = subTasks.get(newTask.hashCode());
            if(!oldTask.status.equals(newTask.status))                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask);
                addToList(newTask);
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
            removeFromList(tasks.get(id));
            tasks.remove(id);
        } else
        if(epics.containsKey(id))
        {
            removeFromList(epics.get(id));
            epics.remove(id);
        } else
        if(subTasks.containsKey(id))
        {
            removeFromList(subTasks.get(id));
            subTasks.remove(id);
        }
    }
    //Теперь на вход идет сама задача
    private void addToList(Task task)                 //Добавление в списки new, inProgress и done -Tasks
    {
        switch (task.status) {
            case "NEW": {
                newTasks.add(task.hashCode());
                break;
            }
            case "IN_PROGRESS": {
                inProgressTasks.add(task.hashCode());
                break;
            }
            case "DONE": {
                doneTasks.add(task.hashCode());
                break;
            }
        }
    }
    //Теперь на вход идет сама задача
    private void removeFromList(Task task)            //Удаление из списков new, inProgress и done -Tasks
    {
        switch (task.status) {
            case "NEW": {
                for(int i = 0; i < newTasks.size(); ++i)
                    if(newTasks.get(i) == task.hashCode())
                    {
                        newTasks.remove(i);
                        break;
                    }
                break;
            }
            case "IN_PROGRESS": {
                for(int i = 0; i < inProgressTasks.size(); ++i)
                    if(inProgressTasks.get(i) == task.hashCode())
                    {
                        inProgressTasks.remove(i);
                        break;
                    }
                break;
            }
            case "DONE": {
                for(int i = 0; i < doneTasks.size(); ++i)
                    if(doneTasks.get(i) == task.hashCode())
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
        //В коде и правда красиво, но вывод получается неинформативный
        //Поэтому решил оставить так
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
        //В коде и правда красиво, но вывод получается неинформативный
        //Поэтому решил оставить так
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
        //В коде и правда красиво, но вывод получается неинформативный
        //Поэтому решил оставить так
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

    public void printAllCodes()
    {
        //Добавил этот метод для демонстрации исправления "Можно лучше"
        System.out.println();
        System.out.println("New задачи:");
        System.out.println(newTasks);

        System.out.println();
        System.out.println("InProgress задачи:");
        System.out.println(inProgressTasks);

        System.out.println();
        System.out.println("Done задачи:");
        System.out.println(doneTasks);
    }
}
