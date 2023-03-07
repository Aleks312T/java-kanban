package managers;

import ClientPart.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskTypes;

import java.io.IOException;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager{
    //Надо объявить до конструктора, чтобы прошла регистрация
    protected KVTaskClient kvTaskClient = new KVTaskClient();
    protected String URI;
    public HTTPTaskManager(String URI) {
        super(URI);
        this.URI = URI;
        startFile(URI);
    }

    @Override
    protected void save() {
        System.out.println("HTTPTaskManager: save | ");
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
        kvTaskClient.put(URI, String.valueOf(result));
    }

    @Override
    protected void loadFromFile(String path) {
        System.out.println("HTTPTaskManager: loadFromFile | " + path);
        try {
            String loadedString = kvTaskClient.load(path);

            if(loadedString == null)
            {
                System.out.println("Входные данные не были найдены в " + path);
                return;
            }
            String[] separatedLines = loadedString.split(System.lineSeparator());
            if(separatedLines[0].equals("id,type,name,status,description,startTime,duration,epic"))
            {
                int flag = 0;
                for(String line : separatedLines) {
                    if(line.equals(""))             //Выход из цикла по окончанию записи задач
                    {
                        flag = 1;
                        continue;
                    }
                    String[] data = line.split(",");
                    //Работа с историей задач
                    if(flag == 1)
                    {
                        this.historyManager.clearHistory();
                        //Убран foreach с целью записи в обратном порядке
                        for(int i = data.length - 1; i >= 0 ; --i)
                        {
                            String stringCode = data[i];
                            int id = Integer.parseInt(stringCode);
                            Task task = returnTaskWithoutHistory(id);
                            this.historyManager.add(task);
                        }
                    } else
                    {   //Работа с задачами
                        //в data лежат данные по Task
                        switch (taskTypeFromString(data[1]))
                        {
                            /*Даже костыли нужно переиспользовать, а не копипастить из класса в класс. */
                            /*Я не понял, что имеется ввиду и что требуется исправить? Если речь идет о том, чтобы вынести
                            * это действие в отдельный метод, то думаю, пока что это лишнее. Пока они используются 2 раза
                            * в одном методе (обычном и перегрузке). Более того, я попытался это переделать на простое
                            * использование taskFromString(line), но типы нормально не приводятся*/

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
                }
            } else
            {
                System.out.println("Некорректные данные в " + path);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void startFile(String path) {
        System.out.println("HTTPTaskManager: startFile | ");
        String result = "id," +
                "type," +
                "name," +
                "status," +
                "description," +
                "startTime," +
                "duration," +
                "epic" +
                System.lineSeparator();
        kvTaskClient.put(URI, result);
    }
}
