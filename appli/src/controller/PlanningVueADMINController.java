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
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Contrôleur pour la vue de planning administrateur.
 * Gère l'affichage du planning des secouristes, les détails des DPS et la navigation entre les vues.
 */
public class PlanningVueADMINController {

    @FXML private Label lblMonthYear;
    @FXML private GridPane planningGrid;
    @FXML private GridPane calendarGrid;
    @FXML private Label jour1Label, jour2Label, jour3Label, jour4Label, jour5Label, jour6Label, jour7Label;
    @FXML private VBox dpsDetailsSection;
    @FXML private Label lblDpsActivite, lblDpsDate, lblDpsHoraires, lblDpsLieu;
    @FXML private VBox vboxCoequipiers;
    @FXML private Label lblTotalHours;
    @FXML private Label planningTitleLabel;
    

    private LocalDate dateAffichee;
    private Secouriste secouristeConnecte;
    private DpsDAO dpsDAO;
    private JourneeDAO journeeDAO;
    private AffectationDAO affectationDAO;
    private VBox dpsSelectionne;

    /**
     * Constructeur par défaut.
     * Initialise les DAO nécessaires pour interagir avec la base de données.
     */
    public PlanningVueADMINController() {
        this.dpsDAO = new DpsDAO();
        this.journeeDAO = new JourneeDAO();
        this.affectationDAO = new AffectationDAO();
    }

    /**
     * Méthode appelée automatiquement par JavaFX pour initialiser la scene.
     * Vérifie si un secouriste est connecté, et si oui, met à jour le titre du planning avec son nom.
     * Met également la date actuelle comme date par défaut et cache le panneau de détails.
     * Enfin, appelle la méthode <code>mettreAJourAffichageComplet()</code> pour afficher le planning.
     */
    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user instanceof Secouriste) {
            this.secouristeConnecte = (Secouriste) user;
            System.out.println("Page planning ouverte pour : " + secouristeConnecte.getNom());
            // On met à jour le titre du planning avec le nom du bon secouriste !
            planningTitleLabel.setText("Planning de " + secouristeConnecte.getPrenom() + " " + secouristeConnecte.getNom());
        } else {
            System.err.println("ERREUR: Aucun secouriste connecté.");
            return;
        }
        dateAffichee = LocalDate.now();
        dpsDetailsSection.setVisible(false);
        mettreAJourAffichageComplet();
    }

    /**
     * Met à jour l'affichage complet du planning.
     * Appelle <code>mettreAJourTitresEtCalendriers()</code> pour mettre à jour les titres et le calendrier
     * et <code>chargerEtAfficherDPS()</code> pour charger et afficher les DPS.
     */
    private void mettreAJourAffichageComplet() {
        mettreAJourTitresEtCalendriers();
        chargerEtAfficherDPS();
    }
    
    /**
     * Met à jour les titres et le calendrier du planning.
     * Mets à jour le label du mois et du numéro de semaine.
     * Mets à jour les labels des jours de la semaine.
     * Mets à jour la grille du calendrier en fonction du mois.
     */
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

    /**
     * Charge et affiche les dispositifs de secours (DPS) du secouriste connecté pour la semaine actuellement affichée.
     * 
     * 1. Supprime les vignettes existantes du planning.
     * 2. Définit les dates de début et de fin de la semaine affichée.
     * 3. Récupère toutes les affectations du secouriste connecté depuis la base de données.
     * 4. Parcourt chaque affectation pour vérifier si elle doit être affichée cette semaine.
     * 5. Si la date du DPS correspond à la semaine affichée, crée et ajoute une vignette au planning.
     * 6. Met à jour l'affichage du total des heures de travail pour la semaine.
     */
    private void chargerEtAfficherDPS() {
        // 1. On vide les anciennes vignettes du planning
        planningGrid.getChildren().removeIf(node -> node instanceof VBox);

        // 2. On définit les dates de début et de fin de la semaine actuellement affichée
        LocalDate premierJourSemaine = dateAffichee.with(DayOfWeek.MONDAY);
        LocalDate dernierJourSemaine = premierJourSemaine.plusDays(6);

        int totalHeuresSemaine = 0;

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

                    totalHeuresSemaine += (dps.getHoraireFin() - dps.getHoraireDepart());
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
        lblTotalHours.setText(String.format("Total heures : %dh00", totalHeuresSemaine));
    }
    
    /**
     * Crée une vignette pour un DPS.
     * La vignette contient le nom du sport, la date et l'heure de début et de fin.
     * Elle est cliquable pour afficher les détails du DPS.
     * 
     * @param dps - l'objet DPS à afficher dans la vignette
     * @return un HBox contenant les informations du DPS
     */
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
    
    /**
     * Gère le clic sur une vignette de DPS.
     * Change le style de la vignette cliquée pour la surbriller.
     * Appelle la méthode {@link #afficherDetails(DPS, List)} pour afficher les détails du DPS et de son équipe.
     * 
     * @param event - l'événement de clic
     */
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

    /**
     * Affiche les détails du DPS et de son équipe.
     * Les détails affichés sont :
     * - le nom du sport
     * - la date du DPS
     * - les heures de début et de fin du DPS
     * - le lieu du DPS
     * - la liste des secouristes affectés
     * 
     * @param dps - l'objet DPS pour lequel afficher les détails
     * @param affectations - la liste des affectations pour ce DPS
     */
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
    
    /**
     * Gère le clic sur le bouton "Semaine précédente".
     * Retire une semaine à la date actuelle et met à jour l'affichage.
     * 
     * @param event - l'événement de clic, non utilisé
     */
    @FXML
    public void onPreviousMonth(ActionEvent event) {
        dateAffichee = dateAffichee.minusWeeks(1);
        mettreAJourAffichageComplet();
    }

    /**
     * Gère le clic sur le bouton "Semaine suivante".
     * Ajoute une semaine à la date actuelle et met à jour l'affichage.
     * 
     * @param event - l'événement de clic, non utilisé
     */
    @FXML
    public void onNextMonth(ActionEvent event) {
        dateAffichee = dateAffichee.plusWeeks(1);
        mettreAJourAffichageComplet();
    }
    
    // Méthodes utilitaires
    /**
     * Trouve une journée dans la liste de journees correspondant à une date donnée.
     * 
     * @param journees - la liste de journees à parcourir
     * @param date - la date à rechercher
     * @return la Journee correspondante ou null si non trouvée
     */
    private Journee trouverJourneePourDate(List<Journee> journees, LocalDate date) {
        return journees.stream()
            .filter(j -> j.getAnnee() == date.getYear() && j.getMois() == date.getMonthValue() && j.getJour() == date.getDayOfMonth())
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Retourne la classe de style CSS correspondant à la couleur du sport.
     * La logique de couleur est simple :
     * - ski : vert
     * - curling : bleu
     * - sinon : jaune
     * 
     * @param nomSport - le nom du sport pour lequel on cherche la couleur
     * @return la classe de style CSS correspondante
     */
    private String getCouleurPourSport(String nomSport) {
        // Logique simple pour attribuer une couleur
        if (nomSport.toLowerCase().contains("ski")) return "dps-vert";
        if (nomSport.toLowerCase().contains("curling")) return "dps-bleu";
        return "dps-jaune";
    }

    /**
     * Gère le clic sur le bouton "Retour" pour revenir à la page de gestion des secouristes.
     * 
     * @param event - l'événement de clic, non utilisé
     */
   @FXML
    public void retourGestionSecouristes(ActionEvent event) {
        System.out.println("Retour vers la page de gestion des secouristes...");
        try {
            // On pointe vers la bonne vue
            changerDeVue(event, "/vue/Modifier-supprimerUnSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue Modifier-supprimerUnSecouriste.fxml");
            e.printStackTrace();
        }
    }


    /**
     * Gestionnaire d'événement pour les boutons qui changent de vue.
     * Charge une nouvelle vue FXML et l'affiche dans la fenêtre actuelle.
     * 
     * @param event - L'événement lié au bouton qui a déclenché le changement de vue.
     * @param fxmlFileName - Chemin relatif du fichier FXML à charger.
     * @throws IOException - Si le fichier FXML ne peut pas être chargé.
     */
    private void changerDeVue(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gestionnaire d'événement pour le bouton "Mes disponibilités".
     * Charge la vue NotificationDisponibilite.fxml et l'affiche dans la scène.
     * 
     * @param event - l'événement de clic
     */
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


    @FXML
    private void telechargerPlanning(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le planning");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image PNG", "*.png"));
        fileChooser.setInitialFileName("planning.png");

        File file = fileChooser.showSaveDialog(planningGrid.getScene().getWindow());

        if (file != null) {
            WritableImage image = planningGrid.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(bufferedImage, "png", file);
                System.out.println("Planning exporté avec succès !");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}