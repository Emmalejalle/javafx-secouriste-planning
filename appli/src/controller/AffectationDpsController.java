package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AffectationDpsController {

    @FXML private BorderPane rootPane;
    @FXML private Pane       headerPlaceholder;
    @FXML private javafx.scene.layout.StackPane contentPane;
    @FXML private Button     btnReturn;

    @FXML
    public void initialize() {
        try {
            // Charge le header commun
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vue/PatronHeaderAdmin.fxml")
            );
            HBox header = loader.load();
            // Récupère son controller pour lui donner un titre
            PatronHeaderAdminController hdrCtrl = loader.getController();
            hdrCtrl.setTitre("Affectation DPS");
            // Remplace la placeholder par le header réel
            rootPane.setTop(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Auto-affectation → simple popup pour l'instant */
    @FXML
    private void onAutoAffectation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Auto-affectation");
        alert.setHeaderText(null);
        alert.setContentText("Auto-affectation bien effectuée !");
        alert.showAndWait();
    }

    /** Sélection d’une DPS dans la liste à gauche */
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

    /** Bouton Retour : on revient à l’accueil */
    @FXML
    void onReturn(ActionEvent event) {
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
