package server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsServer;

/**
 * Point d’entrée principal du serveur HTTPS pour le gestionnaire de mots de passe.
 */
public class Server {

    public static void main(String[] args) {
        try {
            int port = 8443;  // Port standard pour HTTPS
            HttpsServer httpsServer = HttpsServerConfig.createSecureServer(port);

            // Enregistrer les endpoints
            HttpContext loginContext     = httpsServer.createContext("/login", new LoginHandler());
            HttpContext passwordsContext = httpsServer.createContext("/passwords", new PasswordHandler());
            HttpContext debugContext = httpsServer.createContext("/debug", new DebugHandler());


            httpsServer.setExecutor(null);  // utilise l’exécuteur par défaut
            httpsServer.start();

            System.out.println(" Serveur HTTPS lancé sur https://localhost:" + port);
        } catch (Exception e) {
            System.err.println(" Erreur au démarrage du serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
