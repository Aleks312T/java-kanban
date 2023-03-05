package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager
{
    Path saveFile;
    boolean toSave;
    public FileBackedTasksManager(String saveFile) {
        super();
        this.toSave = false;

        //Защита для наследника HTTPTaskManager
        if(saveFile.contains("http://"))
        {
            //startFile(Path.of(saveFile));
        } else
        {
            this.saveFile = Paths.get(saveFile);
            if(Files.exists(this.saveFile))
                loadFromFile(saveFile);
            else
            {
                //Надо дописать корректное создание файла сохранения
                startFile(saveFile);
            }
        }

        this.toSave = true;
    }

    protected void save() {
        StringBuilder result = new StringBuilder(
                "id,type,name,status,description,startTime,duration,epic" + System.lineSeparator());
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

            sb.append(task.getStartTime().format(formatter)).append(",");
            sb.append(task.getDuration().getSeconds()).append(",");

            if(task.getTaskType().equals(TaskTypes.SUBTASK))
                sb.append(task.getParent()).append(",");

            //Записываем в конечный StringBuilder
            result.append(sb).append(System.lineSeparator());
        }
        result.append(System.lineSeparator());

        StringBuilder history = new StringBuilder();
        List<Task> historyList = this.historyManager.getHistory();
        if(historyList != null && !historyList.isEmpty())
        {
            for(Task task : historyList)
                history.append(task.getId()).append(",");
            //Удаляю запятую в конце
            history.deleteCharAt(history.length() - 1);
            //Конечный ответ
            result.append(history);
        }

        try(Writer writer = new FileWriter(saveFile.toFile()))
        {
            writer.write(String.valueOf(result));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void loadFromFile(String path)
    {
        Path currentPath = Paths.get(path);
        try (Reader reader = new FileReader(currentPath.toFile())) {
            BufferedReader br = new BufferedReader(reader);
            String firstLine = br.readLine();
            if(!br.ready())
            {
                System.out.println("Входные данные не были найдены в " + path);
            }
            if(firstLine.equals("id,type,name,status,description,startTime,duration,epic"))
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

                }
                if(!br.ready())
                {
                    System.out.println("Данные об истории не были найдены в " + path);
                } else
                {
                    String line = br.readLine();
                    String[] data = line.split(",");

                    this.historyManager.clearHistory();
                    //Убран foreach с целью записи в обратном порядке
                    for(int i = data.length - 1; i >= 0 ; --i)
                    {
                        String stringCode = data[i];
                        int id = Integer.parseInt(stringCode);
                        Task task = returnTaskWithoutHistory(id);
                        this.historyManager.add(task);
                    }
                }
            } else
            {
                System.out.println("Некорректные данные в " + path);
            }
            br.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void startFile(String path)
    {
        System.out.println("Trying create file in " + path + "...");
        try (PrintWriter writer = new PrintWriter(path)) {

            System.out.println("Trying to write strings...");
            StringBuilder sb = new StringBuilder();
            sb.append("id,");
            sb.append("type,");
            sb.append("name,");
            sb.append("status,");
            sb.append("description,");
            sb.append("startTime,");
            sb.append("duration,");
            sb.append("epic");
            sb.append(System.lineSeparator());
            writer.write(sb.toString());
            writer.close();
            System.out.println("Done!" + System.lineSeparator());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean addTask(Task newTask) throws IOException {
        boolean result = super.addTask(newTask);
        if(toSave && result)
            save();
        return result;
    }

    @Override
    public boolean addTask(Epic newTask) throws IOException {
        boolean result = super.addTask(newTask);
        if(toSave && result)
            save();
        return result;
    }

    @Override
    public boolean addTask(SubTask newTask) throws IOException {
        boolean result = super.addTask(newTask);
        if(toSave && result)
            save();
        return result;
    }

    @Override
    public Task returnTask(int id) {
        Task result = super.returnTask(id);
        if(toSave)
            save();
        return result;
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
    public ArrayList<SubTask> getSubTasksOfEpic(int code) {              //Не добавляю save, потому что ничего не меняется
        return super.getSubTasksOfEpic(code);
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
