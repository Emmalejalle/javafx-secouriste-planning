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

    /**
     * NOUVELLE MÉTHODE : Trouve une journée par sa date.
     * C'est la méthode à utiliser quand l'utilisateur entre une date dans l'interface.
     * @param jour Le jour à rechercher.
     * @param mois Le mois à rechercher.
     * @param annee L'année à rechercher.
     * @return L'objet Journee correspondant, ou null si la date n'existe pas dans la base.
     * @throws SQLException
     */
    public Journee findByDate(int jour, int mois, int annee) throws SQLException {
        String sql = "SELECT * FROM Journee WHERE jour = ? AND mois = ? AND annee = ?";
        Journee journee = null;

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setInt(1, jour);
            st.setInt(2, mois);
            st.setInt(3, annee);
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
    
    /**
     * Trouve une journée par son ID.
     * C'est la méthode à utiliser quand on a déjà l'ID d'une journée.
     * @param id L'ID de la journée à rechercher.
     * @return L'objet Journee correspondant, ou null si l'ID n'existe pas dans la base.
     * @throws SQLException
     */
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

    /**
     * Trouve toutes les jours de la base de données.
     * @return Une liste d'objets Journee contenant toutes les jours de la base de données.
     * @throws SQLException
     */
    @Override
    public ArrayList<Journee> findAll() throws SQLException {
        ArrayList<Journee> journees = new ArrayList<>();
        // On trie par année, puis par mois, puis par jour pour un ordre chronologique.
        String sql = "SELECT * FROM Journee ORDER BY annee, mois, jour";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                journees.add(new Journee(
                    rs.getLong("idJournee"),
                    rs.getInt("jour"),
                    rs.getInt("mois"),
                    rs.getInt("annee")
                ));
            }
        }
        return journees;
    }

    
    /**
     * Insère une nouvelle journée dans la base de données et met à jour l'objet Journee
     * avec l'ID généré par la base de données.
     * 
     * @param obj L'objet Journee à insérer.
     * @return Le nombre de lignes affectées par l'insertion.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
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

    /**
     * Met à jour les informations d'une journée dans la base de données.
     * @param obj L'objet Journee contenant les nouvelles informations.
     * @return Le nombre de lignes affectées par la mise à jour.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
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

    /**
     * Supprime une journée de la base de données.
     * La suppression ne gère pas les contraintes de clé étrangère, il est donc
     * important de configurer la table de jointure des disponibilités avec
     * ON DELETE CASCADE sur la clé étrangère idJournee.
     * @param obj L'objet Journee à supprimer.
     * @return Le nombre de lignes supprimées (normalement 1).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
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
