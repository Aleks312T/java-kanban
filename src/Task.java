import java.util.*;

public class Task {
    protected String name;
    protected String description;
    protected int id;                                           //Пускай это будет и id задачи, и хэш-кодом
    protected String status;

    public Task(String name, String description, String status)
    {
        if(name != null && description != null && status != null)
        {
            this.name = name;
            this.description = description;
            this.status = status;
            this.id = 17 * 31 * 31 + name.hashCode() * 31 + description.hashCode();
        }
    }

    @Override
    public int hashCode() {
        return id;                                              // возвращаем хеш
    }

}
