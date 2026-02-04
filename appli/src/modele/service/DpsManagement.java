package modele.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import modele.DAO.AffectationDAO;
import modele.DAO.CompetenceDAO;
import modele.DAO.DpsDAO;
import modele.DAO.JourneeDAO;
import modele.DAO.SiteDAO;
import modele.DAO.SportDAO;
import modele.persistence.Affectation;
import modele.persistence.Competence;
import modele.persistence.DPS;
import modele.persistence.Journee;
import modele.persistence.Site;
import modele.persistence.Sport;

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
    private AffectationDAO affectationDAO;

    public DpsManagement() {
        this.dpsDAO = new DpsDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
        this.journeeDAO = new JourneeDAO();
        this.competenceDAO = new CompetenceDAO();
        this.affectationDAO = new AffectationDAO(); 
    }


    // --- Méthodes pour récupérer des listes de données ---

    /**
     * Renvoie une liste de tous les DPS de la base de données.
     * 
     * @return Une liste de tous les DPS.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public List<DPS> listerTousLesDps() throws SQLException {
        return dpsDAO.findAll();
    }

    /**
     * Renvoie une liste de tous les sites disponibles dans la base de données.
     * 
     * @return Une liste de tous les sites.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public List<Site> listerTousLesSites() throws SQLException {
        return siteDAO.findAll();
    }

    /**
     * Renvoie une liste de tous les sports disponibles dans la base de données.
     * 
     * @return Une liste de tous les sports.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public List<Sport> listerTousLesSports() throws SQLException {
        return sportDAO.findAll();
    }

    /**
     * Renvoie une liste de toutes les journées disponibles dans la base de données.
     * 
     * @return Une liste de toutes les journées.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public List<Journee> listerToutesLesJournees() throws SQLException {
        return journeeDAO.findAll();
    }

    /**
     * Renvoie une liste de toutes les compétences disponibles dans la base de données.
     * 
     * @return Une liste de toutes les compétences.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
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

    /**
     * Crée un nouveau DPS et ses besoins associés dans la base de données.
     * Si la journée n'existe pas, elle est créée.
     * 
     * @param hDepart - L'heure de départ du DPS.
     * @param hFin - L'heure de fin du DPS.
     * @param site - Le site du DPS.
     * @param sport - Le sport lié au DPS.
     * @param date - La date du DPS.
     * @param besoins - La liste des compétences et de leurs quantités associées.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void creerDps(int hDepart, int hFin, Site site, Sport sport, LocalDate date, Map<Competence, Integer> besoins) throws SQLException {
        Journee journee = findOrCreateJournee(date); // On récupère ou crée la journée
        DPS nouveauDps = new DPS(0, hDepart, hFin, site, sport, journee);
        nouveauDps.setBesoins(besoins);
        dpsDAO.create(nouveauDps);
    }

    /**
     * Modifie un DPS existant et met à jour ses informations dans la base de données.
     * Si la journée n'existe pas, elle est créée.
     * 
     * @param dpsAModifier - Le DPS à modifier.
     * @param hDepart - L'heure de départ du DPS.
     * @param hFin - L'heure de fin du DPS.
     * @param site - Le site du DPS.
     * @param sport - Le sport lié au DPS.
     * @param date - La date du DPS.
     * @param besoins - La liste des compétences et de leurs quantités associées.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
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


    /**
     * Supprime un DPS existant de la base de données.
     * 
     * @param dps - L'objet DPS à supprimer.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void supprimerDps(DPS dps) throws SQLException {
        dpsDAO.delete(dps);
    }

    /**
     * Filtre une liste de DPS en mémoire basé sur un texte de recherche.
     * La recherche est sensible à la casse.
     * 
     * @param liste - la liste de DPS à filtrer
     * @param recherche - le texte à rechercher
     * @return une liste de DPS dont le nom du sport ou le nom du site contient le texte de recherche
     */
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

    /**
     * Récupère la liste des affectations (et donc des secouristes) pour un DPS donné.
     * @param dps Le DPS pour lequel on cherche les secouristes.
     * @return Une liste d'objets Affectation.
     * @throws SQLException
     */
    public List<Affectation> getAffectationsPourDps(DPS dps) throws SQLException {
        if (dps == null) {
            // Retourne une liste vide si le DPS est null pour éviter les erreurs
            return new java.util.ArrayList<>();
        }
        // On suppose que affectationDAO est initialisé dans le constructeur du service
        return affectationDAO.findAffectationsForDps(dps.getId());
    }
}