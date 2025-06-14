package modele.persistence;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * La classe DPS sert à implémenter un DPS
 * @author Elie Tardy
 * @version 1.0
 */
public class DPS{
    
    /**
     * L'id du DPS
     */
    private long id;

    /**
     * Le horaire de depart
     */
    private int horaireDepart;

    /**
     * Le horaire de fin
     */
    private int horaireFin;

    /**
     * Le site
     */
    private Site site;

    /**
     * Le sport
     */
    private ArrayList<Sport> sports = new ArrayList<Sport>();

    /**
     * La journee
     */
    private Journee journee;
    
    /**
     * Les besoins
     */
    private HashMap<Competence, Integer> besoins = null;
   

    /**
     * Le constructeur de la classe
     * @param id l'id du DPS
     * @param horaireDepart l'horaire de depart
     * @param horaireFin l'horaire de fin
     */
    public DPS(long id, int horaireDepart, int horaireFin) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id du DPS doit etre superieur a 0");
        } else {
            this.id = id;
        }

        if(horaireDepart < 0 || horaireFin < 0 || horaireFin < horaireDepart) {
            throw new IllegalArgumentException("L'horaire de DPS est invalide");
        } else {
            this.horaireDepart = horaireDepart;
            this.horaireFin = horaireFin;
        }
    }

   
    /**
     * Retourne l'id du DPS
     * @return l'id du DPS
     */
    public long getId() {
        long ret = this.id;
        return ret;
    }

    /**
     * Retourne l'horaire de depart du DPS
     * @return l'horaire de depart du DPS
     */
    public int getHoraireDepart() {
        int ret = this.horaireDepart;
        return ret;
    }

    /**
     * Retourne l'horaire de fin du DPS
     * @return l'horaire de fin du DPS
     */
    public int getHoraireFin() {
        int ret = this.horaireFin;
        return ret;
    }

    /**
     * Retourne la journee du DPS
     * @return la journee du DPS
     */
    public Journee getJournee() {
        Journee ret = this.journee;
        return ret;
    }


    /**
     * Modifie l'id du DPS
     * @param id l'id du DPS
     */
    public void setId(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id du DPS doit etre superieur a 0");
        } else {
            this.id = id;
        }
    }

    /**
     * Modifie l'horaire de depart du DPS
     * @param horaireDepart l'horaire de depart du DPS
     */
    public void setHoraireDepart(int horaireDepart) {
        if (horaireDepart < 0 || horaireFin < horaireDepart) {
            throw new IllegalArgumentException("L'horaire de DPS est invalide");
        } else {
            this.horaireDepart = horaireDepart;
        }
    }
    /**
     * Modifie l'horaire de fin du DPS
     * @param horaireFin l'horaire de fin du DPS
     * @throws IllegalArgumentException si l'horaire de fin est inférieur à l'horaire de départ
     */
    public void setHoraireFin(int horaireFin) {
        if(horaireFin < horaireDepart) {
            throw new IllegalArgumentException("L'horaire de DPS est invalide");
        } else {
            this.horaireFin = horaireFin;
        }
    }

    /**
     * Modifie la journee du DPS
     * @param journee la nouvelle journee du DPS
     */
    public void setJournee(Journee journee) {
        if(journee == null) {
            throw new IllegalArgumentException("La journee est null");
        } else {
            this.journee = journee;
        }
    }



     
    /**
     * Retourne une chaine de caractères décrivant le DPS.
     * @return une chaine de caractères décrivant le DPS
     */
    public String toString() {
        String ret = "DPS{" + "id=" + id + ", horaireDepart=" + horaireDepart + ", horaireFin=" + horaireFin + '}';
        return ret;
    }

  
    /**
     * Retourne la durée du DPS en heures.
     * @return la durée du DPS en heures
     */
    public int getDuree() {
        int ret = this.horaireFin - this.horaireDepart;
        return ret;
    }

    /**
     * Ajoute un sport au DPS.
     * @param sport le sport a ajouter
     */
    public void ajouterSport(Sport sport) {
        if(sport == null || sports.contains(sport)) {
            throw new IllegalArgumentException("Le sport est null");
        } else {
            this.sports.add(sport);
        }
    }

    /**
     * Retire un sport au DPS
     * @param sport le sport a retirer
     * @throws IllegalArgumentException si le sport ne peut pas etre retirer
    */
    public void retirerSport(Sport sport) {
        if(sport == null || !sports.contains(sport)) {
            throw new IllegalArgumentException("Le sport ne peut pas etre retirer");
        } else {
            this.sports.remove(sport);
        }
    }



}