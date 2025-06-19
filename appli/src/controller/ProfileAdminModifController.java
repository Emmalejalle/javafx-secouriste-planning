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

import javafx.scene.control.Alert;

/**
 * Controller pour la page de modification du profil administrateur.
 */
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



    /**
     * Initialise le contrôleur.
     * Charge l'administrateur courant depuis la session + BDD,
     * l'affiche dans le champ de texte "Nom et prénom",
     * et remplit les champs de formulaire pour modification.
     */
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
            afficherErreur("Erreur lors du chargement des données.");
        }
    }

    /** Valide et enregistre en base */
    
    /**
     * Valide les modifications, met à jour la base et redirige vers la page de profil.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    private void onValidate(ActionEvent event) {
        // Téléphone
        String tel = TelValue.getText().trim();
        if (tel.matches("\\d{10}")) {
            tel = tel.replaceAll("(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3-$4-$5");
            TelValue.setText(tel);
        } else if (!tel.matches("\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}")) {
            afficherErreur("Le numéro de téléphone doit être 10 chiffres ou au format 00-00-00-00-00.");
            return;
        }

        // Date de naissance
        String date = AnnivValue.getText().trim();
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            afficherErreur("La date de naissance doit être au format JJ/MM/AAAA.");
            return;
        }

        // Email
        String email = tfEmailValue.getText().trim();
        if (!email.matches(".+@.+\\..+")) {
            afficherErreur("L'email semble invalide.");
            return;
        }

        // Appliquer les modifications
        currentUser.setTel(tel);
        currentUser.setEmail(email);
        currentUser.setAdresse(AdressValue.getText().trim());
        currentUser.setDateNaissance(date);
        currentUser.setMdp(MDPValue.getText().trim());

        try {
            profilMngt.updateProfil(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de la mise à jour du profil.");
            return;
        }

        goToProfile(event);
    }

    /** Annule sans enregistrer */
    /**
     * Annule les modifications et retourne à la page de profil sans enregistrer.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    private void onCancel(ActionEvent event) {
        goToProfile(event);
    }

    /** Retour à l’accueil Admin */
    /**
     * Retourne à l'écran d'accueil administrateur.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
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
    /**
     * Charge la vue ProfileAdmin.fxml et remplace la scène courante.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    private void goToProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdmin.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche une alerte avec le type ERROR, le titre "Erreur de saisie"
     * et le header "Erreur détectée", et le message indiqué.
     * 
     * @param message - le message à afficher dans l'alerte.
     */
    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText("Erreur détectée");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
