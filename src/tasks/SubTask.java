package tasks;

import main.Status;

public class SubTask extends Task
{
    public int parent;
    public SubTask(String name, String description, Status status, int parent)
    {
        super(name, description, status);
        this.taskType = TaskTypes.SubTask;
        this.parent = parent;
    }

    @Override
    public String toString() {

        return "tasks.SubTask{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.hashCode() +
                ", status='" + status + '\'' +
                ", parent id='" + parent + '\'' +
                '}';
    }
}
