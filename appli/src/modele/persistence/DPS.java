package modele.persistence;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

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
     * L'horaire de début en heure (0-23).
     */
    private int horaireDepart;

    /**
     * L'horaire de fin en heure (0-23).
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
     * @param horaireDepart L'horaire de début en heures.
     * @param horaireFin L'horaire de fin en heures.
     * @param site L'objet Site associé.
     * @param sport L'objet Sport associé.
     * @param journee L'objet Journee associé.
     */
    public DPS(long id, int horaireDepart, int horaireFin, Site site, Sport sport, Journee journee) {
        if (id < 0) {
            throw new IllegalArgumentException("L'ID du DPS ne peut pas être négatif.");
        }
        if (horaireDepart < 0 || horaireDepart >= 24) {
            throw new IllegalArgumentException("L'horaire de départ est invalide.");
        }
        if (horaireFin < 0 || horaireFin >= 24 || horaireFin < horaireDepart) {
            throw new IllegalArgumentException("L'horaire de fin est invalide ou antérieur au départ.");
        }
        if (site == null) {
            throw new IllegalArgumentException("Le site ne peut pas être null.");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Le sport ne peut pas être null.");
        }
        if (journee == null) {
            throw new IllegalArgumentException("La journée ne peut pas être null.");
        }
        this.id = id;
        this.setHoraireDepart(horaireDepart);
        this.setHoraireFin(horaireFin);
        this.setSite(site);
        this.setSport(sport);
        this.setJournee(journee);
        this.besoins = new HashMap<>(); // La liste des besoins est initialisée vide.
    }

    /**
     * Constructeur pour créer un DPS sans ID (utilisé lors de la création d'un nouveau DPS).
     * @param horaireDepart L'horaire de début en Heures.
     * @param horaireFin L'horaire de fin en heures.
     * @param site L'objet Site associé.
     * @param sport L'objet Sport associé.
     * @param journee L'objet Journee associé.
     */
    public DPS(int horaireDepart, int horaireFin, Site site, Sport sport, Journee journee) {
        if (horaireDepart < 0 || horaireDepart >= 24) {
            throw new IllegalArgumentException("L'horaire de départ est invalide.");
        }
        if (horaireFin < 0 || horaireFin >= 24 || horaireFin < horaireDepart) {
            throw new IllegalArgumentException("L'horaire de fin est invalide ou antérieur au départ.");
        }
        if (site == null) {
            throw new IllegalArgumentException("Le site ne peut pas être null.");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Le sport ne peut pas être null.");
        }
        if (journee == null) {
            throw new IllegalArgumentException("La journée ne peut pas être null.");
        }
        this.id = -1; // ID sera auto-généré par la BDD.
        this.setHoraireDepart(horaireDepart);
        this.setHoraireFin(horaireFin);
        this.setSite(site);
        this.setSport(sport);
        this.setJournee(journee);
        // Initialisation des besoins
        // à une nouvelle HashMap vide.
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
        if (horaireDepart < 0 || horaireDepart >= 24) {
            throw new IllegalArgumentException("L'horaire de départ est invalide.");
        }
        this.horaireDepart = horaireDepart;
    }

    public final void setHoraireFin(int horaireFin) {
        if (horaireFin < 0 || horaireFin >= 24 || horaireFin < this.horaireDepart) {
            throw new IllegalArgumentException("L'horaire de fin est invalide ou antérieur au départ.");
        }
        this.horaireFin = horaireFin;
    }
    
    public final void setSite(Site site) {
        if (site == null) throw new IllegalArgumentException("Le site ne peut pas être null.");
        this.site = site;
    }

    public final void setSport(Sport sport) {
        if (sport == null) throw new IllegalArgumentException("Le sport ne peut pas être null.");
        this.sport = sport;
    }

    public final void setJournee(Journee journee) {
        if (journee == null) throw new IllegalArgumentException("La journée ne peut pas être null.");
        this.journee = journee;
    }

    public void setBesoins(Map<Competence, Integer> besoins) {
        this.besoins = besoins;
    }

    /**
     * Méthode de traitement qui retourne la durée du DPS en heures.
     * @return la durée du DPS en heures.
     */
    public int getDuree() {
        return this.horaireFin - this.horaireDepart;
    }

    /**
     * Affiche l'horaire de manière lisible (ex 9h).
     * @param horaire l'horaire en heures depuis minuit.
     * @return Une chaîne de caractères formatée.
     */
    private String formatHoraire(int horaire) {
        int heures = horaire;
        return String.format("%02d:%02d", heures, 0);
    }

    /**
     * Retourne une liste des competences nécessaires pour ce DPS en ArrayList.
     * @return Une liste de Compétences.
     */
    public ArrayList<Competence> getListeCompetences() {
        ArrayList<Competence> listeCompetences = new ArrayList<>();
        for (Map.Entry<Competence, Integer> entry : besoins.entrySet()) {
            listeCompetences.add(entry.getKey());
        }
        return listeCompetences;
    }
    
    @Override
    public String toString() {
        return "DPS " + id + " [" + sport.getNom() + " à " + site.getNom() + "] le " + journee 
                + " de " + formatHoraire(horaireDepart) + " à " + formatHoraire(horaireFin);
    }
}
