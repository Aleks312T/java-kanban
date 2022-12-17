package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager
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
