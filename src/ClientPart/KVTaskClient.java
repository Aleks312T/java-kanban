package ClientPart;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVTaskClient {
    public final int port = 8078;
    public final String baseURI = "http://localhost:" + port;
    protected URI uri;
    protected HttpRequest request;
    protected HttpClient client;
    protected Gson gson;
    protected long token;

    public int getPort() {
        return port;
    }

    public String getBaseURI() {
        return baseURI;
    }

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
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode() + " | " + response.body());
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                token = jsonElement.getAsLong();

            } else {
                System.out.println(
                        "KVTaskClient: Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" +
                    uri + "' возникла ошибка.");
            System.out.println("Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    //Метод load работает с эндингом следующего формата:
    //GET /load/<ключ>?API_TOKEN=
    public String load(String key)
    {
        System.out.println("KVTaskClient: load | " + key);
        URI tempURI = URI.create(baseURI + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(tempURI)
                .GET()
                .build();
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
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" +
                    uri + "' возникла ввода ошибка." + System.lineSeparator());
            System.out.println("Проверьте, пожалуйста, адрес и повторите попытку.");
        } catch (Exception exception) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" +
                    uri + "' возникла неизвестная ошибка.");
            System.out.println("Ошибка:" + exception.getMessage());
            System.out.println("Проверьте, пожалуйста, адрес и повторите попытку.");
        }

        return null;
    }

    //Метод put работает с эндингом следующего формата:
    //POST /save/<ключ>?API_TOKEN=
    public void put(String key, String json)
    {
        System.out.println("KVTaskClient: put | " + key + " | " + json);
        try {
            URI tempURI = URI.create(baseURI + "/save/" + key + "?API_TOKEN=" + token);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(tempURI)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("KVTaskClient: Результаты put: " + response.statusCode() + " | " + response.body());

        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("KVTaskClient: Во время выполнения запроса ресурса по url-адресу: '" +
                    uri + "' возникла ввода ошибка." + System.lineSeparator());
            System.out.println("Проверьте, пожалуйста, адрес и повторите попытку.");
        }

    }
}
