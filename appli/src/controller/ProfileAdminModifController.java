package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modele.persistence.User;
import modele.SessionManager;
import service.ProfilMngt;
import javafx.scene.layout.BorderPane;      // ← IMPORT
import javafx.scene.layout.HBox; 

import java.io.IOException;
import java.sql.SQLException;

public class ProfileAdminModifController {

    @FXML private Label lblName;
    @FXML private TextField TelValue;
    @FXML private TextField tfEmailValue;
    @FXML private TextField StatusValue;
    @FXML private TextField IdValue;
    @FXML private TextField AdressValue;
    @FXML private TextField AnnivValue;
    @FXML private TextField MDPValue;

    private final ProfilMngt profilMngt = new ProfilMngt();
    private final SessionManager session = SessionManager.getInstance();
    private User currentUser;



    @FXML
    public void initialize() {

        try {
            // Récupère l'admin courant depuis la session + BDD
            long userId = session.getCurrentUser().getId();
            currentUser = profilMngt.loadUserById(userId);

            // Affiche nom + prénom
            lblName.setText(currentUser.getPrenom() + " " + currentUser.getNom());

            // Remplit les champs
            TelValue.setText(currentUser.getTel());
            tfEmailValue.setText(currentUser.getEmail());
            StatusValue.setText(currentUser instanceof modele.persistence.Admin ? "Admin" : "Secouriste");
            IdValue.setText(String.valueOf(currentUser.getId()));
            // ID non éditable
            IdValue.setEditable(false);
            AdressValue.setText(currentUser.getAdresse());
            AnnivValue.setText(currentUser.getDateNaissance());
            MDPValue.setText(currentUser.getMdp());

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO : alerte à l'utilisateur
        }
    }

    /** Valide et enregistre en base */
    @FXML
    private void onValidate(ActionEvent event) {
        // Met à jour l'objet
        currentUser.setTel(TelValue.getText());
        currentUser.setEmail(tfEmailValue.getText());
        currentUser.setAdresse(AdressValue.getText());
        currentUser.setDateNaissance(AnnivValue.getText());
        currentUser.setMdp(MDPValue.getText());

        try {
            profilMngt.updateProfil(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO : alerte d’erreur
            return;
        }

        goToProfile(event);
    }

    /** Annule sans enregistrer */
    @FXML
    private void onCancel(ActionEvent event) {
        goToProfile(event);
    }

    /** Retour à l’accueil Admin */
    @FXML
    private void onReturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Charge ProfileAdmin.fxml */
    private void goToProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdmin.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
