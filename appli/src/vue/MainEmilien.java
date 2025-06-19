import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.transform.Scale;
import javafx.beans.value.ObservableValue;

// Importer les classes nécessaires pour l'application JavaFX

/**
 * MainEmilien.java
 * Classe principale de l'application JavaFX.
 * Elle charge l'interface utilisateur à partir d'un fichier FXML et affiche la fenêtre principale.
 */
public class MainEmilien extends Application {
        /**
         * Démarre l'application JavaFX.
         * Charge le fichier FXML de l'interface utilisateur, configure la scène et le stage,
         * et affiche la fenêtre principale.
         * Les listeners sont définis pour adapter la taille de la fenêtre en fonction de la taille de la fenêtre.
         * 
         * @param stage - La fenêtre principale de l'application.
         * @throws IOException Si le fichier FXML ne peut pas être chargé.
         */
    @Override
    public void start(Stage stage) throws Exception {
        double width = 1920; // Largeur de la fenêtre initiale (prévue dans le fxml)
        double height = 1080; // Hauteur de la fenêtre initiale (prévue dans le fxml)

        // Charger le fichier FXML pour l'interface utilisateur
        System.out.println(getClass().getResource("/vue/PatronHeaderAdmin.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Connexion.fxml"));

        // Configurer la scène et le stage
        Scene scene = new Scene(root);

        // Définir le style de la fenêtre
        stage.setTitle("Liste des secouristes");
        stage.setScene(scene);
        stage.show();

        // Changement de la taille de la l'application en fonction de la taille de la fenêtre

        Scale scaleTransform = new Scale(1, 1);
        root.getTransforms().add(scaleTransform);

        // Listener sur la largeur de la fenêtre
        scene.widthProperty().addListener((ObservableValue<? extends Number> obs, Number oldWidth, Number newWidth) -> {
            double scaleX = newWidth.doubleValue() / width;
            scaleTransform.setX(scaleX);
        });

        // Listener sur la hauteur de la fenêtre
        scene.heightProperty().addListener((ObservableValue<? extends Number> obs, Number oldHeight, Number newHeight) -> {
            double scaleY = newHeight.doubleValue() / height;
            scaleTransform.setY(scaleY);
        });
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
