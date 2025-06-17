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

public class PlanningController {

    // === Variables @FXML ===
    @FXML private Label lblMonthYear;
    @FXML private GridPane planningGrid;
    @FXML private Label jour1Label;
    @FXML private Label jour2Label;
    @FXML private Label jour3Label;
    @FXML private Label jour4Label;
    @FXML private Label jour5Label;
    @FXML private Label jour6Label;
    @FXML private Label jour7Label;
    @FXML private GridPane calendarGrid;

    @FXML private VBox dpsDetailsSection;

    @FXML private Label lblDpsActivite;

    @FXML private Label lblDpsDate;

    @FXML private Label lblDpsHoraires;

    @FXML private Label lblDpsLieu;

    @FXML private VBox vboxCoequipiers;

    @FXML private Label lblTotalHours;
    // === Variable pour gérer la date ===
    private LocalDate dateActuelle;


    private VBox dpsSelectionne; // Va mémoriser la dernière vignette cliquée
    
    @FXML
    public void initialize() {
        System.out.println("Le contrôleur PlanningController est initialisé.");
        dateActuelle = LocalDate.now();
        dpsDetailsSection.setVisible(false); // On cache le panneau de détails
        mettreAJourAffichage();
    }

    // Dans PlanningController.java

    private void mettreAJourAffichage() {
        // --- 1. Mettre à jour le titre "Mois Année - Semaine X" ---
        Locale locale = Locale.FRANCE;
        int weekNumber = dateActuelle.get(WeekFields.of(locale).weekOfWeekBasedYear());
        String monthName = dateActuelle.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale));
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        lblMonthYear.setText(String.format("%s - Semaine %d", monthName, weekNumber));

        // --- 2. Mettre à jour les en-têtes des jours (Lundi 10, Mardi 11...) ---
        LocalDate premierJourSemaine = dateActuelle.with(DayOfWeek.MONDAY);
        
        // On met les labels dans un tableau pour pouvoir boucler dessus facilement
        Label[] labelsJours = {jour1Label, jour2Label, jour3Label, jour4Label, jour5Label, jour6Label, jour7Label};
        
        // On crée un formateur pour afficher "lun. 10", "mar. 11", etc.
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE d", locale);

        for (int i = 0; i < 7; i++) {
            LocalDate jourCourant = premierJourSemaine.plusDays(i);
            labelsJours[i].setText(jourCourant.format(dayFormatter));
        }
        
        // --- 3. Effacer et recharger les vignettes DPS ---
        // (Logique à ajouter ici pour le futur)
        System.out.println("Mise à jour de l'affichage pour la semaine du Lundi " + premierJourSemaine);


        // --- 3. Mettre à jour le petit calendrier ---
        YearMonth anneeMoisEnCours = YearMonth.from(dateActuelle);



        // a) On efface les anciens numéros (on garde les en-têtes "lun", "mar"...)
        // La condition "if" est une sécurité si jamais l'fx:id n'est pas trouvé.
        if (calendarGrid != null) {
            calendarGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        }

        // b) On trouve le premier jour du mois et son jour de la semaine pour savoir où commencer
        LocalDate premierDuMois = dateActuelle.withDayOfMonth(1);
        int decalageJourSemaine = premierDuMois.getDayOfWeek().getValue() - 1; // 0 pour Lundi, 6 pour Dimanche

        // c) On boucle pour créer et placer les étiquettes de chaque jour du mois
        for (int jour = 1; jour <= anneeMoisEnCours.lengthOfMonth(); jour++) {
            Label labelJour = new Label(String.valueOf(jour));
            labelJour.setAlignment(Pos.CENTER);
            labelJour.getStyleClass().add("calendar-day-number");

            // Si le jour qu'on dessine est le jour de 'dateActuelle', on le surligne en orange
            if (anneeMoisEnCours.equals(YearMonth.from(dateActuelle)) && jour == dateActuelle.getDayOfMonth()) {
                labelJour.getStyleClass().add("calendar-day-selected");
}

            // On calcule la bonne case (colonne, ligne) dans la grille
            int col = (jour - 1 + decalageJourSemaine) % 7;
            int row = (jour - 1 + decalageJourSemaine) / 7 + 1; // +1 car la ligne 0 est pour les en-têtes

            if (calendarGrid != null) {
                calendarGrid.add(labelJour, col, row);
            }
        }
    }
    

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



    @FXML
    void afficherMesDispos(ActionEvent event) {
        System.out.println("Clic sur 'Mes disponibilités'. Chargement de NotificationDisponibilite.fxml...");
        try {
            changerDeVue(event, "NotificationDisponibilite.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue NotificationDisponibilite.fxml");
            e.printStackTrace();
        }
    }


    @FXML
    void onDpsClicked(MouseEvent event) {
        VBox vignetteCliquee = (VBox) event.getSource();

        
        // S'il y avait déjà une vignette sélectionnée...
        if (dpsSelectionne != null) {
            //  on lui enlève TOUTES les classes de sélection possibles
            dpsSelectionne.getStyleClass().removeAll("dps-selected-vert", "dps-selected-bleu", "dps-selected-jaune", "dps-selected-rouge");
        }

        // On regarde quelle est la couleur de la vignette cliquée et on ajoute la bonne surbrillance
        if (vignetteCliquee.getStyleClass().contains("dps-vert")) {
            vignetteCliquee.getStyleClass().add("dps-selected-vert");
        } else if (vignetteCliquee.getStyleClass().contains("dps-bleu")) {
            vignetteCliquee.getStyleClass().add("dps-selected-bleu");
        } else if (vignetteCliquee.getStyleClass().contains("dps-jaune")) {
            vignetteCliquee.getStyleClass().add("dps-selected-jaune");
        } else if (vignetteCliquee.getStyleClass().contains("dps-rouge")) {
            vignetteCliquee.getStyleClass().add("dps-selected-rouge");
        }

        dpsSelectionne = vignetteCliquee;
    
        // --- 2. MISE À JOUR DU PANNEAU DE DROITE ---
        String dpsId = vignetteCliquee.getId();

        // On utilise un switch pour afficher des infos différentes selon l'id de la vignette
        // Plus tard, tu remplaceras ce switch par un appel à ton DAO
        switch (dpsId) {
            case "dpsSkiAlpin":
                afficherDetails("Ski Alpin", "9h-11h", "Piste du Diable", "Jean Dupont");
                break;
            case "dpsCurling":
                afficherDetails("Curling", "12h-16h", "Patinoire Olympique", "Alice Durand, Pierre Martin");
                break;
            case "dpsPatinoire":
                afficherDetails("Patinoire", "13h-17h", "Patinoire Municipale", "Chef de poste");
                break;
            case "dpsFormation":
                afficherDetails("Formation", "16h-21h", "Gymnase de la ville", "Léa Garcia, Martin Dubois");
                break;
            case "dpsGarde":
                afficherDetails("TestRien", "20h-23h", "Base nautique", "Personne");
                break;
        }
    }
    
    /**
     * Calcule la durée totale des DPS affichés et met à jour le label.
     */
    private void mettreAJourTotalHeures() {
        // Pour l'instant, on stocke les durées ici.
        // Plus tard, ces informations viendront de tes objets DPS chargés depuis la BDD.
        Map<String, Integer> dureesDps = new HashMap<>();
        dureesDps.put("dpsSkiAlpin", 2);  // 9h -> 11h = 2h
        dureesDps.put("dpsCurling", 4);   // 12h -> 16h = 4h
        dureesDps.put("dpsPatinoire", 4); // 13h -> 17h = 4h
        dureesDps.put("dpsFormation", 5); // 16h -> 21h = 5h
        dureesDps.put("dpsGarde", 3);     // 20h -> 23h = 3h

        int totalHeures = 0;
        // On additionne les durées de tous nos DPS
        for (Integer duree : dureesDps.values()) {
            totalHeures += duree;
        }

        // On met à jour le texte du Label
        lblTotalHours.setText(String.format("Total heures : %02dh00", totalHeures));
    }
    


    private void afficherDetails(String titre, String horaires, String lieu, String equipe) {
        // On met à jour le texte des labels du panneau de droite
        lblDpsActivite.setText(titre);
        lblDpsHoraires.setText(horaires);
        lblDpsLieu.setText(lieu);
        
        // On vide et remplit la VBox des coéquipiers
        vboxCoequipiers.getChildren().clear();
        vboxCoequipiers.getChildren().add(new Label(equipe));

        // On rend le panneau visible au cas où il était caché
        dpsDetailsSection.setVisible(true);
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