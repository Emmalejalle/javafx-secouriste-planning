package modele.DAO;

import modele.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO concret pour la gestion des Affectations.
 * Permet de créer, supprimer, et rechercher des liens entre Secouristes et DPS.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class AffectationDAO extends DAO<Affectation> {

    // L'AffectationDAO a besoin des autres DAO pour reconstruire les objets complets.
    private final UserDAO userDAO;
    private final DpsDAO dpsDAO;
    private final CompetenceDAO competenceDAO;
    
    public AffectationDAO() {
        super();
        this.userDAO = new UserDAO();
        this.dpsDAO = new DpsDAO();
        this.competenceDAO = new CompetenceDAO();
    }
    
    /**
     * Crée une nouvelle affectation dans la base de données.
     * @param obj L'objet Affectation contenant le Secouriste, le DPS et la Compétence.
     * @return 1 si la création a réussi.
     * @throws SQLException
     */
    @Override
    public int create(Affectation obj) throws SQLException {
        // La requête utilise les noms de colonnes de votre script CREATE TABLE.
        String sql = "INSERT INTO Affectation (idSecouAffect, idCompAffect, idDPSAffect) VALUES (?, ?, ?)";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getSecouriste().getId());
            st.setLong(2, obj.getCompetenceRemplie().getIdComp());
            st.setLong(3, obj.getDps().getId());
            return st.executeUpdate();
        }
    }

    /**
     * Supprime une affectation de la base de données.
     * @param obj L'objet Affectation à supprimer.
     * @return Le nombre de lignes supprimées.
     * @throws SQLException
     */
    @Override
    public int delete(Affectation obj) throws SQLException {
        String sql = "DELETE FROM Affectation WHERE idSecouAffect = ? AND idCompAffect = ? AND idDPSAffect = ?";
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, obj.getSecouriste().getId());
            st.setLong(2, obj.getCompetenceRemplie().getIdComp());
            st.setLong(3, obj.getDps().getId());
            return st.executeUpdate();
        }
    }
    
    /**
     * Trouve toutes les affectations pour un DPS donné (getSecouristeParDPS).
     * @param dpsId L'ID du DPS.
     * @return Un ArrayList d'objets Affectation.
     * @throws SQLException
     */
    public ArrayList<Affectation> findAffectationsForDps(long dpsId) throws SQLException {
        ArrayList<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM Affectation WHERE idDPSAffect = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, dpsId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    // Pour chaque ligne, on reconstruit l'objet Affectation complet
                    User user = userDAO.findByID(rs.getLong("idSecouAffect"));
                    DPS dps = dpsDAO.findByID(rs.getLong("idDPSAffect")); // On pourrait passer l'objet DPS directement
                    Competence comp = competenceDAO.findByID(rs.getLong("idCompAffect"));

                    // On vérifie que le User est bien un Secouriste et que rien n'est null
                    if (user instanceof Secouriste && dps != null && comp != null) {
                        affectations.add(new Affectation((Secouriste) user, dps, comp));
                    }
                }
            }
        }
        return affectations;
    }
    
    /**
     * Trouve toutes les affectations pour un Secouriste donné (getDPSparSecouriste).
     * @param secouristeId L'ID du Secouriste.
     * @return Un ArrayList d'objets Affectation.
     * @throws SQLException
     */
    public ArrayList<Affectation> findAffectationsForSecouriste(long secouristeId) throws SQLException {
        ArrayList<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT * FROM Affectation WHERE idSecouAffect = ?";
        
        try (PreparedStatement st = this.connect.prepareStatement(sql)) {
            st.setLong(1, secouristeId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    User user = userDAO.findByID(rs.getLong("idSecouAffect"));
                    DPS dps = dpsDAO.findByID(rs.getLong("idDPSAffect"));
                    Competence comp = competenceDAO.findByID(rs.getLong("idCompAffect"));

                    if (user instanceof Secouriste && dps != null && comp != null) {
                        affectations.add(new Affectation((Secouriste) user, dps, comp));
                    }
                }
            }
        }
        return affectations;
    }


    // --- Méthodes non supportées pour cette entité d'association ---

    @Override
    public Affectation findByID(long id) {
        throw new UnsupportedOperationException("Affectation n'a pas d'ID unique. Utilisez les méthodes de recherche spécifiques comme findAffectationsForDps().");
    }
    
    @Override
    public ArrayList<Affectation> findAll() {
        throw new UnsupportedOperationException("Non implémenté. Charger toutes les affectations de la base peut être très lourd et est rarement utile.");
    }
    
    @Override
    public int update(Affectation obj) {
        throw new UnsupportedOperationException("Une affectation ne se met pas à jour. Pour la modifier, il faut la supprimer et en créer une nouvelle.");
    }
}

