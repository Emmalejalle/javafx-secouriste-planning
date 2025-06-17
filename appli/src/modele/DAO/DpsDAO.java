package modele.DAO;

import modele.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO concret pour l'entité DPS (Dispositif Prévisionnel de Secours).
 * Cette classe gère la communication entre l'application Java et la base de données
 * pour tout ce qui concerne les objets DPS.
 * Elle collabore avec d'autres DAO pour construire les objets DPS complets.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class DpsDAO extends DAO<DPS> {

    // Le DpsDAO a besoin d'instances des autres DAO pour charger les objets liés (Site, Sport, etc.).
    private final SiteDAO siteDAO;
    private final SportDAO sportDAO;
    private final JourneeDAO journeeDAO;
    private final CompetenceDAO competenceDAO;

    /**
     * Constructeur du DpsDAO.
     * Initialise la connexion à la BDD via le constructeur parent et
     * instancie les autres DAO nécessaires à son fonctionnement.
     */
    public DpsDAO() {
        super(); // Appelle le constructeur de DAO<T> pour obtenir la connexion.
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
        this.journeeDAO = new JourneeDAO();
        this.competenceDAO = new CompetenceDAO();
    }
    
    /**
     * Trouve un DPS spécifique par son identifiant unique.
     * @param id L'identifiant du DPS à rechercher.
     * @return Un objet DPS complet avec ses besoins chargés, ou null s'il n'est pas trouvé.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public DPS findByID(long id) throws SQLException {
        String sql = "SELECT * FROM DPS WHERE idDPS = ?";
        DPS dps = null;

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    // La méthode mapResultSetToDps s'occupe de créer l'objet de base.
                    dps = mapResultSetToDps(rs);
                }
            }
        }
        // Si un DPS a été trouvé, on charge séparément ses besoins en compétences.
        if (dps != null) {
            loadBesoinsFor(dps);
        }
        return dps;
    }
    
    /**
     * Récupère la liste de tous les DPS de la base de données.
     * @return Un ArrayList d'objets DPS, chacun avec ses besoins chargés.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public ArrayList<DPS> findAll() throws SQLException {
        ArrayList<DPS> dpsList = new ArrayList<>();
        String sql = "SELECT * FROM DPS ORDER BY idJourneeDPS, horaireDepart";

        try (Statement st = this.connect.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                DPS dps = mapResultSetToDps(rs);
                // Pour chaque DPS de la liste, on charge ses besoins.
                loadBesoinsFor(dps);
                dpsList.add(dps);
            }
        }
        return dpsList;
    }

    /**
     * NOUVELLE MÉTHODE : Récupère la liste de tous les DPS pour une journée donnée.
     * @param journeeId L'identifiant de la journée pour laquelle on veut les DPS.
     * @return Un ArrayList d'objets DPS pour cette journée, triés par heure de début.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public ArrayList<DPS> findAllByJournee(long journeeId) throws SQLException {
        ArrayList<DPS> dpsList = new ArrayList<>();
        String sql = "SELECT * FROM DPS WHERE idJourneeDPS = ? ORDER BY horaireDepart";

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, journeeId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    DPS dps = mapResultSetToDps(rs);
                    // Pour chaque DPS de la liste, on charge ses besoins.
                    loadBesoinsFor(dps);
                    dpsList.add(dps);
                }
            }
        }
        return dpsList;
    }

    /**
     * Charge les besoins en compétences pour un objet DPS donné et les ajoute à sa map 'besoins'.
     * C'est une approche de "chargement paresseux" (Lazy Loading).
     * @param dps L'objet DPS à "hydrater" avec ses besoins.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void loadBesoinsFor(DPS dps) throws SQLException {
        String sql = "SELECT idBesoinComp, nombre FROM Besoin WHERE idBesoinDPS = ?";
        Map<Competence, Integer> besoins = new HashMap<>();

        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, dps.getId());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    long idComp = rs.getLong("idBesoinComp");
                    int nombre = rs.getInt("nombre");
                    
                    // On utilise le CompetenceDAO pour trouver l'objet Competence correspondant à l'ID.
                    Competence competence = competenceDAO.findByID(idComp);
                    if (competence != null) {
                        besoins.put(competence, nombre);
                    }
                }
            }
        }
        // Met à jour l'objet DPS avec la map de besoins que l'on vient de construire.
        dps.setBesoins(besoins);
    }
    
    /**
     * Crée un nouveau DPS et ses besoins associés dans la base de données.
     * L'opération est transactionnelle : soit tout réussit, soit tout est annulé.
     * @param obj L'objet DPS à créer, contenant déjà ses objets Site, Sport, Journee et sa map de besoins.
     * @return 1 en cas de succès, sinon une exception est levée.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int create(DPS obj) throws SQLException {
        try {
            // On désactive l'auto-commit pour gérer la transaction manuellement.
            this.connect.setAutoCommit(false);

            // Étape 1 : Insérer le DPS principal pour obtenir son ID auto-généré.
            String sqlDps = "INSERT INTO DPS (horaireDepart, horaireFin, codeSiteDPS, codeSportDPS, idJourneeDPS) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement st = this.connect.prepareStatement(sqlDps, Statement.RETURN_GENERATED_KEYS)) {
                st.setInt(1, obj.getHoraireDepart());
                st.setInt(2, obj.getHoraireFin());
                st.setLong(3, obj.getSite().getCode());
                st.setLong(4, obj.getSport().getCode());
                st.setLong(5, obj.getJournee().getId());
                st.executeUpdate();
                
                // On récupère l'ID généré pour le mettre à jour dans notre objet Java.
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        obj.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("La création du DPS a échoué, aucun ID obtenu.");
                    }
                }
            }

            // Étape 2 : Insérer les besoins associés dans la table `Besoin`.
            String sqlBesoin = "INSERT INTO Besoin (idBesoinDPS, idBesoinComp, nombre) VALUES (?, ?, ?)";
            try (PreparedStatement st = this.connect.prepareStatement(sqlBesoin)) {
                // On parcourt la map des besoins de l'objet DPS.
                for (Map.Entry<Competence, Integer> entry : obj.getBesoins().entrySet()) {
                    st.setLong(1, obj.getId()); // L'ID du DPS qu'on vient de créer.
                    st.setLong(2, entry.getKey().getIdComp()); // L'ID de la compétence requise.
                    st.setInt(3, entry.getValue()); // Le nombre de secouristes nécessaires.
                    st.addBatch(); // On ajoute la commande à un lot pour l'exécuter en groupe.
                }
                st.executeBatch(); // Exécute toutes les commandes d'un coup, c'est plus performant.
            }

            // Si toutes les opérations ont réussi, on valide la transaction.
            this.connect.commit();
            return 1;
        } catch (SQLException e) {
            // En cas d'erreur, on annule toutes les opérations de la transaction.
            this.connect.rollback();
            throw e; // On propage l'exception pour que l'appelant sache que ça a échoué.
        } finally {
            // Dans tous les cas, on réactive le mode auto-commit.
            this.connect.setAutoCommit(true);
        }
    }

    /**
     * Met à jour un DPS, ses besoins, et supprime les affectations existantes pour ce DPS.
     * Cette approche garantit que les affectations sont toujours cohérentes avec la définition du DPS.
     * @param obj L'objet DPS avec ses nouvelles informations.
     * @return 1 en cas de succès.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int update(DPS obj) throws SQLException {
        try {
            // Début de la transaction pour garantir que toutes les étapes réussissent ensemble.
            this.connect.setAutoCommit(false);

            // Étape 1 : Mettre à jour les informations du DPS lui-même dans la table `DPS`.
            String sqlDps = "UPDATE DPS SET horaireDepart = ?, horaireFin = ?, codeSiteDPS = ?, codeSportDPS = ?, idJourneeDPS = ? WHERE idDPS = ?";
            try (PreparedStatement st = this.connect.prepareStatement(sqlDps)) {
                st.setInt(1, obj.getHoraireDepart());
                st.setInt(2, obj.getHoraireFin());
                st.setLong(3, obj.getSite().getCode());
                st.setLong(4, obj.getSport().getCode());
                st.setLong(5, obj.getJournee().getId());
                st.setLong(6, obj.getId());
                st.executeUpdate();
            }
            
            // Étape 2 : Vider toutes les affectations existantes pour ce DPS.
            // C'est l'étape cruciale pour garantir la cohérence : si un DPS change, les anciennes affectations ne sont plus valides.
            String sqlDeleteAffectations = "DELETE FROM Affectation WHERE idDPSAffect = ?";
            try (PreparedStatement st = this.connect.prepareStatement(sqlDeleteAffectations)) {
                st.setLong(1, obj.getId());
                st.executeUpdate();
            }

            // Étape 3 : Remplacer les besoins en supprimant d'abord les anciens...
            String sqlDeleteBesoins = "DELETE FROM Besoin WHERE idBesoinDPS = ?";
            try (PreparedStatement st = this.connect.prepareStatement(sqlDeleteBesoins)) {
                st.setLong(1, obj.getId());
                st.executeUpdate();
            }

            // Étapimport java.util.List;e 4 : ...et en insérant les nouveaux.
            String sqlInsertBesoins = "INSERT INTO Besoin (idBesoinDPS, idBesoinComp, nombre) VALUES (?, ?, ?)";
            try (PreparedStatement st = this.connect.prepareStatement(sqlInsertBesoins)) {
                for (Map.Entry<Competence, Integer> entry : obj.getBesoins().entrySet()) {
                    st.setLong(1, obj.getId());
                    st.setLong(2, entry.getKey().getIdComp());
                    st.setInt(3, entry.getValue());
                    st.addBatch();
                }
                st.executeBatch();
            }

            // Si tout s'est bien passé, on sauvegarde toutes les modifications.
            this.connect.commit(); 
            return 1;
        } catch (SQLException e) {
            // S'il y a eu une erreur, on annule tout ce qui a été fait dans cette transaction.
            this.connect.rollback(); 
            throw e;
        } finally {
            // On s'assure de réactiver le mode auto-commit pour les autres opérations.
            this.connect.setAutoCommit(true); 
        }
    }

    /**
     * Supprime un DPS de la base de données.
     * Grâce à `ON DELETE CASCADE`, les besoins et les affectations associés seront aussi supprimés automatiquement.
     * @param obj Le DPS à supprimer.
     * @return Le nombre de lignes supprimées (normalement 1).
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    @Override
    public int delete(DPS obj) throws SQLException {
        String sql = "DELETE FROM DPS WHERE idDPS = ?";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getId());
            return st.executeUpdate();
        }
    }
    
    /**
     * Méthode privée utilitaire pour transformer une ligne de ResultSet en objet DPS.
     * Elle ne charge que les informations de base du DPS et ses objets liés directement (Site, Sport, Journee).
     * Les besoins sont chargés séparément par `loadBesoinsFor`.
     * @param rs Le ResultSet positionné sur la ligne à convertir.
     * @return un nouvel objet DPS.
     * @throws SQLException
     */
    private DPS mapResultSetToDps(ResultSet rs) throws SQLException {
        // Récupérer les clés étrangères depuis la ligne actuelle du ResultSet.
        long idSite = rs.getLong("codeSiteDPS");
        long idSport = rs.getLong("codeSportDPS");
        long idJournee = rs.getLong("idJourneeDPS");

        // Utiliser les autres DAO pour récupérer les objets complets correspondants.
        Site site = siteDAO.findByID(idSite);
        Sport sport = sportDAO.findByID(idSport);
        Journee journee = journeeDAO.findByID(idJournee);
        
        // Créer l'objet DPS en assemblant toutes les informations.
        return new DPS(
            rs.getLong("idDPS"),
            rs.getInt("horaireDepart"),
            rs.getInt("horaireFin"),
            site,
            sport,
            journee
        );
    }
}
