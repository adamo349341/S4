package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import util.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler to show debugging information for the server.
 */
public class DebugHandler implements HttpHandler {

    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("activeTokens", Session.getAllTokens()); // Assuming you implement this
        debugInfo.put("passwordEntries", Databasemanager.getInstance().loadPasswords());

        String json = gson.toJson(debugInfo);
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
