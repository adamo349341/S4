package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import util.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Handler pour la connexion utilisateur.
 */
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
        byte[] data = is.readAllBytes();
        String body = new String(data, StandardCharsets.UTF_8);

        String[] parts = body.split("&");
        String username = null, password = null;
        for (String part : parts) {
            if (part.startsWith("username=")) {
                username = part.substring("username=".length());
            } else if (part.startsWith("password=")) {
                password = part.substring("password=".length());
            }
        }

        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            String token = Session.generateToken(username);
            Session.storeToken(username,token);

            byte[] response = token.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        } else {
            exchange.sendResponseHeaders(401, -1); // Unauthorized
        }
    }
}
