package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.DAO.AffectationDAO;
import modele.DAO.DpsDAO;
import modele.DAO.JourneeDAO;
import modele.SessionManager;
import modele.persistence.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlanningController {

    @FXML private Label lblMonthYear;
    @FXML private GridPane planningGrid;
    @FXML private GridPane calendarGrid;
    @FXML private Label jour1Label, jour2Label, jour3Label, jour4Label, jour5Label, jour6Label, jour7Label;
    @FXML private VBox dpsDetailsSection;
    @FXML private Label lblDpsActivite, lblDpsDate, lblDpsHoraires, lblDpsLieu;
    @FXML private VBox vboxCoequipiers;
    @FXML private Label lblTotalHours;

    private LocalDate dateAffichee;
    private Secouriste secouristeConnecte;
    private DpsDAO dpsDAO;
    private JourneeDAO journeeDAO;
    private AffectationDAO affectationDAO;
    private VBox dpsSelectionne;

    public PlanningController() {
        this.dpsDAO = new DpsDAO();
        this.journeeDAO = new JourneeDAO();
        this.affectationDAO = new AffectationDAO();
    }

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user instanceof Secouriste) {
            this.secouristeConnecte = (Secouriste) user;
            System.out.println("Page planning ouverte pour : " + secouristeConnecte.getNom());
        } else {
            System.err.println("ERREUR: Aucun secouriste connecté.");
            return;
        }
        dateAffichee = LocalDate.now();
        dpsDetailsSection.setVisible(false);
        mettreAJourAffichageComplet();
    }

    private void mettreAJourAffichageComplet() {
        mettreAJourTitresEtCalendriers();
        chargerEtAfficherDPS();
    }
    
    private void mettreAJourTitresEtCalendriers() {
        Locale locale = Locale.FRANCE;
        int weekNumber = dateAffichee.get(WeekFields.of(locale).weekOfWeekBasedYear());
        String monthName = dateAffichee.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale));
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        lblMonthYear.setText(String.format("%s - Semaine %d", monthName, weekNumber));

        LocalDate premierJourSemaine = dateAffichee.with(DayOfWeek.MONDAY);
        Label[] labelsJours = {jour1Label, jour2Label, jour3Label, jour4Label, jour5Label, jour6Label, jour7Label};
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE d", locale);
        for (int i = 0; i < 7; i++) {
            labelsJours[i].setText(premierJourSemaine.plusDays(i).format(dayFormatter));
        }

        if (calendarGrid != null) {
            calendarGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
            YearMonth anneeMoisEnCours = YearMonth.from(dateAffichee);
            LocalDate premierDuMois = dateAffichee.withDayOfMonth(1);
            int decalage = premierDuMois.getDayOfWeek().getValue() - 1;
            for (int jour = 1; jour <= anneeMoisEnCours.lengthOfMonth(); jour++) {
                Label labelJour = new Label(String.valueOf(jour));
                labelJour.setAlignment(Pos.CENTER);
                labelJour.getStyleClass().add("calendar-day-number");
                if (jour == dateAffichee.getDayOfMonth() && anneeMoisEnCours.equals(YearMonth.from(dateAffichee))) {
                    labelJour.getStyleClass().add("calendar-day-selected");
                }
                calendarGrid.add(labelJour, (jour - 1 + decalage) % 7, (jour - 1 + decalage) / 7 + 1);
            }
        }
    }

    private void chargerEtAfficherDPS() {
        // 1. On vide les anciennes vignettes du planning
        planningGrid.getChildren().removeIf(node -> node instanceof VBox);

        // 2. On définit les dates de début et de fin de la semaine actuellement affichée
        LocalDate premierJourSemaine = dateAffichee.with(DayOfWeek.MONDAY);
        LocalDate dernierJourSemaine = premierJourSemaine.plusDays(6);

        try {
            // 3. ON UTILISE TA MÉTHODE : On récupère TOUTES les affectations du secouriste connecté
            List<Affectation> mesAffectations = affectationDAO.findAffectationsForSecouriste(secouristeConnecte.getId());

            System.out.println("Trouvé " + mesAffectations.size() + " affectation(s) pour " + secouristeConnecte.getNom());

            // 4. On parcourt chaque affectation pour voir si elle doit être affichée cette semaine
            for (Affectation affect : mesAffectations) {
                DPS dps = affect.getDps();

                // On transforme la Journee du DPS en un objet LocalDate pour pouvoir les comparer
                Journee journeeDuDps = dps.getJournee();
                LocalDate dateDuDps = LocalDate.of(journeeDuDps.getAnnee(), journeeDuDps.getMois(), journeeDuDps.getJour());

                // 5. On vérifie si la date du DPS est bien dans la semaine qu'on affiche
                if (!dateDuDps.isBefore(premierJourSemaine) && !dateDuDps.isAfter(dernierJourSemaine)) {

                    // Si oui, on crée et on place la vignette
                    VBox vignette = creerVignettePourDps(dps);

                    int col = dateDuDps.getDayOfWeek().getValue(); // Lundi=1, Mardi=2...
                    int row = dps.getHoraireDepart() - 6; // Notre grille commence la ligne 1 à 7h
                    int duree = dps.getHoraireFin() - dps.getHoraireDepart();

                    planningGrid.add(vignette, col, row, 1, Math.max(1, duree));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des affectations du secouriste.");
            e.printStackTrace();
        }
    }
    
    private VBox creerVignettePourDps(DPS dps) {
        VBox vignette = new VBox(5);
        vignette.setAlignment(Pos.CENTER);
        vignette.getStyleClass().addAll("dps-card", getCouleurPourSport(dps.getSport().getNom()));
        vignette.setUserData(dps);
        vignette.setOnMouseClicked(this::onDpsClicked);
        Label titre = new Label(dps.getSport().getNom());
        titre.getStyleClass().add("dps-title");
        Label horaires = new Label(String.format("%dh-%dh", dps.getHoraireDepart(), dps.getHoraireFin()));
        horaires.getStyleClass().add("dps-time");
        vignette.getChildren().addAll(titre, horaires);
        return vignette;
    }
    
    @FXML
    public void onDpsClicked(MouseEvent event) {
        VBox vignetteCliquee = (VBox) event.getSource();
        DPS dps = (DPS) vignetteCliquee.getUserData(); // On récupère l'objet DPS qu'on a attaché à la vignette

        if (dps == null) return; // Sécurité

        // Gérer la surbrillance (ne change pas)
        if (dpsSelectionne != null) {
            dpsSelectionne.getStyleClass().removeAll("dps-selected-vert", "dps-selected-bleu", "dps-selected-jaune", "dps-selected-rouge");
        }
        vignetteCliquee.getStyleClass().add(getCouleurPourSport(dps.getSport().getNom()).replace("dps-", "dps-selected-"));
        dpsSelectionne = vignetteCliquee;

        try {
            // C'EST TA LOGIQUE : on utilise le DAO pour trouver les affectations pour ce DPS
            System.out.println("Recherche des affectations pour le DPS ID: " + dps.getId());
            List<Affectation> affectations = affectationDAO.findAffectationsForDps(dps.getId());
            
            // On passe le DPS et la liste des affectations à la méthode d'affichage
            afficherDetails(dps, affectations);

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des affectations.");
            e.printStackTrace();
        }
    }

    private void afficherDetails(DPS dps, List<Affectation> affectations) {
        // Mettre à jour les infos générales du DPS
        lblDpsActivite.setText(dps.getSport().getNom());
        lblDpsDate.setText(dps.getJournee().toString());
        lblDpsHoraires.setText(String.format("%dh - %dh", dps.getHoraireDepart(), dps.getHoraireFin()));
        lblDpsLieu.setText(dps.getSite().getNom());

        // Vider l'ancienne liste de coéquipiers
        vboxCoequipiers.getChildren().clear();

        // Boucler sur la liste des affectations pour afficher chaque secouriste
        if (affectations.isEmpty()) {
            vboxCoequipiers.getChildren().add(new Label("Aucun secouriste affecté"));
        } else {
            for (Affectation affect : affectations) {
                Secouriste secouriste = affect.getSecouriste();
                String nomComplet = secouriste.getPrenom() + " " + secouriste.getNom();

                // On crée le Label
                Label labelCoequipier = new Label(nomComplet);

                // On lui ajoute notre classe de style pour le texte blanc
                labelCoequipier.getStyleClass().add("dps-coequipier-text"); 

                // Et seulement ensuite on l'ajoute au VBox
                vboxCoequipiers.getChildren().add(labelCoequipier);
            }
        }

        // Rendre le panneau de détails visible
        dpsDetailsSection.setVisible(true);
    }
    
    @FXML
    public void onPreviousMonth(ActionEvent event) {
        dateAffichee = dateAffichee.minusWeeks(1);
        mettreAJourAffichageComplet();
    }

    @FXML
    public void onNextMonth(ActionEvent event) {
        dateAffichee = dateAffichee.plusWeeks(1);
        mettreAJourAffichageComplet();
    }
    
    // Méthodes utilitaires
    private Journee trouverJourneePourDate(List<Journee> journees, LocalDate date) {
        return journees.stream()
            .filter(j -> j.getAnnee() == date.getYear() && j.getMois() == date.getMonthValue() && j.getJour() == date.getDayOfMonth())
            .findFirst()
            .orElse(null);
    }
    
    private String getCouleurPourSport(String nomSport) {
        // Logique simple pour attribuer une couleur
        if (nomSport.toLowerCase().contains("ski")) return "dps-vert";
        if (nomSport.toLowerCase().contains("curling")) return "dps-bleu";
        return "dps-jaune";
    }

    @FXML
    public void retourAccueilAdmin(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement de accueilSecouriste.fxml...");
        try {
            // Attention, le chemin doit être absolu depuis le classpath
            changerDeVue(event, "/vue/accueilSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue accueilSecouriste.fxml");
            e.printStackTrace();
        }
    }

    private void changerDeVue(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void afficherMesDispos(ActionEvent event) {
        System.out.println("Clic sur 'Mes disponibilités'. Chargement de NotificationDisponibilite.fxml...");
        try {
            changerDeVue(event, "/vue/NotificationDisponibilite.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue NotificationDisponibilite.fxml");
            e.printStackTrace();
        }
    }
}