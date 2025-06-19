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

    
    /**
     * Exporte les affectations pour une journée donnée dans un fichier CSV.
     * Demande à l'utilisateur de choisir un emplacement pour le fichier.
     * Si l'utilisateur clique sur "Enregistrer", le fichier est créé et les affectations sont exportées dedans.
     *
     * @param event - L'événement déclenché par le bouton "Exporter les affectations en CSV"
     */
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

    /**
     * Initialisation du contrôleur.
     * Charge le header commun et l'affecte au rootPane.
     */
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

    /**
     * Demande à l'utilisateur de choisir une méthode d'affectation automatique,
     * supprime les affectations existantes pour la journée choisie et lance l'affectation.
     * Les méthodes proposées sont :
     * - Gloutonne Optimale : affecte les DPS les plus demandés en premier
     * - Exhaustive : affecte les DPS les plus demandés en premier, mais en essayant toutes les combinaisons
     * - Gloutonne Naïve : affecte les DPS dans l'ordre de leur apparition dans la liste des DPS
     * L'utilisateur peut également annuler l'opération.
     * Affiche un message de confirmation si l'opération est réussie.
     * 
     * @param event - événement lié au bouton "Affectation Automatique"
     */
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
    
    /**
     * Supprime toutes les affectations pour la journée sélectionnée.
     * Affiche un message de confirmation avec le nombre d'affectations supprimées.
     * Affiche un message d'erreur si une erreur survient.
     *
     * @param event - événement lié au bouton "Supprimer affectations"
     */
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
    
    /**
     * Sélection d’une DPS dans la liste à gauche
     * Affiche les détails de la DPS sélectionnée dans le contentPane.
     * Si la DPS est sélectionnée, charge la vue "AffecterUnDps.fxml" dans le contentPane.
     * Si la DPS est désélectionnée, vide le contentPane.
     *
     * @param event - événement lié à la sélection de la DPS
     */
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

    /**
     * Retour à l'accueil de l'administrateur. (Bouton "Retour")
     * Charge la vue "AccueilAdmin.fxml".
     * 
     * @param event - événement lié au clic sur le bouton "Retour"
     */
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
