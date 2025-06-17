package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class PasswordEntry implements Serializable {

    private transient StringProperty service;
    private transient StringProperty username;
    private transient StringProperty password;


    private String serviceValue;
    private String usernameValue;
    private String passwordValue;

    // Constructeurs
    public PasswordEntry() {
        this("", "", "");
    }

    public PasswordEntry(String service, String username, String password) {
        this.service = new SimpleStringProperty(service);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);

        this.serviceValue = service;
        this.usernameValue = username;
        this.passwordValue = password;
    }


    public String getService() {
        return service.get();
    }

    public void setService(String service) {
        this.service.set(service);
        this.serviceValue = service;
    }

    public StringProperty serviceProperty() {
        if (service == null) {
            service = new SimpleStringProperty(serviceValue);
        }
        return service;
    }


    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
        this.usernameValue = username;
    }

    public StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty(usernameValue);
        }
        return username;
    }

    // Getter et Setter pour password
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
        this.passwordValue = password;
    }

    public StringProperty passwordProperty() {
        if (password == null) {
            password = new SimpleStringProperty(passwordValue);
        }
        return password;
    }

}
