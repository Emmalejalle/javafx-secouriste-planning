package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import modele.SessionManager;
import modele.persistence.Admin;
import modele.persistence.User;
import service.ProfilMngt;

public class ProfileAdminController {

    @FXML private BorderPane rootPane;

    // Header button & title are handled by PatronHeaderAdminController

    // Profile fields
    @FXML private Label lblName;
    @FXML private Label lblPhoneValue;
    @FXML private Label lblEmailValue;
    @FXML private Label lblStatusValue;
    @FXML private Label lblIdValue;
    @FXML private Label lblAddressValue;
    @FXML private Label lblBirthValue;
    @FXML private Label lblPwdValue;

    @FXML private Button btnModifyProfile;
    @FXML private Button btnReturn;

    private final ProfilMngt profilMngt = new ProfilMngt();
    private final SessionManager session = SessionManager.getInstance();

    @FXML
    public void initialize() {
        // 1. Injecter le header commun
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/PatronHeaderAdmin.fxml"));
            HBox header = loader.load();
            PatronHeaderAdminController hdrCtrl = loader.getController();
            hdrCtrl.setTitre("Profil Administrateur");
            rootPane.setTop(header);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Charger et afficher les données de l'admin courant
        User current = session.getCurrentUser();
        try {
            User user = profilMngt.loadUserById(current.getId());
            if (user != null) {
                lblName       .setText(user.getPrenom() + " " + user.getNom());
                lblPhoneValue .setText(user.getTel());
                lblEmailValue .setText(user.getEmail());
                lblStatusValue.setText(user instanceof Admin ? "Admin" : "Secouriste");
                lblIdValue    .setText(String.valueOf(user.getId()));
                lblAddressValue .setText(user.getAdresse());
                lblBirthValue   .setText(user.getDateNaissance());
                lblPwdValue     .setText("••••••••");  // on masque le mot de passe
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // ici on peut afficher une alerte d'erreur BDD
        }
    }

    /** Passe en mode édition du profil */
    @FXML
    void onModifyProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdminModif.fxml"));
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            // alerte d'erreur de chargement
        }
    }

    /** Retour à l'écran précédent sans rien sauvegarder */
    @FXML
    void onReturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
            ((Node)event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            // alerte d'erreur de chargement
        }
    }
}
