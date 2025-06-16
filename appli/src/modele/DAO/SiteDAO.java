package modele.DAO;

import modele.persistence.Site;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * DAO concret pour l'entité 'Site'.
 * Implémente les opérations CRUD pour les objets Site.
 * La clé primaire 'code' n'étant pas auto-générée, la méthode create est directe.
 */
public class SiteDAO extends DAO<Site> {

    /**
     * Récupère un site à partir de son identifiant.
     * @param id L'identifiant du site à trouver.
     * @return Le site trouvé, ou null s'il n'existe pas.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public Site findByID(long id) throws SQLException {
        String sql = "SELECT * FROM Site WHERE codeSite = ?";
        Site site = null;

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    site = new Site(
                        rs.getLong("code"),
                        rs.getString("nom"),
                        rs.getFloat("longitude"),
                        rs.getFloat("latitude")
                    );
                }
            }
        }
        return site;
    }

    /**
     * Récupère la liste de tous les sites.
     * @return Une liste de tous les sites.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public ArrayList<Site> findAll() throws SQLException {
        ArrayList<Site> sites = new ArrayList<>();
        String sql = "SELECT * FROM Site ORDER BY nomSite";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sites.add(new Site(
                    rs.getLong("code"),
                    rs.getString("nom"),
                    rs.getFloat("longitude"),
                    rs.getFloat("latitude")
                ));
            }
        }
        return sites;
    }

    /**
     * Crée une nouvelle entrée dans la base de données à partir d'un objet Site.
     * Comme 'code' est une clé primaire non auto-générée, on l'insère directement.
     * @param obj L'objet Site à créer.
     * @return Le nombre de lignes affectées par l'insertion.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int create(Site obj) throws SQLException {
        // Comme 'code' est une clé primaire non auto-générée, on l'insère directement.
        String sql = "INSERT INTO Site (codeSite, nomSite, longitude, latitude) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getCode());
            st.setString(2, obj.getNom());
            st.setFloat(3, obj.getLongitude());
            st.setFloat(4, obj.getLatitude());
            return st.executeUpdate();
        }
    }

    /**
     * Met à jour un site dans la base de données.
     * @param obj L'objet Site à mettre à jour.
     * @return Le nombre de lignes affectées par la mise à jour.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int update(Site obj) throws SQLException {
        String sql = "UPDATE Site SET nomSite = ?, longitude = ?, latitude = ? WHERE codeSite = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setString(1, obj.getNom());
            st.setFloat(2, obj.getLongitude());
            st.setFloat(3, obj.getLatitude());
            st.setLong(4, obj.getCode()); // Le 'code' est dans la clause WHERE
            return st.executeUpdate();
        }
    }

    /**
     * Supprime un site de la base de données.
     * @param obj L'objet Site à supprimer.
     * @return Le nombre de lignes affectées par la suppression.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int delete(Site obj) throws SQLException {
        // Assurez-vous que ON DELETE CASCADE est configuré pour les tables qui utilisent
        // le code du site comme clé étrangère (ex: la table DPS).
        String sql = "DELETE FROM Site WHERE codeSite = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getCode());
            return st.executeUpdate();
        }
    }
}