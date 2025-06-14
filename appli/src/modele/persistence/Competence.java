package modele.persistence;

import java.util.ArrayList;

/**
 * La classe Competence sert à implémenter une competence
 * @author Elie Tardy
 * @version 1.0
 */
public class Competence {
    
    /**
     * L'id de la competence
     */
    private int idComp;

    /**
     * L'intitule de la competence
     */
    private String intitule;

    /**
     * l'abréviation de l'intitulé de la competence
     */
    private String abrevComp;

    /**
     * Les prerequis de la competence
     */
    private String prerequis;

    /**
     * Les secouristes ayant cette competence
     */
    private ArrayList<Secouriste> secouristes = new ArrayList<Secouriste>();

    /**
     * Le constructeur de la classe
     * @param idComp l'id de la competence
     * @param intitule l'intitule de la competence
     * @param abrevComp l'abreviation de l'intitule de la competence
     * @param prerequis les prerequis de la competence
     */
    public Competence(int idComp, String intitule,String abrevComp, String prerequis) {
        if(idComp < 0) {
            throw new IllegalArgumentException("L'id de la competence doit etre superieur a 0");
        } else {
            this.idComp = idComp;
        }

        if(intitule == null || intitule.length() < 2 || intitule.length() > 40) {
            throw new IllegalArgumentException("L'intitule de la competence doit etre compris entre 2 et 20 caracteres");
        } else {
            this.intitule = intitule;
        }
        
        if (abrevComp == null || abrevComp.length() > 5) {
            throw new IllegalArgumentException("L'abreviation de l'intitule de la competence doit etre inferieur a 5 caracteres");
        } else {
            this.abrevComp = abrevComp;
        }
        
        if(prerequis == null || prerequis.length() > 100) {
            throw new IllegalArgumentException("Les prerequis de la competence doivent etre inferieurs a 100 caracteres");
        } else {
            this.prerequis = prerequis;
        }
    }

    /**
     * Ajoute un secouriste à la liste des secouristes possédant cette compétence.
     * @param secouriste le secouriste à ajouter
     */
    public void ajouterSecouriste(Secouriste secouriste) {
        if (secouriste == null) {
            throw new IllegalArgumentException("Le secouriste est null");
        } else if(secouristes.contains(secouriste)) {
            throw new IllegalArgumentException("Le secouriste existe deja");
        }
        secouristes.add(secouriste);
    }

    /**
     * Retire un secouriste de la liste des secouristes possédant cette compétence.
     * @param secouriste le secouriste à retirer
     */
    public void retirerSecouriste(Secouriste secouriste) {
        if (secouriste == null) {
            throw new IllegalArgumentException("Le secouriste est null");
        } else if(secouristes.contains(secouriste)) {
            secouristes.remove(secouriste);
        } else {
            throw new IllegalArgumentException("Le secouriste n'existe pas");
        }
    }



    /**
     * Le getter de l'id de la competence
     * @return l'id de la competence
     */
    public int getIdComp() {
        int ret = this.idComp;
        return ret;
    }

    /**
     * Le getter de l'intitulé de la compétence
     * @return l'intitulé de la compétence
     */
    public String getIntitule() {
        String ret = this.intitule;
        return ret;
    }

    /**
     * Modifie l'id de la competence.
     * @param idComp l'id de la competence
     * @throws IllegalArgumentException si l'id de la competence est inferieur a 0
     */
    public void setIdComp(int idComp) {
        if(idComp < 0) {
            throw new IllegalArgumentException("L'id de la competence doit etre superieur a 0");
        } else {
            this.idComp = idComp;
        }
    }   

    /**
     * Modifie l'intitulé de la competence.
     * @param intitule l'intitulé de la competence
     */
    public void setIntitule(String intitule) {
        if(intitule == null || intitule.length() < 2 || intitule.length() > 40) {
            throw new IllegalArgumentException("L'intitule de la competence doit etre compris entre 2 et 20 caracteres");
        } else {
            this.intitule = intitule;
        }
    }  


    /**
     * Retourne une chaine de caracteres qui decrit la competence
     * @return une chaine de caracteres qui decrit la competence
     */
    public String toString() {
        String ret = "Fiche competence :\n";
        ret += "ID : " + this.idComp + "\n";
        ret += "Intitule : " + this.intitule + "\n";
        ret += "Abreviation : " + this.abrevComp + "\n";
        ret += "Prerequis : " + this.prerequis + "\n";
        return ret;
    }

    
}
