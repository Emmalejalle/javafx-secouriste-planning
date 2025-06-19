package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import modele.service.DispoMngt;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

/**
 * Contrôleur pour la vue Disponibilite.fxml
 * Gère l'affichage et la modification des disponibilités des secouristes.
 * Permet de naviguer entre les mois, de sélectionner des jours et d'appliquer les changements.
 */
public class DisponibiliteController {

    // --- Variables @FXML ---
    @FXML private Label lblMonthYear;
    @FXML private GridPane calendarGrid;
    @FXML private Button btnPreviousMonth;
    @FXML private Button btnNextMonth;
    @FXML private Button btnRetour;
    @FXML private Button btnAppliquer;
    @FXML private BorderPane rootPane;
    @FXML private Pane headerPlaceholder;

    // --- Logique interne ---
    private YearMonth moisAffiche;
    private Map<LocalDate, Boolean> disponibilites;
    private DispoMngt dispoMngt;

    /**
     * Initialisation du contrôleur.
     * Charge le header commun et l'affecte au rootPane.
     * Au démarrage, on affiche le mois actuel.
     * On remplit la grille du calendrier.
     */
    @FXML
    public void initialize() {
        try {
            // Charger le header
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/PatronHeaderSecouriste.fxml"));
            HBox header = loader.load();
            headerPlaceholder.getChildren().add(header);
            header.prefWidthProperty().bind(headerPlaceholder.widthProperty());
            header.prefHeightProperty().bind(headerPlaceholder.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Initialiser le service de gestion des disponibilités
        dispoMngt = new DispoMngt();
        
        // Au démarrage, on affiche le mois actuel
        moisAffiche = YearMonth.now();
        
        // Charger les disponibilités et mettre à jour le calendrier
        chargerDisponibilitesMoisCourant();
    }

    /**
     * Charge les disponibilités du mois courant depuis la base de données
     */
    private void chargerDisponibilitesMoisCourant() {
        try {
            disponibilites = dispoMngt.chargerDisponibilitesMois(moisAffiche.getYear(), moisAffiche.getMonthValue());
            mettreAJourCalendrier();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les disponibilités : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Met à jour l'affichage du calendrier en fonction du mois sélectionné.
     * Remplit la grille du calendrier avec les jours du mois.
     * Applique un style différent en fonction de la disponibilité du secouriste.
     */
    private void mettreAJourCalendrier() {
        // Met à jour le label du mois (ex: "Juin 2025")
        Locale locale = Locale.FRANCE;
        String nomMois = moisAffiche.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale));
        nomMois = nomMois.substring(0, 1).toUpperCase() + nomMois.substring(1);
        lblMonthYear.setText(nomMois);

        // Efface l'ancien contenu de la grille
        calendarGrid.getChildren().clear();

        // Logique pour dessiner les jours du mois - 6 LIGNES (42 cases)
        LocalDate premierDuMois = moisAffiche.atDay(1);
        int decalage = premierDuMois.getDayOfWeek().getValue() - 1; // 0=Lundi...

        for (int i = 0; i < 42; i++) { // 6 lignes × 7 jours = 42 cases
            int col = i % 7;
            int row = i / 7;
            
            LocalDate dateCase = premierDuMois.plusDays(i - decalage);

            StackPane dayCell = new StackPane();
            dayCell.setPrefHeight(60); // Limiter la hauteur des cellules
            dayCell.setOnMouseClicked(this::onDayCellClicked);
            dayCell.setUserData(dateCase); // On stocke la date dans la case !

            Label dayNumber = new Label(String.valueOf(dateCase.getDayOfMonth()));
            dayCell.getChildren().add(dayNumber);
            
            // On applique le style en fonction du mois et de la disponibilité
            if (dateCase.getMonth().equals(moisAffiche.getMonth())) {
                boolean estDispo = disponibilites.getOrDefault(dateCase, false);
                dayCell.getStyleClass().add(estDispo ? "jour-disponible" : "jour-indisponible");
                dayNumber.getStyleClass().add("day-number");
            } else {
                dayNumber.getStyleClass().add("day-number-inactive");
            }

            calendarGrid.add(dayCell, col, row);
        }
    }
    
    /**
     * Gestionnaire d'événement pour le clic sur une case du calendrier.
     * Permet de basculer la disponibilité du secouriste pour le jour correspondant.
     */
    @FXML
    public void onDayCellClicked(MouseEvent event) {
        StackPane dayCell = (StackPane) event.getSource();
        LocalDate date = (LocalDate) dayCell.getUserData();

        // On ne peut modifier que les jours du mois en cours
        if (!date.getMonth().equals(moisAffiche.getMonth())) {
            return;
        }

        // On récupère la dispo actuelle (false par défaut) et on l'inverse
        boolean estActuellementDispo = disponibilites.getOrDefault(date, false);
        disponibilites.put(date, !estActuellementDispo);

        // On met à jour le style de la case cliquée
        dayCell.getStyleClass().removeAll("jour-disponible", "jour-indisponible");
        dayCell.getStyleClass().add(!estActuellementDispo ? "jour-disponible" : "jour-indisponible");
        
        System.out.println("Disponibilité modifiée pour le " + date + " : " + !estActuellementDispo);
    }

    /**
     * Gestionnaire d'événement pour le bouton "Mois précédent".
     */
    @FXML
    public void moisPrecedent(ActionEvent event) {
        moisAffiche = moisAffiche.minusMonths(1);
        chargerDisponibilitesMoisCourant();
    }

    /**
     * Gestionnaire d'événement pour le bouton "Mois suivant".
     */
    @FXML
    public void moisSuivant(ActionEvent event) {
        moisAffiche = moisAffiche.plusMonths(1);
        chargerDisponibilitesMoisCourant();
    }
    
    /**
     * Gestionnaire d'événement pour le bouton "Appliquer".
     * Sauvegarde les disponibilités modifiées dans la base de données.
     */
    @FXML
    public void appliquerChangements(ActionEvent event) {
        try {
            dispoMngt.sauvegarderDisponibilites(disponibilites);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vos disponibilités ont été sauvegardées avec succès !");
            System.out.println("Disponibilités sauvegardées : " + disponibilites);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de sauvegarder les disponibilités : " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }
    }

    /**
     * Gestionnaire d'événement pour le bouton "Retour".
     */
    @FXML
    public void retourPagePrecedente(ActionEvent event) {
        try {
            changerDeVue(event, "accueilSecouriste.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change de vue en chargeant un nouveau fichier FXML.
     */
    private void changerDeVue(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Affiche une alerte à l'utilisateur.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}