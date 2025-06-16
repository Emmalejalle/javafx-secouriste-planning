package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.application.Platform;


import java.io.IOException;

/**
 * Contrôleur pour la vue de connexion.
 * Gère l'accès Secouriste, Admin et la réinitialisation du mot de passe.
 */
public class ConnexionController {

    // === Boutons du FXML ===
    @FXML private Button btnConnexionSecouriste;
    @FXML private Button btnConnexionAdmin;
    @FXML private Button btnMdpOublie;

    @FXML
    public void initialize() {
        System.out.println("ConnexionController initialisé.");
    }

    /**
     * Navigue vers l'accueil Secouriste.
     */
    @FXML
    void onConnexionSecouriste(ActionEvent event) {
        changeView(event, "/vue/accueilSecouriste.fxml");
    }

    /**
     * Navigue vers l'accueil Admin.
     */
    @FXML
    void onConnexionAdmin(ActionEvent event) {
        changeView(event, "/vue/accueilAdmin.fxml");
    }

    /**
     * Navigue vers la page "Mot de passe oublié".
     */
    @FXML
    void onMdpOublie(ActionEvent event) {
        changeView(event, "/vue/MdpOublie.fxml");
    }

    /** Quitte l'application. */
    @FXML
    void onQuitter(ActionEvent event) {
        Platform.exit();
    }


    /**
     * Méthode utilitaire pour changer de vue sans recréer la fenêtre.
     */
    private void changeView(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            System.err.println("ERREUR: impossible de charger " + fxmlPath);
            e.printStackTrace();
        }
    }
}
