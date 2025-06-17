package vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainErvenn extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(getClass().getResource("/vue/ProfileAdmin.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/vue/Connexion.fxml"));
        stage.setTitle("Liste des secouristes");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
