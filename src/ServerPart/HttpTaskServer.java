package ServerPart;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.FileBackedTasksManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

public class HttpTaskServer extends FileBackedTasksManager {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();

    public HttpTaskServer(Path saveFile) throws IOException {
        super(saveFile);
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathParts = path.split("/");
            if(pathParts.length <= 2 || !pathParts[1].equals("tasks")) {
                writeResponse(exchange, "Некорректный URI", 400);
                return;
            }
            switch (method)
            {
                case "GET":
                    commonHandleGet(exchange);
                    break;
                case "POST":
                    commonHandlePost(exchange);
                    break;
                case "DELETE":
                    commonHandleDelete(exchange);
                    break;
                default:

            }

        }

        public void commonHandleGet(HttpExchange exchange) throws IOException {

        }

        public void commonHandlePost(HttpExchange exchange) throws IOException {

        }

        public void commonHandleDelete(HttpExchange exchange) throws IOException {

        }
        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if(responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }
    }
    @Override
    protected void save() {
        super.save();
    }

    @Override
    protected void loadFromFile(Path path) {
        super.loadFromFile(path);
    }

    @Override
    protected void startFile(Path path) {
        super.startFile(path);
    }

    @Override
    public boolean addTask(Task newTask) throws IOException {
        return super.addTask(newTask);
    }

    @Override
    public boolean addTask(Epic newTask) throws IOException {
        return super.addTask(newTask);
    }

    @Override
    public boolean addTask(SubTask newTask) throws IOException {
        return super.addTask(newTask);
    }

    @Override
    public Task returnTask(int id) {
        return super.returnTask(id);
    }

    @Override
    public void deleteTask(int id) throws IOException {
        super.deleteTask(id);
    }

    @Override
    public int countEpics() {
        return super.countEpics();
    }

    @Override
    public boolean containEpic(int code) {
        return super.containEpic(code);
    }

    @Override
    public ArrayList<SubTask> getSubTasks(int code) {
        return super.getSubTasks(code);
    }

    @Override
    public void printNewTasks() {
        super.printNewTasks();
    }

    @Override
    public void printInProgressTasks() {
        super.printInProgressTasks();
    }

    @Override
    public void printDoneTasks() {
        super.printDoneTasks();
    }

    @Override
    public void deleteAllTasks() throws IOException {
        super.deleteAllTasks();
    }

    @Override
    public void printAllCodes() {
        super.printAllCodes();
    }
}
