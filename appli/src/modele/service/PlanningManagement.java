package modele.service;

import modele.DAO.AffectationDAO;
import modele.persistence.Affectation;
import modele.persistence.DPS;
import modele.persistence.Secouriste;

import java.sql.SQLException;
import java.util.List;

/**
 * Couche de service pour la logique liée à l'affichage du planning.
 * Communique avec les DAOs nécessaires.
 */
public class PlanningManagement {

    private AffectationDAO affectationDAO;

    public PlanningManagement() {
        this.affectationDAO = new AffectationDAO();
    }

    /**
     * Récupère toutes les affectations (et donc les DPS) pour un secouriste donné.
     * @param secouriste Le secouriste dont on veut le planning.
     * @return Une liste de ses affectations.
     * @throws SQLException
     */
    public List<Affectation> getPlanningPourSecouriste(Secouriste secouriste) throws SQLException {
        if (secouriste == null) {
            throw new IllegalArgumentException("Le secouriste ne peut pas être null.");
        }
        return affectationDAO.findAffectationsForSecouriste(secouriste.getId());
    }

    /**
     * Récupère la liste des coéquipiers pour un DPS spécifique.
     * @param dps Le DPS concerné.
     * @return La liste des affectations (secouristes) pour ce DPS.
     * @throws SQLException
     */
    public List<Affectation> getEquipePourDps(DPS dps) throws SQLException {
        if (dps == null) {
            throw new IllegalArgumentException("Le DPS ne peut pas être null.");
        }
        return affectationDAO.findAffectationsForDps(dps.getId());
    }
}
