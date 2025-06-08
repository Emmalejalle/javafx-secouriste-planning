package modele.persistence;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DPS{
    

    private long id;
    private int horaireDepart;
    private int horaireFin;

    
    private Site site;
    private Sport sport;
    private Journee journee;
    //private HashMap<Competence, Integer> besoins = new HashMap<>();
   


    //je mets pas journee car c'est initialisé par la classe Journee
    public DPS(long id, int horaireDepart, int horaireFin) {
        this.id = id;
        this.horaireDepart = horaireDepart;
        this.horaireFin = horaireFin;
    }

   
    public long getId() {
        return id;
    }
    public int getHoraireDepart() {
        return horaireDepart;
    }

    public int getHoraireFin() {
        return horaireFin;
    }

    public Journee getJournee() {
        Journee ret = this.journee;
        return ret;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setHoraireDepart(int horaireDepart) {
        this.horaireDepart = horaireDepart;
    }
    public void setHoraireFin(int horaireFin) {
        this.horaireFin = horaireFin;
    }
    public void setJournee(Journee journee) {
        this.journee = journee;
    }



     
    public String toString() {
        return "DPS{" +
                "id=" + id +
                ", horaireDepart=" + horaireDepart +
                ", horaireFin=" + horaireFin +
                ", journee=" + journee +
                '}';
    }

  
    public int getDuree() {
        return horaireFin - horaireDepart;
    }

    /**    // retourne vrai si le dps est deja placé sur une journée
    public boolean estComplet(){

        for (Integer besoin : besoins.values()) {
            if (besoin > 0) {
                return false; // si un besoin est inférieur ou égal à 0, le DPS n'est pas complet
            }
        }
        return true; // tous les besoins sont satisfaits
    }
*/



    


}