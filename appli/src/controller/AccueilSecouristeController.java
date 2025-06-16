package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Contrôleur pour la vue d'accueil du secouriste (accueilSecouriste.fxml).
 * Gère la navigation vers le planning et les disponibilités.
 */
public class AccueilSecouristeController {

    /**
     * Cette méthode est appelée par JavaFX au chargement de la vue.
     */
    @FXML
    public void initialize() {
        System.out.println("Le contrôleur AccueilSecouriste est initialisé.");
    }

    /**
     * Appelée par le onMouseClicked du VBox de gauche (Planning).
     * Navigue vers la vue du planning.
     */
    @FXML
    public void goPlanning(MouseEvent event) {
        System.out.println("Clic sur la zone Planning. Chargement de planningSecou.fxml...");
        try {
            changerDeVue(event, "planningSecou.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue planningSecou.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Appelée par le onMouseClicked du VBox de droite (Disponibilités).
     * Navigue vers la vue des disponibilités.
     */
    @FXML
    public void goDisponibilite(MouseEvent event) {
        System.out.println("Clic sur la zone Disponibilité. Chargement de NotificationDisponibilite.fxml...");
        try {
            changerDeVue(event, "NotificationDisponibilite.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue NotificationDisponibilite.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Méthode générique pour changer de vue à partir d'un clic de souris.
     */
    private void changerDeVue(MouseEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}