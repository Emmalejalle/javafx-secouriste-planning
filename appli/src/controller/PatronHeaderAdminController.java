package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;

/**
 * Contrôleur pour l'en-tête de la vue administrateur.
 * Gère les actions des boutons de navigation et le titre de la page.
 */
public class PatronHeaderAdminController {

    @FXML private Button btnHome;
    @FXML private Button btnSecouriste;
    @FXML private Button btnDPS;
    @FXML private Button btnComp;
    @FXML private Button btnDispo;
    @FXML private Button btnApp;
    @FXML private Label  lblTitre;
    @FXML private Button btnProfil;
    @FXML private Button btnQuit;

    /**
     * Méthode appelée après chargement du FXML.
     * Ici on pourrait récupérer le nom de la page et l'affecter dynamiquement
     * au label lblTitre.
     */
    @FXML
    public void initialize() {
        // ici on pourrait récupérer le nom de la page et l'affecter dynamiquement
    }

    /**
     * Setter pour modifier dynamiquement le titre depuis le contrôleur parent.
     * 
     * @param titre - Le titre à afficher dans lblTitre.
     */
    public void setTitre(String titre) {
        lblTitre.setText(titre);
    }

    /**
     * Méthode appelée quand le bouton "Accueil" est cliqué.
     * Charge la vue accueilAdmin.fxml et l'affiche.
     * 
     * @param e - l'événement lié au clic sur le bouton "Accueil".
     * @throws IOException si le fichier FXML n'est pas trouvé.
     */
    @FXML
    private void onHome(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    /**
     * Méthode appelée quand le bouton "Secouriste" est cliqué.
     * Charge la vue Modifier-supprimerUnSecouriste.fxml et l'affiche.
     * 
     * @param e - l'événement lié au clic sur le bouton "Secouriste".
     * @throws IOException si le fichier FXML n'est pas trouvé.
     */
    @FXML
    private void onSecouriste(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Modifier-supprimerUnSecouriste.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    /**
     * Méthode appelée quand le bouton "DPS" est cliqué.
     * Charge la vue Modifier-supprimerUnDps.fxml et l'affiche.
     * 
     * @param e - l'événement lié au clic sur le bouton "DPS".
     * @throws IOException si le fichier FXML n'est pas trouvé.
     */
    @FXML
    private void onDPS(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Modifier-supprimerUnDps.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    /**
     * Méthode appelée quand le bouton "Compétences" est cliqué.
     * Charge la vue Modifier-supprimerUneCompétence.fxml et l'affiche.
     * 
     * @param e - l'événement lié au clic sur le bouton "Compétences".
     * @throws IOException si le fichier FXML n'est pas trouvé.
     */
    @FXML
    private void onCompetences(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Modifier-supprimerUneCompétence.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    /**
     * Méthode appelée quand le bouton "Disponibilités" est cliqué.
     * Charge la vue AffectationDpsDeBase.fxml et l'affiche.
     * 
     * @param e - l'événement lié au clic sur le bouton "Disponibilités".
     * @throws IOException si le fichier FXML n'est pas trouvé.
     */
    @FXML
    private void onDisponibilites(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/AffectationDpsDeBase.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    /**
     * Méthode appelée quand le bouton "Mon profil" est cliqué.
     * Charge la vue ProfileAdmin.fxml et l'affiche.
     * 
     * @param e - l'événement lié au clic sur le bouton "Mon profil".
     * @throws IOException si le fichier FXML n'est pas trouvé.
     */
    @FXML
    private void onProfil(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdmin.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    /**
     * Quitte l'application.
     * 
     * @param e - l'événement lié au clic sur le bouton "Quitter".
     */
    @FXML
    private void onQuit(ActionEvent e) {
        System.exit(0);
    }
}
