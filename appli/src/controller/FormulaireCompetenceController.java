package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import modele.persistence.Competence;
import java.util.List;

/**
 * Contrôleur pour la vue FormulaireCompetence.fxml
 * Gère l'affichage et la validation du formulaire de création ou de modification d'une compétence.
 * Permet de saisir l'intitulé, l'abréviation et les prérequis de la compétence.
 */
public class FormulaireCompetenceController {

    // Les @FXML de la vue FormulaireCompetence.fxml
    @FXML private Label titreFormLabel;
    @FXML private TextField intituleField;
    @FXML private TextField abrevField;
    @FXML private ListView<Competence> prerequisListView;

    private Competence competenceAModifier;
    private GererCompetencesController mainController;

    /**
     * Méthode d'initialisation pour passer les données.
     * 
     * @param competence - La compétence à modifier (ou null si on est en création).
     * @param allCompetences - La liste de toutes les compétences pour le choix des prérequis.
     * @param mainController - La référence vers le contrôleur principal.
     */
    public void initData(Competence competence, List<Competence> allCompetences, GererCompetencesController mainController) {
        this.competenceAModifier = competence;
        this.mainController = mainController;
        boolean estEnModeCreation = (competence == null);

        // On met à jour le titre et on remplit les champs
        titreFormLabel.setText(estEnModeCreation ? "Ajouter une compétence" : "Modifier la compétence");
        intituleField.setText(estEnModeCreation ? "" : competence.getIntitule());
        abrevField.setText(estEnModeCreation ? "" : competence.getAbrevComp());
        
        // On configure la liste des prérequis
        prerequisListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        prerequisListView.getItems().setAll(allCompetences);

        // Si on est en mode modification, on ne peut pas se sélectionner soi-même et on présélectionne les anciens prérequis
        if (!estEnModeCreation) {
            prerequisListView.getItems().remove(competence);
            for (Competence prerequis : competence.getPrerequis()) {
                prerequisListView.getSelectionModel().select(prerequis);
            }
        }
    }
    
    /**
     * Appelée quand on clique sur le bouton "Valider".
     * Récupère toutes les infos et les envoie au contrôleur principal pour traitement.
     * 
     * @param event - événement lié au clic sur le bouton.
     */
    @FXML
    private void onValider(ActionEvent event) {
        String intitule = intituleField.getText();
        String abrev = abrevField.getText();
        List<Competence> prerequis = prerequisListView.getSelectionModel().getSelectedItems();
        mainController.validerFormulaire(competenceAModifier, intitule, abrev, prerequis);
    }
}
