package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;


public class DisponibiliteController {

    // --- Variables @FXML ---
    @FXML private Label lblMonthYear;
    @FXML private GridPane calendarGrid;
    @FXML private Button btnPreviousMonth;
    @FXML private Button btnNextMonth;
    @FXML private Button btnRetour;
    @FXML private Button btnAppliquer;

    @FXML private BorderPane rootPane;
    @FXML private Pane       headerPlaceholder;


    // --- Logique interne ---
    private YearMonth moisAffiche;
    // On utilise une Map pour stocker les disponibilités.
    // La clé est la date, la valeur est un booléen (true = dispo, false = pas dispo)
    private Map<LocalDate, Boolean> disponibilites = new HashMap<>();

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/PatronHeaderSecouriste.fxml"));
            HBox header = loader.load();
            headerPlaceholder.getChildren().add(header);  // <-- AJOUT DU HEADER DANS LE PANE
            // Ajuste éventuellement les dimensions ou l'ancrage
            header.prefWidthProperty().bind(headerPlaceholder.widthProperty());
            header.prefHeightProperty().bind(headerPlaceholder.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: gestion propre de l'erreur
        }
        // Au démarrage, on affiche le mois actuel
        moisAffiche = YearMonth.now();
        // On remplit la grille du calendrier
        mettreAJourCalendrier();
    }

    private void mettreAJourCalendrier() {
        // Met à jour le label du mois (ex: "Juin 2025")
        Locale locale = Locale.FRANCE;
        String nomMois = moisAffiche.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale));
        nomMois = nomMois.substring(0, 1).toUpperCase() + nomMois.substring(1);
        lblMonthYear.setText(nomMois);

        // Efface l'ancien contenu de la grille
        calendarGrid.getChildren().clear();

        // Logique pour dessiner les jours du mois
        LocalDate premierDuMois = moisAffiche.atDay(1);
        int decalage = premierDuMois.getDayOfWeek().getValue() - 1; // 0=Lundi...

        for (int i = 0; i < 35; i++) {
            int col = i % 7;
            int row = i / 7;
            
            LocalDate dateCase = premierDuMois.plusDays(i - decalage);

            StackPane dayCell = new StackPane();
            dayCell.setOnMouseClicked(this::onDayCellClicked);
            dayCell.setUserData(dateCase); // On stocke la date dans la case !

            Label dayNumber = new Label(String.valueOf(dateCase.getDayOfMonth()));
            dayCell.getChildren().add(dayNumber);
            
            // On applique le style en fonction du mois et de la disponibilité
            if (dateCase.getMonth().equals(moisAffiche.getMonth())) {
                boolean estDispo = disponibilites.getOrDefault(dateCase, false); // Par défaut, non dispo
                dayCell.getStyleClass().add(estDispo ? "jour-disponible" : "jour-indisponible");
                dayNumber.getStyleClass().add("day-number");
            } else {
                dayNumber.getStyleClass().add("day-number-inactive");
            }

            calendarGrid.add(dayCell, col, row);
        }
    }
    
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
    }

    @FXML
    public void moisPrecedent(ActionEvent event) {
        moisAffiche = moisAffiche.minusMonths(1);
        mettreAJourCalendrier();
    }

    @FXML
    public void moisSuivant(ActionEvent event) {
        moisAffiche = moisAffiche.plusMonths(1);
        mettreAJourCalendrier();
    }
    
    @FXML
    public void appliquerChangements(ActionEvent event) {
        System.out.println("Clic sur Appliquer. Sauvegarde des disponibilités...");
        // Ici, tu mettras le code pour enregistrer la Map 'disponibilites' dans la base de données via le DAO.
        System.out.println(disponibilites);
        
        // Puis on retourne au planning
        try {
            changerDeVue(event, "planningSecou.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void retourPagePrecedente(ActionEvent event) {
        // Pour l'instant, on retourne au planning.
        // Gérer un retour "intelligent" est plus complexe et demande de passer l'information d'une vue à l'autre.
        System.out.println("Clic sur Retour. Retour vers le planning...");
        try {
            changerDeVue(event, "accueilSecouriste.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changerDeVue(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}