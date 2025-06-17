package server;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du service de gestion de mots de passe.
 */
public class PasswordServiceImpl implements PasswordService {

    private final List<PasswordEntry> passwordList;
    private final Databasemanager dbManager;

    public PasswordServiceImpl() {
        this.dbManager = new Databasemanager();
        this.passwordList = dbManager.loadPasswords();
    }

    @Override
    public synchronized String getAllPasswordsAsJson() {
        return dbManager.toJson();
    }

    @Override
    public synchronized void addPassword(String site, String username, String password) {
        PasswordEntry entry = new PasswordEntry(site, username, password);
        passwordList.add(entry);
        dbManager.savePasswords(passwordList);
    }

    @Override
    public synchronized void deletePassword(String site, String username) {
        passwordList.removeIf(e ->
                e.getSite().equals(site) && e.getUsername().equals(username));
        dbManager.savePasswords(passwordList);
    }
}
