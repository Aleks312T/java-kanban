package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {

    public static LocalDateTime startTime;
    public static LocalDateTime endTime;
    public static Duration duration;

    @BeforeAll
    public static void beforeAll()
    {
        startTime = LocalDateTime.now();
    }

    @Test
    void changeStatus() {
    }

    @Test
    void addSubTask() {
    }

    @Test
    void getSubTasks() {
    }

    @Test
    void testToString() {
    }

    @AfterAll
    public static void afterAll()
    {
        endTime = LocalDateTime.now();
        duration = Duration.between(startTime, endTime);

    }
}