package controller;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller de la fiche détaillée (VoirLeProfilDunSecouriste.fxml).
 * Ne contient PAS contentPane ni vboxListeProfils, c’est géré par le parent.
 */
public class VoirLeProfilDunSecouristeController {

    /** Interface que devra implémenter le parent pour recevoir les événements */
    public interface Delegate {
        void onModifier(String prenom, String nom);
        void onSupprimer(String prenom, String nom);
    }

    private Delegate delegate;

    /** Appelé par le parent juste après le load pour passer le listener 
     * @param delegate l'instance qui implémente l'interface Delegate pour recevoir les événements de modification/suppression.
     */
    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @FXML public Label lblPrenomSecouriste;
    @FXML public Label lblNomSecouriste;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;

    /**
     * Initialise les boutons de modification et suppression :
     * - clic sur "Modifier" : appelle onModifier sur le delegate
     * - clic sur "Supprimer" : appelle onSupprimer sur le delegate
     */
    @FXML
    public void initialize() {
        btnModifier.setOnAction(evt -> {
            if (delegate != null) {
                delegate.onModifier(
                  lblPrenomSecouriste.getText(),
                  lblNomSecouriste.getText()
                );
            }
        });
        btnSupprimer.setOnAction(evt -> {
            if (delegate != null) {
                delegate.onSupprimer(
                  lblPrenomSecouriste.getText(),
                  lblNomSecouriste.getText()
                );
            }
        });
    }
}
