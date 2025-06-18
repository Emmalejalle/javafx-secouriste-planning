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

public class GererCompetencesController extends BaseController {

    // --- FXML de la vue principale ---
    @FXML private TextField RechercheCompTextField;
    @FXML private VBox vboxListeCompetences;
    @FXML private BorderPane detailsPane;
    
    // --- Logique interne ---
    private CompetenceManagement competenceMngt;
    private List<Competence> listeDeToutesLesCompetences; // Garde en cache la liste complète
    private HBox vignetteSelectionnee; // Pour gérer le style de la sélection

    public GererCompetencesController() {
        this.competenceMngt = new CompetenceManagement();
    }

    @FXML
    public void initialize() {
        chargerEtAfficherCompetences();
        configurerRecherche();
        afficherMessageAccueil();
    }

    // --- GESTION DE LA LISTE DE GAUCHE ---

    private void chargerEtAfficherCompetences() {
        try {
            this.listeDeToutesLesCompetences = competenceMngt.listerToutesLesCompetences();
            afficherListe(this.listeDeToutesLesCompetences);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de charger les compétences.");
            e.printStackTrace();
        }
    }

    private void afficherListe(List<Competence> competencesAAfficher) {
        vboxListeCompetences.getChildren().clear();
        for (Competence competence : competencesAAfficher) {
            vboxListeCompetences.getChildren().add(creerVignetteCompetence(competence));
        }
    }

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
    
    private void configurerRecherche() {
        RechercheCompTextField.textProperty().addListener((obs, oldV, newV) -> {
            List<Competence> competencesFiltrees = competenceMngt.filtrerCompetences(listeDeToutesLesCompetences, newV);
            afficherListe(competencesFiltrees);
        });
    }

    // --- GESTION DE L'AFFICHAGE (LE CHEF D'ORCHESTRE) ---

    private void onCompetenceClicked(MouseEvent event, Competence competence) {
        if (vignetteSelectionnee != null) {
            vignetteSelectionnee.getStyleClass().remove("vignette-competence-selected");
        }
        vignetteSelectionnee = (HBox) event.getSource();
        vignetteSelectionnee.getStyleClass().add("vignette-competence-selected");
        afficherDetails(competence);
    }

    @FXML
    public void onAjouterCompetenceClicked(ActionEvent event) {
        afficherFormulaire(null); // null signifie qu'on est en mode création
    }
    
    // Appelle le chargement de la sous-vue "Fiche"
    public void afficherDetails(Competence competence) {
        chargerSousVue("/vue/FicheCompetence.fxml", competence);
    }
    
    // Appelle le chargement de la sous-vue "Formulaire"
    public void afficherFormulaire(Competence competence) {
        chargerSousVue("/vue/FormulaireCompetence.fxml", competence);
    }
    
    private void afficherMessageAccueil() {
        detailsPane.setCenter(new Label("Sélectionnez une compétence ou cliquez sur 'Ajouter'."));
    }

    // --- MÉTHODES APPELÉES PAR LES SOUS-CONTRÔLEURS ---

    public void validerFormulaire(Competence competenceAModifier, String intitule, String abrev, List<Competence> prerequis) {
        try {
            if (intitule == null || intitule.trim().isEmpty() || abrev == null || abrev.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs vides", "L'intitulé et l'abréviation sont requis.");
                return;
            }
            Competence competenceResultat;
            if (competenceAModifier == null) { // Mode Création
                competenceMngt.creerCompetence(intitule, abrev, new ArrayList<>(prerequis));
                competenceResultat = competenceMngt.findByIntitule(intitule);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La compétence a été créée.");
            } else { // Mode Modification
                competenceMngt.modifierCompetence(competenceAModifier, intitule, abrev, new ArrayList<>(prerequis));
                competenceResultat = competenceAModifier;
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La compétence a été modifiée.");
            }
            chargerEtAfficherCompetences();
            afficherDetails(competenceResultat);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'opération a échoué : " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    @FXML
    public void onRetourClicked(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement de accueilAdmin.fxml...");
        changeView(event, "/vue/accueilAdmin.fxml");
    }
}