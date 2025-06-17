package test;

import rpc.SSLUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Accepter tous les certificats SSL (utile pour localhost et auto-signé)
            SSLUtil.trustAllCertificates();

            URL url = new URL("https://localhost:8443/login");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(SSLUtil.getSocketFactory());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Corps de la requête
            String body = "username=admin&password=admin";
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int code = connection.getResponseCode();
            System.out.println("Réponse serveur : " + code);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
