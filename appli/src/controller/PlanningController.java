package controleur;

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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class PlanningController {

    // === Variables @FXML ===
    @FXML private Label lblMonthYear;
    @FXML private GridPane planningGrid;
    // ... et les autres fx:id que tu veux manipuler

    // === Variable pour gérer la date ===
    private LocalDate dateActuelle;

    @FXML
    public void initialize() {
        System.out.println("Le contrôleur PlanningController est initialisé.");
        dateActuelle = LocalDate.now();
        mettreAJourAffichage();
    }

    private void mettreAJourAffichage() {
        Locale locale = Locale.FRANCE;
        int weekNumber = dateActuelle.get(WeekFields.of(locale).weekOfWeekBasedYear());
        String monthName = dateActuelle.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale));
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        lblMonthYear.setText(String.format("%s - Semaine %d", monthName, weekNumber));
        System.out.println("Mise à jour de l'affichage pour la semaine du " + dateActuelle);
    }

    // =================================================================================
    // ===        GESTIONNAIRES D'ÉVÉNEMENTS (Maintenant tous présents)              ===
    // =================================================================================

    @FXML
    void onPreviousMonth(ActionEvent event) {
        System.out.println("Clic sur 'Semaine Précédente'");
        dateActuelle = dateActuelle.minusWeeks(1);
        mettreAJourAffichage();
    }

    @FXML
    void onNextMonth(ActionEvent event) {
        System.out.println("Clic sur 'Semaine Suivante'");
        dateActuelle = dateActuelle.plusWeeks(1);
        mettreAJourAffichage();
    }

    @FXML
    void retourAccueilAdmin(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement de accueilSecouriste.fxml...");
        try {
            changerDeVue(event, "accueilSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue accueilSecouriste.fxml");
            e.printStackTrace();
        }
    }
    
    /**
     * MÉTHODE AJOUTÉE : Gère le clic sur les boutons "En savoir plus" des vignettes.
     */
    @FXML
    void showDpsDetails(ActionEvent event) {
        System.out.println("Clic sur 'En savoir plus' d'une vignette DPS.");
        // Ici, on mettra la logique pour afficher les détails dans le panneau de droite
        // et appliquer la surbrillance.
    }

    // =================================================================================
    // ===          MÉTHODE UTILITAIRE (Changement de vue)                       ===
    // =================================================================================
    private void changerDeVue(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}