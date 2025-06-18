package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import modele.persistence.Competence;
import modele.persistence.DPS;
import modele.persistence.Journee;
import modele.persistence.Site;
import modele.persistence.Sport;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormulaireDpsController {

    @FXML private Label titreFormLabel;
    @FXML private ComboBox<Sport> sportComboBox;
    @FXML private ComboBox<Site> siteComboBox;
    @FXML private DatePicker journeePicker;
    @FXML private Spinner<Integer> heureDebutSpinner;
    @FXML private Spinner<Integer> heureFinSpinner;
    @FXML private ComboBox<Competence> competenceComboBox;
    @FXML private Spinner<Integer> nombreSpinner;
    @FXML private ListView<String> besoinsListView;

    private DPS dpsAModifier;
    private GererDpsController mainController;
    private Map<Competence, Integer> besoinsMap = new HashMap<>();

    /**
     * Méthode d'initialisation pour remplir le formulaire.
     */
    public void initData(DPS dps, GererDpsController mainController, List<Sport> sports, List<Site> sites, List<Competence> competences) {
        this.dpsAModifier = dps;
        this.mainController = mainController;
        boolean estEnModeCreation = (dps == null);

        // On configure les ComboBox avec les listes de la BDD
        configurerComboBoxes(sports, sites, competences);

        if (estEnModeCreation) {
            titreFormLabel.setText("Créer un nouveau DPS");
            // Les champs restent vides ou avec des valeurs par défaut
        } else {
            titreFormLabel.setText("Modifier ce DPS");
            // On remplit le formulaire avec les données du DPS existant
            sportComboBox.setValue(dps.getSport());
            siteComboBox.setValue(dps.getSite());
            Journee j = dps.getJournee();
            journeePicker.setValue(LocalDate.of(j.getAnnee(), j.getMois(), j.getJour()));
            heureDebutSpinner.getValueFactory().setValue(dps.getHoraireDepart());
            heureFinSpinner.getValueFactory().setValue(dps.getHoraireFin());

            // On remplit la map et la liste des besoins
            this.besoinsMap = new HashMap<>(dps.getBesoins());
            mettreAJourAffichageBesoins();
        }
    }

    private void configurerComboBoxes(List<Sport> sports, List<Site> sites, List<Competence> competences) {
        // Remplir les ComboBox
        sportComboBox.getItems().setAll(sports);
        siteComboBox.getItems().setAll(sites);
        competenceComboBox.getItems().setAll(competences);

        // Définir comment afficher le nom dans la ComboBox au lieu de l'objet brut
        sportComboBox.setConverter(new StringConverter<Sport>() {
            @Override public String toString(Sport s) { return s == null ? "" : s.getNom(); }
            @Override public Sport fromString(String s) { return null; }
        });
        siteComboBox.setConverter(new StringConverter<Site>() {
            @Override public String toString(Site s) { return s == null ? "" : s.getNom(); }
            @Override public Site fromString(String s) { return null; }
        });
        competenceComboBox.setConverter(new StringConverter<Competence>() {
            @Override public String toString(Competence c) { return c == null ? "" : c.getIntitule(); }
            @Override public Competence fromString(String s) { return null; }
        });
    }

    @FXML
    private void onAjouterBesoin(ActionEvent event) {
        Competence comp = competenceComboBox.getValue();
        Integer nombre = nombreSpinner.getValue();
        if (comp != null && nombre > 0) {
            besoinsMap.put(comp, nombre);
            mettreAJourAffichageBesoins();
        }
    }

    private void mettreAJourAffichageBesoins() {
        besoinsListView.getItems().clear();
        for (Map.Entry<Competence, Integer> entry : besoinsMap.entrySet()) {
            besoinsListView.getItems().add(entry.getKey().getIntitule() + " (Quantité : " + entry.getValue() + ")");
        }
    }

    @FXML
    private void onValider(ActionEvent event) {
        // Récupération et validation des données du formulaire...
        Sport sport = sportComboBox.getValue();
        Site site = siteComboBox.getValue();
        LocalDate date = journeePicker.getValue();
        // ... autres validations ...

        mainController.validerFormulaireDps(
            dpsAModifier,
            sport, site, date,
            heureDebutSpinner.getValue(),
            heureFinSpinner.getValue(),
            besoinsMap
        );
    }

    @FXML
    private void onAnnuler(ActionEvent event) {
        if (dpsAModifier != null) {
            mainController.afficherDetails(dpsAModifier);
        } else {
            mainController.afficherMessageAccueil();
        }
    }
}