package model;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Cette classe sert à implémenter un sport
 */
public class Sport{

    private String nom ;
    private String code;
    // Map pour stocker les DPS associés à ce sport
    // La clé c l'identifiant du DPS, la valeur c l objet DPS
    private HashMap<Long, DPS> dpsMap = new HashMap<>();

//constructeur
    public Sport(String nom, String code) {
        this.nom = nom;
        this.code = code;
    }



    public String getNom() {
        return nom;
    }

    public String getCode() {
        return code;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCode(String code) {
        this.code = code;
    }
    

    
    public String toString() {
        return "Sport{" +
                "nom='" + nom + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
/**
   public Site[] voirSiteSport(){
        Site [] tabSite = new Site[dpsMap.size()];
        int i = 0;
        for (Site site : dpsMap.values()) {
            tabSite[i] = site.getSite();
            i++;
        }
        return tabSite;
    }
    */


  

}