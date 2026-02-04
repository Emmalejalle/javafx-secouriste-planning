package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Classe de base pour les contrôleurs de l'application.
 * Fournit des méthodes utilitaires pour changer de vue et afficher des alertes.
 */
public abstract class BaseController {

    /**
     * Charge une nouvelle vue à partir d'un fichier FXML.
     * Remplace la racine de la scène actuelle par la racine de la nouvelle vue.
     * 
     * @param event - événement lié au bouton qui a déclenché le changement de vue.
     * @param fxmlPath - chemin relatif du fichier FXML à charger.
     */
    protected void changeView(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche une alerte avec le type, le titre et le message indiqués.
     * 
     * @param alertType - type d'alerte (par exemple, ERROR ou INFORMATION).
     * @param title - titre de l'alerte.
     * @param message - message de l'alerte.
     */
    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}