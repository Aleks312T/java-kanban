import java.util.*;

public class SubTask extends Task
{
    public static final String type = "SubTask";
    public SubTask(String name, String description, String status)
    {
        super(name, description, status);
    }

    @Override
    public String toString() {

        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + this.hashCode() +
                ", status='" + status + '\'' +
                '}';
    }
}
