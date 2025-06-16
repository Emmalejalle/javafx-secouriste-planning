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


/**
 * Contrôleur pour la vue de profil en modification (profilModif.fxml).
 */
public class ProfilModifController {

    @FXML private Button btnValider;
    @FXML private Button btnAnnuler;
    @FXML private Button btnRetour;

    @FXML
    public void initialize() {
        System.out.println("ProfilModifController initialisé (mode édition).\n");
    }

    /**
     * Bouton Valider sauvegarde puis revient en mode lecture.
     */
    @FXML
    void onValider(ActionEvent event) {
        // TODO: récupérer et sauvegarder les nouveautés
        retourLecture(event);
    }

    /**
     * Bouton Annuler annule les modifications puis revient en lecture.
     */
    @FXML
    void onAnnuler(ActionEvent event) {
        retourLecture(event);
    }

    /**
     * Bouton Retour revient en mode lecture sans sauvegarde.
     */
    @FXML
    void onRetour(ActionEvent event) {
        retourLecture(event);
    }

    /**
     * Méthode utilitaire pour recharger la vue profil.fxml sans recréer la fenêtre.
     */
    private void retourLecture(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/profil.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
