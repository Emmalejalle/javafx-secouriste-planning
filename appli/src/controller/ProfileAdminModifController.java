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

public class ProfileAdminModifController {

    @FXML private Button btnValidate;
    @FXML private Button btnCancel;
    @FXML private Button btnReturn;

    @FXML
    public void initialize() {
        System.out.println("ProfileAdminModifController initialisé (mode édition).");
    }

    @FXML
    void onValidate(ActionEvent event) {
        // TODO : récupérer et sauvegarder les nouvelles valeurs
        retourLecture(event);
    }

    @FXML
    void onCancel(ActionEvent event) {
        retourLecture(event);
    }

    @FXML
    void onReturn(ActionEvent event) {
        retourLecture(event);
    }

    /** Recharge simplement la vue en lecture seule */
    private void retourLecture(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdmin.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
