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

/**
 * Contrôleur pour la vue FormulaireDps.fxml
 * Gère l'affichage et la modification d'un DPS (Dispositif de Protection et de Sécurité).
 * Permet de créer ou modifier un DPS en remplissant un formulaire.
 */
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
     * 
     * @param dps - Le DPS à modifier (null si on crée un nouveau DPS)
     * @param mainController - Le contrôleur principal pour les interactions
     * @param sports - Liste des sports disponibles pour le ComboBox
     * @param sites - Liste des sites disponibles pour le ComboBox
     * @param competences - Liste des compétences disponibles pour le ComboBox
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

    /**
     * Configure les ComboBox pour les sports, les sites et les compétences.
     * Remplit les items des ComboBox avec les listes données en paramètres.
     * Définit un StringConverter pour afficher le nom de l'objet au lieu de l'objet brut.
     * 
     * @param sports - la liste des sports
     * @param sites - la liste des sites
     * @param competences - la liste des compétences
     */
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

    /**
     * Gère le clic sur le bouton "Ajouter" pour un besoin de compétence.
     * Ajoute la compétence sélectionnée et le nombre choisi à la map des besoins.
     * Met à jour la liste des besoins affichée.
     * 
     * @param event - événement lié au clic sur le bouton
     */
    @FXML
    private void onAjouterBesoin(ActionEvent event) {
        Competence comp = competenceComboBox.getValue();
        Integer nombre = nombreSpinner.getValue();
        if (comp != null && nombre > 0) {
            besoinsMap.put(comp, nombre);
            mettreAJourAffichageBesoins();
        }
    }

    /**
     * Met à jour l'affichage de la liste des besoins en compétences.
     * Vide la liste, puis ajoute chaque compétence avec son nombre dans la map
     * des besoins, en formatant le texte pour afficher le nom de la compétence
     * suivie de la quantité.
     */
    private void mettreAJourAffichageBesoins() {
        besoinsListView.getItems().clear();
        for (Map.Entry<Competence, Integer> entry : besoinsMap.entrySet()) {
            besoinsListView.getItems().add(entry.getKey().getIntitule() + " (Quantité : " + entry.getValue() + ")");
        }
    }

    /**
     * Appelée quand on clique sur le bouton "Valider".
     * Récupère et valide les données du formulaire.
     * Passe les données validées au contrôleur principal pour traitement.
     * 
     * @param event - événement lié au clic sur le bouton
     */
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

    /**
     * Appelée quand on clique sur le bouton "Annuler".
     * Demande au contrôleur principal de ré-afficher les détails (si on était en modif) ou l'accueil.
     * 
     * @param event - événement lié au clic sur le bouton
     */
    @FXML
    private void onAnnuler(ActionEvent event) {
        if (dpsAModifier != null) {
            mainController.afficherDetails(dpsAModifier);
        } else {
            mainController.afficherMessageAccueil();
        }
    }
}