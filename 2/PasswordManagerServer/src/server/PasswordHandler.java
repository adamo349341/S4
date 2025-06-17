package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.PasswordEntry;
import server.Databasemanager;
import util.Session;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler pour gérer les opérations sur les mots de passe : GET, POST, DELETE.
 */
public class PasswordHandler implements HttpHandler {

    private final Gson gson = new Gson();
    private final Databasemanager db = Databasemanager.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // Vérifier que le token est présent et valide
        Map<String, String> query = parseQuery(exchange.getRequestURI());
        String token = query.get("token");

        if (token == null || !Session.isValid(token)) {
            exchange.sendResponseHeaders(401, -1); // Unauthorized
            return;
        }

        switch (method) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange, query);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<PasswordEntry> entries = db.loadPasswords();
        String json = gson.toJson(entries);
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        PasswordEntry entry = gson.fromJson(body, PasswordEntry.class);
        db.addPassword(entry);
        exchange.sendResponseHeaders(200, -1);
    }

    private void handleDelete(HttpExchange exchange, Map<String, String> query) throws IOException {
        String service = query.get("site");
        String username = query.get("username");
        if (service == null || username == null) {
            exchange.sendResponseHeaders(400, -1); // Bad Request
            return;
        }

        db.deletePassword(service, username);
        exchange.sendResponseHeaders(200, -1);
    }

    private Map<String, String> parseQuery(URI uri) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        String query = uri.getRawQuery();
        if (query == null) return result;

        for (String param : query.split("&")) {
            String[] parts = param.split("=");
            if (parts.length == 2) {
                String key = URLDecoder.decode(parts[0], "UTF-8");
                String value = URLDecoder.decode(parts[1], "UTF-8");
                result.put(key, value);
            }
        }
        return result;
    }
}
