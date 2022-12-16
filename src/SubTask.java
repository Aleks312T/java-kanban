import java.util.*;
public class SubTask extends Task
{
    public static final String type = "SubTask";
    public int parent;
    public SubTask(String name, String description, String status, int parent)
    {
        super(name, description, status);
        this.parent = parent;
    }

    @Override
    public String toString() {

        return "SubTask{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.hashCode() +
                ", status='" + status + '\'' +
                ", parent id='" + parent + '\'' +
                '}';
    }
}
