package service;

import modele.DAO.UserDAO;
import modele.persistence.User;
import java.sql.SQLException;

/**
 * Service de gestion du profil utilisateur.
 * Ne fait appel qu'au DAO pour persister les modifications,
 * sans exposer le DAO aux contrôleurs.
 */
public class ProfilMngt {

    private final UserDAO userDao = new UserDAO();

    /**
     * Charge l'utilisateur depuis la base de données.
     * @param id identifiant de l'utilisateur
     * @return l'utilisateur ou null si non trouvé
     * @throws SQLException en cas d'erreur d'accès à la BDD
     */
    public User loadUserById(long id) throws SQLException {
        return userDao.findByID(id);
    }

    /**
     * Met à jour uniquement les informations de profil de l'utilisateur
     * (sans toucher à ses compétences ni disponibilités).
     * Utilise la méthode updateProfil du DAO.
     * @param user objet User avec les nouveaux champs à mettre à jour
     * @throws SQLException en cas d'erreur d'accès à la BDD
     */
    public void updateProfil(User user) throws SQLException {
        userDao.updateProfil(user);
    }

    /**
     * Crée un nouvel utilisateur (Admin ou Secouriste).
     * @param user objet User à créer
     * @throws SQLException en cas d'erreur d'accès à la BDD
     */
    public void createUser(User user) throws SQLException {
        userDao.create(user);
    }

    /**
     * Supprime un utilisateur de la base.
     * @param user objet User à supprimer
     * @throws SQLException en cas d'erreur d'accès à la BDD
     */
    public void deleteUser(User user) throws SQLException {
        userDao.delete(user);
    }
}
