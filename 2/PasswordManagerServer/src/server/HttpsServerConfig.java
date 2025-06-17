package server;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

/**
 * Classe utilitaire pour créer un HttpsServer sécurisé (TLS).
 */
public final class HttpsServerConfig {

    private static final String KEYSTORE_PATH     = "keystore/password_manager_server.keystore";
    private static final String KEYSTORE_PASSWORD = "123456";

    private HttpsServerConfig() { /* utilitaire : constructeur privé */ }

    /**
     * Crée et retourne un HttpsServer configuré pour TLS.
     *
     * @param port Port d’écoute (ex. 8443)
     * @return Instance prête à être démarrée (server.start())
     * @throws Exception si configuration SSL invalide
     */
    public static HttpsServer createSecureServer(int port) throws Exception {

        /* ---------- Charger le keystore (JKS) ---------- */
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
            ks.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }

        /* ---------- Initialiser KeyManager / TrustManager ---------- */
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        /* ---------- Contexte SSL ---------- */
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        /* ---------- Créer le HttpsServer ---------- */
        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(port), 0);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                try {
                    // Paramètres par défaut
                    SSLContext ctx = getSSLContext();
                    SSLEngine engine = ctx.createSSLEngine();
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    SSLParameters defaultSSLParams = ctx.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParams);
                } catch (Exception ex) {
                    System.err.println("Erreur configuration HTTPS: " + ex.getMessage());
                }
            }
        });

        return httpsServer;
    }
}
