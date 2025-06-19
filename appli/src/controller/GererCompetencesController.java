package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modele.persistence.Competence;
import modele.service.CompetenceManagement;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la vue GererCompetences.fxml
 * Gère l'affichage et la gestion des compétences dans l'application.
 * Permet de créer, modifier, supprimer et rechercher des compétences.
 */
public class GererCompetencesController extends BaseController {

    // --- FXML de la vue principale ---
    @FXML private TextField RechercheCompTextField;
    @FXML private VBox vboxListeCompetences;
    @FXML private BorderPane detailsPane;
    @FXML private BorderPane rootPane;   
    
    // --- Logique interne ---
    private CompetenceManagement competenceMngt;
    private List<Competence> listeDeToutesLesCompetences; // Garde en cache la liste complète
    private HBox vignetteSelectionnee; // Pour gérer le style de la sélection

    /**
     * Constructeur de GererCompetencesController.
     * Initialise le gestionnaire de compétences pour accéder aux données des compétences.
     */
    public GererCompetencesController() {
        this.competenceMngt = new CompetenceManagement();
    }

    /**
     * Initialisation de la vue :
     * - charge le header avec le bon titre
     * - charge la liste des compétences
     * - configure la recherche
     * - affiche le message d'accueil
     */
    @FXML
    public void initialize() {

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/vue/PatronHeaderAdmin.fxml")
            );
            HBox header = loader.load();
            PatronHeaderAdminController hdr = loader.getController();
            hdr.setTitre("Gestion des compétences");
            rootPane.setTop(header);
        } catch (IOException e) {
            e.printStackTrace();
        }

        chargerEtAfficherCompetences();
        configurerRecherche();
        afficherMessageAccueil();
    }

    // --- GESTION DE LA LISTE DE GAUCHE ---

    /**
     * Charge la liste de toutes les compétences en BDD et l'affiche dans la liste de gauche.
     * Si une erreur de BDD survient, affiche une alerte d'erreur.
     */
    private void chargerEtAfficherCompetences() {
        try {
            this.listeDeToutesLesCompetences = competenceMngt.listerToutesLesCompetences();
            afficherListe(this.listeDeToutesLesCompetences);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de charger les compétences.");
            e.printStackTrace();
        }
    }

    /**
     * Vide la liste de gauche et la recharge avec les compétences passées en paramètre.
     * Chaque compétence est représentée par une "vignette" créée par {@link #creerVignetteCompetence(Competence)}.
     *
     * @param competencesAAfficher - la liste des compétences à afficher
     */
    private void afficherListe(List<Competence> competencesAAfficher) {
        vboxListeCompetences.getChildren().clear();
        for (Competence competence : competencesAAfficher) {
            vboxListeCompetences.getChildren().add(creerVignetteCompetence(competence));
        }
    }
    

    /**
     * Crée une "vignette" pour représenter une compétence dans la liste de gauche.
     * La vignette contient le nom de la compétence et a un style "vignette-competence"
     * qui est défini dans le fichier de style.
     * La classe "nom-competence-liste" est appliquée au label contenant le nom de la compétence
     * pour définir son style.
     * Lorsqu'on clique sur la vignette, on appelle la méthode {@link #onCompetenceClicked(MouseEvent, Competence)}
     * avec l'événement et la compétence correspondante.
     *
     * @param competence - la compétence à représenter
     * @return la vignette créée
     */
    private HBox creerVignetteCompetence(Competence competence) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("vignette-competence");
        Label nomCompetence = new Label(competence.getIntitule());
        nomCompetence.getStyleClass().add("nom-competence-liste");
        HBox.setHgrow(nomCompetence, Priority.ALWAYS);
        hbox.getChildren().add(nomCompetence);
        hbox.setOnMouseClicked(event -> onCompetenceClicked(event, competence));
        return hbox;
    }
    
    /**
     * Configure le champ de recherche pour filtrer les compétences.
     * Lorsqu'il y a un changement dans le champ de recherche, on filtre la liste des compétences
     * en fonction de la valeur tapée et on recharge la liste de gauche avec le résultat.
     */
    private void configurerRecherche() {
        RechercheCompTextField.textProperty().addListener((obs, oldV, newV) -> {
            List<Competence> competencesFiltrees = competenceMngt.filtrerCompetences(listeDeToutesLesCompetences, newV);
            afficherListe(competencesFiltrees);
        });
    }

    // --- GESTION DE L'AFFICHAGE (LE CHEF D'ORCHESTRE) ---

    
        
    /**
     * Gère le clic sur une vignette de compétence dans la liste de gauche.
     * Change le style de la vignette cliquée pour la surbriller.
     * Appelle la méthode {@link #afficherDetails(Competence)} pour afficher les détails de la compétence.
     * 
     * @param event - l'événement de clic
     * @param competence - la compétence correspondant à la vignette cliquée
     */
    private void onCompetenceClicked(MouseEvent event, Competence competence) {
        if (vignetteSelectionnee != null) {
            vignetteSelectionnee.getStyleClass().remove("vignette-competence-selected");
        }
        vignetteSelectionnee = (HBox) event.getSource();
        vignetteSelectionnee.getStyleClass().add("vignette-competence-selected");
        afficherDetails(competence);
    }

    /**
     * Gère le clic sur le bouton "Ajouter" en haut à gauche.
     * Appelle la méthode {@link #afficherFormulaire(Competence)} pour afficher le formulaire de création.
     * 
     * @param event - l'événement de clic
     */
    @FXML
    public void onAjouterCompetenceClicked(ActionEvent event) {
        afficherFormulaire(null); // null signifie qu'on est en mode création
    }
    
    /** Appelle le chargement de la sous-vue "Fiche"
     * Affiche les détails d'une compétence dans le panneau de droite.
     * Charge la sous-vue FicheCompetence.fxml et lui passe la compétence à afficher.
     * Si aucune compétence n'est sélectionnée, affiche un message d'accueil.
     * 
     * @param competence - la compétence à afficher
     */
    public void afficherDetails(Competence competence) {
        chargerSousVue("/vue/FicheCompetence.fxml", competence);
    }
    
    // Appelle le chargement de la sous-vue "Formulaire"
    /**
     * Affiche le formulaire de création ou de modification d'une compétence.
     * Charge la sous-vue FormulaireCompetence.fxml et lui passe la compétence à modifier
     * (ou null si on est en mode création).
     * 
     * @param competence - la compétence à modifier (null pour créer une nouvelle compétence)
     */
    public void afficherFormulaire(Competence competence) {
        chargerSousVue("/vue/FormulaireCompetence.fxml", competence);
    }
    

    /** Affiche un message d'accueil dans le panneau de droite.
     * Affiche un label indiquant à l'utilisateur de sélectionner une compétence
     * ou de cliquer sur "Ajouter" pour créer une nouvelle compétence.
     * Si aucune compétence n'est sélectionnée, on affiche ce message par défaut.
     */
    private void afficherMessageAccueil() {
        detailsPane.setCenter(new Label("Sélectionnez une compétence ou cliquez sur 'Ajouter'."));
    }

    // --- MÉTHODES APPELÉES PAR LES SOUS-CONTRÔLEURS ---

    /**
     * Appelée par le contrôleur du formulaire de compétence pour valider les données.
     * 
     * @param competenceAModifier - la compétence à modifier (ou null si on est en mode création)
     * @param intitule - l'intitulé de la compétence
     * @param abrev - l'abréviation de la compétence
     * @param prerequis - les compétences prérequis
     */
    public void validerFormulaire(Competence competenceAModifier, String intitule, String abrev, List<Competence> prerequis) {
        try {
            if (intitule == null || intitule.trim().isEmpty() || abrev == null || abrev.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs vides", "L'intitulé et l'abréviation sont requis.");
                return;
            }

            boolean operationReussie = false;
            if (competenceAModifier == null) { // Mode Création
                operationReussie = competenceMngt.creerCompetence(intitule, abrev, new ArrayList<>(prerequis));
                if (operationReussie) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "La compétence '" + intitule + "' a été créée.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de logique", "Impossible de créer la compétence. Un cycle de dépendances serait créé (ex: A a besoin de B et B a besoin de A).");
                }
            } else { // Mode Modification
                operationReussie = competenceMngt.modifierCompetence(competenceAModifier, intitule, abrev, new ArrayList<>(prerequis));
                if(operationReussie) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "La compétence '" + intitule + "' a été modifiée.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de logique", "Impossible de modifier la compétence. Un cycle de dépendances serait créé.");
                }
            }

            // Si l'opération a réussi, on rafraîchit la vue
            if (operationReussie) {
                chargerEtAfficherCompetences();
                // On recherche la compétence pour la réafficher
                Competence competenceAffichee = competenceMngt.findByIntitule(intitule);
                if(competenceAffichee != null){
                    afficherDetails(competenceAffichee);
                } else {
                    afficherMessageAccueil();
                }
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'opération a échoué : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Appelée par le contrôleur du formulaire de modification de compétence
     * pour demander la suppression d'une compétence.
     * Demande confirmation à l'utilisateur avant de supprimer la compétence.
     * Si l'utilisateur confirme, appelle le gestionnaire de compétences pour
     * supprimer la compétence et recharge la liste des compétences.
     * 
     * @param competence - la compétence à supprimer
     */
    public void supprimerCompetence(Competence competence) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer la compétence '" + competence.getIntitule() + "' ?", ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    competenceMngt.supprimerCompetence(competence);
                    chargerEtAfficherCompetences();
                    afficherMessageAccueil();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de supprimer la compétence.");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Le "moteur" qui charge une sous-vue FXML dans le panneau de droite.
     * Charge le fichier FXML spécifié et initialise le contrôleur associé
     * avec les données nécessaires (compétence à afficher ou modifier).
     * 
     * @param fxmlPath - le chemin du fichier FXML à charger
     * @param competence - la compétence à afficher ou modifier (peut être null si on est en création).
     */
    private void chargerSousVue(String fxmlPath, Competence competence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent subView = loader.load();

            // On regarde quel type de mini-contrôleur a été chargé
            if (loader.getController() instanceof FicheCompetenceController) {
                // Si c'est la fiche, on lui passe la compétence à afficher et une référence vers nous-mêmes
                ((FicheCompetenceController) loader.getController()).initData(competence, this);
            } else if (loader.getController() instanceof FormulaireCompetenceController) {
                // Si c'est le formulaire, on lui passe la compétence, la liste complète et une référence vers nous-mêmes
                ((FormulaireCompetenceController) loader.getController()).initData(competence, this.listeDeToutesLesCompetences, this);
            }
            
            detailsPane.setCenter(subView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appelée par le bouton "Retour" en haut à gauche.
     * Charge la vue accueilAdmin.fxml et la place dans la scène.
     * 
     * @param event - l'événement de clic
     */
    @FXML
    public void onRetourClicked(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement de accueilAdmin.fxml...");
        changeView(event, "/vue/accueilAdmin.fxml");
    }
}