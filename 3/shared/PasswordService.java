package shared;

import model.PasswordEntry;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PasswordService extends Remote {
    boolean register(String username, String password) throws RemoteException;
    String login(String username, String password) throws RemoteException; // retourne un token ou null
    boolean isValidToken(String token) throws RemoteException;
    void logout(String token) throws RemoteException;
    String hashPassword(String password) throws RemoteException;
    List<PasswordEntry> getPasswords(String username) throws RemoteException;
    boolean addPassword(String username, PasswordEntry entry) throws RemoteException;
    boolean deletePassword(String username, String service) throws RemoteException;
    String getUsernameFromToken(String token) throws RemoteException;
}
