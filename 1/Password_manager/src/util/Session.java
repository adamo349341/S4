package util;

public class Session {
    private static String token;

    public static void setToken(String tokenValue) {
        token = tokenValue;
    }

    public static String getToken() {
        return token;
    }

    public static boolean isAuthenticated() {
        return token != null && !token.isEmpty();
    }

    public static void clear() {
        token = null;
    }
}
