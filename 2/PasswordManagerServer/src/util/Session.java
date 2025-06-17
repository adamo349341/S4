package util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestion des sessions côté serveur : génération, stockage et validation de tokens.
 */
public class Session {

    private static final Map<String, String> tokenMap = new ConcurrentHashMap<>();
    private static final SecureRandom random = new SecureRandom();

    /**
     * Génère un token sécurisé pour un utilisateur.
     * @param username le nom d'utilisateur
     * @return un token unique
     */
    public static String generateToken(String username) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        storeToken(username, token);
        return token;
    }

    /**
     * Stocke un token pour un utilisateur.
     * @param username le nom d'utilisateur
     * @param token le token à associer
     */
    public static void storeToken(String username, String token) {
        tokenMap.put(token, username);
    }

    /**
     * Vérifie si un token est valide.
     * @param token le token à vérifier
     * @return true si le token est présent dans la session
     */
    public static boolean isValid(String token) {
        return token != null && tokenMap.containsKey(token);
    }

    /**
     * Récupère le nom d'utilisateur associé à un token.
     * @param token le token
     * @return le nom d'utilisateur ou null si token invalide
     */
    public static String getUsername(String token) {
        return tokenMap.get(token);
    }

    /**
     * Invalide un token (par exemple à la déconnexion).
     * @param token le token à supprimer
     */
    public static void invalidate(String token) {
        tokenMap.remove(token);
    }
    public static Set<String> getAllTokens() {
        // Return a copy to avoid exposing the internal structure
        synchronized (tokenMap) {
            return new HashSet<>(tokenMap.keySet());
        }
    }
}
