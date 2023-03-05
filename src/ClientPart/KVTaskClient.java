package ClientPart;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVTaskClient {
    URI uri;
    HttpRequest request;
    HttpClient client;
    Gson gson;
    final int port = 8078;
    final String baseURI = "http://localhost:" + port;
    long token;

    public KVTaskClient() {
        // используем код состояния как часть URL-адреса
        uri = URI.create(baseURI);
        request = HttpRequest.newBuilder().GET().uri(uri).build();
        client = HttpClient.newBuilder() // получаем экземпляр билдера
                .build(); // заканчиваем настройку и создаём ("строим") HTTP-клиент
        gson = new Gson();
        register();
    }

    protected void register()
    {
        System.out.println("KVTaskClient: register | ");
        URI tempURI = URI.create(baseURI + "/register" );
        HttpRequest request = HttpRequest.newBuilder().GET().uri(tempURI).build();
        String result = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode() + " | " + response.body());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                token = jsonElement.getAsLong();

            } else {
                System.out.println("KVTaskClient: Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    //GET /load/<ключ>?API_TOKEN=
    public String load(String key)
    {
        System.out.println("KVTaskClient: load | " + key);
        URI tempURI = URI.create(baseURI + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(tempURI)
                //.header("API_TOKEN", Integer.toString(token))
                .GET()
                .build();
        String result = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("KVTaskClient: Результаты load: " + response.statusCode() + " | " + response.body());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                return jsonElement.getAsString();
            } else {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ввода ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        } catch (Exception exception) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла неизвестная ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

        return null;
    }

    //POST /save/<ключ>?API_TOKEN=
    public void put(String key, String json)
    {
        System.out.println("KVTaskClient: put | " + key + " | " + json);
        try {
            URI tempURI = URI.create(baseURI + "/save/" + key + "?API_TOKEN=" + token);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(tempURI)
                    //.header("API_TOKEN", Integer.toString(token))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("KVTaskClient: Результаты put: " + response.statusCode() + " | " + response.body());

        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

    }
}
