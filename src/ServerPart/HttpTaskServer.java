package ServerPart;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.FileBackedTasksManager;
import managers.HTTPTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    HTTPTaskManager httpTaskManager;
    //Path httpSaveFile = Paths.get("SaveFiles/httpSaveFile.txt");
    public HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpTaskManager = new HTTPTaskManager("SaveFiles/httpSaveFile.txt");

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

    }

    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathParts = path.split("/");
            if(!pathParts[1].equals("tasks")) {
                writeResponse(exchange, "Некорректный URI", 400);
                return;
            }
            if(pathParts.length == 2 && method.equals("GET"))
            {
                writeResponse(exchange, gson.toJson(httpTaskManager.getSortedTasks()), 200);
                return;
            }
            if(pathParts.length == 3 && method.equals("GET") && pathParts[2].equals("history"))
            {
                writeResponse(exchange, gson.toJson(httpTaskManager.getHistory()), 200);
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
            System.out.println("Вызван метод commonHandleGet");
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            System.out.println(Arrays.toString(pathParts) + "\n");
            Headers requestHeaders = exchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("id");
            String result = null;
            switch (pathParts[2])
            {
                case "task":
                    if ((contentTypeValues != null) && !contentTypeValues.isEmpty()) {
                        String contentTypeFirstValue = contentTypeValues.get(0);
                        int id = Integer.parseInt(contentTypeFirstValue);
                        Task task = httpTaskManager.returnTask(id);
                        result = gson.toJson(task);
                        writeResponse(exchange, result, 200);
                    } else
                    {
                        result = gson.toJson(httpTaskManager.getTasks());
                        writeResponse(exchange, result, 200);
                    }
                    break;
                case "subtask":
                    if ((contentTypeValues != null) && !contentTypeValues.isEmpty()) {
                        if(pathParts[3].equals("epic"))
                        {
                            String contentTypeFirstValue = contentTypeValues.get(0);
                            int id = Integer.parseInt(contentTypeFirstValue);
                            Epic epic = (Epic) httpTaskManager.returnTask(id);
                            result = gson.toJson(epic.getSubTasks());
                            writeResponse(exchange, result, 200);
                        } else
                        {
                            String contentTypeFirstValue = contentTypeValues.get(0);
                            int id = Integer.parseInt(contentTypeFirstValue);
                            SubTask subTask = (SubTask) httpTaskManager.returnTask(id);
                            result = gson.toJson(subTask);
                            writeResponse(exchange, result, 200);
                        }
                    } else
                    {
                        result = gson.toJson(httpTaskManager.getSubTasks());
                        writeResponse(exchange, result, 200);
                    }
                    break;
                case "epic":
                    if ((contentTypeValues != null) && !contentTypeValues.isEmpty()) {
                        String contentTypeFirstValue = contentTypeValues.get(0);
                        int id = Integer.parseInt(contentTypeFirstValue);
                        Epic epic = (Epic) httpTaskManager.returnTask(id);
                        result = gson.toJson(epic);
                        writeResponse(exchange, result, 200);
                    }else
                    {
                        result = gson.toJson(httpTaskManager.getEpics());
                        writeResponse(exchange, result, 200);
                    }
                    break;

            }
            writeResponse(exchange, result, 200);
        }

        public void commonHandlePost(HttpExchange exchange) throws IOException {
            System.out.println("Вызван метод commonHandlePost\n");
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task newTask = null;
            try
            {
                newTask = gson.fromJson(body, Task.class);
                if(newTask.getName() == null || newTask.getDescription() == null)
                    throw new IOException();

                if(newTask.getName().equals("") || newTask.getDescription() .equals(""))
                    throw new IOException();
            } catch (IOException exception)
            {
                writeResponse(exchange, "Поля комментария не могут быть пустыми", 400);
            }
            catch (JsonSyntaxException exception)
            {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            }

            writeResponse(exchange, "Вызван метод commonHandlePost", 200);
        }

        public void commonHandleDelete(HttpExchange exchange) throws IOException {
            System.out.println("Вызван метод commonHandleDelete\n");


            writeResponse(exchange, "Вызван метод commonHandleDelete", 200);
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
}
