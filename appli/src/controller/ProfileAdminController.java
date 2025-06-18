package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import modele.persistence.User;
import modele.SessionManager;
import service.ProfilMngt;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileAdminController implements Initializable {

    // Les labels de valeur
    @FXML private Label lblName;
    @FXML private Label lblPhoneValue;
    @FXML private Label lblEmailValue;
    @FXML private Label lblStatusValue;
    @FXML private Label lblIdValue;
    @FXML private Label lblAddressValue;
    @FXML private Label lblBirthValue;
    @FXML private Label lblCreatedValue;
    @FXML private Label lblPwdValue;

    // Vos boutons
    @FXML private Button btnModifyProfile;
    @FXML private Button btnReturn;

    private final ProfilMngt profilMngt = new ProfilMngt();
    private final SessionManager session = SessionManager.getInstance();

    /**
     * Charge et affiche les informations de l'admin courant.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Récupère l'utilisateur en session
            User sessionUser = session.getCurrentUser();
            // (Optionnel) re-fetch depuis la BDD pour être sûr d'avoir toutes les données à jour :
            User user = profilMngt.loadUserById(sessionUser.getId());

            // Remplissage des champs
            lblName         .setText(user.getPrenom() + " " + user.getNom());
            lblPhoneValue   .setText(user.getTel());
            lblEmailValue   .setText(user.getEmail());
            lblStatusValue  .setText("Admin");  // ou user.isAdmin() ? "Admin":"Secouriste"
            lblIdValue      .setText(String.valueOf(user.getId()));
            lblAddressValue .setText(user.getAdresse());
            lblBirthValue   .setText(user.getDateNaissance());
            // Si vous avez un champ création de compte dans User, utilisez-le ici :
            lblCreatedValue .setText("01/01/2024"); 
            lblPwdValue     .setText("••••••••");
        } catch (SQLException e) {
            e.printStackTrace();
            // Vous pouvez afficher une alerte à l'utilisateur en cas d'erreur
        }
    }

    /**
     * Passe en mode édition (charge la vue ProfileAdminModif.fxml).
     */
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

    /**
     * Retourne à l'accueil Admin sans sauvegarder.
     */
    @FXML
    void onReturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            System.err.println("ERREUR : Impossible de charger accueilAdmin.fxml");
            e.printStackTrace();
        }
    }
}
