package managers;

import tasks.Task;

import java.util.ArrayList;

public interface HistoryManager
{
    void add(Task Task);

    ArrayList <Task> getHistory();
    //List<Task> getHistory();
    int getMaxSize();

    void remove(int id);

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
