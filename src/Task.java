import java.util.*;

public class Task {
    public static final String type = "Task";
    private String name;
    private String description;
    private int id;                                           //Пускай это будет и id задачи, и хэш-кодом
    protected String status;
    protected Status enumStatus;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if(status != null)
            this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name)
                && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }
}
