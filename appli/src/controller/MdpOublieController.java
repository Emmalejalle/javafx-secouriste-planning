package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;

import java.io.IOException;

/**
 * Contrôleur pour la vue "Mot de passe oublié" (MdpOublie.fxml).
 * Gère la navigation de retour vers la page de connexion.
 */
public class MdpOublieController {

    @FXML private Hyperlink linkRetour;

    @FXML
    public void initialize() {
        System.out.println("MdpOublieController initialisé.");
    }

    /**
     * Lorsque l'utilisateur clique sur le lien "Retour à la connexion".
     */
    @FXML
    void onRetourConnexion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/Connexion.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            System.err.println("ERREUR: impossible de charger Connexion.fxml");
            e.printStackTrace();
        }
    }
}
