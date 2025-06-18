package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modele.persistence.User;
import modele.SessionManager;
import service.ProfilMngt;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileAdminModifController {

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
            // On récupère l'admin courant depuis la session + BDD
            long userId = session.getCurrentUser().getId();
            currentUser = profilMngt.loadUserById(userId);

            // On pré-remplit les champs
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
            // afficher une alerte en pratique
        }
    }

    /** Bouton “Valider” : on récupère les valeurs et on met à jour la BDD */
    @FXML
    private void onValidate(ActionEvent event) {
        // On met à jour l'objet
        currentUser.setTel(TelValue.getText());
        currentUser.setEmail(tfEmailValue.getText());
        // on n'édite pas ID ni statut ni nom/prenom ici
        currentUser.setAdresse(AdressValue.getText());
        currentUser.setDateNaissance(AnnivValue.getText());
        currentUser.setMdp(MDPValue.getText());

        try {
            profilMngt.updateProfil(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
            // en prod : alerte d’erreur
            return;
        }

        // Retour à la vue profile après succès
        goToProfile(event);
    }

    /** Bouton “Annuler” : on retourne à la vue profile sans enregistrer */
    @FXML
    private void onCancel(ActionEvent event) {
        goToProfile(event);
    }

    /** Bouton bas “← Retour” : on retourne à l’accueil admin */
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

    /** Méthode utilitaire pour charger ProfileAdmin.fxml */
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
