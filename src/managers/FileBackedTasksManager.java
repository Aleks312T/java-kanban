package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager
{

    public FileBackedTasksManager(Task newTask) {
        super();
    }

    public void save()
    {

    }

    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public void addTask(Epic newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public void addTask(SubTask newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public Task returnTask(int id) {
        return super.returnTask(id);
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public int countEpics() {
        return super.countEpics();
    }

    @Override
    public boolean containEpic(int code) {
        return super.containEpic(code);
    }

    @Override
    public ArrayList<SubTask> findSubTasks(int code) {
        return super.findSubTasks(code);
    }

    @Override
    public void printNewTasks() {
        super.printNewTasks();
    }

    @Override
    public void printInProgressTasks() {
        super.printInProgressTasks();
    }

    @Override
    public void printDoneTasks() {
        super.printDoneTasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void printAllCodes() {
        super.printAllCodes();
    }
}
