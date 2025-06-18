package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AffectationDpsController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Button btnReturn;

    // appelé quand on clique sur "Auto Affectation"
    @FXML
    private void onAutoAffectation(ActionEvent event) {
        // TODO: implémenter votre logique d'auto-affectation ici

        // Affichage d'un pop-up de confirmation
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Auto-affectation");
        alert.setHeaderText(null);
        alert.setContentText("Auto-affectation bien effectuée !");
        alert.showAndWait();
    }

    // appelé quand on coche ou décoche une des cases à gauche
    @FXML
    private void onSelectDps(ActionEvent event) {
        CheckBox cb = (CheckBox) event.getSource();
        if (cb.isSelected()) {
            try {
                Parent detailView = FXMLLoader.load(
                    getClass().getResource("/vue/AffecterUnDps.fxml")
                );
                contentPane.getChildren().setAll(detailView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            contentPane.getChildren().clear();
        }
    }

    @FXML
    void onReturn(ActionEvent event) {
        retourLecture(event);
    }

    /** Recharge simplement la vue en lecture seule */
    private void retourLecture(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/vue/accueilAdmin.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
