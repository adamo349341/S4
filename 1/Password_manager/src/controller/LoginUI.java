package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rpc.RpcClient;
import util.Session;

public class LoginUI {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final RpcClient rpcClient = new RpcClient();

    @FXML
    public void initialize() {
        RpcClient.disableSSLCertificateChecking(); // ← important
    }

    @FXML
    private void handleLogin() {


        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            String token = rpcClient.login(username, password);
            if (token != null && !token.isEmpty()) {
                Session.setToken(token);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/password.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Gestionnaire de Mots de Passe");
                stage.setScene(new Scene(loader.load()));
                stage.show();

                // Fermer la fenêtre de login
                ((Stage) usernameField.getScene().getWindow()).close();
            } else {
                showAlert("Connexion échouée", "Nom d'utilisateur ou mot de passe invalide.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur de communication avec le serveur.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
