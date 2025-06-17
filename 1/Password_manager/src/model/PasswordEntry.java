package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PasswordEntry {

    private final StringProperty service = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();

    public PasswordEntry() {}

    public PasswordEntry(String service, String username, String password) {
        this.service.set(service);
        this.username.set(username);
        this.password.set(password);
    }

    public String getService() {
        return service.get();
    }

    public void setService(String service) {
        this.service.set(service);
    }

    public StringProperty serviceProperty() {
        return service;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }
}
