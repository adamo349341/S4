package rpc;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

    private static final Map<String, String> tokenMap = new ConcurrentHashMap<>();
    private static final SecureRandom random = new SecureRandom();

    // Add this static field to hold the current token (for client-side session tracking)
    private static String token;

    public static String generateToken(String username) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        storeToken(username, token);
        return token;
    }

    public static void setToken(String token) {
        Session.token = token;  // assign to the static field
    }

    public static String getToken() {
        return token;  // return the static field
    }

    public static void clearToken() {
        token = null;  // clear the static field
    }

    public static void storeToken(String username, String token) {
        tokenMap.put(token, username);
    }

    public static boolean isValid(String token) {
        return token != null && tokenMap.containsKey(token);
    }

    public static String getUsername(String token) {
        return tokenMap.get(token);
    }

    public static void invalidate(String token) {
        tokenMap.remove(token);
    }
}
