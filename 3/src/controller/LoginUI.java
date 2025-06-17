package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rpc.RpcClient;

public class LoginUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        RpcClient client = new RpcClient();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String token = client.login(username, password);

            if (token != null) {
                outputArea.appendText(" Connexion réussie. Token: " + token + "\n");
            } else {
                outputArea.appendText(" Échec de connexion\n");
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            boolean success = client.register(username, password);
            outputArea.appendText(success ? " Inscription réussie\n" : " Utilisateur existant\n");
        });

        VBox root = new VBox(10,
                new Label("Username"),
                usernameField,
                new Label("Password"),
                passwordField,
                loginButton,
                registerButton,
                outputArea);
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}
