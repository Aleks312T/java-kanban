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
                        name = scanner.nextLine();
                        flag = 1;
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
                    String type;
                    switch (input) {
                        case 1: {
                            type = "Task";
                            break;
                        }
                        case 2: {
                            type = "Epic";
                            break;
                        }
                        case 3: {
                            type = "SubTask";
                            break;
                        }
                        default:
                            type = "";
                    }

                    taskManager.addTask(name, description, status, type);
                    break;
                }
                case(2):
                {
                    System.out.println("Выбрана команда 2");
                    System.out.println("Вывести все NEW задачи");
                    taskManager.printNewTasks();
                    break;
                }
                case(3):
                {
                    System.out.println("Выбрана команда 3");
                    System.out.println("Вывести все IN_PROGRESS задачи");
                    taskManager.printInProgressTasks();

                    break;
                }
                case(4):
                {
                    System.out.println("Выбрана команда 5");
                    System.out.println("Вывести все DONE задачи");
                    taskManager.printDoneTasks();
                    break;
                }
                case(5):
                {
                    System.out.println("Выбрана команда 5");
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
            System.out.println("Какая команда вас интересует?");
            System.out.println("1 - Добавление задачи");
            System.out.println("2 - Вывести все NEW задачи");
            System.out.println("3 - Вывести все IN_PROGRESS задачи");
            System.out.println("4 - Вывести все DONE задачи");
            System.out.println("5 - ");
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

