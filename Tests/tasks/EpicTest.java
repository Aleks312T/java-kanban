package tasks;

import main.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

class EpicTest {

    static ArrayList<SubTask> subTasks;
    @BeforeAll
    public static void beforeAll()
    {
        //testStartTime = LocalDateTime.now();
        subTasks = new ArrayList<>(5);
    }

    @Test
    void shouldHaveInitialStatusTest() {                //Тест a
        subTasks.clear();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        assertEquals(testEpic.getStatus(), Status.DONE, "Тест a статус не совпадает с ожидаемым");

        assertEquals(subTasks, testEpic.getSubTasks(), "Тест a подзадачи не совпадают с ожидаемыми");
    }

    @Test
    void shouldHaveNewStatusTest() {                    //Тест b
        subTasks.clear();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        SubTask subTask1 = new SubTask("name1", "description1", Status.NEW,
                localDateTime.plusDays(1), duration, testEpic.id);

        testEpic.addSubTask(subTask1);
        assertEquals(testEpic.getStatus(), Status.NEW, "Тест b статус не совпадает с ожидаемым");

        SubTask subTask2 = new SubTask("name2", "description2", Status.NEW,
                localDateTime.plusDays(2), duration, testEpic.id);

        testEpic.addSubTask(subTask2);
        assertEquals(testEpic.getStatus(), Status.NEW, "Тест b статус не совпадает с ожидаемым");

        subTasks.add(subTask1);
        subTasks.add(subTask2);
        assertEquals(subTasks, testEpic.getSubTasks(), "Тест b подзадачи не совпадают с ожидаемыми");
    }

    @Test
    void shouldHaveDoneStatusTest() {                    //Тест c
        subTasks.clear();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        SubTask subTask1 = new SubTask("name1", "description1", Status.DONE,
                localDateTime.plusDays(1), duration, testEpic.id);
        testEpic.addSubTask(subTask1);
        assertEquals(testEpic.getStatus(), Status.DONE, "Тест c статус не совпадает с ожидаемым");

        SubTask subTask2 = new SubTask("name2", "description2", Status.DONE,
                localDateTime.plusDays(2), duration, testEpic.id);

        testEpic.addSubTask(subTask2);
        assertEquals(testEpic.getStatus(), Status.DONE, "Тест c статус не совпадает с ожидаемым");

        subTasks.add(subTask1);
        subTasks.add(subTask2);
        assertEquals(subTasks, testEpic.getSubTasks(), "Тест c подзадачи не совпадают с ожидаемыми");
    }

    @Test
    void shouldHaveInProgressStatusTest() {                    //Тест d
        subTasks.clear();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        SubTask subTask1 = new SubTask("name1", "description1", Status.DONE,
                localDateTime.plusDays(1), duration, testEpic.id);
        testEpic.addSubTask(subTask1);
        assertEquals(testEpic.getStatus(), Status.DONE, "Тест d статус не совпадает с ожидаемым");

        SubTask subTask2 = new SubTask("name2", "description2", Status.NEW,
                localDateTime.plusDays(2), duration, testEpic.id);

        testEpic.addSubTask(subTask2);
        assertEquals(testEpic.getStatus(), Status.IN_PROGRESS, "Тест d статус не совпадает с ожидаемым");

        subTasks.add(subTask1);
        subTasks.add(subTask2);
        assertEquals(subTasks, testEpic.getSubTasks(), "Тест d подзадачи не совпадают с ожидаемыми");
    }

    @Test
    void shouldHaveStatusBasedOnSubTasksTest() {              //Тест e
        subTasks.clear();
        LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 10, 13, 0);
        Duration duration = Duration.ofHours(1);
        Epic testEpic = new Epic("EpicName", "EpicDescription", Status.DONE, localDateTime, duration);
        SubTask subTask1 = new SubTask("name1", "description1", Status.NEW,
                localDateTime.plusDays(1), duration, testEpic.id);
        testEpic.addSubTask(subTask1);
        assertEquals(testEpic.getStatus(), Status.NEW, "Тест e статус не совпадает с ожидаемым");

        SubTask subTask2 = new SubTask("name2", "description2", Status.IN_PROGRESS,
                localDateTime.plusDays(2), duration, testEpic.id);
        testEpic.addSubTask(subTask2);
        assertEquals(testEpic.getStatus(), Status.IN_PROGRESS, "Тест e статус не совпадает с ожидаемым");

        SubTask subTask3 = new SubTask("name3", "description3", Status.DONE,
                localDateTime.plusDays(3), duration, testEpic.id);

        testEpic.addSubTask(subTask3);
        assertEquals(testEpic.getStatus(), Status.IN_PROGRESS, "Тест e статус не совпадает с ожидаемым");

        subTasks.add(subTask1);
        subTasks.add(subTask2);
        subTasks.add(subTask3);
        assertEquals(subTasks, testEpic.getSubTasks(), "Тест e подзадачи не совпадают с ожидаемыми");
    }

    @AfterAll
    public static void afterAll()
    {
        //testEndTime = LocalDateTime.now();
        //testDuration = Duration.between(startTime, endTime);

    }
}