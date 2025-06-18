package modele.persistence;
/**
 * La classe administrateur le compte de l'administrateur.
 * @author Elie Tardy et Emilien EMERIAU
 * @version 2.0
 */
public class Admin extends User {

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
    public Admin(long id, String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse) {
        super(id, mdp, nom, prenom, dateNaissance, email, tel, adresse);

    }
}
