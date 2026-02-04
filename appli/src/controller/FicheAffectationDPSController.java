package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import modele.service.AffectationMngt;
import modele.DAO.UserDAO;
import modele.DAO.AffectationDAO;
import modele.persistence.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour la fiche détaillée d'un DPS et ses affectations.
 */
public class FicheAffectationDPSController {

    @FXML private Label lblTitre;
    @FXML private Label lblSport;
    @FXML private Label lblSite;
    @FXML private Label lblDate;
    @FXML private Label lblHeureDebut;
    @FXML private Label lblHeureFin;
    @FXML private Label lblStatut;
    @FXML private VBox vboxCompetences;
    @FXML private Button btnRafraichir;
    @FXML private Button btnFermer;

    private DPS dpsActuel;
    private AffectationMngt affectationMngt;
    private UserDAO userDAO;
    private AffectationDAO affectationDAO;
    
    // Callback pour fermer la vue
    private Runnable onCloseCallback;

    /**
     * Initialisation du contrôleur
     */
    @FXML
    public void initialize() {
        affectationMngt = new AffectationMngt();
        userDAO = new UserDAO();
        affectationDAO = new AffectationDAO();
    }

    /**
     * Définit le DPS à afficher et charge ses informations
     */
    public void setDPS(DPS dps) {
        this.dpsActuel = dps;
        if (dps != null) {
            chargerInformationsDPS();
            chargerAffectationsCompetences();
        }
    }

    /**
     * Définit le callback à appeler lors de la fermeture
     */
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    /**
     * Charge les informations générales du DPS
     */
    private void chargerInformationsDPS() {
        lblTitre.setText("Fiche DPS - " + dpsActuel.getSport().getNom());
        lblSport.setText(dpsActuel.getSport().getNom());
        lblSite.setText(dpsActuel.getSite().getNom());
        lblDate.setText(dpsActuel.getJournee().toString());
        lblHeureDebut.setText(String.format("%02d:00", dpsActuel.getHoraireDepart()));
        lblHeureFin.setText(String.format("%02d:00", dpsActuel.getHoraireFin()));
        
        // Déterminer le statut du DPS
        try {
            String statut = determinerStatutDPS();
            lblStatut.setText(statut);
            lblStatut.getStyleClass().removeAll("statut-complet", "statut-partiel", "statut-vide");
            switch (statut) {
                case "Complet":
                    lblStatut.getStyleClass().add("statut-complet");
                    break;
                case "Partiel":
                    lblStatut.getStyleClass().add("statut-partiel");
                    break;
                case "Vide":
                    lblStatut.getStyleClass().add("statut-vide");
                    break;
            }
        } catch (SQLException e) {
            lblStatut.setText("Erreur");
            e.printStackTrace();
        }
    }

    /**
     * Détermine le statut du DPS (Complet, Partiel, Vide)
     */
    private String determinerStatutDPS() throws SQLException {
        List<Affectation> affectations = affectationDAO.findAffectationsForDps(dpsActuel.getId());
        
        int postesRequis = 0;
        for (Integer nb : dpsActuel.getBesoins().values()) {
            postesRequis += nb;
        }
        
        int postesOccupes = affectations.size();
        
        if (postesOccupes == 0) {
            return "Vide";
        } else if (postesOccupes >= postesRequis) {
            return "Complet";
        } else {
            return "Partiel";
        }
    }

    /**
     * Charge les lignes d'affectation pour chaque compétence
     */
    private void chargerAffectationsCompetences() {
        vboxCompetences.getChildren().clear();

        try {
            // Récupérer les affectations existantes
            List<Affectation> affectationsExistantes = affectationDAO.findAffectationsForDps(dpsActuel.getId());
            Map<Long, List<Affectation>> affectationsParCompetence = new HashMap<>();
            
            for (Affectation aff : affectationsExistantes) {
                long compId = aff.getCompetenceRemplie().getIdComp();
                affectationsParCompetence.computeIfAbsent(compId, k -> new ArrayList<>()).add(aff);
            }

            // Créer une ligne pour chaque poste requis
            for (Map.Entry<Competence, Integer> entry : dpsActuel.getBesoins().entrySet()) {
                Competence competence = entry.getKey();
                int nbPostes = entry.getValue();
                
                List<Affectation> affectationsComp = affectationsParCompetence.getOrDefault(competence.getIdComp(), new ArrayList<>());
                
                // Créer autant de lignes que de postes requis
                for (int i = 0; i < nbPostes; i++) {
                    Affectation affectationActuelle = (i < affectationsComp.size()) ? affectationsComp.get(i) : null;
                    HBox ligneAffectation = creerLigneAffectation(competence, affectationActuelle, i + 1);
                    vboxCompetences.getChildren().add(ligneAffectation);
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les affectations : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crée une ligne d'affectation pour une compétence
     */
    private HBox creerLigneAffectation(Competence competence, Affectation affectationActuelle, int numeroPoste) {
        HBox ligne = new HBox(15.0);
        ligne.setAlignment(Pos.CENTER_LEFT);
        ligne.setPadding(new Insets(15, 20, 15, 20));
        ligne.getStyleClass().add("ligne-affectation");
        ligne.setPrefWidth(750);

        // Label de la compétence avec numéro de poste
        Label lblCompetence = new Label(competence.getIntitule() + " (Poste " + numeroPoste + ")");
        lblCompetence.getStyleClass().add("competence-label");
        lblCompetence.setPrefWidth(200);
        lblCompetence.setMinWidth(200);

        // ComboBox pour sélectionner le secouriste
        ComboBox<SecouristeItem> cbSecouriste = new ComboBox<>();
        cbSecouriste.setPrefWidth(250);
        cbSecouriste.setMinWidth(250);
        cbSecouriste.getItems().add(new SecouristeItem(null, "Non affecté"));

        try {
            // Charger les secouristes disponibles pour cette compétence
            ArrayList<Secouriste> secouristesDispos = userDAO.findAvailableAndSkilledSecouristes(
                dpsActuel.getJournee().getId(), 
                competence.getIdComp()
            );
            
            for (Secouriste s : secouristesDispos) {
                cbSecouriste.getItems().add(new SecouristeItem(s, s.getPrenom() + " " + s.getNom()));
            }
            
            // Sélectionner le secouriste actuellement affecté s'il y en a un
            if (affectationActuelle != null) {
                for (SecouristeItem item : cbSecouriste.getItems()) {
                    if (item.getSecouriste() != null && 
                        item.getSecouriste().getId() == affectationActuelle.getSecouriste().getId()) {
                        cbSecouriste.setValue(item);
                        break;
                    }
                }
            } else {
                cbSecouriste.setValue(cbSecouriste.getItems().get(0)); // "Non affecté"
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les secouristes : " + e.getMessage());
        }

        // Bouton Appliquer - PLUS LONG
        Button btnAppliquer = new Button("Appliquer");
        btnAppliquer.getStyleClass().add("btn-appliquer");
        btnAppliquer.setPrefWidth(120); // Augmenté de 100 à 120
        btnAppliquer.setMinWidth(120);
        btnAppliquer.setOnAction(event -> {
            try {
                appliquerAffectation(competence, affectationActuelle, cbSecouriste.getValue());
                chargerAffectationsCompetences(); // Recharger pour mettre à jour l'affichage
                chargerInformationsDPS(); // Mettre à jour le statut
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'affectation : " + e.getMessage());
            }
        });

        // Label d'affichage du secouriste actuellement affecté
        Label lblSecouristeActuel = new Label();
        lblSecouristeActuel.getStyleClass().add("secouriste-actuel");
        lblSecouristeActuel.setPrefWidth(180);
        lblSecouristeActuel.setMinWidth(180);
        
        if (affectationActuelle != null) {
            lblSecouristeActuel.setText("→ " + affectationActuelle.getSecouriste().getPrenom() + 
                                       " " + affectationActuelle.getSecouriste().getNom());
            lblSecouristeActuel.getStyleClass().add("secouriste-affecte");
        } else {
            lblSecouristeActuel.setText("→ Non affecté");
            lblSecouristeActuel.getStyleClass().add("secouriste-non-affecte");
        }

        ligne.getChildren().addAll(lblCompetence, cbSecouriste, btnAppliquer, lblSecouristeActuel);
        return ligne;
    }

    /**
     * Applique ou supprime une affectation
     */
    private void appliquerAffectation(Competence competence, Affectation affectationActuelle, SecouristeItem nouveauSecouriste) throws SQLException {
        // Supprimer l'ancienne affectation si elle existe
        if (affectationActuelle != null) {
            affectationMngt.deleteAffectation(competence, affectationActuelle.getSecouriste(), dpsActuel);
        }

        // Créer la nouvelle affectation si un secouriste est sélectionné
        if (nouveauSecouriste != null && nouveauSecouriste.getSecouriste() != null) {
            affectationMngt.createAffectation(competence, nouveauSecouriste.getSecouriste(), dpsActuel);
        }
    }

    /**
     * Action du bouton Rafraîchir
     */
    @FXML
    private void onRafraichir(ActionEvent event) {
        if (dpsActuel != null) {
            chargerInformationsDPS();
            chargerAffectationsCompetences();
        }
    }

    /**
     * Action du bouton Fermer
     */
    @FXML
    private void onFermer(ActionEvent event) {
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }

    /**
     * Affiche une alerte
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Classe interne pour représenter un item de la ComboBox
     */
    private static class SecouristeItem {
        private final Secouriste secouriste;
        private final String displayName;

        public SecouristeItem(Secouriste secouriste, String displayName) {
            this.secouriste = secouriste;
            this.displayName = displayName;
        }

        public Secouriste getSecouriste() {
            return secouriste;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
