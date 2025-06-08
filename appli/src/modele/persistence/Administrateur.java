package model;
import java.time.LocalDate;

/**
 * La classe administrateur le compte de l'administrateur.
 * @author Elie Tardy
 * @version 1.0
 */
public class Administrateur extends User {

    /**
     * Le constructeur de l'administrateur
     * @param id l'id de l'administrateur
     * @param mdp le mot de passe de l'administrateur
     * @param nom le nom de l'administrateur
     * @param prenom le prénom de l'administrateur
     * @param dateNaissance la date de naissance de l'administrateur
     * @param email l'email de l'administrateur
     * @param tel le tel de l'administrateur
     * @param adresse l'adresse de l'administrateur
     */
    public Administrateur(long id, String mdp, String nom, String prenom, LocalDate dateNaissance, String email, String tel, String adresse) {
        super(id, mdp, nom, prenom, dateNaissance, email, tel, adresse);

    }
}
