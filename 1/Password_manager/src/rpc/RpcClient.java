package rpc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.PasswordEntry;
import rpc.SSLUtil;
import util.Session;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import util.PasswordEntryMapper;
import network.NetworkPasswordEntry;

public class RpcClient {

    private static final String BASE_URL   = "https://localhost:8443";
    private static final String LOGIN_URL  = BASE_URL + "/login";
    private static final String PASS_URL   = BASE_URL + "/passwords";

    private final Gson gson = new Gson();

    public RpcClient() {
        try {
            SSLUtil.disableSSLVerification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* ---- AUTHENTIFICATION ------------------------------------------------ */

    public String login(String username, String password) throws IOException {
        System.out.println("Tentative de connexion à : " + LOGIN_URL);
        String body = "username=" + urlEncode(username) + "&password=" + urlEncode(password);
        HttpsURLConnection conn = openPost(LOGIN_URL, "application/x-www-form-urlencoded");
        writeBody(conn, body);

        if (conn.getResponseCode() == 200) {
            String token = readBody(conn);
            Session.setToken(token);
            return token;
        } else {
            throw new IOException("Login failed with code: " + conn.getResponseCode());
        }
    }

    /* ---- LECTURE DES MOTS DE PASSE --------------------------------------- */

    public List<PasswordEntry> getPasswords(String token) throws IOException {
        HttpsURLConnection conn = openGet(PASS_URL + "?token=" + urlEncode(token));
        if (conn.getResponseCode() == 200) {
            String json = readBody(conn);
            Type listType = new TypeToken<List<PasswordEntry>>() {}.getType();
            return gson.fromJson(json, listType);
        } else {
            throw new IOException("Failed to get passwords: " + conn.getResponseCode());
        }
    }

    /* ---- AJOUT / SUPPRESSION --------------------------------------------- */

    public void addPassword(String token, PasswordEntry fxEntry) throws IOException {
        NetworkPasswordEntry entryToSend = PasswordEntryMapper.toNetworkModel(fxEntry);
        String json = gson.toJson(entryToSend);

        HttpsURLConnection conn = openPost(PASS_URL + "?token=" + urlEncode(token), "application/json");
        writeBody(conn, json);

        if (conn.getResponseCode() != 200) {
            throw new IOException("Échec de l'ajout : " + conn.getResponseCode());
        }
    }


    public void deletePassword(String token, PasswordEntry entry) throws IOException {
        String url = PASS_URL + "?token=" + urlEncode(token)
                + "&site=" + urlEncode(entry.getService())
                + "&username=" + urlEncode(entry.getUsername());
        HttpsURLConnection conn = openDelete(url);
        if (conn.getResponseCode() != 200) {
            throw new IOException("Suppression échouée : " + conn.getResponseCode());
        }
    }

    /* ---- HTTPS UTILS ----------------------------------------------------- */

    private HttpsURLConnection openBase(URL url) throws IOException {
        HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
        c.setSSLSocketFactory(SSLUtil.getSocketFactory());
        c.setConnectTimeout(5000);
        c.setReadTimeout(5000);
        return c;
    }

    private HttpsURLConnection openPost(String url, String contentType) throws IOException {
        HttpsURLConnection c = openBase(new URL(url));
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setRequestProperty("Content-Type", contentType);
        return c;
    }

    private HttpsURLConnection openGet(String url) throws IOException {
        HttpsURLConnection c = openBase(new URL(url));
        c.setRequestMethod("GET");
        return c;
    }

    private HttpsURLConnection openDelete(String url) throws IOException {
        HttpsURLConnection c = openBase(new URL(url));
        c.setRequestMethod("DELETE");
        return c;
    }

    private void writeBody(HttpsURLConnection conn, String body) throws IOException {
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String readBody(HttpsURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return s; // fallback
        }
    }

    /* ---- METHODE MANQUANTE AJOUTÉE ICI ----------------------------------- */

    public String sendRequest(String action, String jsonData, String token) throws IOException {
        // Utiliser le bon endpoint REST pour ajouter un mot de passe
        URL url = new URL(PASS_URL + "?token=" + urlEncode(token));
        HttpsURLConnection conn = openPost(url.toString(), "application/json");
        writeBody(conn, jsonData);

        if (conn.getResponseCode() == 200) {
            return readBody(conn);
        } else {
            throw new IOException("Erreur réseau : " + conn.getResponseCode());
        }
    }
    public static void disableSSLCertificateChecking() {
        try {
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{
                    new javax.net.ssl.X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    }
            };

            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
