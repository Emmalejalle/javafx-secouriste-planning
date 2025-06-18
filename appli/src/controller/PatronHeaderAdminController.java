package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


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

    @FXML public void initialize() {
        // ici on pourrait récupérer le nom de la page et l'affecter dynamiquement :
        // lblTitre.setText(...);
    }

    @FXML
    private void onHome(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilAdmin.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onSecouriste(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilSecouriste.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onDPS(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/planning.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onCompetences(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/competences.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onDisponibilites(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/disponibilites.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onProfil(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/profilAdmin.fxml"));
        ((Node)e.getSource()).getScene().setRoot(root);
    }

    @FXML private void onQuit(ActionEvent e) throws Exception {
        // Fermer l'application
        System.exit(0);
    }
}
