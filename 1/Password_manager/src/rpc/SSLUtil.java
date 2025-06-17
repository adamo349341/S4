package rpc;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;


/**
 * Initialise un SSLContext qui fait confiance au certificat du serveur
 * présent dans le truststore (JKS).
 */
public final class SSLUtil {

    private static final String TRUSTSTORE_PATH     = "keystore/clienttruststore.keystore";
    private static final String TRUSTSTORE_PASSWORD = "123456";

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
    public static void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Ignorer la vérification de l'hôte
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void disableSSLVerification() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
