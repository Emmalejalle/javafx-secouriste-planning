package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import java.io.IOException;


public class SignalementController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTel;
    @FXML private ComboBox<String> comboCategorie;
    @FXML private ComboBox<String> comboSousCategorie;
    @FXML private TextArea txtSignalement;
    @FXML private Button btnEnvoyer;
    @FXML private Button btnRetour;

    @FXML
    public void initialize() {
        comboCategorie.getItems().addAll(
            "Emploi du temps",
            "Affectation",
            "Problème personnel",
            "Transport"
        );

        comboSousCategorie.getItems().addAll(
            "Lieu",
            "Equipe",
            "Demande d’info",
            "Autre"
        );

        btnEnvoyer.setOnAction(event -> verifierEtEnvoyer());
    }

    private void verifierEtEnvoyer() {
        // Vérification des champs obligatoires
        if (champVide(txtNom) || champVide(txtPrenom) || champVide(txtEmail) || champVide(txtTel)
                || comboCategorie.getValue() == null || comboSousCategorie.getValue() == null
                || champVide(txtSignalement)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs avant d'envoyer votre signalement.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Signalement envoyé");
            alert.setContentText("Votre signalement a bien été envoyé !");
            alert.showAndWait();

            // Optionnel : vider les champs après envoi
            viderChamps();
        }
    }

    private boolean champVide(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private boolean champVide(TextArea area) {
        return area.getText() == null || area.getText().trim().isEmpty();
    }

    private void viderChamps() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtTel.clear();
        comboCategorie.setValue(null);
        comboSousCategorie.setValue(null);
        txtSignalement.clear();
    }

    /** Retour à l’accueil Admin */
    /**
     * Retourne à l'écran d'accueil administrateur.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    private void onReturn(ActionEvent event) {
        System.out.println("Bouton retour cliqué !");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/planningSecou.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
