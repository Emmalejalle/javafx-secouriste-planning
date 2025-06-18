package modele.service;

import modele.DAO.DpsDAO;
import modele.DAO.SiteDAO;
import modele.DAO.SportDAO;
import modele.DAO.JourneeDAO;
import modele.DAO.CompetenceDAO;
import modele.persistence.Competence;
import modele.persistence.DPS;
import modele.persistence.Journee;
import modele.persistence.Site;
import modele.persistence.Sport;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Couche de service pour la gestion des DPS.
 * Contient la logique métier et communique avec les DAOs nécessaires.
 */
public class DpsManagement {

    private DpsDAO dpsDAO;
    private SiteDAO siteDAO;
    private SportDAO sportDAO;
    private JourneeDAO journeeDAO;
    private CompetenceDAO competenceDAO;

    public DpsManagement() {
        this.dpsDAO = new DpsDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
        this.journeeDAO = new JourneeDAO();
        this.competenceDAO = new CompetenceDAO();
    }

    // --- Méthodes pour récupérer des listes de données ---

    public List<DPS> listerTousLesDps() throws SQLException {
        return dpsDAO.findAll();
    }

    public List<Site> listerTousLesSites() throws SQLException {
        return siteDAO.findAll();
    }

    public List<Sport> listerTousLesSports() throws SQLException {
        return sportDAO.findAll();
    }

    public List<Journee> listerToutesLesJournees() throws SQLException {
        return journeeDAO.findAll();
    }

    public List<Competence> listerToutesLesCompetences() throws SQLException {
        return competenceDAO.findAll();
    }

    // --- Méthodes de logique métier ---

    // Méthode privée pour trouver une journée ou la créer si elle n'existe pas
    private Journee findOrCreateJournee(LocalDate date) throws SQLException {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être nulle.");
        }
        // On utilise la méthode findByDate que tu as créée dans le DAO
        Journee journee = journeeDAO.findByDate(date.getDayOfMonth(), date.getMonthValue(), date.getYear());

        // Si la journée n'existe pas en BDD...
        if (journee == null) {
            System.out.println("La journée du " + date + " n'existe pas, création...");
            // ... on la crée.
            journee = new Journee(0, date.getDayOfMonth(), date.getMonthValue(), date.getYear());
            journeeDAO.create(journee); // Le DAO mettra à jour l'ID de l'objet
        }
        return journee;
    }

    public void creerDps(int hDepart, int hFin, Site site, Sport sport, LocalDate date, Map<Competence, Integer> besoins) throws SQLException {
        Journee journee = findOrCreateJournee(date); // On récupère ou crée la journée
        DPS nouveauDps = new DPS(0, hDepart, hFin, site, sport, journee);
        nouveauDps.setBesoins(besoins);
        dpsDAO.create(nouveauDps);
    }

    public void modifierDps(DPS dpsAModifier, int hDepart, int hFin, Site site, Sport sport, LocalDate date, Map<Competence, Integer> besoins) throws SQLException {
        Journee journee = findOrCreateJournee(date); // On récupère ou crée la journée
        dpsAModifier.setHoraireDepart(hDepart);
        dpsAModifier.setHoraireFin(hFin);
        dpsAModifier.setSite(site);
        dpsAModifier.setSport(sport);
        dpsAModifier.setJournee(journee); // On met à jour avec la bonne journée
        dpsAModifier.setBesoins(besoins);
        dpsDAO.update(dpsAModifier);
    }


    public void supprimerDps(DPS dps) throws SQLException {
        dpsDAO.delete(dps);
    }

    public List<DPS> filtrerDps(List<DPS> liste, String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return liste;
        }
        String rechercheMiniscule = recherche.toLowerCase();
        return liste.stream()
            .filter(dps -> dps.getSport().getNom().toLowerCase().contains(rechercheMiniscule) || 
                           dps.getSite().getNom().toLowerCase().contains(rechercheMiniscule))
            .collect(Collectors.toList());
    }
}