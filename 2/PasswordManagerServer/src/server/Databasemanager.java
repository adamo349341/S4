package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.PasswordEntry;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour gérer la lecture et l’écriture des mots de passe dans un fichier JSON.
 */
public class Databasemanager {

    private static final String DB_FILE = "data/passwords.json";
    private static Databasemanager instance;
    private final Gson gson = new Gson();

    public Databasemanager() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(DB_FILE);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(DB_FILE)) {
                gson.toJson(new ArrayList<PasswordEntry>(), writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Databasemanager getInstance() {
        if (instance == null) {
            instance = new Databasemanager();
        }
        return instance;
    }

    public synchronized List<PasswordEntry> loadPasswords() {
        try (Reader reader = new FileReader(DB_FILE)) {
            Type listType = new TypeToken<List<PasswordEntry>>() {}.getType();
            List<PasswordEntry> entries = gson.fromJson(reader, listType);
            return entries != null ? entries : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public synchronized void addPassword(PasswordEntry entry) {
        List<PasswordEntry> entries = loadPasswords();
        entries.add(entry);
        savePasswords(entries);
    }

    public synchronized void deletePassword(String service, String username) {
        List<PasswordEntry> entries = loadPasswords();
        entries.removeIf(e -> e.getService().equals(service) && e.getUsername().equals(username));
        savePasswords(entries);
    }

    public synchronized void savePasswords(List<PasswordEntry> entries) {
        try (Writer writer = new FileWriter(DB_FILE)) {
            gson.toJson(entries, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convertit tous les mots de passe en JSON (sous forme de String).
     */
    public synchronized String toJson() {
        List<PasswordEntry> entries = loadPasswords();
        return gson.toJson(entries);
    }
}
