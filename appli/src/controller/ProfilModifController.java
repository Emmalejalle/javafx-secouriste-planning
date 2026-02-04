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
     * 
     * @param user L'utilisateur dont on modifie le profil.
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

    /**
     * Charge le header commun depuis le fichier FXML donné et l'injecte
     * dans la placeholder correspondante.
     * Met également le titre du header.
     * 
     * @param fxmlPath - le chemin relatif du fichier FXML contenant le header.
     * @param titre - le titre à afficher dans le header.
     */
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

    /** Valide les modifications, met à jour la base de données et revient à la vue profil.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    private void onValider(ActionEvent event) {
        // Séparation du nom / prénom
        String[] parts = txtNomPrenom.getText().trim().split("\\s+", 2);
        if (parts.length < 1 || parts[0].isEmpty()) {
            afficherErreur("Le nom est obligatoire.");
            return;
        }

        // Vérification du numéro de téléphone : format 00-00-00-00-00
        // Numéro de téléphone
        String tel = txtTel.getText().trim();
        if (tel.matches("\\d{10}")) {
            // Formater automatiquement
            tel = tel.replaceAll("(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3-$4-$5");
            txtTel.setText(tel);  // Met à jour le champ pour l'utilisateur
        } else if (!tel.matches("\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}")) {
            afficherErreur("Le numéro doit être 10 chiffres ou au format 00-00-00-00-00.");
            return;
        }

        // Vérification de la date de naissance : format JJ/MM/AAAA
        String dateNaissance = txtDateNaissance.getText().trim();
        if (!dateNaissance.matches("\\d{2}/\\d{2}/\\d{4}")) {
            afficherErreur("La date de naissance doit être au format JJ/MM/AAAA.");
            return;
        }

        // (Optionnel) Vérification email basique
        String email = txtEmail.getText().trim();
        if (!email.matches(".+@.+\\..+")) {
            afficherErreur("L'email semble invalide.");
            return;
        }

        // Si tout est OK : appliquer les données
        currentUser.setNom(parts[0]);
        currentUser.setPrenom(parts.length > 1 ? parts[1] : "");
        currentUser.setTel(tel);
        currentUser.setEmail(email);
        currentUser.setMdp(txtMdp.getText().trim());
        currentUser.setAdresse(txtAdresse.getText().trim());
        currentUser.setDateNaissance(dateNaissance);

        try {
            profilMngt.updateProfil(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
            afficherErreur("Erreur lors de la mise à jour du profil en base.");
            return;
        }

        // Retour si tout s'est bien passé
        onRetour(event);
    }
    
    /**
     * Annule les modifications et revient à la vue de profil sans enregistrer.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    private void onAnnuler(ActionEvent event) {
        onRetour(event);
    }

    /**
     * Affiche une alerte avec le type ERROR, le titre "Erreur de saisie"
     * et le header "Erreur détectée", et le message indiqué.
     * 
     * @param message - le message à afficher dans l'alerte.
     */
    private void afficherErreur(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText("Erreur détectée");
        alert.setContentText(message);
        alert.showAndWait();
    }
    

    /** Charge à nouveau `profil.fxml` et affiche le profil à jour. 
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
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
