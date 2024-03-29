package ServerPart;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();
    private final Gson gson = new Gson();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);

        System.out.println("KVServer: Запускаем сервер на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();                                 // запускаем сервер
    }

    private void load(HttpExchange h) throws IOException {
        System.out.println("KVServer: load | ");
        String requestPath = h.getRequestURI().getPath();
        String requestMethod = h.getRequestMethod();
        String[] pathParts = requestPath.split("/");
        String result = null;
        if (!hasAuth(h)) {
            System.out.println(
                    "KVServer: Запрос не авторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
            h.sendResponseHeaders(403, 0);
            h.getResponseBody().write("".getBytes());
        } else
        if (!"GET".equals(requestMethod)) {
            System.out.println("KVServer: /load ждёт GET-запрос, а получил " + h.getRequestMethod());
            h.sendResponseHeaders(405, 0);
            h.getResponseBody().write("".getBytes());
        } else
        {
            try
            {
                result = data.get(pathParts[2]);
            } catch (Exception ignored)
            {
                System.out.println("KVServer: Значения с ключом Key " + pathParts[2] + " не найдены!");
                h.sendResponseHeaders(400, 0);
                h.getResponseBody().write("".getBytes());
            }
            System.out.println("KVServer: /load выдал: " + result + "\n");
            this.sendText(h, gson.toJson(result));
        }
        h.close();
    }

    private void save(HttpExchange h) throws IOException {
        //Данный метод напичкан таким количество else, потому что при остановке с помощью return
        //Insomnia не получал ответ, и надо было запрашивать дважды
        //(В load то же самое)
        try {
            System.out.println("KVServer: save | ");
            if (!hasAuth(h)) {
                System.out.println(
                        "KVServer: Запрос не авторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
            } else
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("KVServer: Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                } else
                {
                    String value = readText(h);
                    if (value.isEmpty()) {
                        System.out.println("KVServer: Value для сохранения пустой. value указывается в теле запроса");
                        h.sendResponseHeaders(400, 0);
                    } else
                    {
                        data.put(key, value);
                        System.out.println("KVServer: Для ключа " + key + " успешно обновлено значение: " + value);
                        h.sendResponseHeaders(200, 0);
                    }
                }

            } else {
                System.out.println("KVServer: /save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("KVServer: register | ");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
