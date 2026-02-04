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

/**
 * Contrôleur pour la vue Connexion.fxml
 * Gère les connexions des utilisateurs (secouristes et administrateurs).
 * Permet de valider les identifiants et de rediriger vers la page d'accueil appropriée.
 */
public class ConnexionController extends BaseController {

    @FXML private TextField tfEmailSecourist;
    @FXML private PasswordField pfPasswordSecourist;
    @FXML private TextField tfEmailAdmin;
    @FXML private PasswordField pfPasswordAdmin;

    private UserDAO userDAO;

    /**
     * Constructeur de ConnexionController.
     * Initialise le UserDAO pour accéder aux données des utilisateurs.
     */
    public ConnexionController() {
        this.userDAO = new UserDAO();
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Connexion" pour les secouristes.
     * Récupère le texte des champs de saisie email et mot de passe pour les secouristes,
     * et appelle la méthode validerConnexion avec isAdminRole = false.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    public void onConnexionSecouriste(ActionEvent event) {
        validerConnexion(event, tfEmailSecourist.getText(), pfPasswordSecourist.getText(), false);
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Connexion" pour les administrateurs.
     * Récupère le texte des champs de saisie email et mot de passe pour les administrateurs,
     * et appelle la méthode validerConnexion avec isAdminRole = true.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    public void onConnexionAdmin(ActionEvent event) {
        validerConnexion(event, tfEmailAdmin.getText(), pfPasswordAdmin.getText(), true);
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Connexion" pour les secouristes ou les administrateurs.
     * Vérifie que les champs de saisie email et mot de passe ne sont pas vides,
     * cherche l'utilisateur correspondant dans la base de données,
     * et si il est trouvé, connecte l'utilisateur et change la vue en conséquence.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     * @param email - L'email de l'utilisateur.
     * @param mdp - Le mot de passe de l'utilisateur.
     * @param isAdminRole - True si l'utilisateur se connecte comme administrateur, false sinon.
     */
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
    
    /**
     * Gère le clic sur le lien "Mot de passe oublié".
     * Change la vue en affichant la page de récupération de mot de passe.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    public void onMdpOublie(ActionEvent event) {
        System.out.println("Clic sur Mot de passe oublié");
        changeView(event, "/vue/MdpOublie.fxml");
        // Logique pour la récupération de mdp
    }
    
    /**
     * Ferme l'application.
     * 
     * @param event - L'événement ActionEvent déclenché par le bouton.
     */
    @FXML
    public void onQuitter(ActionEvent event) {
        Platform.exit();
    }
}