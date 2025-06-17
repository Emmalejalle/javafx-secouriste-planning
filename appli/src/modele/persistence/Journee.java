package modele.persistence;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * La classe Journee représente une date spécifique en utilisant des entiers séparés.
 * La logique de gestion des secouristes disponibles est déportée dans les DAOs.
 * 
 * 
 * @author Emilien EMERIAU et Elie Tardy
 * @version 1.0
 */
public class Journee {

    /**
     * L'ID unique de la journée, généré par la base de données.
     */
    private long id;

    /**
     * Le jour du mois (1-31).
     */
    private int jour;

    /**
     * Le mois de l'année (1-12).
     */
    private int mois;

    /**
     * L'année.
     */
    private int annee;

    /**
     * Constructeur pour créer une journée.
     * @param id L'identifiant unique de la journée.
     * @param jour Le jour du mois (1-31).
     * @param mois Le mois de l'année (1-12).
     * @param annee L'année.
     */
    public Journee(long id, int jour, int mois, int annee) {
        // La validation est faite dans les setters.
        this.setId(id);
        this.setJour(jour);
        this.setMois(mois);
        this.setAnnee(annee);
    }
    
    // --- Getters ---
    public long getId() {
        return this.id;
    }

    public int getJour() {
        return this.jour;
    }

    public int getMois() {
        return this.mois;
    }

    public int getAnnee() {
        return this.annee;
    }

    // --- Setters avec validation ---
    public final void setId(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("L'ID de la journée doit être positif ou nul.");
        }
        this.id = id;
    }

    public final void setJour(int jour) {
        if (jour < 1 || jour > 31) {
            throw new IllegalArgumentException("Le jour doit être compris entre 1 et 31.");
        }
        this.jour = jour;
    }

    public final void setMois(int mois) {
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Le mois doit être compris entre 1 et 12.");
        }
        this.mois = mois;
    }

    public final void setAnnee(int annee) {
        if (annee < 2000) { // On peut supposer une année minimale raisonnable
            throw new IllegalArgumentException("L'année semble invalide.");
        }
        this.annee = annee;
    }

     /**
     * Méthode de traitement qui retourne le nom du jour de la semaine en français.
     * @return Le nom du jour (ex: "Lundi", "Mardi", ...).
     */
    public String getJourDeLaSemaine() {
        // Crée un objet LocalDate à partir des attributs pour utiliser les API modernes
        LocalDate date = LocalDate.of(this.annee, this.mois, this.jour);
        DayOfWeek jourDeSemaine = date.getDayOfWeek();
        
        // Retourne le nom complet du jour en français
        return jourDeSemaine.getDisplayName(TextStyle.FULL, Locale.FRENCH);
    }

    @Override
    public String toString() {
        // Formatte la date en "jour/mois/année" avec des zéros initiaux si nécessaire.
        return String.format("%02d/%02d/%d", jour, mois, annee);
    }
}
