package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modele.DAO.UserDAO;
import modele.SessionManager;
import modele.persistence.Admin;
import modele.persistence.Secouriste;
import modele.persistence.User;
import java.sql.SQLException;
import java.util.List;

public class ConnexionController extends BaseController {

    @FXML private TextField tfEmailSecourist;
    @FXML private PasswordField pfPasswordSecourist;
    @FXML private TextField tfEmailAdmin;
    @FXML private PasswordField pfPasswordAdmin;

    private UserDAO userDAO;

    public ConnexionController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    public void onConnexionSecouriste(ActionEvent event) {
        validerConnexion(event, tfEmailSecourist.getText(), pfPasswordSecourist.getText(), false);
    }

    @FXML
    public void onConnexionAdmin(ActionEvent event) {
        validerConnexion(event, tfEmailAdmin.getText(), pfPasswordAdmin.getText(), true);
    }

    private void validerConnexion(ActionEvent event, String email, String mdp, boolean isAdminRole) {
        if (email.isEmpty() || mdp.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }
        try {
            List<User> users = userDAO.findAll();
            for(  User user : users) {
                System.out.println("Utilisateur trouvé : " + user.getEmail());
            }
            User userTrouve = users.stream()
                .filter(user -> user.getEmail().equals(email) && user.getMdp().equals(mdp))
                .findFirst()
                .orElse(null);

            if (userTrouve != null) {
                if ((isAdminRole && userTrouve instanceof Admin) || (!isAdminRole && userTrouve instanceof Secouriste)) {
                    SessionManager.getInstance().setCurrentUser(userTrouve);
                    String nextPage = isAdminRole ? "/vue/accueilAdmin.fxml" : "/vue/accueilSecouriste.fxml";
                    changeView(event, nextPage);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de rôle", "Vous tentez de vous connecter sur le mauvais portail.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Email ou mot de passe incorrect.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur Base de Données", "Impossible de contacter la base de données. Vérifiez qu'elle est bien lancée et accessible.");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onMdpOublie(ActionEvent event) {
        System.out.println("Clic sur Mot de passe oublié");
        // Logique pour la récupération de mdp
    }
    
    @FXML
    public void onQuitter(ActionEvent event) {
        Platform.exit();
    }
}