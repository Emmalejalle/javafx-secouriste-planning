package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import modele.service.AffectationMngt;
import modele.DAO.DpsDAO;
import modele.DAO.JourneeDAO;
import modele.DAO.AffectationDAO;
import modele.persistence.DPS;
import modele.persistence.Journee;
import modele.persistence.Affectation;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour la vue d'affectation des DPS (Dispositifs de Protection et de Sécurité).
 * Gère l'affichage des détails d'une DPS sélectionnée et les interactions utilisateur.
 * Permet également l'auto-affectation et le retour à l'accueil.
 *
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class AffectationDpsController {

    @FXML private BorderPane rootPane;
    @FXML private Pane headerPlaceholder;
    @FXML private javafx.scene.layout.StackPane contentPane;
    @FXML private Button btnReturn;
    @FXML private Button btnAutoAffectation;
    @FXML private Button btnClearAffectation;
    @FXML private VBox vboxListeDps;
    @FXML private ComboBox<Integer> cbJour;
    @FXML private ComboBox<Integer> cbMois;
    @FXML private ComboBox<Integer> cbAnnee;
    @FXML private Button btnExportCSV;
    @FXML private TextField RechercheSecouristeTextFil; // Barre de recherche

    private AffectationMngt affectationMngt = new AffectationMngt();
    private DpsDAO dpsDAO = new DpsDAO();
    private JourneeDAO journeeDAO = new JourneeDAO();
    private AffectationDAO affectationDAO = new AffectationDAO();
    private List<DPS> tousLesDPS = new ArrayList<>(); // Liste complète des DPS
    private VBox dpsSelectionnee = null; // DPS actuellement sélectionnée
    
    // Images pour les états des DPS
    private Image imageComplet;
    private Image imagePartiel;
    private Image imageVide;
    
    /**
     * Exporte les affectations pour une journée donnée dans un fichier CSV.
     */
    @FXML
    private void onExportAffectationsCSV(ActionEvent event) {
        Integer jour = cbJour.getValue();
        Integer mois = cbMois.getValue();
        Integer annee = cbAnnee.getValue();
        if (jour == null || mois == null || annee == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date valide.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les affectations en CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        fileChooser.setInitialFileName("affectations_" + jour + "_" + mois + "_" + annee + ".csv");
        File file = fileChooser.showSaveDialog(cbJour.getScene().getWindow());
        if (file != null) {
            try {
                affectationMngt.exportAffectationsJourneeToCSV(jour, mois, annee, file);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Exportation réussie vers " + file.getName());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'exportation : " + e.getMessage());
            }
        }
    }

    /**
     * Initialisation du contrôleur.
     */
    @FXML
    public void initialize() {
        try {
            // Charge le header commun
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/PatronHeaderAdmin.fxml"));
            HBox header = loader.load();
            PatronHeaderAdminController hdrCtrl = loader.getController();
            hdrCtrl.setTitre("Affectation DPS");
            rootPane.setTop(header);
            
            // Charger les images de statut
            chargerImagesStatut();
            
            // Charger tous les DPS au démarrage
            chargerTousLesDPS();
            
            // Configurer la barre de recherche
            RechercheSecouristeTextFil.setOnKeyReleased(this::onRechercheKeyReleased);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge les images pour les différents états des DPS
     */
    private void chargerImagesStatut() {
        try {
            imageComplet = new Image(getClass().getResourceAsStream("/images/check-circle-green.png"));
            imagePartiel = new Image(getClass().getResourceAsStream("/images/warning-orange.png"));
            imageVide = new Image(getClass().getResourceAsStream("/images/circle-red.png"));
        } catch (Exception e) {
            // Images par défaut si les fichiers ne sont pas trouvés
            System.out.println("Images de statut non trouvées, utilisation d'images par défaut");
            // Vous pouvez créer des images simples par code si nécessaire
        }
    }

    /**
     * Charge tous les DPS depuis la base de données et les affiche
     */
    private void chargerTousLesDPS() {
        try {
            tousLesDPS = dpsDAO.findAll();
            afficherDPS(tousLesDPS);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les DPS : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche la liste des DPS dans la VBox
     */
    private void afficherDPS(List<DPS> dpsList) {
        vboxListeDps.getChildren().clear();
        
        for (DPS dps : dpsList) {
            VBox dpsCard = creerCarteDPS(dps);
            vboxListeDps.getChildren().add(dpsCard);
        }
    }

    /**
     * Crée une carte cliquable pour un DPS
     */
    private VBox creerCarteDPS(DPS dps) {
        VBox carte = new VBox();
        carte.setSpacing(4);
        carte.setPadding(new Insets(12, 18, 12, 18)); // Padding optimisé pour rectangle
        carte.setUserData(dps);
        carte.getStyleClass().add("dps-card");
        
        // Créer le contenu de la carte
        HBox ligneHaut = new HBox();
        ligneHaut.setSpacing(15);
        ligneHaut.setAlignment(Pos.CENTER_LEFT);
        
        // Symbole de statut
        Label symboleStatut = new Label();
        symboleStatut.getStyleClass().add("dps-symbole");
        
        // Déterminer le symbole selon le statut du DPS
        try {
            StatutDPS statut = obtenirStatutDPS(dps);
            switch (statut) {
                case COMPLET:
                    symboleStatut.setText("✓");
                    symboleStatut.getStyleClass().add("symbole-complet");
                    break;
                case PARTIEL:
                    symboleStatut.setText("⚠");
                    symboleStatut.getStyleClass().add("symbole-partiel");
                    break;
                case VIDE:
                default:
                    symboleStatut.setText("○");
                    symboleStatut.getStyleClass().add("symbole-vide");
                    break;
            }
        } catch (SQLException e) {
            symboleStatut.setText("○");
            symboleStatut.getStyleClass().add("symbole-vide");
        }
        
        // Labels d'informations
        VBox infos = new VBox();
        infos.setSpacing(3);
        
        // Ligne 1: Sport - Lieu
        Label ligneSportLieu = new Label(dps.getSport().getNom() + " - " + dps.getSite().getNom());
        ligneSportLieu.getStyleClass().add("dps-sport-lieu");
        
        // Ligne 2: Date - HeureDepart - HeureFin
        String dateStr = dps.getJournee().toString();
        String heureDepart = String.format("%02d:00", dps.getHoraireDepart());
        String heureFin = String.format("%02d:00", dps.getHoraireFin());
        Label ligneDateTime = new Label(dateStr + " - " + heureDepart + " - " + heureFin);
        ligneDateTime.getStyleClass().add("dps-date-time");
        
        infos.getChildren().addAll(ligneSportLieu, ligneDateTime);
        ligneHaut.getChildren().addAll(symboleStatut, infos);
        carte.getChildren().add(ligneHaut);
        
        // Gestionnaire de clic
        carte.setOnMouseClicked(this::onDpsCardClicked);
        
        return carte;
    }

    /**
     * Enumération pour les différents statuts d'un DPS
     */
    private enum StatutDPS {
        COMPLET, PARTIEL, VIDE
    }

    /**
     * Détermine le statut d'un DPS (complet, partiel, vide)
     */
    private StatutDPS obtenirStatutDPS(DPS dps) throws SQLException {
        List<Affectation> affectations = affectationDAO.findAffectationsForDps(dps.getId());
        
        // Calculer le nombre total de postes requis
        int postesRequis = 0;
        for (Integer nb : dps.getBesoins().values()) {
            postesRequis += nb;
        }
        
        int postesOccupes = affectations.size();
        
        if (postesOccupes == 0) {
            return StatutDPS.VIDE;
        } else if (postesOccupes >= postesRequis) {
            return StatutDPS.COMPLET;
        } else {
            return StatutDPS.PARTIEL;
        }
    }

    /**
     * Gère le clic sur une carte DPS
     */
    @FXML
    private void onDpsCardClicked(MouseEvent event) {
        VBox carteCliquee = (VBox) event.getSource();
        
        // Désélectionner l'ancienne carte
        if (dpsSelectionnee != null) {
            dpsSelectionnee.getStyleClass().remove("dps-card-selected");
        }
        
        // Sélectionner la nouvelle carte
        if (dpsSelectionnee == carteCliquee) {
            // Si on clique sur la même carte, on la désélectionne
            dpsSelectionnee = null;
            contentPane.getChildren().clear();
        } else {
            // Sélectionner la nouvelle carte
            dpsSelectionnee = carteCliquee;
            dpsSelectionnee.getStyleClass().add("dps-card-selected");
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/FicheAffectationDPS.fxml"));
                Parent detailView = loader.load();
                
                // Passer le DPS sélectionné au contrôleur de la vue de détail
                FicheAffectationDPSController controller = loader.getController();
                DPS dpsSelectionne = (DPS) carteCliquee.getUserData();
                controller.setDPS(dpsSelectionne);
                
                // Définir le callback de fermeture
                controller.setOnCloseCallback(() -> {
                    contentPane.getChildren().clear();
                    if (dpsSelectionnee != null) {
                        dpsSelectionnee.getStyleClass().remove("dps-card-selected");
                    }
                    dpsSelectionnee = null;
                    // Rafraîchir la liste des DPS pour mettre à jour les statuts
                    chargerTousLesDPS();
                });
                
                contentPane.getChildren().setAll(detailView);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la vue de détail");
            }
        }
    }

    /**
     * Gère la recherche en temps réel dans la liste des DPS
     */
    @FXML
    private void onRechercheKeyReleased(KeyEvent event) {
        String recherche = RechercheSecouristeTextFil.getText().toLowerCase().trim();
        
        if (recherche.isEmpty()) {
            // Si la barre de recherche est vide, afficher tous les DPS
            afficherDPS(tousLesDPS);
        } else {
            // Filtrer les DPS selon le texte de recherche
            List<DPS> dpsFiltres = new ArrayList<>();
            for (DPS dps : tousLesDPS) {
                String texteDPS = (dps.getSport().getNom() + " " + 
                                 dps.getSite().getNom() + " " + 
                                 dps.getJournee().toString()).toLowerCase();
                if (texteDPS.contains(recherche)) {
                    dpsFiltres.add(dps);
                }
            }
            afficherDPS(dpsFiltres);
        }
    }

    // ... Le reste des méthodes reste identique (onAutoAffectation, onClearAffectation, etc.)
    
    /**
     * Auto-affectation avec choix d'algorithme et pop-up de résultats
     */
    @FXML
    private void onAutoAffectation(ActionEvent event) {
        Integer jour = cbJour.getValue();
        Integer mois = cbMois.getValue();
        Integer annee = cbAnnee.getValue();
        if (jour == null || mois == null || annee == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date valide.");
            return;
        }

        // Choix de la méthode d'affectation
        Alert choix = new Alert(Alert.AlertType.CONFIRMATION);
        choix.setTitle("Choix de la méthode d'affectation");
        choix.setHeaderText("Quelle méthode d'affectation souhaitez-vous utiliser ?");
        choix.setContentText("• Gloutonne Optimale : Rapide, privilégie les compétences rares\n" +
                           "• Exhaustive : Optimal mais lent, teste toutes les combinaisons\n" +
                           "• Gloutonne Naïve : Très rapide, affectation simple");
        
        ButtonType btnGlouton = new ButtonType("Gloutonne Optimale");
        ButtonType btnExhaustif = new ButtonType("Exhaustive");
        ButtonType btnNaif = new ButtonType("Gloutonne Naïve");
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        choix.getButtonTypes().setAll(btnGlouton, btnExhaustif, btnNaif, btnAnnuler);

        choix.showAndWait().ifPresent(type -> {
            if (type == btnAnnuler) return;
            
            try {
                // Suppression des anciennes affectations
                int suppressions = affectationMngt.deleteAllAffectationJournee(jour, mois, annee);
                
                // Variables pour les statistiques
                int nbAffectations = 0;
                String nomAlgorithme = "";
                long tempsDebut = System.currentTimeMillis();
                
                // Exécution de l'algorithme choisi
                if (type == btnGlouton) {
                    nbAffectations = affectationMngt.affectationAutoGloutonnePourJournee(jour, mois, annee).size();
                    nomAlgorithme = "Gloutonne Optimale";
                } else if (type == btnExhaustif) {
                    nbAffectations = affectationMngt.affectationAutoExhaustivePourJournee(jour, mois, annee).size();
                    nomAlgorithme = "Exhaustive";
                } else if (type == btnNaif) {
                    nbAffectations = affectationMngt.affectationAutoNaivePourJournee(jour, mois, annee).size();
                    nomAlgorithme = "Gloutonne Naïve";
                }
                
                long tempsFin = System.currentTimeMillis();
                long duree = tempsFin - tempsDebut;
                
                // Rafraîchir l'affichage des DPS pour mettre à jour les statuts
                chargerTousLesDPS();
                
                // Calcul des statistiques pour le pop-up
                afficherResultatsAffectation(jour, mois, annee, nomAlgorithme, nbAffectations, suppressions, duree);
                
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Erreur lors de l'affectation automatique : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Affiche un pop-up avec les résultats de l'affectation automatique
     */
    private void afficherResultatsAffectation(int jour, int mois, int annee, String algorithme, 
                                            int nbAffectations, int suppressions, long duree) {
        try {
            // Récupérer les statistiques détaillées
            Journee journee = journeeDAO.findByDate(jour, mois, annee);
            if (journee != null) {
                List<DPS> dpsJournee = dpsDAO.findAllByJournee(journee.getId());
                
                Alert resultat = new Alert(Alert.AlertType.INFORMATION);
                resultat.setTitle("Résultats de l'affectation automatique");
                resultat.setHeaderText("Affectation terminée avec succès !");
                
                StringBuilder message = new StringBuilder();
                message.append("📅 Date : ").append(jour).append("/").append(mois).append("/").append(annee).append("\n");
                message.append("⚙️ Algorithme utilisé : ").append(algorithme).append("\n");
                message.append("⏱️ Temps d'exécution : ").append(duree).append(" ms\n\n");
                
                message.append("📊 Résultats :\n");
                message.append("• ").append(suppressions).append(" ancienne(s) affectation(s) supprimée(s)\n");
                message.append("• ").append(nbAffectations).append(" nouvelle(s) affectation(s) créée(s)\n");
                message.append("• ").append(dpsJournee.size()).append(" DPS total(aux) pour cette journée\n");
                
                if (nbAffectations > 0) {
                    double pourcentage = (double) nbAffectations / getTotalPostesRequis(dpsJournee) * 100;
                    message.append("• Taux de couverture : ").append(String.format("%.1f", pourcentage)).append("%");
                }
                
                resultat.setContentText(message.toString());
                
                ButtonType btnFermer = new ButtonType("Fermer", ButtonBar.ButtonData.OK_DONE);
                ButtonType btnExporter = new ButtonType("Exporter CSV");
                resultat.getButtonTypes().setAll(btnExporter, btnFermer);
                
                resultat.showAndWait().ifPresent(response -> {
                    if (response == btnExporter) {
                        // Déclencher l'export CSV
                        onExportAffectationsCSV(new ActionEvent());
                    }
                });
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "Information", 
                "Affectation réalisée avec succès !\n" +
                "• " + nbAffectations + " affectation(s) créée(s)\n" +
                "• Algorithme : " + algorithme + "\n" +
                "• Temps : " + duree + " ms");
        }
    }

    /**
     * Calcule le nombre total de postes requis pour une liste de DPS
     */
    private int getTotalPostesRequis(List<DPS> dpsList) {
        int total = 0;
        for (DPS dps : dpsList) {
            for (Integer nbPostes : dps.getBesoins().values()) {
                total += nbPostes;
            }
        }
        return total;
    }
    
    /**
     * Supprime toutes les affectations pour la journée sélectionnée.
     */
    @FXML
    private void onClearAffectation(ActionEvent event) {
        Integer jour = cbJour.getValue();
        Integer mois = cbMois.getValue();
        Integer annee = cbAnnee.getValue();
        if (jour == null || mois == null || annee == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date valide.");
            return;
        }
        
        try {
            int nb = affectationMngt.deleteAllAffectationJournee(jour, mois, annee);
            if (nb > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    nb + " affectation(s) supprimée(s) pour le " + jour + "/" + mois + "/" + annee);
                // Rafraîchir l'affichage des DPS
                chargerTousLesDPS();
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Information", 
                    "Aucune affectation trouvée pour le " + jour + "/" + mois + "/" + annee);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    /**
     * Retour à l'accueil de l'administrateur.
     */
    @FXML
    void onReturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode utilitaire pour afficher des alertes
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}