package modele.persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe DPS (Dispositif Prévisionnel de Secours) représente un événement
 * nécessitant des secours à une date, un lieu et pour un sport donnés.
 * 
 * 
 * @author Emilien EMERIAU et Elie Tardy
 * @version 1.0
 */
public class DPS {
    
    /**
     * L'ID unique du DPS, auto-généré par la BDD.
     */
    private long id;

    /**
     * L'horaire de début, en minutes depuis minuit (ex: 8h30 -> 510).
     */
    private int horaireDepart;

    /**
     * L'horaire de fin, en minutes depuis minuit.
     */
    private int horaireFin;

    // Objets liés qui seront chargés par le DAO
    private Site site;
    private Sport sport;
    private Journee journee;
    
    /**
     * Les besoins en personnel pour ce DPS.
     * La clé est la Compétence requise, la valeur est le nombre de secouristes nécessaires.
     */
    private Map<Competence, Integer> besoins;

    /**
     * Constructeur principal utilisé par le DAO pour créer l'objet.
     * @param id L'ID du DPS.
     * @param horaireDepart L'horaire de début en minutes.
     * @param horaireFin L'horaire de fin en minutes.
     * @param site L'objet Site associé.
     * @param sport L'objet Sport associé.
     * @param journee L'objet Journee associé.
     */
    public DPS(long id, int horaireDepart, int horaireFin, Site site, Sport sport, Journee journee) {
        this.id = id;
        this.setHoraireDepart(horaireDepart);
        this.setHoraireFin(horaireFin);
        this.setSite(site);
        this.setSport(sport);
        this.setJournee(journee);
        this.besoins = new HashMap<>(); // La liste des besoins est initialisée vide.
    }

    // --- Getters ---
    public long getId() { 
        return id; 
    }
    public int getHoraireDepart() { 
        return horaireDepart; 
    }
    public int getHoraireFin() { 
        return horaireFin; 
    }
    public Site getSite() { 
        return site; 
    }
    public Sport getSport() { 
        return sport; 
    }
    public Journee getJournee() { 
        return journee; 
    }
    public Map<Competence, Integer> getBesoins() { 
        return besoins; 
    }
    
    // --- Setters ---
    public void setId(long id) { this.id = id; }
    
    public final void setHoraireDepart(int horaireDepart) {
        if (horaireDepart < 0 || horaireDepart >= 24 * 60) {
            throw new IllegalArgumentException("L'horaire de départ est invalide.");
        }
        this.horaireDepart = horaireDepart;
    }

    public final void setHoraireFin(int horaireFin) {
        if (horaireFin < 0 || horaireFin >= 24 * 60 || horaireFin < this.horaireDepart) {
            throw new IllegalArgumentException("L'horaire de fin est invalide ou antérieur au départ.");
        }
        this.horaireFin = horaireFin;
    }
    
    public final void setSite(Site site) {
        if (site == null) throw new IllegalArgumentException("Le site ne peut pas être nul.");
        this.site = site;
    }

    public final void setSport(Sport sport) {
        if (sport == null) throw new IllegalArgumentException("Le sport ne peut pas être nul.");
        this.sport = sport;
    }

    public final void setJournee(Journee journee) {
        if (journee == null) throw new IllegalArgumentException("La journée ne peut pas être nulle.");
        this.journee = journee;
    }

    public void setBesoins(Map<Competence, Integer> besoins) {
        this.besoins = besoins;
    }

    /**
     * Méthode de traitement qui retourne la durée du DPS en minutes.
     * @return la durée du DPS en minutes.
     */
    public int getDureeEnMinutes() {
        return this.horaireFin - this.horaireDepart;
    }

    /**
     * Affiche l'horaire de manière lisible (ex: "08:30").
     * @param horaireEnMinutes l'horaire en minutes depuis minuit.
     * @return Une chaîne de caractères formatée.
     */
    private String formatHoraire(int horaireEnMinutes) {
        int heures = horaireEnMinutes / 60;
        int minutes = horaireEnMinutes % 60;
        return String.format("%02d:%02d", heures, minutes);
    }

    @Override
    public String toString() {
        return "DPS " + id + " [" + sport.getNom() + " à " + site.getNom() + "] le " + journee 
                + " de " + formatHoraire(horaireDepart) + " à " + formatHoraire(horaireFin);
    }
}
