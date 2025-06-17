package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import util.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();

        Map<String, String> params = parseFormData(body);

        String username = params.get("username");
        String password = params.get("password");

        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            String token = Session.generateToken(username);
            Session.storeToken(username, token);

            byte[] response = token.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            String errorMessage = "Unauthorized";
            byte[] response = errorMessage.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(401, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }

    private Map<String, String> parseFormData(String body) {
        Map<String, String> params = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }
}
