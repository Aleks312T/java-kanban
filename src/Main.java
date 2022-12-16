import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Поехали!
        Scanner scanner = new Scanner(System.in);
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        TaskManager inMemoryTaskManager = Managers.getDefault();

        System.out.println("Приветствую!");
        System.out.println();
        int exit = 0;
        int flag = 0;
        while(exit == 0)
        {
            int command = DoCommand();
            switch (command) {
                case(1):
                {
                    System.out.println("Выбрана команда 1");
                    System.out.println("Введите имя");
                    String name;
                    if(flag == 0)
                    {
                        /*У меня какой-то странный баг, не идет считывание имени после первой задачи,
                        а без корректного имени сложно отлаживать кое-какие части.
                        * Не могу понять с чем это связано, пока исправил вот так. */
                        flag = 1;
                    } else
                    {
                        //System.out.println("ПРОВЕРКА БАГА С NAME - БАГ ВСЕ ЕЩЕ ЕСТЬ");
                        name = scanner.nextLine();
                    }
                    name = scanner.nextLine();

                    System.out.println("Введите описание");
                    String description = scanner.nextLine();

                    System.out.println("Введите статус");
                    System.out.println("1 - NEW; 2 - IN_PROGRESS; 3 - DONE");
                    int input = scanner.nextInt();
                    String status;
                    Status enumStatus;
                    switch (input) {
                        case 1: {
                            status = "NEW";
                            enumStatus = Status.NEW;
                            break;
                        }
                        case 2: {
                            status = "IN_PROGRESS";
                            enumStatus = Status.IN_PROGRESS;
                            break;
                        }
                        case 3: {
                            status = "DONE";
                            enumStatus = Status.DONE;
                            break;
                        }
                        default:
                            status = "";
                    }

                    System.out.println("Введите тип задачи (Task; Epic; SubTask)");
                    System.out.println("1 - Task; 2 - Epic; 3 - SubTask");
                    input = scanner.nextInt();
                    switch (input) {
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
                                System.out.print("Введите код Epic задачи: ");
                                int id = scanner.nextInt();
                                if(taskManager.containEpic(id))
                                {
                                    SubTask newTask = new SubTask(name, description, status, id);
                                    taskManager.addTask(newTask);
                                } else
                                    System.out.print("Ошибка, такого эпика нет");

                            } else
                            {
                                System.out.print("Невозможно создать, т.к. нет Epic задач");
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
                    int input = scanner.nextInt();
                    if(input == 1)
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
                    int id = scanner.nextInt();
                    Task demandedTask = taskManager.returnTask(id);
                    System.out.print(demandedTask.toString());
                    break;
                }
                case(5):
                {
                    System.out.println("Выбрана команда 5");
                    System.out.println("Удаление по идентификатору");
                    System.out.println();
                    System.out.print("Введите код задачи: ");
                    int id = scanner.nextInt();
                    taskManager.deleteTask(id);
                    break;
                }
                case(6):
                {
                    System.out.println("Выбрана команда 6");
                    System.out.println("Получение листа задач одного эпика");
                    System.out.println();
                    System.out.print("Введите код эпика: ");
                    int id = scanner.nextInt();
                    if(taskManager.containEpic(id))
                    {
                        ArrayList <SubTask> result = taskManager.findSubTasks(id);
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
                    System.out.println("8 - Вывести историю просмотров задач (не сделано)");

                    break;
                }
                case(0):
                {
                    exit = 1;
                    break;
                }
            }
        }
        System.out.println();
        System.out.println("До свидания!");
    }

    public static int DoCommand()
    {
        String command;
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
            System.out.println("8 - Вывести историю просмотров задач (не сделано)");
            System.out.println("0 - Выход из программы");
            System.out.print("Введите команду: ");
            command = scanner.nextLine();                       //Сделано с целью конвертации ввода из строки
            switch (command) {                                  //в обычное число в обход возможных ошибок
                case  ("1"):
                    return 1;
                case  ("2"):
                    return 2;
                case  ("3"):
                    return 3;
                case  ("4"):
                    return 4;
                case  ("5"):
                    return 5;
                case  ("6"):
                    return 6;
                case  ("7"):
                    return 7;
                case  ("8"):
                    return 8;
                case  ("0"):
                    return 0;
                default:
                    System.out.println("Неверная команда!");
            }
        }
    }
}

