package main;

import managers.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{
        // Поехали!
        final String HOME = System.getProperty("user.home");
        Scanner scanner = new Scanner(System.in);

        Path saveFile = Paths.get("SaveFile.txt");
        FileBackedTasksManager taskManager = new FileBackedTasksManager(saveFile);

        System.out.println("Приветствую!");
        System.out.println();
        int exit = 0;
        while(exit == 0)
        {
            int command = DoCommand();
            switch (command) {
                case(1):
                {
                    System.out.println("Выбрана команда 1");
                    System.out.println("Введите имя");
                    String name;
                    name = scanner.nextLine();

                    System.out.println("Введите описание");
                    String description = scanner.nextLine();

                    System.out.println("Введите статус");
                    System.out.println("1 - NEW; 2 - IN_PROGRESS; 3 - DONE");
                    String input = scanner.nextLine();
                    int intInput = tryParseInt(input);
                    Status status;
                    switch (intInput) {
                        case 1: {
                            status = Status.NEW;
                            break;
                        }
                        case 2: {
                            status = Status.IN_PROGRESS;
                            break;
                        }
                        case 3: {
                            status = Status.DONE;
                            break;
                        }
                        default:
                            status = Status.NONE;
                    }

                    System.out.println("Введите тип задачи (Task; Epic; SubTask)");
                    System.out.println("1 - Task; 2 - Epic; 3 - SubTask");
                    input = scanner.nextLine();
                    intInput = tryParseInt(input);
                    switch (intInput) {
                        case 1: {
                            Task newTask = new Task(name, description, status);
                            taskManager.addTask(newTask);
                            break;
                        }
                        case 2: {
                            Epic newTask = new Epic(name, description, status);
                            taskManager.addTask(newTask);
                            break;
                        }
                        case 3: {
                            if(taskManager.countEpics() > 0)
                            {
                                System.out.print("Введите код tasks.Epic задачи: ");
                                String id = scanner.nextLine();
                                int intId = tryParseInt(id);
                                if(taskManager.containEpic(intId))
                                {
                                    SubTask newTask = new SubTask(name, description, status, intId);
                                    taskManager.addTask(newTask);
                                } else
                                    System.out.print("Ошибка, такого эпика нет");

                            } else
                            {
                                System.out.print("Невозможно создать, т.к. нет tasks.Epic задач");
                            }
                            break;
                        }
                        default:
                            System.out.println("Некорректный тип задачи");
                    }
                    break;
                }
                case(2):
                {
                    System.out.println("Выбрана команда 2");
                    System.out.println("Вывести все задачи");
                    System.out.println();
                    System.out.println("New задачи:");
                    taskManager.printNewTasks();
                    System.out.println();
                    System.out.println("InProgress задачи:");
                    taskManager.printInProgressTasks();
                    System.out.println();
                    System.out.println("Done задачи:");
                    taskManager.printDoneTasks();
                    break;
                }
                case(3):
                {
                    System.out.println("Выбрана команда 3");
                    System.out.println("Удалить все задачи");
                    System.out.println();
                    System.out.println("Вы уверены, что хотите удалить все задачи?");
                    System.out.print("Введите 1 для подтверждения: ");
                    String input = scanner.nextLine();
                    int intInput = tryParseInt(input);
                    if(intInput == 1)
                    {
                        taskManager.deleteAllTasks();
                        System.out.println("Все задачи удалены");
                    }
                    break;
                }
                case(4):
                {
                    System.out.println("Выбрана команда 4");
                    System.out.println("Получение по идентификатору");
                    System.out.println();
                    System.out.print("Введите код задачи: ");

                    String id = scanner.nextLine();
                    int intId = tryParseInt(id);

                    Task demandedTask = taskManager.returnTask(intId);
                    System.out.print(demandedTask);
                    break;
                }
                case(5):
                {
                    System.out.println("Выбрана команда 5");
                    System.out.println("Удаление по идентификатору");
                    System.out.println();
                    System.out.print("Введите код задачи: ");

                    String id = scanner.nextLine();
                    int intId = tryParseInt(id);

                    taskManager.deleteTask(intId);
                    break;
                }
                case(6):
                {
                    System.out.println("Выбрана команда 6");
                    System.out.println("Получение листа задач одного эпика");
                    System.out.println();
                    System.out.print("Введите код эпика: ");

                    String id = scanner.nextLine();
                    int intId = tryParseInt(id);

                    if(taskManager.containEpic(intId))
                    {
                        ArrayList <SubTask> result = taskManager.findSubTasks(intId);
                        if(result.size() == 0)
                            System.out.print("У данного эпика нет подзадач");
                        else
                            for(SubTask subTask : result)
                                System.out.println(subTask.toString());
                    } else
                    {
                        System.out.println("Такого эпика нет");
                    }
                    break;
                }
                case(7):
                {
                    System.out.println("Выбрана команда 7");
                    System.out.println("Вывести все по кодам");

                    taskManager.printAllCodes();
                    break;
                }
                case(8):
                {
                    System.out.println("Выбрана команда 8");
                    System.out.println("8 - Вывести историю просмотров задач");
                    System.out.println();
                    System.out.println("История просмотров");
                    List<Task> historyList = taskManager.getHistory();

                    int size = Integer.min(10, historyList.size());
                    for(int i = 0; i < size; ++i)
                        System.out.println((i + 1) + " - " + historyList.get(i));

                    System.out.println();
                    break;
                }
                case(0):
                {
                    //Автосохранение никто не отменял
                    taskManager.save();
                    exit = 1;
                    break;
                }
                default:
                    System.out.println("Такой команды нет!");
            }
        }
        System.out.println();
        System.out.println("До свидания!");
    }

    public static int DoCommand()
    {
        String command;
        int result;
        Scanner scanner = new Scanner(System.in);
        while(true)
        {
            System.out.println();
            System.out.println("Какая команда вас интересует?");
            System.out.println("1 - Добавление задачи");
            System.out.println("2 - Вывести все задачи");
            System.out.println("3 - Удалить все задачи");
            System.out.println("4 - Получение по идентификатору");
            System.out.println("5 - Удаление по идентификатору");
            System.out.println("6 - Получение листа задач одного эпика");
            System.out.println("7 - Вывести все идентификаторы задач");
            System.out.println("8 - Вывести историю просмотров задач");
            System.out.println("--------------------------------------");
            System.out.println("0 - Выход из программы");
            System.out.print("Введите команду: ");

            command = scanner.nextLine();
            result = tryParseInt(command);
            if(result == -1)
                System.out.println("Неверный ввод команды!");
            else
                return result;
        }
    }

    //Сделано с целью конвертации ввода из строки
    //в обычное число в обход возможных ошибок
    public static int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

