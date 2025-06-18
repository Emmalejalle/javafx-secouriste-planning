package controller;


import modele.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import modele.persistence.Competence;
import modele.persistence.Secouriste;

public class FicheSecouristeController {

    // FXML de la vue FicheSecouriste.fxml
    @FXML private Label nomText;
    @FXML private Label prenomText;
    @FXML private Label emailText;
    @FXML private Label telText;
    @FXML private Label adresseText;
    @FXML private Label dateNaissanceText;
    @FXML private VBox competencesVBox;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;
    @FXML private Button planningButton;

    private Secouriste secouristeAffiche;
    private GererSecouristesController mainController;

    /**
     * Méthode d'initialisation appelée par le contrôleur principal
     * pour passer les données nécessaires.
     */
    public void initData(Secouriste secouriste, GererSecouristesController mainController) {
        this.secouristeAffiche = secouriste;
        this.mainController = mainController;

        // On remplit les labels avec les informations du secouriste
        nomText.setText(secouriste.getNom());
        prenomText.setText(secouriste.getPrenom());
        emailText.setText(secouriste.getEmail());
        telText.setText(secouriste.getTel());
        adresseText.setText(secouriste.getAdresse());
        dateNaissanceText.setText(secouriste.getDateNaissance());

        // On remplit la liste des compétences
        competencesVBox.getChildren().clear();
        if (secouriste.getCompetences() == null || secouriste.getCompetences().isEmpty()) {
            competencesVBox.getChildren().add(new Label("Aucune compétence"));
        } else {
            for (Competence c : secouriste.getCompetences()) {
                competencesVBox.getChildren().add(new Label("• " + c.getIntitule()));
            }
        }
    }

    /**
     * Appelée quand on clique sur "Modifier".
     * Demande au contrôleur principal d'afficher le formulaire de modification.
     */
    @FXML
    private void onModifier(ActionEvent event) {
        mainController.afficherFormulaire(secouristeAffiche);
    }

    /**
     * Appelée quand on clique sur "Supprimer".
     * Demande au contrôleur principal de gérer la suppression.
     */
    @FXML
    private void onSupprimer(ActionEvent event) {
        mainController.supprimerSecouriste(secouristeAffiche);
    }

    /**
     * Appelée quand on clique sur "Voir le planning".
     * Demande au contrôleur principal de naviguer vers la vue du planning.
     */
    
    @FXML
    private void onVoirPlanning(ActionEvent event) {
        System.out.println("Demande d'affichage du planning pour " + secouristeAffiche.getNom());

        // On place temporairement le secouriste sélectionné dans la session
        SessionManager.getInstance().setCurrentUser(secouristeAffiche);

        // On navigue vers la page du planning
        mainController.changeView(event, "/vue/PlanningVueADMIN.fxml"); // On appelle la méthode héritée de BaseController
    }
}