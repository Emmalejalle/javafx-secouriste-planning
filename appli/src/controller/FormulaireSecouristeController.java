package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import modele.persistence.Competence;
import modele.persistence.Secouriste;
import java.util.List;

public class FormulaireSecouristeController {

    // FXML de la vue FormulaireSecouriste.fxml
    @FXML private Label titreFormLabel;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telField;
    @FXML private TextField adresseField;
    @FXML private TextField dateNaissanceField;
    @FXML private PasswordField mdpField;
    @FXML private ListView<Competence> competencesListView;
    @FXML private Button annulerButton;
    @FXML private Button validerButton;

    private Secouriste secouristeAModifier;
    private GererSecouristesController mainController;

    @FXML
    public void initialize() {
        annulerButton.getStyleClass().addAll("button-cancel", "mr-10");
    }

    /**
     * Méthode d'initialisation pour passer les données.
     */
    public void initData(Secouriste secouriste, List<Competence> allCompetences, GererSecouristesController mainController) {
        this.secouristeAModifier = secouriste;
        this.mainController = mainController;
        boolean estEnModeCreation = (secouriste == null);

        // On met à jour le titre et on remplit les champs
        titreFormLabel.setText(estEnModeCreation ? "Création du secouriste" : "Modification de la fiche personnelle");
        nomField.setText(estEnModeCreation ? "" : secouriste.getNom());
        prenomField.setText(estEnModeCreation ? "" : secouriste.getPrenom());
        emailField.setText(estEnModeCreation ? "" : secouriste.getEmail());
        telField.setText(estEnModeCreation ? "" : secouriste.getTel());
        adresseField.setText(estEnModeCreation ? "" : secouriste.getAdresse());
        dateNaissanceField.setText(estEnModeCreation ? "" : secouriste.getDateNaissance());
        mdpField.setText(estEnModeCreation ? "" : secouriste.getMdp());

        // On configure la liste des compétences
        competencesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        competencesListView.getItems().setAll(allCompetences);

        if (!estEnModeCreation && secouriste.getCompetences() != null) {
            for (Competence c : secouriste.getCompetences()) {
                competencesListView.getSelectionModel().select(c);
            }
        }
    }

    /**
     * Appelée quand on clique sur "Valider".
     * Récupère toutes les infos et les envoie au contrôleur principal pour traitement.
     */
    @FXML
    private void onValider(ActionEvent event) {
        mainController.validerFormulaire(
            secouristeAModifier,
            mdpField.getText(),
            nomField.getText(),
            prenomField.getText(),
            dateNaissanceField.getText(),
            emailField.getText(),
            telField.getText(),
            adresseField.getText(),
            competencesListView.getSelectionModel().getSelectedItems()
        );
    }

    /**
     * Appelée quand on clique sur "Annuler".
     * Demande au contrôleur principal de ré-afficher les détails (si on était en modif) ou l'accueil.
     */
    @FXML
    private void onAnnuler(ActionEvent event) {
        if (secouristeAModifier != null) {
            mainController.afficherDetails(secouristeAModifier);
        } else {
            mainController.afficherMessageAccueil();
        }
    }
}
