import java.util.ArrayList;

public class Managers {
    public static TaskManager getDefault()
    {
        return new InMemoryTaskManager();
    }
}
