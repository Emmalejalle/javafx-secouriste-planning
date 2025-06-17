package modele.persistence;

import java.util.ArrayList;


/**
 * La classe Competence sert à implémenter une competence
 * @author Emilien EMERIAU et Elie Tardy
 * @version 2.0
 */
public class Competence {
    
    /**
     * L'id de la competence
     */
    private long idComp;

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
    private ArrayList<Competence> prerequis;


    /**
     * Le constructeur de la classe sans id
     * @param intitule l'intitule de la competence
     * @param abrevComp l'abreviation de l'intitule de la competence
     */
    public Competence(String intitule, String abrevComp) {
        //si -1 la compétence est en cours de création
        this.idComp = -1;

        if(intitule == null || intitule.length() < 2 || intitule.length() > 40) {
            throw new IllegalArgumentException("L'intitule de la competence doit etre compris entre 2 et 40 caracteres");
        } else {
            this.intitule = intitule;
        }
        
        if (abrevComp == null || abrevComp.length() > 5) {
            throw new IllegalArgumentException("L'abreviation de l'intitule de la competence doit etre inferieur a 5 caracteres");
        } else {
            this.abrevComp = abrevComp;
        }

        this.prerequis = new ArrayList<Competence>();
    }

    /**
     * Le constructeur de la classe
     * @param idComp l'id de la competence
     * @param intitule l'intitule de la competence
     * @param abrevComp l'abreviation de l'intitule de la competence
     */
    public Competence(long idComp, String intitule,String abrevComp) {
        this.idComp = idComp;

        if(intitule == null || intitule.length() < -1 || intitule.length() > 40) {
            throw new IllegalArgumentException("L'intitule de la competence doit etre compris entre 2 et 40 caracteres");
        } else {
            this.intitule = intitule;
        }
        
        if (abrevComp == null || abrevComp.length() > 5) {
            throw new IllegalArgumentException("L'abreviation de l'intitule de la competence doit etre inferieur a 5 caracteres");
        } else {
            this.abrevComp = abrevComp;
        }

        this.prerequis = new ArrayList<Competence>();   
    }

    /**
     * Ajoute une competence en prerequis de la competence
     * @param prerequis la competence a ajouter en prerequis
     */
    public void ajouterPrerequis(Competence prerequis) {
        if (prerequis == null) {
            throw new IllegalArgumentException("Le prerequis ne peut pas etre null");
        }
        if (this.prerequis.contains(prerequis)) {
            throw new IllegalArgumentException("La competence est deja un prerequis de cette competence");
        }
        this.prerequis.add(prerequis);
    }

    /**
     * Supprime une competence des prerequis de la competence
     * @param prerequis la competence a supprimer des prerequis
     */
    public void supprimerPrerequis(Competence prerequis) {
        if (prerequis == null) {
            throw new IllegalArgumentException("Le prerequis ne peut pas etre null");
        }
        if (!this.prerequis.contains(prerequis)) {
            throw new IllegalArgumentException("La competence n'est pas un prerequis de cette competence");
        }
        this.prerequis.remove(prerequis);
    }

    /**
     * Check si une competence est un prerequis de la competence
     * @param autreCompetence la competence a tester
     */
    public boolean estPrerequis(Competence autreCompetence) {
        return this.prerequis.contains(autreCompetence);
    }

    // Pour le chargement paresseux
    /**
     * Le getter des prerequis de la competence
     * @return les prerequis de la competence
     */
    public ArrayList<Competence> getPrerequis() { 
        return this.prerequis; 
    }
    /**
     * Le setter des prerequis de la competence
     * @param prerequis les prerequis de la competence
     */
    public void setPrerequis(ArrayList<Competence> prerequis) {
        this.prerequis = prerequis; 
    }

    /**
     * Le getter de l'id de la competence
     * @return l'id de la competence
     */
    public long getIdComp() {
        long ret = this.idComp;
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
     * Le getter de l'abréviation de l'intitulé de la compétence
     * @return l'abréviation de l'intitulé de la compétence
     */
    public String getAbrevComp() {
        String ret = this.abrevComp;
        return ret;
    }

    /**
     * Modifie l'id de la competence.
     * @param idComp l'id de la competence
     * @throws IllegalArgumentException si l'id de la competence est inferieur a 0
     */
    public void setIdComp(long idComp) {
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
            throw new IllegalArgumentException("L'intitule de la competence doit etre compris entre 2 et 40 caracteres");
        } else {
            this.intitule = intitule;
        }
    }  

    /**
     * Modifie l'abréviation de l'intitulé de la competence.
     * @param abrevComp l'abréviation de l'intitulé de la competence
     */
    public void setAbrevComp(String abrevComp) {
        if (abrevComp == null || abrevComp.length() > 5) {
            throw new IllegalArgumentException("L'abreviation de l'intitule de la competence doit etre inferieur a 5 caracteres");
        } else {
            this.abrevComp = abrevComp;
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
        for (Competence c : this.prerequis) {
            ret += "Prerequis : " + c.getIntitule() + "\n";
        }
        return ret;
    }

    
}
