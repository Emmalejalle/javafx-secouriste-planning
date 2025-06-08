package modele.persistence;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Journee {
    /**
     * le jour
     */
    private int jour;

    /**
     * le mois
     */
    private int mois;

    /**
     * l'annee
     */
    private int annee;

    private ArrayList<Secouriste> secouristes = new ArrayList<Secouriste>();

    /**
     * le constructeur
     * @param jour le jour
     * @param mois le mois
     * @param annee l'annee
     */
    public Journee (int jour, int mois, int annee) {
        if (jour < 1 || jour > 31) {
            throw new IllegalArgumentException("Le jour doit être compris entre 1 et 31");
        } else {
            this.jour = jour;
        }

        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Le mois doit être compris entre 1 et 12");
        } else {
            this.mois = mois;
        }

        if (annee < 1) {
            throw new IllegalArgumentException("L'année doit être supérieure à 0");
        } else {
            this.annee = annee;
        }
        
    }

    /**
     * le getter du jour
     * @return le jour
     */
    public int getJour() {
        return this.jour;
    }

    /**
     * le getter du mois
     * @return le mois
     */
    public int getMois() {
        return this.mois;
    }

    /**
     * le getter de l'annee
     * @return l'annee
     */
    public int getAnnee() {
        return this.annee;
    }

    /**
     * setter du jour
     * @param jour le jour
     * @throws IllegalArgumentException si le jour est inférieur à 1 ou supérieur à 31
     */
    public void setJour(int jour) throws IllegalArgumentException {
        if (jour < 1 || jour > 31) {
            throw new IllegalArgumentException("Le jour doit être compris entre 1 et 31");
        } else {
            this.jour = jour;
        }
    }

    /**
     * setter du mois
     * @param mois le mois
     * @throws IllegalArgumentException si le mois est inférieur à 1 ou supérieur à 12
     */
    public void setMois(int mois) throws IllegalArgumentException {
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Le mois doit être compris entre 1 et 12");
        } else {
            this.mois = mois;
        }
    }


    /**
     * setter de l'annee
     * @param annee l'annee
     * @throws IllegalArgumentException si l'annee est inférieur à 1
     */
    public void setAnnee(int annee) throws IllegalArgumentException {
        if (annee < 1) {
            throw new IllegalArgumentException("L'année doit être supérieure à 0");
        } else {
            this.annee = annee;
        }
    }


    /**
     * Cette méthode sert à ajouter un secouriste pour une journée s'il y ai déjà sinon message d'erreur
     * @param secouriste le secouriste
     * @throws IllegalArgumentException si le secouriste existe deja ou est null
     */
    public void ajouterSecouriste(Secouriste secouriste) throws IllegalArgumentException {
        if (secouriste == null) {
            throw new IllegalArgumentException("Le secouriste est null");
        } else if (secouristes.contains(secouriste)) {
            throw new IllegalArgumentException("Le secouriste existe deja");
        } else {
            secouristes.add(secouriste);
        }
    }



    /**
     * Cette méthode sert à supprimer un secouriste pour une journée s'il n'y ai déjà pas sinon message d'erreur
     * @param secouriste le secouriste
     * @throws IllegalArgumentException si le secouriste existe deja ou est null
     */
    public void retirerSecouriste(Secouriste secouriste) throws IllegalArgumentException {
        if (secouriste == null) {
            throw new IllegalArgumentException("Le secouriste est null");
        } else if (secouristes.contains(secouriste)) {
            secouristes.remove(secouriste);
        } else {
            throw new IllegalArgumentException("Le secouriste n'existe pas");
        }
    }


    /**
     * Méthode qui met en string la journee
     * @return une chaine representant la journée
     */
    public String toString() {
        String ret = jour + "/" + mois + "/" + annee;
        return ret;
    }


    



    
}
