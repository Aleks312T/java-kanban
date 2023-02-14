package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

public interface TaskManager
{
    void addTask(Task newTask) throws IOException;

    void addTask(Epic newTask) throws IOException;

    void addTask(SubTask newTask) throws IOException;

    Task returnTask(int id);

    void deleteTask(int id) throws IOException;

    int countEpics();

    boolean containEpic(int code);

    ArrayList <SubTask> findSubTasks(int code);

    void printNewTasks();

    void printInProgressTasks();

    void printDoneTasks();

    void deleteAllTasks() throws IOException;

    void printAllCodes();
}
