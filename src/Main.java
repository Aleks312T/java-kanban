import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Поехали!
        Scanner scanner = new Scanner(System.in);

        System.out.println("Приветствую!\n");
        int exit = 0;
        while(exit == 0)
        {
            int command = DoCommand();
            switch (command) {
                case(1):
                {
                    System.out.println("Выбрана команда 1");
                    break;
                }
                case(2):
                {
                    System.out.println("Выбрана команда 2");
                    break;
                }
                case(3):
                {
                    System.out.println("Выбрана команда 3");
                    break;
                }
                case(4):
                {
                    System.out.println("Выбрана команда 4");
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
            System.out.println("1 - ");
            System.out.println("2 - ");
            System.out.println("3 - ");
            System.out.println("4 - ");
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

