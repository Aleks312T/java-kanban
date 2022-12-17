import java.util.ArrayList;

public class InMemoryHistoryManager extends Managers implements HistoryManager
{
    ArrayList<Task> history;
    final static int maxSize = 10;
    public InMemoryHistoryManager()
    {
        history = new ArrayList<>();
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void add(Task task)
    {
        if(history.size() == maxSize)
        {
            history.remove(0);
            history.add(task);
        } else
        {
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory()
    {
        return history;
    }
}
