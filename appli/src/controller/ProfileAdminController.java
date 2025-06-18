package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modele.persistence.Admin;
import modele.persistence.User;
import modele.SessionManager;
import service.ProfilMngt;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileAdminController {

    // les fx:id définis dans ProfileAdmin.fxml
    @FXML private Label lblName;
    @FXML private Label lblPhoneValue;
    @FXML private Label lblEmailValue;
    @FXML private Label lblStatusValue;
    @FXML private Label lblIdValue;
    @FXML private Label lblAddressValue;
    @FXML private Label lblBirthValue;
    @FXML private Label lblCreatedValue;
    @FXML private Label lblPwdValue;

    @FXML private Button btnModifyProfile;
    @FXML private Button btnReturn;

    private final ProfilMngt profilMngt = new ProfilMngt();
    private final SessionManager session = SessionManager.getInstance();

    @FXML
    public void initialize() {
        System.out.println("ProfileAdminController initialisé (mode lecture).");
        try {
            // Charge l'utilisateur courant depuis la BDD
            long id = session.getCurrentUser().getId();
            User user = profilMngt.loadUserById(id);
            if (user != null) {
                // Remplit les champs
                lblName.setText(user.getNom() + " " + user.getPrenom());
                lblPhoneValue.setText(user.getTel());
                lblEmailValue.setText(user.getEmail());
                lblStatusValue.setText(user instanceof Admin ? "Admin" : "Utilisateur");
                lblIdValue.setText(String.valueOf(user.getId()));
                lblAddressValue.setText(user.getAdresse());
                lblBirthValue.setText(user.getDateNaissance());
                // Si vous avez un champ "date de création", ajoutez-la dans User et récupérez-la ici
                lblCreatedValue.setText("—"); 
                // Pour le mot de passe, affichez des ••• si vous ne voulez pas dévoiler la valeur brute
                lblPwdValue.setText("••••••••");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Vous pouvez afficher une alerte ici pour informer l'utilisateur
        }
    }

    @FXML
    void onModifyProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdminModif.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Retour à l'accueil Admin */
    @FXML
    void onReturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
            ((Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("ERREUR : Impossible de charger accueilAdmin.fxml");
            e.printStackTrace();
        }
    }
}
