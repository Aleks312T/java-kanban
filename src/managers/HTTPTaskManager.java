package managers;

import java.nio.file.Paths;

public class HTTPTaskManager extends FileBackedTasksManager{

    public HTTPTaskManager(String URI) {
        super(Paths.get(URI));

    }


}
