package managers;

import tasks.Task;

import java.util.ArrayList;

public interface HistoryManager
{
    void add(Task Task);

    ArrayList <Task> getHistory();

    int getMaxSize();

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
