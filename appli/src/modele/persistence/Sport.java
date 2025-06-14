package modele.persistence;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Cette classe sert à implémenter un sport
 */
public class Sport{

    /**
     * Le nom du sport
     */
    private String nom;

    /**
     * Le code du sport
     */
    private String code;

    /**
     * La liste des DPS du sport
     */
    private HashMap<Long, DPS> dpsMap = new HashMap<>();

    /**
     * Constructeur de la classe Sport
     * @param nom le nom du sport
     * @param code le code du sport
     */
    public Sport(String nom, String code) {
        if(nom == null || nom.length() > 50) {
            throw new IllegalArgumentException("Le nom ou le code du sport est null");
        } else {
            this.code = code;
        }

        if(code == null || code.length() > 10) {
            throw new IllegalArgumentException("Le nom ou le code du sport est null");
        } else {
            this.code = code;
        }
    }


    /**
     * Retourne le nom du sport
     * @return le nom du sport
     */
    public String getNom() {
        String ret = this.nom;
        return ret;
    }

    /**
     * Retourne le code du sport
     * @return le code du sport
     */
    public String getCode() {
        String ret = this.code;
        return ret;
    }

    /**
     * Met le nom du sport
     * @param nom le nom du sport
     */
    public void setNom(String nom) {
        if(nom == null || nom.length() > 50) {
            throw new IllegalArgumentException("Le nom ou le code du sport est null");
        } else {
            this.nom = nom;
        }
    }

    /**
     * Met le code du sport
     * @param code le code du sport
     */
    public void setCode(String code) {
        if(code == null || code.length() > 10) {
            throw new IllegalArgumentException("Le nom ou le code du sport est null");
        } else {
            this.code = code;
        }
    }
    

    /**
     * Retourne une chaine de caractères décrivant le sport.
     * @return une chaine de caractères décrivant le sport
     */
    public String toString() {
        String ret = "Sport{" + "nom=" + nom + ", code=" + code + '}';
        return ret;
    }

    /**
     * Retourne la liste des DPS du sport
     * @return la liste des DPS du sport
     */
   public Site[] voirSiteSport(){
        Site [] tabSite = new Site[dpsMap.size()];
        int i = 0;
        for (DPS dps : dpsMap.values()) {
            tabSite[i] = dps.getSite();
            i++;
        }
        return tabSite;
    }


  

}