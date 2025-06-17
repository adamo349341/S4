package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.PasswordEntry;

public class PasswordEditDialog {

    @FXML
    private TextField serviceField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Stage dialogStage;
    private PasswordEntry passwordEntry;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPasswordEntry(PasswordEntry entry) {
        this.passwordEntry = entry;

        if (entry != null) {
            serviceField.setText(entry.getService());
            usernameField.setText(entry.getUsername());
            passwordField.setText(entry.getPassword());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            passwordEntry.setService(serviceField.getText());
            passwordEntry.setUsername(usernameField.getText());
            passwordEntry.setPassword(passwordField.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (serviceField.getText() == null || serviceField.getText().isEmpty()) {
            errorMessage += "Le champ 'Service' ne peut pas être vide.\n";
        }
        if (usernameField.getText() == null || usernameField.getText().isEmpty()) {
            errorMessage += "Le champ 'Nom d'utilisateur' ne peut pas être vide.\n";
        }
        if (passwordField.getText() == null || passwordField.getText().isEmpty()) {
            errorMessage += "Le champ 'Mot de passe' ne peut pas être vide.\n";
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez corriger les champs invalides");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
