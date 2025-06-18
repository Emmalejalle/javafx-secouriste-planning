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
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;

import modele.SessionManager;
import modele.service.AffectationMngt;

import java.io.IOException;

/**
 * Contrôleur pour la vue d'affectation des DPS (Dispositifs de Protection et de Sécurité).
 * Gère l'affichage des détails d'une DPS sélectionnée et les interactions utilisateur.
 * Permet également l'auto-affectation et le retour à l'accueil.
 *
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class AffectationDpsController {

    @FXML private BorderPane rootPane;
    @FXML private Pane       headerPlaceholder;
    @FXML private javafx.scene.layout.StackPane contentPane;
    @FXML private Button     btnReturn;
    @FXML private Button     btnAutoAffectation;
    @FXML private Button     btnClearAffectation;
    @FXML private VBox       vboxListeDps;
    @FXML private ComboBox<Integer> cbJour;
    @FXML private ComboBox<Integer> cbMois;
    @FXML private ComboBox<Integer> cbAnnee;

    private AffectationMngt affectationMngt = new AffectationMngt();

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

    @FXML
    private void onAutoAffectation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Auto-affectation");
        alert.setHeaderText(null);
        alert.setContentText("Auto-affectation bien effectuée !");
        alert.showAndWait();
        int jour = cbJour.getValue();
        int mois = cbMois.getValue();
        int annee = cbAnnee.getValue();
        try {
            // On clear d'abord les affectations existantes pour la journée
            affectationMngt.deleteAllAffectationJournee(jour, mois, annee);
            // Puis on lance l'affectation auto gloutonne
            affectationMngt.affectationAutoGloutonnePourJournee(jour, mois, annee);
            System.out.println("Affectation auto gloutonne effectuée pour " + jour + "/" + mois + "/" + annee);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'auto-affectation : " + e.getMessage());
        }
    }

    @FXML
    private void onClearAffectation(ActionEvent event) {
        
        int jour = cbJour.getValue();
        int mois = cbMois.getValue();
        int annee = cbAnnee.getValue();
        try {
            int nb = affectationMngt.deleteAllAffectationJournee(jour, mois, annee);
            System.out.println("Affectations supprimées : " + nb);
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression des affectations : " + e.getMessage());
        }
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
