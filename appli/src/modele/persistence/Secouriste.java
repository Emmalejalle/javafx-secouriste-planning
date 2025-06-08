package modele.persistence;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * la classe secouriste qui à pour base User
 * @author Elie Tardy
 * @version 1.0
 */
public class Secouriste extends User {
    
    /**
     * Liste des disponibilités
     */
    private ArrayList<Journee> disponibilites = new ArrayList<Journee>();
    
    /**
     * le constructeur
     * @param id l'id
     * @param mdp le mot de passe
     * @param nom le nom
     * @param prenom le prenom
     * @param dateNaissance la date de naissance
     * @param email l'email
     * @param tel le tel
     * @param adresse l'adresse
     */
    public Secouriste(long id, String mdp, String nom, String prenom, LocalDate dateNaissance, String email, String tel, String adresse) {
        super(id, mdp, nom, prenom, dateNaissance, email, tel, adresse);
        
    }

    /**
     * Ajoute une disponibilité à un secouriste pour une journee sauf si la disponibilité existe deja alors message d'erreur
     * @param journee la journee ajouter
     * @throws IllegalArgumentException si la disponibilité existe deja ou est null
     */
    public void ajouterDisponibiliter(Journee journee) {
        if(journee == null) {
            throw new IllegalArgumentException("La disponibilité est null");
        } else if (disponibilites.contains(journee)) {
            throw new IllegalArgumentException("La disponibilité existe deja");
        } else {
            disponibilites.add(journee);
        }   

    }


    
    /**
     * Retire une disponibilité à un secouriste pour une journee sauf si la disponibilité existe deja alors message d'erreur
     * @param journee la journee ajouter
     * @throws IllegalArgumentException si la disponibilité existe deja ou est null
     */
    public void retirerDisponibiliter(Journee journee) {
        if(journee == null) {
            throw new IllegalArgumentException("La disponibilité est null");
        } else if (disponibilites.contains(journee)) {
            disponibilites.remove(journee);
        } else {
            throw new IllegalArgumentException("La disponibilité n'existe pas");
        }   

    }


    /**
     * Vérifie si un secouriste est disponible pour une journée donnée.
     * @param journee la journée à vérifier
     * @return true si le secouriste est disponible pour la journée, false sinon
     */
    public boolean estDisponible(Journee journee) {
        boolean ret;
        if(disponibilites.contains(journee)) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    



    
}
