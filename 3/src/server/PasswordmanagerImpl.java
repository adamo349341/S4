package server;

import shared.PasswordService;
import model.PasswordEntry;
import util.HashUtil;
import util.Session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordmanagerImpl extends UnicastRemoteObject implements PasswordService {

    private final Map<String, String> userDatabase = new HashMap<>();

    public PasswordmanagerImpl() throws RemoteException {
        super();
        userDatabase.put("admin", HashUtil.hash("admin123"));
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        if (userDatabase.containsKey(username)) return false;
        userDatabase.put(username, HashUtil.hash(password));
        return true;
    }

    @Override
    public String login(String username, String password) throws RemoteException {
        String storedHash = userDatabase.get(username);
        if (storedHash != null && storedHash.equals(HashUtil.hash(password))) {
            return Session.generateToken(username);
        }
        return null;
    }

    @Override
    public boolean isValidToken(String token) throws RemoteException {
        return Session.isValid(token);
    }

    @Override
    public void logout(String token) throws RemoteException {
        Session.invalidate(token);
    }

    @Override
    public String hashPassword(String password) throws RemoteException {
        return HashUtil.hash(password);
    }

    @Override
    public List<PasswordEntry> getPasswords(String username) throws RemoteException {
        return Databasemanager.getInstance().loadPasswords(username);
    }

    @Override
    public boolean addPassword(PasswordEntry entry) throws RemoteException {
        return Databasemanager.getInstance().addPassword(entry);
    }

    @Override
    public void addPassword(String username, PasswordEntry entry) throws RemoteException {
        if (!username.equals(entry.getUsername())) {
            throw new RemoteException("Username mismatch");
        }
        Databasemanager.getInstance().addPassword(entry);
    }


    @Override
    public boolean deletePassword(String username, String service) throws RemoteException {
        return Databasemanager.getInstance().deletePassword(username, service);
    }


    @Override
    public String getUsernameFromToken(String token) throws RemoteException {
        return Session.getUsername(token);
    }
}
