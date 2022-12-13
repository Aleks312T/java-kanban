import java.util.*;

public class Task {
    protected String name;
    protected String description;
    private int id;                                           //Пускай это будет и id задачи, и хэш-кодом
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
    public String toString() {

        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return id;                                              // возвращаем хеш
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status != null)
            this.status = status;
    }
}
