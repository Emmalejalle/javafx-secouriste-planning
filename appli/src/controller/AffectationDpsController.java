package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;
import javafx.scene.layout.StackPane;
import javafx.scene.control.CheckBox;


public class AffectationDpsController {

    @FXML
    private StackPane contentPane;
    @FXML private Button btnReturn;

    // appelé quand on clique sur "Auto Affectation"
    @FXML
    private void onAutoAffectation(ActionEvent event) {
        try {
            Parent autoRoot = FXMLLoader.load(
                getClass().getResource("/vue/AffectationDpsAutoAffect.fxml")
            );
            // On récupère la Scene actuelle
            Scene scene = ((Node) event.getSource()).getScene();
            // On remplace la racine par la nouvelle vue
            scene.setRoot(autoRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // appelé quand on coche une des cases à gauche
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
            // si on décochait, on peut vider ou remettre un placeholder
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
            Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
