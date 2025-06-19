package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

import modele.persistence.User;
import modele.persistence.Secouriste;
import modele.persistence.Competence;
import modele.SessionManager;
import service.ProfilMngt;

import controller.ControllerHeader;  // Ton contrôleur de header
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


/**
 * Controller pour la vue de modification du profil (profilModif.fxml).
 */
public class ProfilModifController {

    @FXML private TextField txtNomPrenom;
    @FXML private TextField txtTel;
    @FXML private TextField txtEmail;
    @FXML private TextField txtStatus;
    @FXML private TextField txtId;
    @FXML private PasswordField txtMdp;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtDateNaissance;

    @FXML private VBox certBoxModif;

    @FXML private Button btnValider;
    @FXML private Button btnAnnuler;
    @FXML private Button btnRetour;

    @FXML private Pane paneEnteteModif;


    private final ProfilMngt profilMngt = new ProfilMngt();
    private final SessionManager session   = SessionManager.getInstance();
    private User currentUser;

    /**
     * Initialise les champs avec les données de l'utilisateur et
     * affiche uniquement ses compétences dans certBoxModif.
     */
    public void initData(User user) {

        chargerEtInsererHeader("/vue/PatronHeaderSecouriste.fxml", "Modification de Profil");


        this.currentUser = user;

        // Nom + prénom en un seul champ
        txtNomPrenom.setText(user.getNom() + " " + user.getPrenom());

        txtTel.setText(user.getTel());
        txtEmail.setText(user.getEmail());

        // Statut
        txtStatus.setText(user instanceof Secouriste ? "Secouriste" : "Admin");
        txtStatus.setEditable(false);

        // ID en lecture seule
        txtId.setText(String.valueOf(user.getId()));
        txtId.setEditable(false);

        txtMdp.setText(user.getMdp());
        txtAdresse.setText(user.getAdresse());
        txtDateNaissance.setText(user.getDateNaissance());

        // Affichage dynamique des compétences
        certBoxModif.getChildren().clear();
        if (user instanceof Secouriste) {
            for (Competence comp : ((Secouriste) user).getCompetences()) {
                Button btn = new Button(comp.getIntitule());
                btn.setPrefWidth(377);
                btn.setPrefHeight(50);
                btn.getStyleClass().add("cert-button-outline");
                certBoxModif.getChildren().add(btn);
            }
        }
    }

    private void chargerEtInsererHeader(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Region header = loader.load();
    
            // Met le titre
            ControllerHeader ctrl = loader.getController();
            ctrl.setTitre(titre);
    
            paneEnteteModif.getChildren().clear();
            paneEnteteModif.getChildren().add(header);
    
            header.prefWidthProperty().bind(paneEnteteModif.widthProperty());
            header.prefHeightProperty().bind(paneEnteteModif.heightProperty());
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Valide les modifications, met à jour la base et revient à la vue profil. */
    @FXML
    private void onValider(ActionEvent event) {
        // Séparation rapide du nom et prénom (simplifié)
        String[] parts = txtNomPrenom.getText().trim().split("\\s+", 2);
        currentUser.setNom(parts[0]);
        currentUser.setPrenom(parts.length > 1 ? parts[1] : "");

        currentUser.setTel(txtTel.getText());
        currentUser.setEmail(txtEmail.getText());
        currentUser.setMdp(txtMdp.getText());
        currentUser.setAdresse(txtAdresse.getText());
        currentUser.setDateNaissance(txtDateNaissance.getText());

        try {
            profilMngt.updateProfil(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionnel : afficher une alerte à l'utilisateur
        }

        // Retour à la vue profil classique
        onRetour(event);
    }

    /** Annule et revient à la vue profil sans enregistrer. */
    @FXML
    private void onAnnuler(ActionEvent event) {
        onRetour(event);
    }

    /** Charge à nouveau `profil.fxml` et affiche le profil à jour. */
    @FXML
    private void onRetour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/profil.fxml"));
            Parent root = loader.load();

            // La session contient déjà currentUser, et ProfilController.initialize() lira ces données.
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Optionnel : alerte générique
        }
    }
}
