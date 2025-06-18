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

    @FXML
    public void initialize() {
        // ici on pourrait récupérer le nom de la page et l'affecter dynamiquement
    }

    /**
     * Setter pour modifier dynamiquement le titre depuis le contrôleur parent
     */
    public void setTitre(String titre) {
        lblTitre.setText(titre);
    }

    @FXML
    private void onHome(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onSecouriste(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Modifier-supprimerUnSecouriste.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onDPS(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Modifier-supprimerUnDps.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onCompetences(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Modifier-supprimerUneCompétence.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onDisponibilites(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/AffectationDpsDeBase.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onProfil(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/ProfileAdmin.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onQuit(ActionEvent e) {
        System.exit(0);
    }
}
