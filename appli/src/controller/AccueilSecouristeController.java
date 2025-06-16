package controller;

import java.io.IOException;

import org.w3c.dom.Node;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AccueilSecouristeController {

    @FXML private Button btnPlanningGlobal;
    @FXML private Button btnDisponibilite;
    
    @FXML
    void goPlanning(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers planningSecou.fxml");
        try {
            changerDeVue(event, "planningSecou.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue planningSecou.fxml");
            e.printStackTrace();
        }
    }

    @FXML
    void goDisponibilite(ActionEvent event) {
        System.out.println("Clic sur Retour. Chargement vers planningSecou.fxml");
        try {
            changerDeVue(event, "planningSecou.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue planningSecou.fxml");
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
