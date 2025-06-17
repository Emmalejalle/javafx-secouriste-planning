package modele.DAO;

import modele.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * DAO pour la hiérarchie d'utilisateurs (User, Admin, Secouriste).
 * Gère la persistance avec une seule table 'User' et un champ 'isAdmin'.
 * Gère également les relations des secouristes avec les compétences et les disponibilités.
 * 
 * @author Emilien EMERIAU
 * @version 2.0
 */
public class UserDAO extends DAO<User> {

    // Les DAO pour les relations spécifiques aux secouristes.
    private final CompetenceDAO competenceDAO;
    private final JourneeDAO journeeDAO;

    /**
     * Constructeur. Récupère l'instance de la connexion à la BDD via le singleton MyConnection.
     * Initialise les DAO nécessaires pour les relations des secouristes.
     */
    public UserDAO() {
        super();
        this.competenceDAO = new CompetenceDAO();
        this.journeeDAO = new JourneeDAO();
    }
    
    /**
     * Récupère un utilisateur par son identifiant.
     * Si l'utilisateur est un secouriste, charge également ses compétences et disponibilités.
     * @param id L'identifiant de l'utilisateur à trouver.
     * @return L'utilisateur trouvé, ou null s'il n'existe pas.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public User findByID(long id) throws SQLException {
        String sql = "SELECT * FROM User WHERE idUser = ?";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    // Si l'utilisateur est un secouriste, on charge ses listes associées.
                    if (user instanceof Secouriste) {
                        loadCompetencesFor((Secouriste) user);
                        loadDisponibilitesFor((Secouriste) user);
                    }
                    return user;
                }
            }
        }
        return null;
    }
    
    /**
     * Récupère tous les utilisateurs (Admin et Secouristes) de la base de données.
     * Charge également les compétences et disponibilités pour les secouristes.
     * @return Une liste d'objets User (Admin ou Secouriste).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public ArrayList<User> findAll() throws SQLException {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM User ORDER BY nomUser, prenomUser";
        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                if (user instanceof Secouriste) {
                    loadCompetencesFor((Secouriste) user);
                    loadDisponibilitesFor((Secouriste) user);
                }
                userList.add(user);
            }
        }
        return userList;
    }

    /**
     * NOUVELLE MÉTHODE : Récupère la liste de tous les secouristes disponibles pour une journée donnée.
     * @param journeeId L'identifiant de la journée pour laquelle on veut les secouristes.
     * @return Un ArrayList d'objets User (qui seront tous des Secouristes).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public ArrayList<User> findAvailableUsersForJournee(long journeeId) throws SQLException {
        ArrayList<User> userList = new ArrayList<>();
        // Jointure entre la table User et la table de disponibilité 'Dispo'
        String sql = "SELECT u.* FROM User u JOIN Dispo d ON u.idUser = d.idSecouriste WHERE d.idJourneeDispo = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, journeeId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    // On charge les autres informations pour avoir un objet complet.
                    if (user instanceof Secouriste) {
                        loadCompetencesFor((Secouriste) user);
                        loadDisponibilitesFor((Secouriste) user);
                    }
                    userList.add(user);
                }
            }
        }
        return userList;
    }
    
    /**
     * Crée un utilisateur (Admin ou Secouriste) et ses relations associées.
     * L'opération est transactionnelle pour garantir la cohérence des données.
     * @param obj L'utilisateur à créer.
     * @return Le nombre de lignes affectées (1 si succès).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int create(User obj) throws SQLException {
        try {
            this.connect.setAutoCommit(false); // Début de la transaction

            // 1. Insérer l'utilisateur dans la table 'User'
            String sqlUser = "INSERT INTO User (mdpUser, nomUser, prenomUser, dateNaissance, emailUser, telUser, adresseUser, isAdmin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement st = this.connect.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, obj.getMdp()); // NOTE: le mot de passe devrait être crypté ici
                st.setString(2, obj.getNom());
                st.setString(3, obj.getPrenom());

                // Plus besoin de conversion : on passe directement le String.
                st.setString(4, obj.getDateNaissance());

                st.setString(5, obj.getEmail());
                st.setString(6, obj.getTel());
                st.setString(7, obj.getAdresse());
                st.setInt(8, (obj instanceof Admin) ? 1 : 0); // 1 pour Admin, 0 pour Secouriste
                
                st.executeUpdate();
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        obj.setId(generatedKeys.getLong(1));
                    }
                }
            }

            // 2. Si c'est un secouriste, insérer ses compétences
            if (obj instanceof Secouriste) {
                Secouriste secouriste = (Secouriste) obj;
                updateCompetencesFor(secouriste);
            }

            this.connect.commit(); // Valider la transaction
            return 1;
        } catch (SQLException e) {
            this.connect.rollback();
            throw new SQLException("Échec de la création de l'utilisateur : " + e.getMessage(), e);
        } finally {
            this.connect.setAutoCommit(true);
        }
    }

    /** 
     * Met à jour un utilisateur (Admin ou Secouriste) et ses relations associées.
     * L'opération est transactionnelle pour garantir la cohérence des données.
     * @param obj L'utilisateur à mettre à jour.
     * @return Le nombre de lignes affectées (1 si succès).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int update(User obj) throws SQLException {
        try {
            this.connect.setAutoCommit(false);

            // 1. Mettre à jour les informations de base de l'utilisateur
            String sqlUser = "UPDATE User SET mdpUser = ?, nomUser = ?, prenomUser = ?, dateNaissance = ?, emailUser = ?, telUser = ?, adresseUser = ?, isAdmin = ? WHERE idUser = ?";
            try (PreparedStatement st = this.connect.prepareStatement(sqlUser)) {
                st.setString(1, obj.getMdp());
                st.setString(2, obj.getNom());
                st.setString(3, obj.getPrenom());

                // Plus besoin de conversion : on passe directement le String.
                st.setString(4, obj.getDateNaissance());

                st.setString(5, obj.getEmail());
                st.setString(6, obj.getTel());
                st.setString(7, obj.getAdresse());
                st.setInt(8, (obj instanceof Admin) ? 1 : 0);
                st.setLong(9, obj.getId());
                st.executeUpdate();
            }

            // 2. Si c'est un secouriste, remplacer ses compétences et disponibilités
            if (obj instanceof Secouriste) {
                Secouriste secouriste = (Secouriste) obj;
                // L'approche "supprimer puis recréer" est la plus simple pour les relations
                clearAllLinksForSecouriste(secouriste.getId());
                updateCompetencesFor(secouriste);
                updateDisponibilitesFor(secouriste);
            }

            this.connect.commit();
            return 1;
        } catch (SQLException e) {
            this.connect.rollback();
            throw new SQLException("Échec de la mise à jour de l'utilisateur : " + e.getMessage(), e);
        } finally {
            this.connect.setAutoCommit(true);
        }
    }

    /**
     * Supprime un utilisateur de la base de données.
     * Grâce à `ON DELETE CASCADE`, les relations associées sont également supprimées.
     * @param obj L'utilisateur à supprimer.
     * @return Le nombre de lignes supprimées (normalement 1).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int delete(User obj) throws SQLException {
        // ON DELETE CASCADE est supposé être configuré sur les tables de jointure
        String sql = "DELETE FROM User WHERE idUser = ?";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getId());
            return st.executeUpdate();
        }
    }

    // --- Méthodes privées et de chargement ---
    
    /**
     * Crée le bon type d'objet (Admin ou Secouriste) à partir d'une ligne de la BDD.
     * @param rs Le ResultSet contenant les données de l'utilisateur.
     * @return Un objet User (Admin ou Secouriste) avec les données de la BDD.
     * @throws SQLException En cas d'erreur lors de la lecture du ResultSet.
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        boolean isAdmin = rs.getInt("isAdmin") == 1;

        // Plus besoin de conversion : on lit directement le String de la BDD.
        String dateNaissanceStr = rs.getString("dateNaissance");

        if (isAdmin) {
            return new Admin(rs.getLong("idUser"), rs.getString("mdpUser"), rs.getString("nomUser"), rs.getString("prenomUser"), dateNaissanceStr, rs.getString("emailUser"), rs.getString("telUser"), rs.getString("adresseUser"));
        } else {
            return new Secouriste(rs.getLong("idUser"), rs.getString("mdpUser"), rs.getString("nomUser"), rs.getString("prenomUser"), dateNaissanceStr, rs.getString("emailUser"), rs.getString("telUser"), rs.getString("adresseUser"));
        }
    }

    /**
     * Supprime toutes les relations d'un secouriste (compétences, disponibilités, affectations).
     * Utilisé lors de la mise à jour ou de la suppression d'un secouriste.
     * @param secouristeId L'identifiant du secouriste dont on veut supprimer les relations.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    private void clearAllLinksForSecouriste(long secouristeId) throws SQLException {
        try (Statement st = this.connect.createStatement()) {
            // Note: Adaptez les noms des tables de jointure si nécessaire
            st.addBatch("DELETE FROM ListCompSecouriste WHERE idSecouCompList = " + secouristeId);
            st.addBatch("DELETE FROM Dispo WHERE idSecouriste = " + secouristeId);
            st.addBatch("DELETE FROM Affectation WHERE idSecouAffect = " + secouristeId);
            st.executeBatch();
        }
    }

    /**
     * Met à jour les compétences d'un secouriste dans la base de données.
     * Supprime les anciennes compétences et insère les nouvelles.
     * @param secouriste Le secouriste dont on veut mettre à jour les compétences.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    private void updateCompetencesFor(Secouriste secouriste) throws SQLException {
        String sql = "INSERT INTO ListCompSecouriste (idSecouCompList, idCompList) VALUES (?, ?)";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            for (Competence comp : secouriste.getCompetences()) {
                st.setLong(1, secouriste.getId());
                st.setLong(2, comp.getIdComp());
                st.addBatch();
            }
            st.executeBatch();
        }
    }

    /**
     * Met à jour les disponibilités d'un secouriste dans la base de données.
     * Supprime les anciennes disponibilités et insère les nouvelles.
     * @param secouriste Le secouriste dont on veut mettre à jour les disponibilités.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    private void updateDisponibilitesFor(Secouriste secouriste) throws SQLException {
        String sql = "INSERT INTO Dispo (idSecouriste, idJourneeDispo) VALUES (?, ?)";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            for (Journee jour : secouriste.getDisponibilites()) {
                st.setLong(1, secouriste.getId());
                st.setLong(2, jour.getId());
                st.addBatch();
            }
            st.executeBatch();
        }
    }

    /**
     * Charge les compétences d'un secouriste à partir de la base de données.
     * Utilisé lors de la récupération d'un secouriste pour peupler ses compétences.
     * @param secouriste Le secouriste dont on veut charger les compétences.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void loadCompetencesFor(Secouriste secouriste) throws SQLException {
        String sql = "SELECT idCompList FROM ListCompSecouriste WHERE idSecouCompList = ?";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, secouriste.getId());
            ResultSet rs = st.executeQuery();
            ArrayList<Competence> competences = new ArrayList<>();
            while(rs.next()) {
                competences.add(competenceDAO.findByID(rs.getLong("idCompList")));
            }
            secouriste.setCompetences(competences);
        }
    }

    /**
     * Charge les disponibilités d'un secouriste à partir de la base de données.
     * Utilisé lors de la récupération d'un secouriste pour peupler ses disponibilités.
     * @param secouriste Le secouriste dont on veut charger les disponibilités.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void loadDisponibilitesFor(Secouriste secouriste) throws SQLException {
        String sql = "SELECT idJourneeDispo FROM Dispo WHERE idSecouriste = ?";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, secouriste.getId());
            ResultSet rs = st.executeQuery();
            ArrayList<Journee> disponibilites = new ArrayList<>();
            while(rs.next()) {
                disponibilites.add(journeeDAO.findByID(rs.getLong("idJourneeDispo")));
            }
            secouriste.setDisponibilites(disponibilites);
        }
    }
}
