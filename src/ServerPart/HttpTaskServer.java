package ServerPart;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
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
import java.util.Arrays;
import java.util.List;

public class HttpTaskServer {
    private static final int THIS_PORT = 8080;
    private static final int KV_PORT = 8078;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    HTTPTaskManager httpTaskManager;
    public HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        String baseURI = "http://localhost:" + KV_PORT;
        httpTaskManager = new HTTPTaskManager(baseURI);

        httpServer.bind(new InetSocketAddress(THIS_PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start(); // запускаем сервер

        System.out.println("HttpTaskServer: HTTP-сервер запущен на " + THIS_PORT + " порту!");

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
                    writeResponse(exchange, gson.toJson("Такого эндпоинта не существует"), 404);
            }
        }

        public void commonHandleGet(HttpExchange exchange) throws IOException {
            System.out.println("HttpTaskServer: Вызван метод commonHandleGet");
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            System.out.println(Arrays.toString(pathParts) + System.lineSeparator());
            Headers requestHeaders = exchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("id");
            String result;
            switch (pathParts[2])
            {
                case "task":
                    if ((contentTypeValues != null) && !contentTypeValues.isEmpty()) {
                        String contentTypeFirstValue = contentTypeValues.get(0);
                        int id = Integer.parseInt(contentTypeFirstValue);
                        Task task = httpTaskManager.returnTask(id);
                        result = gson.toJson(task);
                    } else
                    {
                        result = gson.toJson(httpTaskManager.getTasks());
                    }
                    writeResponse(exchange, result, 200);
                    break;
                case "subtask":
                    if ((contentTypeValues != null) && !contentTypeValues.isEmpty()) {
                        String contentTypeFirstValue = contentTypeValues.get(0);
                        int id = Integer.parseInt(contentTypeFirstValue);

                        if(pathParts[3].equals("epic"))
                        {
                            Epic epic = (Epic) httpTaskManager.returnTask(id);
                            result = gson.toJson(epic.getSubTasks());
                        } else
                        {
                            SubTask subTask = (SubTask) httpTaskManager.returnTask(id);
                            result = gson.toJson(subTask);
                        }
                        writeResponse(exchange, result, 200);
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
                    } else
                    {
                        result = gson.toJson(httpTaskManager.getEpics());
                        writeResponse(exchange, result, 200);
                    }
                    break;
                default:
                    result = "Некорректный запрос в типе tasks";
                    writeResponse(exchange, result, 400);
            }

        }

        public void commonHandlePost(HttpExchange exchange) throws IOException {
            System.out.println("Вызван метод commonHandlePost" + System.lineSeparator());
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            Task newTask = null;
            try
            {
                newTask = gson.fromJson(body, Task.class);
                if(newTask.getName() == null || newTask.getDescription() == null)
                    throw new IOException();
                if(newTask.getTaskType() == null || newTask.getStatus() == null)
                    throw new IOException();
                if(newTask.getName().equals("") || newTask.getDescription() .equals(""))
                    throw new IOException();
            } catch (IOException exception)
            {
                writeResponse(exchange, "Поля комментария не могут быть пустыми", 400);
                return;
            } catch (JsonSyntaxException exception)
            {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            } catch (Exception exception)
            {
                writeResponse(exchange, "Возникла непредвиденная ошибка", 400);
                return;
            }
            boolean success;
            switch (newTask.getTaskType())
            {
                case TASK:
                case EPIC:
                case SUBTASK:
                    success = httpTaskManager.addTask(newTask);
                    break;
                default:
                    success = false;
            }
            if(success)
                writeResponse(exchange, "Задача успешно добавлена!", 200);
            else
                writeResponse(exchange, "Задача конфликтует с другими по времени", 409);
        }

        public void commonHandleDelete(HttpExchange exchange) throws IOException {
            System.out.println("HttpTaskServer: Вызван метод commonHandleDelete");
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            System.out.println(Arrays.toString(pathParts));
            Headers requestHeaders = exchange.getRequestHeaders();
            List<String> contentTypeValues = requestHeaders.get("id");
            String result;
            if(!pathParts[2].equals("task")) {
                writeResponse(exchange, gson.toJson("HttpTaskServer: Такого эндпоинта не существует"), 404);
                return;
            }
            System.out.println(contentTypeValues + System.lineSeparator());
            if ((contentTypeValues != null) && !contentTypeValues.isEmpty())
            {
                try
                {
                    String contentTypeFirstValue = contentTypeValues.get(0);
                    int id = Integer.parseInt(contentTypeFirstValue);
                    Task task = httpTaskManager.returnTask(id);
                    if(task != null)
                    {
                        httpTaskManager.deleteTask(id);
                        result = gson.toJson("HttpTaskServer: Задача успешно удалена");
                        writeResponse(exchange, result, 200);
                    } else
                    {
                        result = gson.toJson("HttpTaskServer: Задачи с таким номером нет!");
                        writeResponse(exchange, result, 404);
                    }
                } catch(Exception exception)
                {
                    System.out.println(exception);
                    result = gson.toJson(exception.getMessage());
                    writeResponse(exchange, result, 501);
                }
            } else
            {
                httpTaskManager.deleteAllTasks();
                result = gson.toJson("HttpTaskServer: Все задачи успешно удалены");
                writeResponse(exchange, result, 200);
            }
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
