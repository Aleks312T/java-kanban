package managers;

import main.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

class InMemoryHistoryManagerTest {
    static LocalDateTime testStartTime;
    static LocalDateTime testEndTime;
    static Duration testDuration;
    static InMemoryHistoryManager historyManager;
    @BeforeAll
    public static void beforeAll()
    {
        testStartTime = LocalDateTime.now();
        Path saveFile = Paths.get("SaveFileForTest.txt");
        historyManager = new InMemoryHistoryManager();
    }

    @AfterAll
    public static void afterAll()
    {
        testEndTime = LocalDateTime.now();
        testDuration = Duration.between(testStartTime, testEndTime);

    }

    @Test
    void shouldRemoveHistoryCorrectly() throws IOException {
        historyManager.clearHistory();
        ArrayList<Task> result = historyManager.getHistory();
        assertEquals(new ArrayList<Task>(), result);
    }

    @Test
    void shouldAddAndRemoveTasksCorrectly() throws IOException {
        historyManager.clearHistory();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        SubTask testSubTask1 = new SubTask("SubTaskName1", "SubTaskDescription1", Status.NEW,
                localDateTime.plusDays(1), duration, testEpic.getId());
        testEpic.addSubTask(testSubTask1);
        Task testTask = new Task("TaskName1", "TaskDescription1", Status.DONE, localDateTime, duration);

        assertEquals(0, historyManager.getCurrentSize());
        historyManager.add(testTask);
        assertEquals(1, historyManager.getCurrentSize());

        historyManager.add(testEpic);
        assertEquals(2, historyManager.getCurrentSize());

        historyManager.add(testSubTask1);
        assertEquals(3, historyManager.getCurrentSize());

        historyManager.remove(testTask.getId());
        assertEquals(2, historyManager.getCurrentSize());

        //Второй раз ничего не удалится
        historyManager.remove(testTask.getId());
        assertEquals(2, historyManager.getCurrentSize());

        //Ничего не удалится
        historyManager.remove(12345);
        assertEquals(2, historyManager.getCurrentSize());

        historyManager.remove(testEpic.getId());
        assertEquals(1, historyManager.getCurrentSize());

        historyManager.remove(testSubTask1.getId());
        assertEquals(0, historyManager.getCurrentSize());

        //Ничего не удалится
        historyManager.remove(12345);
        assertEquals(0, historyManager.getCurrentSize());
    }


    @Test
    void shouldGetMaxSize() {
        historyManager.clearHistory();
        assertEquals(10, historyManager.getMaxSize());
    }

    @Test
    void shouldGetHistoryCorrectly() {
        historyManager.clearHistory();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        SubTask testSubTask1 = new SubTask("SubTaskName1", "SubTaskDescription1", Status.NEW,
                localDateTime.plusDays(1), duration, testEpic.getId());
        testEpic.addSubTask(testSubTask1);
        Task testTask = new Task("TaskName1", "TaskDescription1", Status.DONE, localDateTime, duration);

        ArrayList<Task> list = new ArrayList<>();

        list.add(testEpic);
        historyManager.add(testEpic);
        list.add(testTask);
        historyManager.add(testTask);
        list.add(testSubTask1);
        historyManager.add(testSubTask1);

        Collections.reverse(list);
        assertEquals(list, historyManager.getHistory());

        //testTask был удален
        list.remove(1);
        historyManager.remove(testTask.getId());

        assertEquals(list, historyManager.getHistory());

        //Добавляем testEpic еще раз
        historyManager.add(testEpic);
        //Приводим list к требуемому результату
        list.clear();
        list.add(testSubTask1);
        list.add(testEpic);
        Collections.reverse(list);

        assertEquals(list, historyManager.getHistory());
    }
}