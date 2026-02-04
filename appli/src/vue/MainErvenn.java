package vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainErvenn.java
 * Classe principale de l'application JavaFX.
 * Elle charge l'interface utilisateur à partir d'un fichier FXML et affiche la fenêtre principale.
 */
public class MainErvenn extends Application {
    /**
     * Démarre l'application JavaFX.
     * Charge l'interface utilisateur à partir d'un fichier FXML et affiche la fenêtre principale.
     * 
     * @param stage - La fenêtre principale de l'application.
     * @throws IOException Si le fichier FXML ne peut pas être chargé.
     */
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vue/ProfileAdmin.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Connexion.fxml"));
        stage.setTitle("Liste des secouristes");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Point d'entrée de l'application JavaFX.
     * 
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
