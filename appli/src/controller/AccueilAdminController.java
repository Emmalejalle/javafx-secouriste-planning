package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.DayOfWeek;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollPane;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.geometry.Pos;
import java.time.YearMonth;

/**
 * Contrôleur pour la vue AccueilAdmin.fxml
 * Gère les interactions de l'utilisateur avec les boutons de la page d'accueil de l'administrateur.
 */
public class AccueilAdminController {

    @FXML private Button btnListeSecouristes;
    @FXML private Button btnGererSecouristes;
    @FXML private Button btnGererDPS;
    @FXML private Button btnAffecterDPS;

    /**
     * Initialisation du contrôleur AccueilAdminController.
     * Affiche un message de debug pour confirmer l'initialisation.
     */
    @FXML
    public void initialize() {
        System.out.println("Le contrôleur AccueilAdminController est initialisé.");
    }

    
    /**
     * Appelée par le bouton "Liste des secouristes".
     * Charge la vue "Modifier-supprimerUnSecouriste.fxml".
     * 
     * @param event - événement lié au clic sur le bouton.
     */
    @FXML
    public void goListeSecouriste(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers Modifier-supprimerUnSecouriste.fxml");
        try {
            changerDeVue(event, "Modifier-supprimerUnSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue Modifier-supprimerUnSecouriste.fxml");
            e.printStackTrace();
        }
    }

    
    /**
     * Appelée par le bouton "Gérer les compétences".
     * Charge la vue "Modifier-supprimerUneCompétence.fxml".
     * 
     * @param event - événement lié au clic sur le bouton.
     */
    @FXML
    public void goGererCompetences(ActionEvent event) { // <-- NOM CORRIGÉ
        System.out.println("Chargement de la vue pour Gérer les Compétences...");
        try {
            changerDeVue(event, "Modifier-supprimerUneCompétence.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue Modifier-supprimerUneCompétence.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Appelée par le bouton "Gérer les DPS".
     * Charge la vue "GererDps.fxml".
     * 
     * @param event - événement lié au clic sur le bouton.
     */
    @FXML
    public void goGererDPS(ActionEvent event) {
        System.out.println("Clic sur GererDps. Chargement de la vue pour Gérer les DPS...");
        try {
            changerDeVue(event, "GererDps.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue GererDps.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Appelée par le bouton "Affecter DPS".
     * Charge la vue "AffectationDpsDeBase.fxml".
     * 
     * @param event - événement lié au clic sur le bouton.
     */
    @FXML
    public void goAffecterDPS(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers accueilSecouriste.fxml...");
        try {
            changerDeVue(event, "AffectationDpsDeBase.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue accueilSecouriste.fxml");
            e.printStackTrace();
        }
    }


    /**
     * Patron pour changer de vue 
     * 
     * @param event - quand c'est cliqué (quand il y a un evénement)
     * @param fxmlFileName - la nouvelle vue 
     * @throws IOException - erreur si pas le bon nom
     */
    private void changerDeVue(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
