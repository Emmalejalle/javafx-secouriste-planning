package controller;


import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label; 
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modele.persistence.Competence;
import modele.service.CompetenceManagement; // On importe notre service !

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GererCompetencesController extends BaseController {

    @FXML private TextField RechercheCompTextField;
    @FXML private VBox vboxListeCompetences;
    @FXML private Button AjouterComp;
    @FXML private Button btnRetour;
    @FXML private BorderPane detailsPane;
    private HBox vignetteSelectionnee;
    
    private CompetenceManagement competenceMngt;
    private List<Competence> listeDeToutesLesCompetences;
    private Competence competenceSelectionnee;

    public GererCompetencesController() {
        this.competenceMngt = new CompetenceManagement();
    }

    @FXML
    public void initialize() {
        vboxListeCompetences.getStyleClass().add("vbox-liste-competences");
        chargerEtAfficherCompetences();
        configurerRecherche();
        afficherMessageAccueil();
    }

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
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("blocBleuSecouriste");
        
        Label nomCompetence = new Label(competence.getIntitule());
        nomCompetence.getStyleClass().add("nomPrenomSecouriste");
        HBox.setHgrow(nomCompetence, Priority.ALWAYS);

        hbox.getChildren().add(nomCompetence);
        hbox.setOnMouseClicked(event -> onCompetenceClicked(event, competence));
        
        return hbox;
    }

    private void onCompetenceClicked(MouseEvent event, Competence competence) {
        // On enlève le style de l'ancienne vignette sélectionnée
        if (vignetteSelectionnee != null) {
            vignetteSelectionnee.getStyleClass().remove("vignette-competence-selected");
        }

        // On récupère le HBox sur lequel on a cliqué et on applique le nouveau style
        HBox vignetteCliquee = (HBox) event.getSource();
        vignetteCliquee.getStyleClass().add("vignette-competence-selected");

        // On mémorise la nouvelle sélection
        this.vignetteSelectionnee = vignetteCliquee;
        this.competenceSelectionnee = competence;

        afficherDetailsCompetence(competence);
    }
    
    private void configurerRecherche() {
        RechercheCompTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Competence> competencesFiltrees = competenceMngt.filtrerCompetences(listeDeToutesLesCompetences, newValue);
            afficherListe(competencesFiltrees);
        });
    }
    
    @FXML
    void onAjouterCompetenceClicked(ActionEvent event) {
        afficherFormulaire(null); // null signifie qu'on crée une nouvelle compétence
    }

    
    
    
    

    private void afficherMessageAccueil() {
        detailsPane.setTop(null);
        detailsPane.setCenter(null);
        detailsPane.setBottom(null);
        detailsPane.setLeft(null);
        detailsPane.setRight(null);

        Label msg = new Label("Sélectionnez une compétence pour voir ses détails, ou cliquez sur 'Ajouter une compétence'.");
        msg.setStyle("-fx-font-size: 18px; -fx-opacity: 0.6;");

        // On place le message dans la zone centrale du BorderPane
        detailsPane.setCenter(msg);

        // Et on dit au BorderPane d'aligner cet enfant (msg) au centre de sa zone
        BorderPane.setAlignment(msg, Pos.CENTER);
    }


    @FXML
    public void onRetourClicked(ActionEvent event) {
        // Navigue vers la page d'accueil de l'admin
        System.out.println("Clic sur Retour. Chargement de accueilAdmin.fxml...");
        changeView(event, "/vue/accueilAdmin.fxml");
    }


    protected void changeView(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            System.err.println("ERREUR: impossible de charger " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
 * Construit et affiche le panneau de droite pour VOIR les détails d'une compétence.
 * @param competence La compétence sélectionnée.
 */
private void afficherDetailsCompetence(Competence competence) {
    // On utilise un BorderPane pour la mise en page : haut, centre, bas
    BorderPane detailsLayout = new BorderPane();
    detailsPane.setCenter(detailsLayout);
    detailsLayout.getStyleClass().add("details-pane"); // Applique le fond blanc et la bordure

    // --- PARTIE HAUT : Titre et boutons d'action ---
    Label titre = new Label("Fiche de la compétence");
    titre.getStyleClass().add("details-titre");

    Region spacer = new Region(); // Espaceur flexible pour pousser les boutons à droite
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button btnModifier = new Button("Modifier");
    btnModifier.getStyleClass().add("details-button-modifier");
    btnModifier.setOnAction(event -> afficherFormulaire(competence));

    Button btnSupprimer = new Button("Supprimer la compétence");
    btnSupprimer.getStyleClass().add("details-button-supprimer");
    btnSupprimer.setOnAction(event -> supprimerCompetence(competence));

    HBox topBar = new HBox(15, titre, spacer, btnModifier, btnSupprimer);
    topBar.setAlignment(Pos.CENTER_LEFT);
    topBar.setPadding(new Insets(5, 15, 15, 15));

    detailsLayout.setTop(topBar);

    // --- PARTIE CENTRE : Informations ---
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(20); // Plus d'espace vertical
    grid.setPadding(new Insets(15));

    Label intituleTitre = new Label("Intitulé");
    intituleTitre.getStyleClass().add("details-label");
    Label intituleValeur = new Label(competence.getIntitule());
    intituleValeur.getStyleClass().add("details-text");

    Label abrevTitre = new Label("Abréviation");
    abrevTitre.getStyleClass().add("details-label");
    Label abrevValeur = new Label(competence.getAbrevComp());
    abrevValeur.getStyleClass().add("details-text");

    Label prerequisTitre = new Label("Compétences requises");
    prerequisTitre.getStyleClass().add("details-label");
    VBox prerequisBox = new VBox(5);
    if (competence.getPrerequis().isEmpty()) {
        Label aucunPrerequis = new Label("Aucun");
        aucunPrerequis.getStyleClass().add("details-prerequis-item");
        prerequisBox.getChildren().add(aucunPrerequis);
    } else {
        for (Competence prerequis : competence.getPrerequis()) {
            Label labelPrerequis = new Label("• " + prerequis.getIntitule());
            labelPrerequis.getStyleClass().add("details-prerequis-item");
            prerequisBox.getChildren().add(labelPrerequis);
        }
    }

    grid.add(intituleTitre, 0, 0);
    grid.add(intituleValeur, 1, 0);
    grid.add(abrevTitre, 0, 1);
    grid.add(abrevValeur, 1, 1);
    grid.add(prerequisTitre, 0, 2);
    grid.add(prerequisBox, 1, 2);
    GridPane.setValignment(prerequisTitre, VPos.TOP); // Aligne le label "Compétences requises" en haut

    detailsLayout.setCenter(grid);
}

/**
 * Construit et affiche le panneau de droite pour AJOUTER ou MODIFIER une compétence.
 * @param competence La compétence à modifier, ou null si on est en mode création.
 */
private void afficherFormulaire(Competence competence) {
    boolean estEnModeCreation = (competence == null);

    BorderPane formLayout = new BorderPane();
    detailsPane.setCenter(formLayout);
    formLayout.getStyleClass().add("details-pane");

    // --- PARTIE HAUT : Titre ---
    Label titreForm = new Label(estEnModeCreation ? "Ajouter une compétence" : "Modifier la compétence");
    titreForm.getStyleClass().add("details-titre");
    HBox topBar = new HBox(titreForm);
    topBar.setPadding(new Insets(5, 15, 15, 15));
    formLayout.setTop(topBar);

    // --- PARTIE CENTRE : Champs de saisie ---
    VBox formContainer = new VBox(10);
    formContainer.setPadding(new Insets(15));

    Label intituleLabel = new Label("Intitulé");
    intituleLabel.getStyleClass().add("form-label");
    TextField intituleField = new TextField(estEnModeCreation ? "" : competence.getIntitule());

    Label abrevLabel = new Label("Abréviation");
    abrevLabel.getStyleClass().add("form-label");
    TextField abrevField = new TextField(estEnModeCreation ? "" : competence.getAbrevComp());

    Label prerequisLabel = new Label("Compétences requises (maintenir Ctrl pour en sélectionner plusieurs)");
    prerequisLabel.getStyleClass().add("form-label");
    ListView<Competence> prerequisListView = new ListView<>();
    prerequisListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    prerequisListView.getItems().addAll(this.listeDeToutesLesCompetences);

    if (!estEnModeCreation) {
        prerequisListView.getItems().remove(competence);
        for (Competence prerequis : competence.getPrerequis()) {
            prerequisListView.getSelectionModel().select(prerequis);
        }
    }

    formContainer.getChildren().addAll(intituleLabel, intituleField, abrevLabel, abrevField, prerequisLabel, prerequisListView);
    formLayout.setCenter(formContainer);

    // --- PARTIE BAS : Bouton Valider ---
    Button btnValider = new Button("Valider");
    btnValider.getStyleClass().add("action-button-primary"); // Style bleu et imposant
    btnValider.setOnAction(event -> validerFormulaire(competence, intituleField.getText(), abrevField.getText(), prerequisListView.getSelectionModel().getSelectedItems()));


    HBox boutonsBox = new HBox(btnValider);
    boutonsBox.setAlignment(Pos.CENTER_RIGHT);
    boutonsBox.setPadding(new Insets(20));
    formLayout.setBottom(boutonsBox);
}


private void validerFormulaire(Competence competenceAModifier, String intitule, String abrev, List<Competence> prerequis) {
    try {
        if (intitule.isEmpty() || abrev.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "L'intitulé et l'abréviation ne peuvent pas être vides.");
            return;
        }

        if (competenceAModifier == null) { // Mode Création
            competenceMngt.creerCompetence(intitule, abrev, prerequis);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "La compétence '" + intitule + "' a été créée.");
        } else { // Mode Modification
            competenceMngt.modifierCompetence(competenceAModifier, intitule, abrev, prerequis);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "La compétence '" + intitule + "' a été modifiée.");
        }
        // On rafraîchit la liste de gauche et on nettoie le panneau de droite
        chargerEtAfficherCompetences();
        afficherMessageAccueil();
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "L'opération a échoué : " + e.getMessage());
        e.printStackTrace();
    }
}

private void supprimerCompetence(Competence competence) {
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer la compétence '" + competence.getIntitule() + "' ?", ButtonType.YES, ButtonType.NO);
    confirmation.setTitle("Confirmation de suppression");
    confirmation.showAndWait().ifPresent(response -> {
        if (response == ButtonType.YES) {
            try {
                competenceMngt.supprimerCompetence(competence);
                chargerEtAfficherCompetences();
                afficherMessageAccueil();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur BDD", "Impossible de supprimer la compétence, elle est probablement utilisée ailleurs.");
                e.printStackTrace();
            }
        }
    });
}


}
