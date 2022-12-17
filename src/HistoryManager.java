import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

interface HistoryManager
{
    void add(Task Task);

    ArrayList <Task> getHistory();

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
