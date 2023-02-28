package managers;

import com.sun.source.tree.Tree;
import tasks.*;
import main.Status;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.HashMap;

public class InMemoryTaskManager extends Managers implements TaskManager
{
    protected Set<Integer> allTaskIDs = new HashSet<>();
    private final HashMap<Integer, Task> tasks = new LinkedHashMap<>(10);
    private final HashMap <Integer, Epic> epics = new LinkedHashMap<>(10);
    private final HashMap <Integer, SubTask> subTasks = new LinkedHashMap<>(10);

    protected ArrayList<Integer> newTasks = new ArrayList<>(10);       //Не уверен, что стоит дублировать
    protected ArrayList<Integer> inProgressTasks = new ArrayList<>(10);//Но, наверно, лучше потратить память
    protected ArrayList<Integer> doneTasks = new ArrayList<>(10);      //Чем быстродействие

    protected static String formatterString = "dd.MM.yyyy HH:mm";
    protected static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterString);

    TaskTimeComparator taskTimeComparator = new TaskTimeComparator();
    protected TreeSet<Task> sortedTasks = new TreeSet<>(taskTimeComparator);

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    public DateTimeFormatter getFormatter()
    {
        return formatter;
    }
    public String getFormatterString()
    {
        return formatterString;
    }

    private boolean taskCanBeAdded(Task task)                 //
    {
        if(sortedTasks.isEmpty())
        {
            //sortedTasks.add(task);
            return true;
        }
        else
        {
            int flag = 0;
            //Чтобы было легче ориентироваться
            LocalDateTime currentStartTime;
            LocalDateTime currentEndTime;
            LocalDateTime taskStartTime = task.getStartTime();
            LocalDateTime taskEndTime = task.getEndTime();

            //Не уверен, что сделал верно, не помешала бы консультация
            for(Task currentTask : sortedTasks)
            {
                currentStartTime = currentTask.getStartTime();
                currentEndTime = currentTask.getEndTime();

                if(taskEndTime.isBefore(currentStartTime))
                {
                    continue;
                }
                else
                if(currentEndTime.isBefore(taskStartTime))
                {
                    flag = 1;
                    break;
                } else
                {
                    flag = -1;
                    break;
                }
            }

            if(flag == -1)
            {
                return false;
            } else
            {
                //sortedTasks.add(task);
                return true;
            }
        }
    }

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
        if(value == null)
            return null;
        String[] split = value.split(",");
        Task result;
        if(split.length == 7 || (split.length == 8 && stringToStatus(split[3]) != Status.NONE)) {
            if(!split[5].equals("") && !split[6].equals(""))
            {
                LocalDateTime localDateTime = LocalDateTime.parse(split[5], formatter);
                Duration duration = Duration.ofSeconds(Long.decode(split[6]));
                //Создание задачи с временными рамками
                switch (split[1])
                {
                    case "TASK":
                        result = new Task(split[2], split[4], stringToStatus(split[3]), localDateTime, duration);
                        break;
                    case "EPIC":
                        result = new Epic(split[2], split[4], stringToStatus(split[3]), localDateTime, duration);
                        break;
                    case "SUBTASK":
                        result = new SubTask(split[2], split[4], stringToStatus(split[3]), localDateTime, duration,
                                Integer.parseInt(split[7]));
                        break;
                    default:
                        result = null;
                        break;
                }
            } else
                switch (split[1])
                {
                    case "TASK":
                        result = new Task(split[2], split[4], stringToStatus(split[3]));
                        break;
                    case "EPIC":
                        result = new Epic(split[2], split[4], stringToStatus(split[3]));
                        break;
                    case "SUBTASK":
                        result = new SubTask(split[2], split[4], stringToStatus(split[3]), Integer.parseInt(split[7]));
                        break;
                    default:
                        result = null;
                        break;
                }

        } else return null;
        return result;
    }

    protected Task returnTaskWithoutHistory(int id)
    {
        Task result;
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
        return null;
    }

    @Override
    public boolean addTask(Task newTask) throws IOException {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.getStatus() == null)
            return false;
        if(newTask.getName().equals("") || newTask.getStatus() == Status.NONE)
            return false;
        //Проверка на пересечения
        if(!taskCanBeAdded(newTask))
            return false;

        allTaskIDs.add(newTask.getId());
        historyManager.add(newTask);
        sortedTasks.add(newTask);
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
        return true;
    }
    @Override
    public boolean addTask(Epic newTask) throws IOException                                   //Решил вынести отдельную версию с tasks.Epic
    {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.getStatus() == null)
            return false;
        if(newTask.getName().equals("") || newTask.getStatus() == Status.NONE)
            return false;
        //Проверка на пересечения
        if(!taskCanBeAdded(newTask))
            return false;

        Epic newEpic = new Epic(newTask.getName(), newTask.getDescription(), newTask.getStatus(),
                newTask.getStartTime(), newTask.getDuration());

        allTaskIDs.add(newEpic.getId());
        historyManager.add(newEpic);
        sortedTasks.add(newTask);
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
        return true;
    }
    @Override
    public boolean addTask(SubTask newTask) throws IOException                                //Решил вынести отдельную версию с tasks.SubTask
    {
        if(newTask.getName() == null || newTask.getDescription() == null || newTask.getStatus() == null)
            return false;
        if(newTask.getName().equals("") || newTask.getStatus() == Status.NONE)
            return false;
        //Проверка на пересечения
        if(!taskCanBeAdded(newTask))
            return false;

        //Добавляю в эпик подзадачу
        for(Integer code : epics.keySet())
        {
            if(code == newTask.getParent())
            {
                Epic oldEpic = epics.get(code);
                removeFromList(oldEpic);

                epics.get(code).addSubTask(newTask);                    //Здесь, в том числе происходит смена типа эпика

                Epic newEpic = epics.get(code);
                addToList(newEpic);
                break;
            }
        }

        allTaskIDs.add(newTask.getId());
        historyManager.add(newTask);
        sortedTasks.add(newTask);
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
        return true;
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
    public void deleteTask(int id) throws IOException {
        if(tasks.containsKey(id))
        {
            removeFromList(tasks.get(id));
            allTaskIDs.remove(id);
            tasks.remove(id);
        } else
        if(epics.containsKey(id))
        {
            removeFromList(epics.get(id));
            allTaskIDs.remove(id);
            epics.remove(id);
        } else
        if(subTasks.containsKey(id))
        {
            removeFromList(subTasks.get(id));
            allTaskIDs.remove(id);
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
    public void deleteAllTasks() throws IOException                                        //Удаления ВСЕХ задач
    {
        tasks.clear();
        epics.clear();
        subTasks.clear();

        newTasks.clear();
        inProgressTasks.clear();
        doneTasks.clear();

        allTaskIDs.clear();
        sortedTasks.clear();

        historyManager.clearHistory();
    }
    @Override
    public void printAllCodes()
    {
        //Добавил этот метод для демонстрации исправления "Можно лучше"
        System.out.println();
        System.out.println(allTaskIDs);
    }
}
