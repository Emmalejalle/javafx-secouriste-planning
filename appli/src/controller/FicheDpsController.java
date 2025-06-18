package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import modele.persistence.Affectation; // Not used
import modele.persistence.Competence;
import modele.persistence.DPS;
import java.util.List; // Not used
import java.util.Map;

public class FicheDpsController {

    @FXML private Label sportText;
    @FXML private Label horairesText;
    @FXML private Label siteText;
    @FXML private Label journeeText;
    @FXML private VBox besoinsVBox; // Pour afficher les besoins en compétences
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;

    private DPS dpsAffiche;
    private GererDpsController mainController;

    public void initData(DPS dps, GererDpsController mainController) {
        this.dpsAffiche = dps;
        this.mainController = mainController;

        // Remplissage des champs d'information
        sportText.setText(dps.getSport().getNom());
        siteText.setText(dps.getSite().getNom());
        journeeText.setText(dps.getJournee().toString());
        horairesText.setText(String.format("%dh - %dh", dps.getHoraireDepart(), dps.getHoraireFin()));

        // Affichage des besoins en compétences
        besoinsVBox.getChildren().clear();
        if (dps.getBesoins() == null || dps.getBesoins().isEmpty()) {
            besoinsVBox.getChildren().add(new Label("Aucun besoin spécifié."));
        } else {
            for (Map.Entry<Competence, Integer> entry : dps.getBesoins().entrySet()) {
                String texteBesoin = "• " + entry.getKey().getIntitule() + " (x" + entry.getValue() + ")";
                besoinsVBox.getChildren().add(new Label(texteBesoin));
            }
        }
    }

    @FXML
    private void onModifier(ActionEvent event) {
        mainController.afficherFormulaireModification(dpsAffiche);
    }

    @FXML
    private void onSupprimer(ActionEvent event) {
        mainController.supprimerDps(dpsAffiche);
    }
    
    // This method is redundant and can be removed, as onModifier already calls mainController.afficherFormulaireModification
    public void afficherFormulaireModification(DPS dps) {
        System.out.println("Affichage du formulaire de modification pour le DPS : " + dps.getSport().getNom());
        mainController.afficherFormulaireModification(dpsAffiche);
    }
}