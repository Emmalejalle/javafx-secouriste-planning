package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.DayOfWeek;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollPane;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.geometry.Pos;
import java.time.YearMonth;

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
        System.out.println("Clic sur Retour. Chargement vers NotificationDisponibilite.fxml");
        try {
            changerDeVue(event, "NotificationDisponibilite.fxml");
        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger la vue NotificationDisponibilite.fxml");
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
