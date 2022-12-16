import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

interface TaskManager
{
    void addTask(Task newTask);

    void addTask(Epic newTask);

    void addTask(SubTask newTask);

    Task returnTask(int id);

    void deleteTask(int id);

    int countEpics();

    boolean containEpic(int code);

    ArrayList <SubTask> findSubTasks(int code);

    void printNewTasks();

    void printInProgressTasks();

    void printDoneTasks();

    void deleteAllTasks();

    void printAllCodes();
}
