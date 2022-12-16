import java.util.ArrayList;

public class InMemoryHistoryManager extends Managers implements HistoryManager
{
    @Override
    public void add(Task Task)
    {

    }

    @Override
    public ArrayList<SubTask> getHistory()
    {
        return new ArrayList<>();
    }
}
