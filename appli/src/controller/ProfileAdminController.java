package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileAdminController {

    @FXML private Button btnModifyProfile;
    @FXML private Button btnReturn;

    @FXML
    public void initialize() {
        System.out.println("ProfileAdminController initialisé (mode lecture).");
    }

    @FXML
    void onModifyProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdminModif.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Bouton Retour revient à l'accueil sans sauvegarder.
     */
    @FXML
    void onReturn(ActionEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    } catch (IOException e) {
        System.err.println("ERREUR : Impossible de charger accueilSecouriste.fxml");
        e.printStackTrace();
    }
}
}
