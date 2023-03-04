package ServerPart;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface PostsHandler {
    void handle(HttpExchange exchange) throws IOException;
}
