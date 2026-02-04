package modele.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modele.persistence.Sport;

/**
 * DAO concret pour l'entité 'Sport'.
 * Implémente les opérations CRUD pour les objets Sport.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class SportDAO extends DAO<Sport> {

    /**
     * Récupère un objet Sport à partir de son identifiant.
     * @param id L'identifiant du sport à trouver (codeSport).
     * @return L'objet Sport trouvé, ou null s'il n'existe pas.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public Sport findByID(long id) throws SQLException {
        // La clé primaire de la table Sport est 'code' de type BIGINT.
        String sql = "SELECT * FROM Sport WHERE codeSport = ?";
        Sport sport = null;

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    sport = new Sport(
                        rs.getLong("codeSport"),
                        rs.getString("nomSport")
                    );
                }
            }
        }
        return sport;
    }
    
    /**
     * Récupère la liste de tous les sports.
     * @return Une liste de tous les objets Sport.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public ArrayList<Sport> findAll() throws SQLException {
        ArrayList<Sport> sports = new ArrayList<>();
        String sql = "SELECT * FROM Sport ORDER BY nomSport";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sports.add(new Sport(
                    rs.getLong("codeSport"),
                    rs.getString("nomSport")
                ));
            }
        }
        return sports;
    }

    /**
     * Récupère la liste de tous les sports, triée par nom.
     * @return Une liste de tous les objets Sport.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int create(Sport obj) throws SQLException {
        String sql = "INSERT INTO Sport (codeSport, nomSport) VALUES (?, ?)";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getCode());
            st.setString(2, obj.getNom());
            return st.executeUpdate();
        }
    }

    /**
     * Met à jour un objet Sport dans la base de données.
     * @param obj L'objet Sport à mettre à jour.
     * @return Le nombre de lignes affectées par la mise à jour.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int update(Sport obj) throws SQLException {
        // On ne met à jour que le nom, car le code est la clé primaire.
        String sql = "UPDATE Sport SET nom = ? WHERE codeSport = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setString(1, obj.getNom());
            st.setLong(2, obj.getCode());
            return st.executeUpdate();
        }
    }

    /**
     * Supprime un objet Sport de la base de données.
     * @param obj L'objet Sport à supprimer.
     * @return Le nombre de lignes supprimées.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int delete(Sport obj) throws SQLException {
        // Assurez-vous que ON DELETE CASCADE est configuré pour la table DPS
        // qui utilise le code du sport comme clé étrangère.
        String sql = "DELETE FROM Sport WHERE codeSport = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getCode());
            return st.executeUpdate();
        }
    }
}