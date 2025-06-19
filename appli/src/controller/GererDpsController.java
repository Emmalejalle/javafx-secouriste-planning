package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modele.persistence.Competence;
import modele.persistence.DPS;
import modele.persistence.Journee;
import modele.persistence.Site;
import modele.persistence.Sport;
import modele.service.DpsManagement;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import modele.DAO.JourneeDAO;
import javafx.geometry.Insets;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la vue GererDps.fxml
 * Gère l'affichage, la création, la modification et la suppression des DPS (Dispositifs de Protection et de Sécurité).
 * Permet de lister les DPS, de filtrer par recherche, et de naviguer vers les détails d'un DPS.
 */
public class GererDpsController extends BaseController {

    // --- FXML de la vue principale ---
    @FXML private TextField rechercheField; // Renomme ce fx:id en "rechercheField" dans le FXML
    @FXML private VBox vboxListe; // Donne cet fx:id au VBox dans le ScrollPane de gauche
    @FXML private BorderPane detailsPane;
    
    // --- Logique interne ---
    private DpsManagement dpsMngt;
    private List<DPS> listeDeTousLesDps;
    private List<Sport> listeSports;
    private List<Site> listeSites;
    private List<Competence> listeCompetences;
    private HBox vignetteSelectionnee;

    private JourneeDAO journeeDAO;

    /**
     * Constructeur du contrôleur.
     * Initialise les services nécessaires pour la gestion des DPS.
     */
    public GererDpsController() {
        this.dpsMngt = new DpsManagement();
        this.journeeDAO = new JourneeDAO();
    }

    /**
     * Initialisation du contrôleur.
     * Charge les données initiales (tous les DPS, les sports, les sites, les compétences) et configure la recherche.
     * Affiche enfin le message d'accueil.
     */
    @FXML
    public void initialize() {
        chargerDonneesInitiales();
        configurerRecherche();
        afficherMessageAccueil();
    }
    
    /**
     * Charge les données initiales : tous les DPS, les sports, les sites et les compétences.
     * Affiche la liste des DPS.
     * Gère les erreurs de BDD en affichant une alerte.
     */
    private void chargerDonneesInitiales() {
        try {
            this.listeDeTousLesDps = dpsMngt.listerTousLesDps();
            this.listeSports = dpsMngt.listerTousLesSports();
            this.listeSites = dpsMngt.listerTousLesSites();
            this.listeCompetences = dpsMngt.listerToutesLesCompetences();
            afficherListe(this.listeDeTousLesDps);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de charger les données.");
            e.printStackTrace();
        }
    }
    
    /**
     * Configures the search field to filter the list of DPS (Dispositifs de Protection et de Sécurité).
     * Adds a listener to the text property of the search field that filters the DPS list 
     * based on the input value and updates the displayed list accordingly.
     */
    private void configurerRecherche() {
        rechercheField.textProperty().addListener((obs, oldV, newV) -> {
            List<DPS> dpsFiltres = dpsMngt.filtrerDps(listeDeTousLesDps, newV);
            afficherListe(dpsFiltres);
        });
    }

    /**
     * Handles the click event on a DPS vignette.
     * Highlights the selected vignette and displays the details of the clicked DPS.
     * 
     * @param event - the mouse event that triggered the click
     * @param dps - the DPS object associated with the clicked vignette
     */
    private void onDpsClicked(MouseEvent event, DPS dps) {
        if (vignetteSelectionnee != null) {
            vignetteSelectionnee.getStyleClass().remove("vignette-selected");
        }
        vignetteSelectionnee = (HBox) event.getSource();
        vignetteSelectionnee.getStyleClass().add("vignette-selected");

        // ADD THIS LINE: Call afficherDetails to load the DPS information
        afficherDetails(dps);
    }
    
    /**
     * Handles the click event on the "Ajouter DPS" button.
     * Loads the DPS creation form.
     * 
     * @param event - the action event that triggered the click
     */
    @FXML
    public void onAjouterDpsClicked(ActionEvent event) {
        afficherFormulaireCreation();
    }
    
    // --- Gestion des sous-vues ---

    /**
     * Affiche les détails d'un DPS (Dispositif de Protection et de Sécurité).
     * Charge la sous-vue FicheTechniqueDPS.fxml et passe le DPS en paramètre.
     * 
     * @param dps - l'objet DPS dont on veut afficher les détails
     */
    public void afficherDetails(DPS dps) {
        chargerSousVue("/vue/FicheTechniqueDPS.fxml", dps);
    }
    
    /**
     * Affiche le formulaire de création d'un DPS.
     * Charge la sous-vue CreerUnDPS.fxml et ne passe aucun paramètre.
     */
    public void afficherFormulaireCreation() {
        chargerSousVue("/vue/CreerUnDPS.fxml", null);
    }

    /**
     * Affiche le formulaire de modification d'un DPS.
     * Charge la sous-vue ModifierUnDPS.fxml et passe le DPS en paramètre.
     * 
     * @param dps - l'objet DPS dont on veut afficher le formulaire de modification
     */
    public void afficherFormulaireModification(DPS dps) {
        chargerSousVue("/vue/ModifierUnDPS.fxml", dps);
    }
    
    /**
     * Affiche un message d'accueil dans le panneau de droite.
     * Affiche un label indiquant à l'utilisateur de sélectionner un DPS
     * ou de cliquer sur "Ajouter" pour en créer un nouveau.
     * Si aucun DPS n'est sélectionné, on affiche ce message par défaut.
     */
    public void afficherMessageAccueil() {
        detailsPane.setCenter(new Label("Sélectionnez un DPS pour voir ses détails, ou cliquez sur 'Ajouter'."));
    }
    
    /**
     * Charge une sous-vue FXML dans le panneau de droite.
     * Charge le fichier FXML spécifié et initialise le contrôleur associé
     * avec les données nécessaires (le DPS à afficher ou modifier).
     * 
     * @param fxmlPath - le chemin du fichier FXML à charger
     * @param dps - le DPS à afficher ou modifier (peut être null si on est en création).
     */
    private void chargerSousVue(String fxmlPath, DPS dps) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent subView = loader.load();

            if (loader.getController() instanceof FicheDpsController) {
                // This branch is for FicheTechniqueDPS.fxml
                ((FicheDpsController) loader.getController()).initData(dps, this);
            } else if (loader.getController() instanceof FormulaireDpsController) {
                // This branch is for CreerUnDPS.fxml and ModifierUnDPS.fxml
                ((FormulaireDpsController) loader.getController()).initData(dps, this, listeSports, listeSites, listeCompetences);
            }
            detailsPane.setCenter(subView);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Impossible de charger la vue : " + fxmlPath);
        }
    }

    // --- Méthodes appelées par les sous-contrôleurs ---

    /**
     * Valide le formulaire pour créer ou modifier un DPS.
     * 
     * @param dpsAModifier - le DPS à modifier (null si on est en mode création)
     * @param sport - le sport lié au DPS
     * @param site - le site lié au DPS
     * @param date - la date liée au DPS
     * @param hDebut - l'heure de début du DPS
     * @param hFin - l'heure de fin du DPS
     * @param besoins - la liste des compétences et de leurs quantités associées
     */
    public void validerFormulaireDps(DPS dpsAModifier, Sport sport, Site site, LocalDate date, int hDebut, int hFin, Map<Competence, Integer> besoins) {
        try {
            // --- Validation des champs ---
            if (sport == null || site == null || date == null) {
                showAlert(Alert.AlertType.WARNING, "Champs requis", "Veuillez sélectionner un sport, un site et une date.");
                return;
            }

            // --- Logique pour la Journée (Chercher ou Créer) ---
            // On utilise la nouvelle méthode findByDate du DAO.
            Journee journee = journeeDAO.findByDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());

            // Si la journée n'existe pas dans la base de données...
            if (journee == null) {
                System.out.println("La journée n'existe pas, création en cours...");
                // ... on la crée.
                journee = new Journee(0, date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                journeeDAO.create(journee); // La méthode create met à jour l'ID de l'objet journée.
                System.out.println("Journée créée avec l'ID : " + journee.getId());
            }

            // --- Logique de Création ou Modification du DPS ---
            if (dpsAModifier == null) { // Mode Création
            dpsMngt.creerDps(hDebut, hFin, site, sport, date, besoins);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le DPS a été créé avec succès.");
        } else { // Mode Modification
            dpsMngt.modifierDps(dpsAModifier, hDebut, hFin, site, sport, date, besoins);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Le DPS a été modifié avec succès.");
        }

            // On rafraîchit toute la vue pour voir les changements
            chargerDonneesInitiales();
            afficherMessageAccueil();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'opération a échoué : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demande confirmation à l'utilisateur avant de supprimer un DPS spécifique.
     * Si l'utilisateur confirme, appelle le gestionnaire de DPS pour supprimer le DPS
     * et recharge la liste des DPS. Affiche un message d'erreur si la suppression échoue.
     * 
     * @param dps - l'objet DPS à supprimer
     */
   public void supprimerDps(DPS dps) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, 
            "Êtes-vous sûr de vouloir supprimer le DPS '" + dps.getSport().getNom() + " du " + dps.getJournee() + "' ?", 
            ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Cette action est irréversible.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    dpsMngt.supprimerDps(dps);
                    chargerDonneesInitiales(); // Rafraîchit la liste
                    afficherMessageAccueil();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de supprimer ce DPS.");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Appelée par le bouton "Retour" en haut à gauche.
     * Charge la vue accueilAdmin.fxml et la place dans la scène.
     * 
     * @param event - l'événement de clic
     */
    @FXML
    public void onRetourClicked(ActionEvent event) {
        // On suppose un retour vers l'accueil de l'administrateur
        changeView(event, "/vue/accueilAdmin.fxml");
    }

    /**
     * Affiche la liste des DPS passés en paramètre dans le panneau de gauche.
     * Chaque DPS est représenté par une "vignette" créée par {@link #creerVignetteDps(DPS)}.
     * La liste est rafraîchie à chaque appel.
     * 
     * @param dpsAAfficher - la liste des DPS à afficher
     */
    private void afficherListe(List<DPS> dpsAAfficher) {
    vboxListe.getChildren().clear();
    for (DPS dps : dpsAAfficher) {
        HBox vignette = creerVignetteDps(dps); // Use your existing method
        vignette.setOnMouseClicked(event -> onDpsClicked(event, dps));
        vignette.setUserData(dps);
        vboxListe.getChildren().add(vignette);
    }
}

// Ensure this method is correctly defined and styled in your CSS
/**
 * Crée une vignette pour un DPS.
 * La vignette contient le nom du sport, la date et l'heure de début et de fin.
 * Elle est cliquable pour afficher les détails du DPS.
 * 
 * @param dps - l'objet DPS à afficher dans la vignette
 * @return un HBox contenant les informations du DPS
 */
private HBox creerVignetteDps(DPS dps) {
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setPadding(new Insets(10));
    hbox.setSpacing(10);
    hbox.getStyleClass().add("vignette"); // Utilise le style générique

    VBox texteVBox = new VBox(-2);
    Label nomDps = new Label(dps.getSport().getNom());
    nomDps.getStyleClass().add("vignette-titre");

    // This is where you include date and time
    String detailsTexte = String.format("%s (%sh-%sh)",
            dps.getJournee().toString(), // Assuming Journee's toString() is well-formatted (e.g., "DD/MM/YYYY")
            dps.getHoraireDepart(),
            dps.getHoraireFin());
    Label detailsLabel = new Label(detailsTexte);
    detailsLabel.getStyleClass().add("vignette-details");

    texteVBox.getChildren().addAll(nomDps, detailsLabel);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    // You had a CheckBox here, but it's not present in your screenshot's list items.
    // If you don't need it, you can remove it.
    // CheckBox checkBox = new CheckBox();

    // hbox.getChildren().addAll(texteVBox, spacer, checkBox);
    hbox.getChildren().addAll(texteVBox, spacer); // Removed checkbox if not needed
    hbox.setOnMouseClicked(event -> onDpsClicked(event, dps));
    hbox.setUserData(dps);
    return hbox;
}
}