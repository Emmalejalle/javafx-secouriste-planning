package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;


import modele.service.AffectationMngt;

import java.io.File;
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
    @FXML private Button btnExportCSV;

    private AffectationMngt affectationMngt = new AffectationMngt();

    @FXML
    private void onExportAffectationsCSV(ActionEvent event) {
        Integer jour = cbJour.getValue();
        Integer mois = cbMois.getValue();
        Integer annee = cbAnnee.getValue();
        if (jour == null || mois == null || annee == null) {
            System.err.println("Veuillez sélectionner une date valide.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les affectations en CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fileChooser.setInitialFileName("affectations_" + jour + "_" + mois + "_" + annee + ".csv");
        File file = fileChooser.showSaveDialog(cbJour.getScene().getWindow());
        if (file != null) {
            try {
                affectationMngt.exportAffectationsJourneeToCSV(jour, mois, annee, file);
                System.out.println("Exportation réussie vers " + file.getAbsolutePath());
            } catch (Exception e) {
                System.err.println("Erreur lors de l'exportation : " + e.getMessage());
            }
        }
    }

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
        Integer jour = cbJour.getValue();
        Integer mois = cbMois.getValue();
        Integer annee = cbAnnee.getValue();
        if (jour == null || mois == null || annee == null) {
            System.err.println("Veuillez sélectionner une date valide.");
            return;
        }

        // Choix de la méthode d'affectation
        Alert choix = new Alert(Alert.AlertType.CONFIRMATION);
        choix.setTitle("Choix de la méthode d'affectation");
        choix.setHeaderText("Quelle méthode d'affectation souhaitez-vous utiliser ?");
        ButtonType btnGlouton = new ButtonType("Gloutonne Optimale");
        ButtonType btnExhaustif = new ButtonType("Exhaustive");
        ButtonType btnAleatoire = new ButtonType("Gloutonne Naïve");
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        choix.getButtonTypes().setAll(btnGlouton, btnExhaustif, btnAleatoire, btnAnnuler);

        choix.showAndWait().ifPresent(type -> {
            if (type == btnAnnuler) return;
            try {
                affectationMngt.deleteAllAffectationJournee(jour, mois, annee);
                if (type == btnGlouton) {
                    affectationMngt.affectationAutoGloutonnePourJournee(jour, mois, annee);
                    System.out.println("Affectation gloutonne effectuée !");
                } else if (type == btnExhaustif) {
                    affectationMngt.affectationAutoExhaustivePourJournee(jour, mois, annee);
                    System.out.println("Affectation exhaustive effectuée !");
                } else if (type == btnAleatoire) {
                    affectationMngt.affectationAutoNaivePourJournee(jour, mois, annee);
                    System.out.println("Affectation Gloutonne naive effectuée !");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'affectation automatique : " + e.getMessage());
            }
        });
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
