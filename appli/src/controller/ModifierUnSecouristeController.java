package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ModifierUnSecouristeController {

    @FXML private Button btnSupprimer;
    @FXML private Button btnValider;

    @FXML
    public void initialize() {
        // Bouton "Supprimer" : on vide le StackPane de la scène
        btnSupprimer.setOnAction(evt -> {
            StackPane contentPane = (StackPane) btnSupprimer
                .getScene()
                .lookup("#contentPane");
            if (contentPane != null) {
                contentPane.getChildren().clear();
            }
        });

        // Bouton "Valider" : on recharge la fiche de profil
        btnValider.setOnAction(evt -> {
            try {
                Parent vue = FXMLLoader.load(
                    getClass().getResource("/vue/VoirLeProfilDunSecouriste.fxml")
                );
                StackPane contentPane = (StackPane) btnValider
                    .getScene()
                    .lookup("#contentPane");
                if (contentPane != null) {
                    contentPane.getChildren().setAll(vue);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
