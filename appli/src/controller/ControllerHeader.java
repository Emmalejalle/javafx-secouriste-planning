package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerHeader {

    @FXML private Button BoutonHomeTopSecouriste;
    @FXML private Button BoutonPlanningTopSecouriste;
    @FXML private Button BoutonDispoTopSecouriste;
    @FXML private Button BoutonProfiTopSecouriste;
    @FXML private Button BoutonQuitterTopSecouriste;
    @FXML private Label  LabelNomDePageSecouriste;

    @FXML
    private void initialize() {
        // On bind chaque bouton à sa méthode de navigation
        BoutonHomeTopSecouriste.setOnMouseClicked(e -> goTo("accueilSecouriste.fxml", "Accueil", e));
        BoutonPlanningTopSecouriste.setOnMouseClicked(e -> goTo("planningSecou.fxml",       "Planning", e));
        BoutonDispoTopSecouriste.setOnMouseClicked(e -> goTo("NotificationDisponibilite.fxml", "Disponibilités", e));
        BoutonProfiTopSecouriste.setOnMouseClicked(e -> goTo("profil.fxml",                  "Profil", e));
        BoutonQuitterTopSecouriste.setOnMouseClicked(e -> quitterAppli(e));
    }

    /** Setter pour modifier dynamiquement le titre depuis le contrôleur parent */
    public void setTitre(String titre) {
        LabelNomDePageSecouriste.setText(titre);
    }

    /**
     * Charge la vue demandée et met à jour le titre.
     */
    private void goTo(String fxmlFile, String titre, MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

            // Si vous voulez que le header affiche le titre de page actif :
            LabelNomDePageSecouriste.setText(titre);
        } catch (IOException ex) {
            System.err.println("ERREUR: impossible de charger " + fxmlFile);
            ex.printStackTrace();
        }
    }
    

    /** Ferme simplement l’application. */
    private void quitterAppli(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
