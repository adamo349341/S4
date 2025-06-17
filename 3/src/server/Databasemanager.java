package server;

import model.PasswordEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Databasemanager {
    private static final Databasemanager instance = new Databasemanager();
    private Connection connection;

    private Databasemanager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:passwords.db");
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS passwords (" +
                        "username TEXT, " +
                        "service TEXT, " +
                        "password TEXT, " +
                        "PRIMARY KEY(username, service))");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Databasemanager getInstance() {
        return instance;
    }

    public boolean addPassword(PasswordEntry entry) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT OR REPLACE INTO passwords (username, service, password) VALUES (?, ?, ?)")) {
            ps.setString(1, entry.getUsername());
            ps.setString(2, entry.getService());
            ps.setString(3, entry.getPassword());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePassword(String username, String service) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM passwords WHERE username = ? AND service = ?")) {
            ps.setString(1, username);
            ps.setString(2, service);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PasswordEntry> loadPasswords(String username) {
        List<PasswordEntry> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT service, password FROM passwords WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PasswordEntry(username, rs.getString("service"), rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
