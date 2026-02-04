package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import modele.persistence.Competence;
import javafx.scene.control.Button;

/**
 * Contrôleur pour la vue FicheCompetence.fxml
 * Gère l'affichage des détails d'une compétence et les interactions utilisateur.
 * Permet de modifier ou supprimer une compétence.
 */
public class FicheCompetenceController {

    // Les @FXML de la vue FicheCompetence.fxml
    @FXML private Label intituleText;
    @FXML private Label abrevText;
    @FXML private VBox prerequisVBox;

    private Competence competenceAffichee;
    private GererCompetencesController mainController;

    /**
     * Méthode d'initialisation appelée par le contrôleur principal
     * pour passer les données nécessaires.
     */
    public void initData(Competence competence, GererCompetencesController mainController) {
        this.competenceAffichee = competence;
        this.mainController = mainController;
        
        // On remplit les labels avec les informations de la compétence
        intituleText.setText(competence.getIntitule());
        abrevText.setText(competence.getAbrevComp());
        
        // On remplit la liste des prérequis
        prerequisVBox.getChildren().clear();
        if (competence.getPrerequis().isEmpty()) {
            prerequisVBox.getChildren().add(new Label("Aucun"));
        } else {
            for (Competence prerequis : competence.getPrerequis()) {
                prerequisVBox.getChildren().add(new Label("• " + prerequis.getIntitule()));
            }
        }
    }
    
    /**
     * Appelée quand on clique sur le bouton "Modifier".
     * Demande au contrôleur principal d'afficher le formulaire de modification.
     * 
     * @param event - événement lié au clic sur le bouton.
     */
    @FXML
    private void onModifier(ActionEvent event) {
        mainController.afficherFormulaire(competenceAffichee);
    }
    
    /**
     * Appelée quand on clique sur le bouton "Supprimer".
     * Demande au contrôleur principal de gérer la suppression.
     * 
     * @param event - événement lié au clic sur le bouton.
     */
    @FXML
    private void onSupprimer(ActionEvent event) {
        mainController.supprimerCompetence(competenceAffichee);
    }
}