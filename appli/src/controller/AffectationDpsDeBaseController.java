package controller;

import java.io.IOException;
import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AffectationDpsDeBaseController {
    @FXML private ScrollPane ScrollPaneRechercheSecou;
    @FXML private StackPane contentPane;
    @FXML private Button btnReturn;

    @FXML
    public void initialize() {
        VBox liste = (VBox) ScrollPaneRechercheSecou.getContent();
        liste.getChildren().stream()
            .filter(node -> node instanceof CheckBox)
            .map(node -> (CheckBox) node)
            .forEach(cb -> cb.setOnAction(evt -> chargerVerifAutoAffect()));
    }

    private void chargerVerifAutoAffect() {
        try {
            Parent vue = FXMLLoader.load(
                getClass().getResource("/vue/AffectationVerifAutoAffect.fxml")
            );
            contentPane.getChildren().setAll(vue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onReturn(ActionEvent event) {
        retourLecture(event);
    }

    /** Recharge simplement la vue en lecture seule */
    private void retourLecture(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/AffectationDpsDeBase.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
