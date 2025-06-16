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
 * Contrôleur pour la vue de profil en lecture seule (profil.fxml).
 */
public class ProfilController {

    @FXML private Button btnModifierProfil;
    @FXML private Button btnRetour;

    @FXML
    public void initialize() {
        System.out.println("ProfilController initialisé (mode lecture).\n");
    }

    /**
     * Bouton Modifier bascule vers la vue de modification.
     */
    @FXML
    void onModifierProfil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/profilModif.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Bouton Retour revient à l'accueil sans sauvegarder.
     */
    @FXML
    void onRetour(ActionEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilSecouriste.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    } catch (IOException e) {
        System.err.println("ERREUR : Impossible de charger accueilSecouriste.fxml");
        e.printStackTrace();
    }
}

}
