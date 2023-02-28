package managers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

abstract public class TaskManagerTest <T extends TaskManager>{
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask1;
    protected SubTask subTask2;
    static LocalDateTime testStartTime;
    static LocalDateTime testEndTime;
    static Duration testDuration;
    @BeforeAll
    public static void beforeAll() {
    }

    @AfterAll
    public static void afterAll()  {
    }

}
