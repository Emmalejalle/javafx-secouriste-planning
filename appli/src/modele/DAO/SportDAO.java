package modele.DAO;

import modele.persistence.Sport;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * DAO concret pour l'entité 'Sport'.
 * Implémente les opérations CRUD pour les objets Sport.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class SportDAO extends DAO<Sport> {

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
                        rs.getLong("code"),
                        rs.getString("nom")
                    );
                }
            }
        }
        return sport;
    }
    
    @Override
    public ArrayList<Sport> findAll() throws SQLException {
        ArrayList<Sport> sports = new ArrayList<>();
        String sql = "SELECT * FROM Sport ORDER BY nomSport";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sports.add(new Sport(
                    rs.getLong("code"),
                    rs.getString("nom")
                ));
            }
        }
        return sports;
    }

    @Override
    public int create(Sport obj) throws SQLException {
        String sql = "INSERT INTO Sport (codeSport, nomSport) VALUES (?, ?)";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getCode());
            st.setString(2, obj.getNom());
            return st.executeUpdate();
        }
    }

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