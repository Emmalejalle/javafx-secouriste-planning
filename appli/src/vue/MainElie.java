import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainElie.java
 * classe principale de l'application JavaFX.
 * Elle charge l'interface utilisateur à partir d'un fichier FXML et affiche la fenêtre principale
 */
public class MainElie extends Application {
    
    /**
     * Démarre l'application JavaFX.
     * Charge l'interface utilisateur à partir d'un fichier FXML et affiche la fenêtre principale.
     * 
     * @param stage - la fenêtre principale de l'application.
     * @throws Exception si une erreur survient lors du chargement du fichier FXML.
     */
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vue/ProfileAdmin.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Profile.fxml"));
        stage.setTitle("Liste des secouristes");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Lance l'application JavaFX.
     * 
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
