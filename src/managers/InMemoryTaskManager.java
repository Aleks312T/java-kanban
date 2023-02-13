package managers;

import tasks.*;
import main.Status;

import java.util.*;
import java.util.HashMap;

public class InMemoryTaskManager extends Managers implements TaskManager
{

    private final HashMap<Integer, Task> tasks = new LinkedHashMap<>();
    private final HashMap <Integer, Epic> epics = new LinkedHashMap<>();
    private final HashMap <Integer, SubTask> subTasks = new LinkedHashMap<>();

    protected ArrayList<Integer> newTasks = new ArrayList<>();          //Не уверен, что стоит дублировать
    protected ArrayList<Integer> inProgressTasks = new ArrayList<>();   //Но, наверно, лучше потратить память
    protected ArrayList<Integer> doneTasks = new ArrayList<>();         //Чем быстродействие

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    private void addToList(Task task)                 //Добавление в списки new, inProgress и done -Tasks
    {
        switch (task.getStatus()) {
            case NEW: {
                newTasks.add(task.getId());
                break;
            }
            case IN_PROGRESS: {
                inProgressTasks.add(task.getId());
                break;
            }
            case DONE: {
                doneTasks.add(task.getId());
                break;
            }
        }
    }

    private void removeFromList(Task task)            //Удаление из списков new, inProgress и done -Tasks
    {
        switch (task.getStatus()) {
            case NEW: {
                for(int i = 0; i < newTasks.size(); ++i)
                    if(newTasks.get(i) == task.getId())
                    {
                        newTasks.remove(i);
                        break;
                    }
                break;
            }
            case IN_PROGRESS: {
                for(int i = 0; i < inProgressTasks.size(); ++i)
                    if(inProgressTasks.get(i) == task.getId())
                    {
                        inProgressTasks.remove(i);
                        break;
                    }
                break;
            }
            case DONE: {
                for(int i = 0; i < doneTasks.size(); ++i)
                    if(doneTasks.get(i) == task.getId())
                    {
                        doneTasks.remove(i);
                        break;
                    }
                break;
            }
        }
    }

    public List<Task> getHistory()
    {
        return historyManager.getHistory();
    }

    public static Status stringToStatus(String input)
    {
        Status result;
        switch (input) {
            case "NEW": {
                result = Status.NEW;
                break;
            }
            case "IN_PROGRESS": {
                result = Status.IN_PROGRESS;
                break;
            }
            case "DONE": {
                result = Status.DONE;
                break;
            }
            default:
                result = Status.NONE;
        }
        return result;
    }

    protected static String taskToString(Task task)
    {
        return task.toString();
    }

    public static TaskTypes taskTypeFromString(String input)
    {
        TaskTypes result;
        switch (input) {
            case "TASK": {
                result = TaskTypes.TASK;
                break;
            }
            case "EPIC": {
                result = TaskTypes.EPIC;
                break;
            }
            case "SUBTASK": {
                result = TaskTypes.SUBTASK;
                break;
            }
            default:
                result = TaskTypes.NONE;
        }
        return result;
    }

    protected static Task taskFromString(String value)       //Надо проверить условие
    {
        String[] split = value.split(",");
        Task result;
        if(split.length == 5 || (split.length == 6 && stringToStatus(split[3]) != Status.NONE)) {
            switch (split[1])
            {
                case "TASK":
                    result = new Task(split[2], split[4], stringToStatus(split[3]));
                    break;
                case "EPIC":
                    result = new Epic(split[2], split[4], stringToStatus(split[3]));
                    break;
                case "SUBTASK":
                    result = new SubTask(split[2], split[4], stringToStatus(split[3]), Integer.parseInt(split[5]));
                break;
                default:
                    result = null;
                    break;
            }
        } else return null;
        return result;
    }

    @Override
    public void addTask(Task newTask)
    {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.getStatus() == null)
            return;
        if(newTask.getName().equals("") || newTask.getStatus() == Status.NONE)
            return;

        historyManager.add(newTask);
        if(!tasks.containsKey(newTask.getId()))
        {
            tasks.put(newTask.getId(), newTask);
            addToList(newTask);
        }
        else
        {
            Task oldTask = tasks.get(newTask.getId());
            if(oldTask.getStatus() != newTask.getStatus())                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask);
                addToList(newTask);
            }
            tasks.replace(newTask.getId(), newTask);
        }
    }
    @Override
    public void addTask(Epic newTask)                                   //Решил вынести отдельную версию с tasks.Epic
    {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.getStatus() == null)
            return;
        if(newTask.getName().equals("") || newTask.getStatus() == Status.NONE)
            return;

        Epic newEpic = new Epic(newTask.getName(), newTask.getDescription(), newTask.getStatus());
        historyManager.add(newEpic);
        if(!epics.containsKey(newTask.getId()))
        {
            epics.put(newTask.getId(), newEpic);
            addToList(newTask);
        }
        else
        {
            Task oldTask = epics.get(newTask.getId());
            if(oldTask.getStatus() != newTask.getStatus())                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask);
                addToList(newTask);
            }
            epics.replace(newTask.getId(), newEpic);
        }
    }
    @Override
    public void addTask(SubTask newTask)                                //Решил вынести отдельную версию с tasks.SubTask
    {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.getStatus() == null)
            return;
        if(newTask.getName().equals("") || newTask.getStatus() == Status.NONE)
            return;

        //Добавляю в эпик подзадачу
        for(Integer code : epics.keySet())
        {
            if(code == newTask.parent)
            {
                Epic oldEpic = epics.get(code);
                removeFromList(oldEpic);

                epics.get(code).addSubTask(newTask);                    //Здесь, в том числе происходит смена типа эпика

                Epic newEpic = epics.get(code);
                addToList(newEpic);
                break;
            }
        }

        historyManager.add(newTask);
        if(!subTasks.containsKey(newTask.getId()))
        {
            subTasks.put(newTask.getId(), newTask);
            addToList(newTask);
        }
        else
        {
            Task oldTask = subTasks.get(newTask.getId());
            if(oldTask.getStatus() != newTask.getStatus())                  //Проверка на изменение статуса
            {
                //Тут я убираю хэшкод из листа со старым статусом, и добавляю в лист с новым
                removeFromList(oldTask);
                addToList(newTask);
            }
            subTasks.replace(newTask.getId(), newTask);
        }
    }
    @Override
    public Task returnTask(int id)
    {
        Task result;
        if(tasks.containsKey(id))
        {
            result = tasks.get(id);
            historyManager.add(tasks.get(id));
            return result;
        } else
        if(epics.containsKey(id))
        {
            result = epics.get(id);
            historyManager.add(epics.get(id));
            return result;
        } else
        if(subTasks.containsKey(id))
        {
            result = subTasks.get(id);
            historyManager.add(subTasks.get(id));
            return result;
        }
        return null;
    }
    @Override
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
    @Override
    public int countEpics()
    {
        return epics.size();
    }
    @Override
    public boolean containEpic(int code)
    {
        return epics.containsKey(code);
    }
    @Override
    public ArrayList <SubTask> findSubTasks(int code)
    {
        Epic parentEpic = epics.get(code);
        return parentEpic.getSubTasks();
    }
    @Override
    public void printNewTasks()                                         //Вывод списка newTasks
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
    @Override
    public void printInProgressTasks()                                  //Вывод списка inProgressTasks
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
    @Override
    public void printDoneTasks()                                        //Вывод списка doneTasks
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
    @Override
    public void deleteAllTasks()                                        //Удаления ВСЕХ задач
    {
        tasks.clear();
        epics.clear();
        subTasks.clear();

        newTasks.clear();
        inProgressTasks.clear();
        doneTasks.clear();

        historyManager.clearHistory();
    }
    @Override
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
