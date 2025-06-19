package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modele.persistence.Competence;
import modele.persistence.Secouriste;
import modele.service.CompetenceManagement;
import modele.service.SecouristeManagement;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la vue GererSecouristes.fxml
 * Gère l'affichage, la recherche et la modification des secouristes.
 * Permet d'ajouter, modifier ou supprimer des secouristes et de voir leurs détails.
 */
public class GererSecouristesController extends BaseController {

    // --- FXML de la vue principale ---
    @FXML private TextField rechercheField;
    @FXML private VBox vboxListe;
    @FXML private BorderPane detailsPane;
    @FXML private Node headerPlaceholder; // Ajout d'un placeholder pour le header
    
    // --- Logique interne ---
    private SecouristeManagement secouristeMngt;
    private CompetenceManagement competenceMngt;
    private List<Secouriste> listeDeTousLesSecouristes;
    private List<Competence> listeDeToutesLesCompetences;
    private Secouriste secouristeSelectionne;
    private HBox vignetteSelectionnee;

    public GererSecouristesController() {
        this.secouristeMngt = new SecouristeManagement();
        this.competenceMngt = new CompetenceManagement();
    }
    /**
     * Méthode d'initialisation appelée par le framework JavaFX.
     * Charge les données initiales et configure la recherche.
     */
    @FXML
    public void initialize() {
        try {
            // Charge le header commun
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/PatronHeaderAdmin.fxml"));
            HBox header = loader.load();
            PatronHeaderAdminController hdrCtrl = loader.getController();
            hdrCtrl.setTitre("Gestion des Secouristes");
            
            // Remplacer le placeholder par le header
            BorderPane root = (BorderPane) headerPlaceholder.getParent();
            root.setTop(header);
            
            // Configuration existante...
            chargerDonneesInitiales();
            configurerRecherche();
            afficherMessageAccueil();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Charge les données initiales : tous les secouristes et les compétences.
     * Affiche la liste des secouristes.
     * Gère les erreurs de BDD en affichant une alerte.
     */
    private void chargerDonneesInitiales() {
        try {
            this.listeDeTousLesSecouristes = secouristeMngt.listerTousLesSecouristes();
            // On charge aussi les compétences une fois pour les passer au formulaire
            this.listeDeToutesLesCompetences = competenceMngt.listerToutesLesCompetences();
            afficherListe(this.listeDeTousLesSecouristes);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de charger les données initiales.");
            e.printStackTrace();
        }
    }


    /**
     * Vide la liste de gauche et la recharge avec les secouristes passés en paramètre.
     * Chaque secouriste est représentée par une "vignette" créée par {@link #creerVignetteSecouriste(Secouriste)}.
     *
     * @param secouristesAAfficher - la liste des secouristes à afficher
     */
    private void afficherListe(List<Secouriste> secouristesAAfficher) {
        vboxListe.getChildren().clear();
        for (Secouriste secouriste : secouristesAAfficher) {
            vboxListe.getChildren().add(creerVignetteSecouriste(secouriste));
        }
    }

    /**
     * Crée une "vignette" pour un secouriste.
     * La vignette contient le nom et le rôle du secouriste.
     * Elle est cliquable pour afficher les détails du secouriste.
     * <p>
     * La méthode utilise le style "vignette-secouriste" défini dans le fichier de style.
     * La classe "nom-secouriste-liste" est appliquée au label contenant le nom du secouriste
     * pour définir son style.
     * La classe "role-secouriste-liste" est appliquée au label contenant le rôle du secouriste
     * pour définir son style.
     *
     * @param secouriste - l'objet Secouriste à afficher dans la vignette
     * @return un HBox contenant les informations du secouriste
     */
    private HBox creerVignetteSecouriste(Secouriste secouriste) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("vignette-secouriste");
        
        VBox nomRoleBox = new VBox();
        Label nomLabel = new Label(secouriste.getPrenom() + " " + secouriste.getNom());
        nomLabel.getStyleClass().add("nom-secouriste-liste");
        Label roleLabel = new Label("Secouriste");
        roleLabel.getStyleClass().add("role-secouriste-liste");
        nomRoleBox.getChildren().addAll(nomLabel, roleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        ImageView icon = new ImageView(getClass().getResource("/img/profilAffectation.png").toExternalForm());
        icon.setFitHeight(32);
        icon.setFitWidth(32);

        hbox.getChildren().addAll(nomRoleBox, spacer, icon);
        hbox.setOnMouseClicked(event -> onSecouristeClicked(event, secouriste));
        return hbox;
    }
    
    /**
     * Configures the search field to filter the list of secouristes.
     * Adds a listener to the text property of the search field that filters the secouristes list 
     * based on the input value and updates the displayed list accordingly.
     */
    private void configurerRecherche() {
        rechercheField.textProperty().addListener((obs, oldV, newV) -> {
            List<Secouriste> secouristesFiltres = secouristeMngt.filtrerSecouristes(listeDeTousLesSecouristes, newV);
            afficherListe(secouristesFiltres);
        });
    }

    /**
     * Appelée lorsqu'un utilisateur clique sur une vignette de secouriste dans la liste.
     * Mise en surbrillance de la vignette cliquée et affichage des détails du secouriste correspondant.
     * 
     * @param event - événement lié au clic de souris
     * @param secouriste - le secouriste cliqué
     */
    private void onSecouristeClicked(MouseEvent event, Secouriste secouriste) {
        this.secouristeSelectionne = secouriste;
        if (vignetteSelectionnee != null) {
            vignetteSelectionnee.getStyleClass().remove("vignette-secouriste-selected");
        }
        vignetteSelectionnee = (HBox) event.getSource();
        vignetteSelectionnee.getStyleClass().add("vignette-secouriste-selected");
        afficherDetails(secouriste);
    }
    
    /**
     * Gère le clic sur le bouton "Ajouter" : supprime la sélection courante et affiche le formulaire de saisie d'un nouveau secouriste.
     * 
     * @param event - l'événement lié au clic de souris
     */
    @FXML
    public void onAjouter(ActionEvent event) {
        this.secouristeSelectionne = null;
        afficherFormulaire(null);
    }

    // --- GESTION DES SOUS-VUES ---

    /**
     * Affiche les détails d'un secouriste en chargeant la sous-vue appropriée.
     * 
     * @param secouriste - le secouriste dont les détails doivent être affichés
     */
    public void afficherDetails(Secouriste secouriste) {
        chargerSousVue("/vue/FicheSecouriste.fxml", secouriste);
    }
    
    /**
     * Affiche le formulaire de saisie d'un secouriste en chargeant la sous-vue appropriée.
     * 
     * @param secouriste - le secouriste que l'on souhaite modifier, ou null si on est en création
     */
    public void afficherFormulaire(Secouriste secouriste) {
        chargerSousVue("/vue/FormulaireSecouriste.fxml", secouriste);
    }

    /**
     * Affiche un message d'accueil dans le panneau de droite.
     * Affiche un label indiquant à l'utilisateur de sélectionner un secouriste
     * ou de cliquer sur "Ajouter" pour en créer un nouveau.
     * Si aucun secouriste n'est sélectionné, on affiche ce message par défaut.
     */
    public void afficherMessageAccueil() {
        detailsPane.setCenter(new Label("Sélectionnez un secouriste ou cliquez sur 'Ajouter'."));
    }
    
    /**
     * Le "moteur" qui charge une sous-vue FXML dans le panneau de droite.
     * Charge le fichier FXML spécifié et initialise le contrôleur associé
     * avec les données nécessaires (secouriste à afficher ou modifier).
     * 
     * @param fxmlPath - le chemin du fichier FXML à charger
     * @param secouriste - le secouriste à afficher ou modifier (peut être null si on est en création).
     */
    private void chargerSousVue(String fxmlPath, Secouriste secouriste) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent subView = loader.load();

            if (loader.getController() instanceof FicheSecouristeController) {
                ((FicheSecouristeController) loader.getController()).initData(secouriste, this);
            } else if (loader.getController() instanceof FormulaireSecouristeController) {
                ((FormulaireSecouristeController) loader.getController()).initData(secouriste, this.listeDeToutesLesCompetences, this);
            }
            
            detailsPane.setCenter(subView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- MÉTHODES APPELÉES PAR LES SOUS-CONTRÔLEURS ---

    /**
     * Valide et traite le formulaire de création ou de modification d'un secouriste.
     * Si le secouriste à modifier est null, un nouveau secouriste est créé.
     * Sinon, les modifications sont appliquées au secouriste existant.
     * Après l'opération, la liste des secouristes est rafraîchie et les détails du 
     * secouriste concerné sont affichés.
     * En cas d'erreur lors de la validation, une alerte est affichée.
     *
     * @param secouristeAModifier - le secouriste à modifier, ou null pour une création.
     * @param mdp - le mot de passe du secouriste.
     * @param nom - le nom du secouriste.
     * @param prenom - le prénom du secouriste.
     * @param dateNaissance - la date de naissance du secouriste.
     * @param email - l'adresse email du secouriste.
     * @param tel - le numéro de téléphone du secouriste.
     * @param adresse - l'adresse du secouriste.
     * @param competences - la liste des compétences du secouriste.
     */
    public void validerFormulaire(Secouriste secouristeAModifier, String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse, List<Competence> competences) {
        try {
            if (secouristeAModifier == null) { // Mode Création
                secouristeMngt.creerSecouriste(mdp, nom, prenom, dateNaissance, email, tel, adresse, competences);
            } else { // Mode Modification
                secouristeMngt.modifierSecouriste(secouristeAModifier, mdp, nom, prenom, dateNaissance, email, tel, adresse, competences);
            }
            chargerDonneesInitiales(); // Rafraîchit la liste et les données
            afficherDetails(secouristeAModifier != null ? secouristeAModifier : secouristeMngt.listerTousLesSecouristes().get(0)); // Affiche le bon profil
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", "L'opération a échoué : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demande confirmation à l'utilisateur avant de supprimer un secouriste.
     * Si l'utilisateur confirme, appelle le gestionnaire de secouristes pour
     * supprimer le secouriste et recharge la liste des secouristes.
     * 
     * @param secouriste - le secouriste à supprimer
     */
    public void supprimerSecouriste(Secouriste secouriste) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer " + secouriste.getPrenom() + " " + secouriste.getNom() + " ?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    secouristeMngt.supprimerSecouriste(secouriste);
                    chargerDonneesInitiales();
                    afficherMessageAccueil();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de supprimer ce secouriste.");
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Demande l'affichage du planning pour le secouriste donné.
     * Affiche un message dans la console avec le nom du secouriste.
     * Prévoit une logique de navigation vers la vue de planning,
     * mais pour l'instant cette navigation est simulée.
     *
     * @param secouriste - le secouriste dont le planning doit être affiché
     */
    public void voirPlanningPour(Secouriste secouriste) {
        System.out.println("Demande d'affichage du planning pour " + secouriste.getNom());
        // Ici, il faudrait une logique de navigation qui passe l'ID du secouriste à la vue planning.
        // Pour l'instant, on simule une navigation simple.
        // changeView(...)
    }


    /**
     * Méthode pour le bouton Retour.
     * 
     * Permet de revenir à l'accueil administrateur.
     * Affiche un message dans la console pour indiquer le retour.
     * Change la vue vers l'accueil admin.
     * @param event - l'événement lié au clic de souris
     */
    @FXML
    public void onRetour(ActionEvent event) {
        System.out.println("Clic sur Retour. Retour vers l'accueil admin...");
        changeView(event, "/vue/accueilAdmin.fxml");
    }
}