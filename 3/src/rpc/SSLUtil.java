package rpc;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * Initialise un SSLContext qui fait confiance au certificat du serveur
 * présent dans le truststore (JKS).
 */
public final class SSLUtil {

    private static final String TRUSTSTORE_PATH     = "keystore/password_manager_client.truststore";
    private static final String TRUSTSTORE_PASSWORD = "clienttrustpass";

    private static SSLSocketFactory socketFactory;

    private SSLUtil() { /* utilitaire */ }

    public static SSLSocketFactory getSocketFactory() {
        if (socketFactory == null) {
            try {
                /* -------- Charger le truststore -------- */
                KeyStore ts = KeyStore.getInstance("JKS");
                try (FileInputStream fis = new FileInputStream(TRUSTSTORE_PATH)) {
                    ts.load(fis, TRUSTSTORE_PASSWORD.toCharArray());
                }

                /* -------- Préparer le TrustManager -------- */
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                tmf.init(ts);

                /* -------- Créer le contexte SSL -------- */
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                socketFactory = context.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize SSL context", e);
            }
        }
        return socketFactory;
    }
}
