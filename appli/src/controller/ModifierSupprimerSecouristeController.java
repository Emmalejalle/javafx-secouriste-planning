package controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ModifierSupprimerSecouristeController
    implements VoirLeProfilDunSecouristeController.Delegate {

    @FXML private Button btnAjouterSecouriste;
    @FXML private VBox   vboxListeProfils;    // conteneur des vignettes bleues
    @FXML private StackPane contentPane;       // zone blanche centrale

    @FXML
    public void initialize() {
        // clic sur "Ajouter un secouriste" → formulaire de création
        btnAjouterSecouriste.setOnAction(evt -> {
            chargerVue("/vue/CreationProfilSecouriste.fxml");
        });

        // clic sur chaque vignette existante → fiche détaillée
        vboxListeProfils.getChildren().forEach(node -> {
            node.setOnMouseClicked((MouseEvent me) -> {
                // on récupère prénom+nom depuis chaque HBox enfant
                String prenom = ((Label)((HBox)node).lookup("#PrenomSecouriste")).getText();
                String nom    = ((Label)((HBox)node).lookup("#NomSecouriste")).getText();

                try {
                    FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/vue/VoirLeProfilDunSecouriste.fxml")
                    );
                    Parent vue = loader.load();

                    // on transmet ce controller comme délégué
                    VoirLeProfilDunSecouristeController ctrl = loader.getController();
                    ctrl.setDelegate(this);

                    // on pré-remplit les labels
                    ctrl.lblPrenomSecouriste.setText(prenom);
                    ctrl.lblNomSecouriste   .setText(nom);

                    contentPane.getChildren().setAll(vue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /** Delegate.onModifier → charger la fiche de modification */
    @Override
    public void onModifier(String prenom, String nom) {
        chargerVue("/vue/ModifierUnSecouriste.fxml");
    }

    /** Delegate.onSupprimer → vider simplement la zone centrale */
    @Override
    public void onSupprimer(String prenom, String nom) {
        contentPane.getChildren().clear();
    }

    /** Méthode utilitaire pour charger un FXML dans contentPane */
    private void chargerVue(String fxmlPath) {
        try {
            Parent vue = FXMLLoader.load(
                getClass().getResource(fxmlPath)
            );
            contentPane.getChildren().setAll(vue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}