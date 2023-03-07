package managers;

public class Managers {
    public static TaskManager getDefaultTaskManager(String URI)
    {
        return new HTTPTaskManager(URI);
    }

    public static HistoryManager getDefaultHistoryManager()
    {
        return new InMemoryHistoryManager();
    }
}
