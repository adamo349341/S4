package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.PasswordEntry;
import rpc.RpcClient;
import rpc.Session;

public class PasswordUI {

    @FXML
    private TableView<PasswordEntry> table;

    @FXML
    private TableColumn<PasswordEntry, String> serviceColumn;

    @FXML
    private TableColumn<PasswordEntry, String> usernameColumn;

    @FXML
    private TableColumn<PasswordEntry, String> passwordColumn;

    private final ObservableList<PasswordEntry> passwordData = FXCollections.observableArrayList();
    private final RpcClient rpcClient = new RpcClient();

    @FXML
    private void initialize() {
        serviceColumn.setCellValueFactory(cellData -> cellData.getValue().serviceProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());

        table.setItems(passwordData);

        loadPasswords();
    }

    private void loadPasswords() {
        try {
            passwordData.setAll(rpcClient.getPasswords(Session.getToken()));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les mots de passe.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        PasswordEntry newEntry = new PasswordEntry();
        boolean okClicked = showEditDialog(newEntry);
        if (okClicked) {
            try {
                rpcClient.addPassword(Session.getToken(), newEntry);
                passwordData.add(newEntry);
            } catch (Exception e) {
                showAlert("Erreur", "Échec de l'ajout du mot de passe.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDelete() {
        PasswordEntry selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String username = rpcClient.getUsernameFromToken(Session.getToken());
                String serviceName = selected.getService();

                boolean success = rpcClient.deletePassword(username, serviceName);
                if (success) {
                    passwordData.remove(selected);
                    showAlert("Succès", "Entrée supprimée.");
                } else {
                    showAlert("Erreur", "Impossible de supprimer l'entrée.");
                }
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue.");
                e.printStackTrace();
            }
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une entrée.");
        }
    }

    private boolean showEditDialog(PasswordEntry entry) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_password.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Éditer le mot de passe");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            PasswordEditDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPasswordEntry(entry);

            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'afficher la boîte de dialogue.");
            return false;
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
