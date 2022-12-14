import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Поехали!
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        System.out.println("Приветствую!\n");
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
                        /*У меня какой-то странный баг, не идет считывание имени после первой задачи
                        а без корректного имени сложно дебажить кое-какие части.
                        * не могу понять с чем это связано, пока исправил вот так. */
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
                    switch (input) {
                        case 1: {
                            status = "NEW";
                            break;
                        }
                        case 2: {
                            status = "IN_PROGRESS";
                            break;
                        }
                        case 3: {
                            status = "DONE";
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
                            SubTask newTask = new SubTask(name, description, status);
                            taskManager.addTask(newTask);
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

                    System.out.println("\nNew задачи:");
                    taskManager.printNewTasks();

                    System.out.println("\nInProgress задачи:");
                    taskManager.printInProgressTasks();

                    System.out.println("\nDone задачи:");
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
            System.out.println("0 - Выход из программы");
            System.out.print("Введите комманду: ");
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
                case  ("0"):
                    return 0;
                default:
                    System.out.println("Неверная команда!");
            }
        }
    }
}

