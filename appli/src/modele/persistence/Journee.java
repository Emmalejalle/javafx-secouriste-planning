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
        this.id = id;

        if (jour < 1 || jour > 31) {
            throw new IllegalArgumentException("Le jour doit être compris entre 1 et 31.");
        }
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Le mois doit être compris entre 1 et 12.");
        }
        if (annee < 2024) { // On peut supposer une année minimale raisonnable
            throw new IllegalArgumentException("L'année semble invalide (2024 ou plus récente).");
        }
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    /**
     * Constructeur pour créer une journée sans ID.
     * @param jour Le jour du mois (1-31).
     * @param mois Le mois de l'année (1-12).
     * @param annee L'année.
     */
    public Journee(int jour, int mois, int annee) {
        // La validation est faite dans les setters.
        this.id = -1; // ID par défaut pour une nouvelle journée
         // On utilise -1 pour indiquer que l'ID n'est pas encore défini.

        if (jour < 1 || jour > 31) {
            throw new IllegalArgumentException("Le jour doit être compris entre 1 et 31.");
        }
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Le mois doit être compris entre 1 et 12.");
        }
        if (annee < 2024) { // On peut supposer une année minimale raisonnable
            throw new IllegalArgumentException("L'année semble invalide.");
        }
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    
    
    // --- Getters ---
    public long getId() {
        return this.id;
    }

    /**
     * Renvoie le jour du mois (1-31).
     * @return Le jour du mois.
     */
    public int getJour() {
        return this.jour;
    }

    /**
     * Renvoie le mois de l'année (1-12).
     * @return Le mois de l'année.
     */
    public int getMois() {
        return this.mois;
    }

    /**
     * Renvoie l'année.
     * @return L'année.
     */
    public int getAnnee() {
        return this.annee;
    }

    // --- Setters avec validation ---
    public final void setId(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("L'ID de la journée doit être positif ou zéro.");
        }
        this.id = id;
    }

    /**
     * Définit le jour du mois pour cette journée.
     * 
     * @param jour Le jour du mois (doit être compris entre 1 et 31).
     * @throws IllegalArgumentException si le jour n'est pas dans la plage valide (1-31).
     */
    public final void setJour(int jour) {
        if (jour < 1 || jour > 31) {
            throw new IllegalArgumentException("Le jour doit être compris entre 1 et 31.");
        }
        this.jour = jour;
    }

    /**
     * Définit le mois de l'année pour cette journée.
     * 
     * @param mois Le mois de l'année (doit être compris entre 1 et 12).
     * @throws IllegalArgumentException si le mois n'est pas dans la plage valide (1-12).
     */
    public final void setMois(int mois) {
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Le mois doit être compris entre 1 et 12.");
        }
        this.mois = mois;
    }

    /**
     * Définit l'année pour cette journée.
     * 
     * @param annee L'année (doit être supérieure ou égale à 2024).
     * @throws IllegalArgumentException si l'année est inférieure à 2024.
     */
    public final void setAnnee(int annee) {
        if (annee < 2024) { // On peut supposer une année minimale raisonnable
            throw new IllegalArgumentException("L'année semble invalide (2024 ou plus récente).");
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

    /**
     * Renvoie une chaîne de caractères représentant la date, formatée en "jour/mois/année"
     * avec des zéros initiaux si nécessaire.
     * @return La date formatée en "jour/mois/année".
     */
    @Override
    public String toString() {
        // Formatte la date en "jour/mois/année" avec des zéros initiaux si nécessaire.
        return String.format("%02d/%02d/%d", jour, mois, annee);
    }
}
