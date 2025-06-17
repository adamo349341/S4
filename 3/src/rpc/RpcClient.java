package rpc;

import shared.PasswordService;
import model.PasswordEntry;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RpcClient {
    private PasswordService service;

    public RpcClient() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (PasswordService) registry.lookup("PasswordService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean register(String username, String password) {
        try {
            return service.register(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String login(String username, String password) {
        try {
            return service.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PasswordEntry> getPasswords(String username) {
        try {
            return service.getPasswords(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addPassword(String username, PasswordEntry entry) {
        try {
            service.addPassword(username, entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deletePassword(String username, String serviceName) {
        try {
            return service.deletePassword(username, serviceName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getUsernameFromToken(String token) {
        try {
            return service.getUsernameFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
