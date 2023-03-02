package managers;

import main.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

        SubTask testSubTask1 = new SubTask("name1", "description1", Status.NEW,
                localDateTime.plusDays(1), duration, testEpic.getId());
        taskManager.addTask(testSubTask1);
        SubTask subTask1Result = (SubTask) taskManager.returnTask(testSubTask1.getId());

        assertEquals(testSubTask1, subTask1Result);

        testEpicResult = (Epic) taskManager.returnTask(testEpic.getId());
        ArrayList<SubTask> subTasks = testEpicResult.getSubTasks();

        assertTrue(subTasks.size() == 1 && subTasks.contains(subTask1Result));
        taskManager.deleteAllTasks();
        Set<Integer> setOfTasks = taskManager.allTaskIDs;
        assertTrue(setOfTasks.isEmpty());
    }

    @Test
    void shouldNOTAddTaskCorrectly() throws IOException {
        taskManager.deleteAllTasks();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);

        Epic testEpic = new Epic(null, "EpicDescription", Status.DONE, localDateTime, duration);
        boolean result = taskManager.addTask(testEpic);
        assertFalse(result);

        testEpic = new Epic("EpicName", null, Status.DONE, localDateTime, duration);
        result = taskManager.addTask(testEpic);
        assertFalse(result);

        testEpic = new Epic("EpicName", "EpicDescription", null, localDateTime, duration);
        result = taskManager.addTask(testEpic);
        assertFalse(result);

        testEpic = new Epic("", "EpicDescription", Status.DONE, localDateTime, duration);
        result = taskManager.addTask(testEpic);
        assertFalse(result);

        testEpic = new Epic("EpicName", "", Status.DONE, localDateTime, duration);
        result = taskManager.addTask(testEpic);
        assertFalse(result);

        testEpic = new Epic("EpicName", "EpicDescription", Status.NONE, localDateTime, duration);
        result = taskManager.addTask(testEpic);
        assertFalse(result);

        result = taskManager.addTask((Epic) null);
        assertFalse(result);

        testEpic = new Epic(null, null, null);
        result = taskManager.addTask(testEpic);
        assertFalse(result);
    }

    @Test
    void shouldDeleteTasksNormally() throws IOException {
        taskManager.deleteAllTasks();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Task testTask = new Task("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        taskManager.addTask(testTask);

        taskManager.deleteTask(testTask.getId());

        Task resultTask = taskManager.returnTask(testTask.getId());
        assertNull(resultTask);

        //Удаление несуществующей задачи
        taskManager.deleteTask(12345);
    }

    @Test
    void shouldWorkWithEpicsAndSubTasksNormally() throws IOException {
        taskManager.deleteAllTasks();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic1 = new Epic("EpicName1", "EpicDescription1", Status.DONE, localDateTime, duration);
        taskManager.addTask(testEpic1);

        SubTask subTask1 = new SubTask("name1", "description1", Status.NEW,
                localDateTime.plusDays(1), duration, testEpic1.getId());
        taskManager.addTask(subTask1);
        SubTask subTask2 = new SubTask("name2", "description2", Status.DONE,
                localDateTime.plusDays(2), duration, testEpic1.getId());

        taskManager.addTask(subTask2);
        Epic testEpic2 = new Epic("EpicName2", "EpicDescription2", Status.DONE,
                localDateTime.plusDays(3), duration);
        taskManager.addTask(testEpic2);

        assertEquals(2, taskManager.countEpics());
        assertTrue(taskManager.containEpic(testEpic1.getId()));
        assertTrue(taskManager.containEpic(testEpic2.getId()));

        ArrayList <SubTask> epic1SubTasks = new ArrayList<>(2);
        epic1SubTasks.add(subTask1);
        epic1SubTasks.add(subTask2);
        ArrayList <SubTask> epic2SubTasks = new ArrayList<>();

        assertEquals(epic1SubTasks, taskManager.getSubTasks(testEpic1.getId()));
        assertEquals(epic2SubTasks, taskManager.getSubTasks(testEpic2.getId()));
    }

}