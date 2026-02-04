package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import modele.persistence.User;
import modele.persistence.Secouriste;
import modele.persistence.Competence;
import modele.SessionManager;
import service.ProfilMngt;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;
import controller.ControllerHeader;



/**
 * Controller pour la page de profil (profil.fxml).
 */
public class ProfilController {
    @FXML private Label lbNomPrenom;
    @FXML private Label lblEmail;
    @FXML private Label lblTel;
    @FXML private Label lblStatut;
    @FXML private Label lblUserId;
    @FXML private Label lblMdp;
    @FXML private Label lblAdresse;
    @FXML private Label lblDateNaissance;
    @FXML private Pane headerPlaceholder;


    @FXML private VBox certBox;               // conteneur pour afficher les compétences
    @FXML private Button btnModifierProfil;
    @FXML private Button btnRetour;

    private final ProfilMngt profilMngt = new ProfilMngt();
    private final SessionManager session = SessionManager.getInstance();
    

    /**
     * Initialise la page de profil :
     * 1) charge le header commun
     * 2) charge l'utilisateur courant depuis la base
     * 3) remplit les champs avec les données de l'utilisateur
     * 4) affiche les compétences si c'est un secouriste
     * 5) active les boutons
     */
    @FXML
    public void initialize() {

        // Charger le header
        chargerEtInsererHeader("/vue/PatronHeaderSecouriste.fxml", "Mon Profil Secouriste");



        // 1) Charger l'utilisateur courant depuis la base
        User user;
        try {
            long id = session.getCurrentUser().getId();
            user = profilMngt.loadUserById(id);
            if (user == null) {
                throw new IllegalStateException("Utilisateur introuvable en BDD : " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors du chargement du profil", e);
        }

        // 2) Remplir les champs
        lbNomPrenom      .setText(user.getNom() + " " + user.getPrenom() + " (id : " + user.getId() + ")");
        lblEmail         .setText(user.getEmail());
        lblTel           .setText(user.getTel());
        lblStatut        .setText(user instanceof Secouriste ? "Secouriste" : "Admin");
        lblUserId        .setText(String.valueOf(user.getId()));
        lblMdp           .setText(user.getMdp());
        lblAdresse       .setText(user.getAdresse());
        lblDateNaissance .setText(user.getDateNaissance());

        // 3) Afficher les compétences si c'est un secouriste
        if (user instanceof Secouriste) {
            Secouriste sec = (Secouriste) user;
            certBox.getChildren().clear();
            for (Competence comp : sec.getCompetences()) {
                Button b = new Button(comp.getIntitule());
                // (optionnel) appliquer un style :
                b.getStyleClass().add("cert-button-outline");
                certBox.getChildren().add(b);
            }
        }

        // 4) Boutons
        btnModifierProfil.setOnAction(this::onModifierProfil);
        btnRetour         .setOnAction(this::onRetour);
    }

    /**
     * Charge le header commun depuis le fichier FXML donné et l'injecte
     * dans la placeholder correspondante.
     * Met également le titre du header.
     * 
     * @param fxmlPath - le chemin relatif du fichier FXML contenant le header.
     * @param titre - le titre à afficher dans le header.
     */
    private void chargerEtInsererHeader(String fxmlPath, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Region header = loader.load();
    
            // Récupérer le contrôleur et mettre le titre
            ControllerHeader ctrl = loader.getController();
            ctrl.setTitre(titre);
    
            headerPlaceholder.getChildren().clear();
            headerPlaceholder.getChildren().add(header);
    
            header.prefWidthProperty().bind(headerPlaceholder.widthProperty());
            header.prefHeightProperty().bind(headerPlaceholder.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Passe en mode édition du profil */
    
    /**
     * Méthode appelée lorsque le bouton "Modifier" est cliqué.
     * Charge la vue de modification du profil, transmet l'utilisateur actuel
     * au contrôleur de modification, puis remplace la scène courante par
     * la nouvelle vue.
     * 
     * @param event - l'événement lié au clic sur le bouton "Modifier".
     * @throws IOException si le fichier FXML n'est pas trouvé.
     */
    @FXML
    private void onModifierProfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/profilModif.fxml"));
            Parent root = loader.load();

            // On transmet l'utilisateur actuel au contrôleur de modification
            ProfilModifController ctrl = loader.getController();
            ctrl.initData(session.getCurrentUser());

            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            // (optionnel) alerte générique
        }
    }

    /** Retourne à l'accueil secouriste */
    /**
     * Méthode appelée lorsque le bouton "Retour" est cliqué.
     * Charge la vue d'accueil du secouriste et remplace la scène courante
     * par la nouvelle vue.
     * 
     * @param event - l'événement lié au clic sur le bouton "Retour".
     */
    @FXML
    private void onRetour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/accueilSecouriste.fxml"));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
