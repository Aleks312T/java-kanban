package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager
{
    Path saveFile;

    public FileBackedTasksManager(Path saveFile) {
        super();
        if(Files.exists(saveFile))
            loadFromFile(saveFile);
        else
            startFile(saveFile);
    }

    private void save()
    {

    }

    protected void loadFromFile(Path path)
    {
        try (Reader reader = new FileReader(path.toFile())) {
            BufferedReader br = new BufferedReader(reader);

            System.out.println("Trying to read strings from " + path);
            if(br.readLine().equals("id,type,name,status,description,epic"))
            {
                int flag = 0;
                while (br.ready() && flag == 0) {
                    String line = br.readLine();
                    if(line.equals(""))             //Выход из цикла по окончанию записи задач
                    {
                        flag = 1;
                        continue;
                    }

                    String[] data = line.split(",");
                    //в data лежат данные по Task



                    //System.out.println(line);

                }
                String line = br.readLine();
                String[] data = line.split(",");
                //History manager
            } else
            {
                System.out.println("Incorrect data in " + path);
            }
            br.close();
            System.out.println("Done!\n");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void startFile(Path path)
    {
        System.out.println("Trying create file in " + path + "...");
        try (PrintWriter writer = new PrintWriter(path.toString())) {

            System.out.println("Trying to write strings...");
            StringBuilder sb = new StringBuilder();
            sb.append("id,");
            sb.append("type,");
            sb.append("name,");
            sb.append("status,");
            sb.append("description,");
            sb.append("epic");
            sb.append('\n');

            /*
            sb.append("17905,");
            sb.append("TASK,");
            sb.append("1,");
            sb.append("NEW,");
            sb.append("1,");
            sb.append("");
            sb.append('\n');

            sb.append("17937,");
            sb.append("EPIC,");
            sb.append("2,");
            sb.append("IN_PROGRESS,");
            sb.append("2,");
            sb.append("");
            sb.append('\n');

            sb.append('\n');
            sb.append("2,1");
            ///////*/
            writer.write(sb.toString());
            writer.close();
            System.out.println("Done!\n");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public void addTask(Epic newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public void addTask(SubTask newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public Task returnTask(int id) {
        return super.returnTask(id);
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public int countEpics() {
        return super.countEpics();                                  //Не добавляю save, потому что ничего не меняется
    }

    @Override
    public boolean containEpic(int code) {                          //Не добавляю save, потому что ничего не меняется
        return super.containEpic(code);
    }

    @Override
    public ArrayList<SubTask> findSubTasks(int code) {              //Не добавляю save, потому что ничего не меняется
        return super.findSubTasks(code);
    }

    @Override
    public void printNewTasks() {                                   //Не добавляю save, потому что ничего не меняется
        super.printNewTasks();
    }

    @Override
    public void printInProgressTasks() {                            //Не добавляю save, потому что ничего не меняется
        super.printInProgressTasks();
    }

    @Override
    public void printDoneTasks() {                                  //Не добавляю save, потому что ничего не меняется
        super.printDoneTasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void printAllCodes() {                                   //Не добавляю save, потому что ничего не меняется
        super.printAllCodes();
    }
}
