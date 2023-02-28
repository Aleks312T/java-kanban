package tasks;

import java.util.Comparator;

public class TaskTimeComparator implements Comparator<Task> { // на месте T - класс Task

    @Override
    public int compare(Task task1, Task task2) {

        // сравниваем задачи — более поздние должны быть дальше в списке
        if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return 1;

        // более ранние — ближе к началу списка
        } else if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return -1;

        // если в один момент времени, нужно вернуть 0
        } else {
            return 0;
        }
    }
}