package modele.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modele.persistence.Journee;

/**
 * DAO concret pour l'entité 'Journee'.
 * Gère les opérations CRUD pour les objets Journee en utilisant des colonnes
 * séparées pour le jour, le mois et l'année.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class JourneeDAO extends DAO<Journee> {

    @Override
    public Journee findByID(long id) throws SQLException {
        String sql = "SELECT * FROM Journee WHERE idJournee = ?";
        Journee journee = null;

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    journee = new Journee(
                        rs.getLong("idJournee"),
                        rs.getInt("jour"),
                        rs.getInt("mois"),
                        rs.getInt("annee")
                    );
                }
            }
        }
        return journee;
    }

    @Override
    public ArrayList<Journee> findAll() throws SQLException {
        ArrayList<Journee> journees = new ArrayList<>();
        // On trie par année, puis par mois, puis par jour pour un ordre chronologique.
        String sql = "SELECT * FROM Journee ORDER BY annee, mois, jour";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                journees.add(new Journee(
                    rs.getLong("id"),
                    rs.getInt("jour"),
                    rs.getInt("mois"),
                    rs.getInt("annee")
                ));
            }
        }
        return journees;
    }

    @Override
    public int create(Journee obj) throws SQLException {
        String sql = "INSERT INTO Journee (jour, mois, annee) VALUES (?, ?, ?)";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, obj.getJour());
            st.setInt(2, obj.getMois());
            st.setInt(3, obj.getAnnee());
            
            int rowsAffected = st.executeUpdate();

            // Met à jour l'objet avec l'ID généré par la BDD
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        obj.setId(generatedKeys.getLong(1));
                    }
                }
            }
            return rowsAffected;
        }
    }

    @Override
    public int update(Journee obj) throws SQLException {
        String sql = "UPDATE Journee SET jour = ?, mois = ?, annee = ? WHERE idJournee = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setInt(1, obj.getJour());
            st.setInt(2, obj.getMois());
            st.setInt(3, obj.getAnnee());
            st.setLong(4, obj.getId());
            return st.executeUpdate();
        }
    }

    @Override
    public int delete(Journee obj) throws SQLException {
        // Pour que cela fonctionne, la table de jointure des disponibilités
        // doit avoir ON DELETE CASCADE sur la clé étrangère idJournee.
        String sql = "DELETE FROM Journee WHERE idJournee = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getId());
            return st.executeUpdate();
        }
    }
}
