package managers;

import main.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTasksManagerTest extends TaskManagerTest{
    static LocalDateTime testStartTime;
    static LocalDateTime testEndTime;
    static Duration testDuration;
    static FileBackedTasksManager taskManager;
    @BeforeAll
    public static void beforeAll()
    {
        testStartTime = LocalDateTime.now();
        Path saveFile = Paths.get("SaveFileForTest.txt");
        taskManager = new FileBackedTasksManager(saveFile);
    }

    @AfterAll
    public static void afterAll()
    {
        testEndTime = LocalDateTime.now();
        testDuration = Duration.between(testStartTime, testEndTime);

    }

    @Test
    void shouldAddAndReturnTaskCorrectly() throws IOException {
        taskManager.deleteAllTasks();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        taskManager.addTask(testEpic);
        Epic testEpicResult = (Epic) taskManager.returnTask(testEpic.getId());

        assertEquals(testEpic, testEpicResult);

        SubTask subTask1 = new SubTask("name1", "description1", Status.NEW,
                localDateTime.plusDays(1), duration, testEpic.getId());
        taskManager.addTask(subTask1);
        SubTask subTask1Result = (SubTask) taskManager.returnTask(subTask1.getId());

        assertEquals(subTask1, subTask1Result);

        testEpicResult = (Epic) taskManager.returnTask(testEpic.getId());
        ArrayList<SubTask> subTasks = testEpicResult.getSubTasks();

        assertTrue(subTasks.size() == 1 && subTasks.contains(subTask1Result));
        taskManager.deleteAllTasks();
    }

    @Test
    void deleteTask() {
    }

    @Test
    void countEpics() {
    }

    @Test
    void containEpic() {
    }

    @Test
    void findSubTasks() {
    }

    @Test
    void printNewTasks() {
    }

    @Test
    void printInProgressTasks() {
    }

    @Test
    void printDoneTasks() {
    }

    @Test
    void deleteAllTasks() {
    }

    @Test
    void printAllCodes() {
    }
}