package server;

/**
 * Représente une entrée de mot de passe pour un site donné.
 */
public class PasswordEntry {

    private String service;
    private String username;
    private String password;

    public PasswordEntry() {
        // Constructeur vide requis pour Gson
    }

    public PasswordEntry(String service, String username, String password) {
        this.service = service;
        this.username = username;
        this.password = password;
    }

    public String getService() {
        return service;
    }

    public String getSite() {
        return service; // Alias pour getService
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PasswordEntry{" +
                "service='" + service + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
