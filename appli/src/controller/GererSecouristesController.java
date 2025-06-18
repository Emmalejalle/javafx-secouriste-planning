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

public class GererSecouristesController extends BaseController {

    // --- FXML de la vue principale ---
    @FXML private TextField rechercheField;
    @FXML private VBox vboxListe;
    @FXML private BorderPane detailsPane;
    
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

    @FXML
    public void initialize() {
        chargerDonneesInitiales();
        configurerRecherche();
        afficherMessageAccueil();
    }
    
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

    private void afficherListe(List<Secouriste> secouristesAAfficher) {
        vboxListe.getChildren().clear();
        for (Secouriste secouriste : secouristesAAfficher) {
            vboxListe.getChildren().add(creerVignetteSecouriste(secouriste));
        }
    }

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
    
    private void configurerRecherche() {
        rechercheField.textProperty().addListener((obs, oldV, newV) -> {
            List<Secouriste> secouristesFiltres = secouristeMngt.filtrerSecouristes(listeDeTousLesSecouristes, newV);
            afficherListe(secouristesFiltres);
        });
    }

    private void onSecouristeClicked(MouseEvent event, Secouriste secouriste) {
        this.secouristeSelectionne = secouriste;
        if (vignetteSelectionnee != null) {
            vignetteSelectionnee.getStyleClass().remove("vignette-secouriste-selected");
        }
        vignetteSelectionnee = (HBox) event.getSource();
        vignetteSelectionnee.getStyleClass().add("vignette-secouriste-selected");
        afficherDetails(secouriste);
    }
    
    @FXML
    public void onAjouter(ActionEvent event) {
        this.secouristeSelectionne = null;
        afficherFormulaire(null);
    }

    // --- GESTION DES SOUS-VUES ---

    public void afficherDetails(Secouriste secouriste) {
        chargerSousVue("/vue/FicheSecouriste.fxml", secouriste);
    }
    
    public void afficherFormulaire(Secouriste secouriste) {
        chargerSousVue("/vue/FormulaireSecouriste.fxml", secouriste);
    }

    public void afficherMessageAccueil() {
        detailsPane.setCenter(new Label("Sélectionnez un secouriste ou cliquez sur 'Ajouter'."));
    }
    
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
    
    public void voirPlanningPour(Secouriste secouriste) {
        System.out.println("Demande d'affichage du planning pour " + secouriste.getNom());
        // Ici, il faudrait une logique de navigation qui passe l'ID du secouriste à la vue planning.
        // Pour l'instant, on simule une navigation simple.
        // changeView(...)
    }


    /**
     * Méthode pour le bouton Retour.
     */
    @FXML
    public void onRetour(ActionEvent event) {
        System.out.println("Clic sur Retour. Retour vers l'accueil admin...");
        changeView(event, "/vue/accueilAdmin.fxml");
    }
}