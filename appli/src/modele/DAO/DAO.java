package modele.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe abstraite et générique pour les objets d'accès aux données (DAO).
 * Elle fournit la connexion à la base de données et définit les opérations
 * CRUD standard que chaque classe fille doit implémenter.
 *
 * @param <T> Le type de l'objet de persistance (ex: Competence, Secouriste).
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public abstract class DAO<T> {

    /**
     * La connexion à la base de données, partagée par les méthodes du DAO.
     */
    protected Connection connect = null;

    /**
     * Constructeur. Récupère l'instance de la connexion à la BDD via le singleton MyConnection.
     */
    public DAO() {
        try {
            this.connect = MyConnection.getMyConnection().getConnection();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    // --- Définition des méthodes CRUD abstraites ---

    /**
     * Récupère un objet à partir de son identifiant.
     * @param id L'identifiant de l'objet à trouver.
     * @return L'objet trouvé, ou null s'il n'existe pas.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public abstract T findByID(long id) throws SQLException;

    /**
     * Récupère la liste de tous les objets de ce type.
     * @return Une liste de tous les objets.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public abstract List<T> findAll() throws SQLException;

    /**
     * Crée une nouvelle entrée dans la base de données à partir d'un objet.
     * @param obj L'objet à créer.
     * @return true si la création a réussi, false sinon.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public abstract int create(T obj) throws SQLException;

    /**
     * Met à jour une entrée dans la base de données à partir d'un objet.
     * @param obj L'objet à mettre à jour.
     * @return true si la mise à jour a réussi, false sinon.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public abstract int update(T obj) throws SQLException;

    /**
     * Supprime une entrée dans la base de données à partir d'un objet.
     * @param obj L'objet à supprimer.
     * @return true si la suppression a réussi, false sinon.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public abstract int delete(T obj) throws SQLException;
}