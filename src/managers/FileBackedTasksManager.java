package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager
{
    Path saveFile;
    boolean toSave;
    public FileBackedTasksManager(Path saveFile) {
        super();
        this.saveFile = saveFile;
        this.toSave = false;

        if(Files.exists(saveFile))
            loadFromFile(saveFile);
        else
            startFile(saveFile);

        this.toSave = true;
    }

    public void save() throws IOException {
        StringBuilder result = new StringBuilder("id,type,name,status,description,epic\n");
        for(Integer id : allTaskIDs)
        {
            //Добавляем поэтапно текущую задачу в StringBuilder
            StringBuilder sb = new StringBuilder();
            Task task = returnTaskWithoutHistory(id);
            sb.append(task.getId()).append(",");
            sb.append(task.getTaskType()).append(",");
            sb.append(task.getName()).append(",");
            sb.append(task.getStatus()).append(",");
            sb.append(task.getDescription()).append(",");
            if(task.getTaskType().equals(TaskTypes.SUBTASK))
                sb.append(task.getParent()).append(",");

            //Записываем в конечный StringBuilder
            result.append(sb).append("\n");
        }
        result.append("\n");

        StringBuilder history = new StringBuilder();
        List<Task> historyList = this.historyManager.getHistory();
        for(Task task : historyList)
            history.append(task.getId()).append(",");
        //Удаляю запятую в конце
        history.deleteCharAt(history.length() - 1);

        //Конечный ответ
        result.append(history);
        try(Writer writer = new FileWriter(saveFile.toFile()))
        {
            writer.write(String.valueOf(result));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(result); System.out.println("--------------------------------------\n\n");

    }

    protected void loadFromFile(Path path)
    {
        try (Reader reader = new FileReader(path.toFile())) {
            BufferedReader br = new BufferedReader(reader);

            //System.out.println("Trying to read strings from " + path);
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
                    switch (taskTypeFromString(data[1]))
                    {
                        //Такие костыли сделаны для того, чтобы типы нормально приводились друг к другу
                        case TASK:{
                            Task newTask = taskFromString(line);
                            this.addTask(newTask);
                            break;
                        }
                        case EPIC:{
                            Epic newTask = (Epic) taskFromString(line);
                            this.addTask(newTask);
                            break;
                        }
                        case SUBTASK: {
                            SubTask newTask = (SubTask) taskFromString(line);
                            this.addTask(newTask);
                            break;
                        }
                    }
                    //System.out.println(line);

                }
                String line = br.readLine();
                String[] data = line.split(",");

                this.historyManager.clearHistory();
                //for(String stringCode : data)
                //Убран foreach с целью записи в обратном порядке
                for(int i = data.length - 1; i >= 0 ; --i)
                {
                    String stringCode = data[i];
                    int id = Integer.parseInt(stringCode);
                    Task task = returnTaskWithoutHistory(id);
                    this.historyManager.add(task);
                }
                //System.out.println(this.historyManager.getHistory().toString());
            } else
            {
                System.out.println("Incorrect data in " + path);
            }
            br.close();
            //System.out.println("Done!\n");

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
    public void addTask(Task newTask) throws IOException {
        super.addTask(newTask);
        if(toSave)
            save();
    }

    @Override
    public void addTask(Epic newTask) throws IOException {
        super.addTask(newTask);
        if(toSave)
            save();
    }

    @Override
    public void addTask(SubTask newTask) throws IOException {
        super.addTask(newTask);
        if(toSave)
            save();
    }

    @Override
    public Task returnTask(int id) {
        return super.returnTask(id);
    }

    @Override
    public void deleteTask(int id) throws IOException {
        super.deleteTask(id);
        if(toSave)
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
    public void deleteAllTasks() throws IOException {
        super.deleteAllTasks();
        if(toSave)
            save();
    }

    @Override
    public void printAllCodes() {                                   //Не добавляю save, потому что ничего не меняется
        super.printAllCodes();
    }
}
