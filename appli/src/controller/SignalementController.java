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
import javafx.scene.input.MouseEvent;

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

    /**
     * Gère le clic sur la zone "Planning".
     * Charge la vue "planningSecou.fxml".
     * 
     * @param event - l'événement de clic de souris
     */
    public void onReturn(MouseEvent event) {
        System.out.println("Clic sur la zone Planning. Chargement de planningSecou.fxml...");
        try {
            changerDeVue(event, "planningSecou.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue planningSecou.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Méthode générique pour changer de vue à partir d'un clic de souris.
     * 
     * @param event - événement lié au clic de souris.
     * @param fxmlFileName - nom du fichier FXML à charger.
     * @throws IOException - si le fichier FXML ne peut pas être chargé.
     */
    private void changerDeVue(MouseEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
