package server;

import shared.PasswordService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            PasswordService service = new PasswordmanagerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("PasswordService", service);
            System.out.println("RMI Server started and bound to 'PasswordService'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
