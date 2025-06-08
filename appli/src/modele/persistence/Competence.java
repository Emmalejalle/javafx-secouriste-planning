package model;

import java.util.ArrayList;
public class Competence {
    private int idComp;
    private String intitule;
    private String prerequis = null;
    private ArrayList<Secouriste> secouristes = new ArrayList<Secouriste>();

    public Competence(int idComp, String intitule, String prerequis) {
        this.idComp = idComp;
        this.intitule = intitule;
        this.prerequis = prerequis;
    }

    public void ajouterSecouriste(Secouriste secouriste) {
        secouristes.add(secouriste);
    }

    public void retirerSecouriste(Secouriste secouriste) {
        secouristes.remove(secouriste);
    }



    public int getIdComp() {
        return this.idComp;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public void setIdComp(int idComp) {
        this.idComp = idComp;
    }   

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }  


    public String toString() {
        return this.idComp + " " + this.intitule + " " + this.prerequis;
    }

    
}
