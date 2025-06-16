
package controller;

import java.io.IOException;

import org.w3c.dom.Node;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


class AccueilAdminController {

    @FXML private Button btnListeSecouristes;
    @FXML private Button btnGererSecouristes;
    @FXML private Button btnGererDPS;
    @FXML private Button btnAffecterDPS;

    @FXML
    void initialize() {
        System.out.println("Le contrôleur AccueilAdminController est initialisé.");
    }

    

    @FXML
    void goListeSecouriste(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers Modifier-supprimerUnSecouriste.fxml");
        try {
            changerDeVue(event, "Modifier-supprimerUnSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue Modifier-supprimerUnSecouriste.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    void goGererSecouriste(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers accueilSecouriste.fxml...");
        try {
            changerDeVue(event, "accueilSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue accueilSecouriste.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    void goGererDPS(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers accueilSecouriste.fxml...");
        try {
            changerDeVue(event, "accueilSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue accueilSecouriste.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    void goAffecterDPS(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers accueilSecouriste.fxml...");
        try {
            changerDeVue(event, "accueilSecouriste.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue accueilSecouriste.fxml");
            e.printStackTrace();
        }
    }


    /**
     * Patron pour changer de vue 
     * @param event quand c'est cliqué 
     * @param fxmlFileName la nouvelle vue 
     * @throws IOException erreur si pas le bon nom
     */
    private void changerDeVue(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vue/" + fxmlFileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}