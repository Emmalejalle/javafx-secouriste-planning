package modele.DAO;

import modele.persistence.Competence;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * DAO concret pour l'entité 'Competence'.
 * Implémente les opérations CRUD pour les objets Competence.
 * La clé primaire 'idComp' n'étant pas auto-générée, la méthode create est directe.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class CompetenceDAO extends DAO<Competence> {

    /*
     * Main method for testing the CompetenceDAO class.
     * This method is not part of the DAO interface and is used for demonstration purposes.
     * It creates a new CompetenceDAO instance and performs some basic operations.
     * @param args Command line arguments (not used).
     * @throws SQLException If there is an error accessing the database.
     * @throws ClassNotFoundException If the JDBC driver class is not found.
     * @deprecated This method is for testing purposes only and should not be used in production code.
     * It is recommended to use the methods provided by the DAO interface instead.
     * @see modele.DAO.DAO
     * @see modele.persistence.Competence
     * @see modele.DAO.MyConnection
     * @see modele.DAO.CompetenceDAO
     * @see modele.DAO.DAO#findByID(long)
     * @see modele.DAO.DAO#findAll()        
     */    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        CompetenceDAO competenceDAO = new CompetenceDAO();
        /** 
        // Test findByID
        Competence competence = competenceDAO.findByID(1);
        if (competence != null) {
            System.out.println("Compétence trouvée : " + competence.getIntitule());
        } else {
            System.out.println("Aucune compétence trouvée avec cet ID.");
        }

        // Test findAll
        List<Competence> competences = competenceDAO.findAll();
        System.out.println("Liste de toutes les compétences :");
        for (Competence c : competences) {
            System.out.println(c.getIntitule());
            System.out.println("Pré-requis : ");
            for (Competence prerequis : c.getPrerequis()) {
                System.out.println(" - " + prerequis.getIntitule());
            }
        }
        // Test create 
        
        ArrayList<Competence> prerequis = new ArrayList<>();
        prerequis.add(new Competence(1, "Cadre Opérationnel", "CO"));
        Competence newCompetence = new Competence(0, "Nouvelle Compétence", "NC");        
        newCompetence.setPrerequis(prerequis);
        System.out.println("Création de la compétence : " + newCompetence.getIntitule());
        competenceDAO.create(newCompetence);
        System.out.println("Compétence crée : " + newCompetence.getIntitule());
        System.out.println("ID de la compétence créée : " + newCompetence.getIdComp());
        */
        // Test update
        
        CompetenceDAO cDao = new CompetenceDAO();
        Competence competence = new Competence(12, "Feu", "Feur");
        Competence competence2 = new Competence( 13, "Coubeh", "abc");
        Competence competence3 = new Competence( 14, "Crampte", "url");
        cDao.create(competence2);
        ArrayList<Competence> prerequis = new ArrayList<>();
        prerequis.add(competence2);
        competence.setPrerequis(prerequis);
        cDao.create(competence);
        cDao.create(competence3);

        ArrayList<Competence> all = (ArrayList<Competence>) cDao.findAll();

        for (Competence c : all) {
            System.out.println("la comp : " + c.toString());   
        }

        prerequis.add(competence3);
        competence.setPrerequis(prerequis);
        cDao.update(competence);

        ArrayList<Competence> all2 = (ArrayList<Competence>) cDao.findAll();

        for (Competence c : all2) {
            System.out.println("la comp : " + c.toString());
        }
    }

    /**
     * Findprerequis renvoie les prerequis d'une competence sous forme d'une liste de Long (IDs des compétences).
     * @param idComp L'identifiant de la compétence dont on veut les prérequis.
     * @return Une liste de Long représentant les IDs des compétences prérequis.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public ArrayList<Long> findPrerequis(Competence competence) throws SQLException {
        ArrayList<Long> prerequisIds = new ArrayList<>();
        String sql = "SELECT idPrerequis FROM PrerequisComp WHERE idCompPre = ?";

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, competence.getIdComp());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    prerequisIds.add(rs.getLong("idPrerequis"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des prérequis : " + e.getMessage());
        }
        return prerequisIds;
    }

    /**
     * findAll récupère toutes les compétences de la base de données.
     * Cette méthode est utilisée pour obtenir une liste de toutes les compétences, y compris leurs prérequis.
     * @return Une liste de toutes les compétences.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public ArrayList<Competence> findAll() throws SQLException {
        // TreeMap pour stocker les competences et leur identifiant
        // dans l'ordre de l'intitulé
        TreeMap<Long, Competence> competencesTreeMap = new TreeMap<>();


        // Utilisation d'un ArrayList pour stocker les compétences
        // et les retourner dans l'ordre de l'intitulé
        ArrayList<Competence> competences = new ArrayList<>();
        String sql = "SELECT * FROM Competence ORDER BY intitule";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Création d'une nouvelle competence
                Competence tmp = new Competence(
                    rs.getLong("idComp"),
                    rs.getString("intitule"),
                    rs.getString("abreviationIntitule")
                );
                competencesTreeMap.put(tmp.getIdComp(), tmp);
            }

            // Pour chaque compétence, on récupère les prérequis
            for (Competence competence : competencesTreeMap.values()) {
                // On récupère les IDs des prérequis pour cette compétence
                // en appelant la méthode findPrerequis
                // qui renvoie une liste de Long (IDs des compétences prérequis)
                ArrayList<Long> prerequisIds = findPrerequis(competence);

                // Pour chaque ID de prérequis, on récupère la compétence correspondante dans le TreeMap
                ArrayList<Competence> prerequis = new ArrayList<>();

                // On parcourt les IDs des prérequis
                // et on ajoute les compétences correspondantes à la liste des prérequis
                for (Long prerequisId : prerequisIds) {
                    Competence prerequisCompetence = competencesTreeMap.get(prerequisId);
                    if (prerequisCompetence != null) {
                        prerequis.add(prerequisCompetence);
                    }
                }

                // On ajoute les prérequis à la compétence
                competence.setPrerequis(prerequis);
                // On ajoute la compétence à la liste finale
                competences.add(competence);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compétences : " + e.getMessage());
        }
        return competences;
    }

    /**
     * FindByID recherche une compétence par son identifiant.
     * @param id L'identifiant de la compétence à rechercher.
     * @return La compétence trouvée, ou null si aucune compétence n'est trouvée avec cet identifiant.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public Competence findByID(long id) throws SQLException {
        String sql = "SELECT * FROM Competence WHERE idComp = ?";
        Competence competence = null;

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    competence = new Competence(
                        rs.getLong("idComp"),
                        rs.getString("intitule"),
                        rs.getString("abreviationIntitule")
                    );
                }
            } 
        }
        return competence;
    }

    /**
     * findAllParresseux récupère toutes les compétences de la base de données.
     * Cette méthode est utilisée pour obtenir une liste de toutes les compétences sans charger les prérequis.
     * @return Une liste de toutes les compétences.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public ArrayList<Competence> findAllParresseux() throws SQLException {
        ArrayList<Competence> competences = new ArrayList<>();
        String sql = "SELECT * FROM Competence ORDER BY intitule";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                competences.add(new Competence(
                    rs.getLong("idComp"),
                    rs.getString("intitule"),
                    rs.getString("abreviationIntitule")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des compétences : " + e.getMessage());
        }
        return competences;
    }

    /**
     * Insère une nouvelle compétence dans la base de données.
     * @param obj La compétence à insérer.
     * @return Le nombre de lignes affectées par l'insertion.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     * La création ne gère pas les prérequis, cela doit être fait séparément
     * en utilisant la méthode createPrerequis.
     */
    @Override
    public int create(Competence obj) throws SQLException {
        int rowsAffected = 0;
        // La création ne gère pas les prérequis, cela doit être fait séparément
        String sql = "INSERT INTO Competence (intitule, abreviationIntitule) VALUES (?, ?)";

     
        
        try (PreparedStatement st = this.connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, obj.getIntitule());
            st.setString(2, obj.getAbrevComp());
            rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        obj.setIdComp(generatedKeys.getLong(1));
                    }
                }
            }
            
        }
           // Insert les prerequis dans la table de jointure
        for (Competence prerequis : obj.getPrerequis()) {
            createPrerequis(obj, prerequis);
        }

        return rowsAffected;
    }

    /**
     * Crée une relation de prérequis entre deux compétences.
     * @param competence La compétence principale.
     * @param prerequis La compétence qui est un prérequis.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void createPrerequis(Competence competence, Competence prerequis) throws SQLException {
        String sql = "INSERT INTO PrerequisComp (idCompPre, idPrerequis) VALUES (?, ?)";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, competence.getIdComp());
            st.setLong(2, prerequis.getIdComp());
            st.executeUpdate();
        }
    }

    /**
     * Supprime une relation de prérequis entre deux compétences.
     * @param competence La compétence principale.
     * @param prerequis La compétence qui est un prérequis.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void deletePrerequis(Competence competence, Competence prerequis) throws SQLException {
        String sql = "DELETE FROM PrerequisComp WHERE idCompPre = ? AND idPrerequis = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, competence.getIdComp());
            st.setLong(2, prerequis.getIdComp());
            st.executeUpdate();
        }
    }

    /**
     * Met à jour une compétence et ses prérequis de manière sécurisée.
     * @param obj L'objet Competence contenant les nouvelles informations.
     * @return 1 en cas de succès, 0 sinon.
     * @throws SQLException En cas d'erreur de base de données.
     */
    @Override
    public int update(Competence obj) throws SQLException {
        try {
            // Début de la transaction pour garantir que toutes les opérations réussissent
            this.connect.setAutoCommit(false);

            // Étape 1 : Mettre à jour les informations de base de la compétence
            // On utilise UPDATE pour ne pas changer l'ID de la compétence.
            String sqlComp = "UPDATE Competence SET intitule = ?, abreviationIntitule = ? WHERE idComp = ?";
            try (PreparedStatement st = this.connect.prepareStatement(sqlComp)) {
                st.setString(1, obj.getIntitule());
                st.setString(2, obj.getAbrevComp());
                st.setLong(3, obj.getIdComp());
                st.executeUpdate();
            }

            // Étape 2 : Supprimer tous les anciens liens de prérequis pour cette compétence
            String sqlDeleteLinks = "DELETE FROM PrerequisComp WHERE idCompPre = ?";
            try (PreparedStatement st = this.connect.prepareStatement(sqlDeleteLinks)) {
                st.setLong(1, obj.getIdComp());
                st.executeUpdate();
            }

            // Étape 3 : Insérer les nouveaux liens de prérequis
            String sqlInsertLinks = "INSERT INTO PrerequisComp (idCompPre, idPrerequis) VALUES (?, ?)";
            try (PreparedStatement st = this.connect.prepareStatement(sqlInsertLinks)) {
                for (Competence prerequis : obj.getPrerequis()) {
                    st.setLong(1, obj.getIdComp());
                    st.setLong(2, prerequis.getIdComp());
                    st.addBatch();
                }
                st.executeBatch();
            }
            
            // Si tout s'est bien passé, on valide la transaction
            this.connect.commit();
            return 1;

        } catch (SQLException e) {
            // En cas d'erreur, on annule tout ce qui a été fait
            this.connect.rollback();
            throw e; // On propage l'erreur
        } finally {
            // On réactive le mode par défaut
            this.connect.setAutoCommit(true);
        }
    }

    /**
     * Supprime une compétence de la base de données.
     * La suppression doit gérer les contraintes de clé étrangère,
     * il est donc important de configurer la table de jointure des prérequis
     * avec ON DELETE CASCADE sur la clé étrangère idComp.
     * @param obj La compétence à supprimer.
     * @return Le nombre de lignes supprimées (normalement 1).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int delete(Competence obj) throws SQLException {
        // Attention: la suppression doit gérer les contraintes de clé étrangère
        // Il faut d'abord supprimer les liens dans la table de jointure des prérequis.
        String sql = "DELETE FROM Competence WHERE idComp = ?"; 
        // Supprime la compétence de la table principale
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getIdComp());
            return st.executeUpdate();
        }
    }
}
