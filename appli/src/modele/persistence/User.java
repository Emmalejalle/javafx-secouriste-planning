package modele.persistence;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * La classe User est la base sur laquelle s'appui les secouristes et les administrateurs.
 * @author Elie Tardy
 * @version 1.0
 */
public abstract class User {

    /**
     * l'id de l'utilisateur
     */
    private long id;

    /**
     * le mot de passe
     */
    private String mdp;

    /**
     * le nom
     */
    private String nom;

    /**
     * le prénom
     */
    private String prenom;

    /**
     * la date de naissance
     */
    private LocalDate dateNaissance;

    /**
     * l'email
     */
    private String email;

    /**
     * le tel
     */
    private String tel;

    /**
     * l'adresse
     */
    private String adresse;

    /**
     * le constructeur
     * @param id l'id de l'utilisateur
     * @param mdp le mot de passe
     * @param nom le nom
     * @param prenom le prénom
     * @param dateNaissance la date de naissance
     * @param email l'email
     * @param tel le tel
     * @param adresse l'adresse
     * @throws IllegalArgumentException si l'id, le mot de passe, le nom, le prénom, la date de naissance, l'email, le tel ou l'adresse sont null
     */
    public User(long id, String mdp, String nom, String prenom, LocalDate dateNaissance, String email, String tel, String adresse) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être négatif");
        } else {
            this.id = id;
        }
        
        if (mdp == null || mdp.length() < 8 || mdp.length() > 20) {
            throw new IllegalArgumentException("Le mot de passe de l'utilisateur est invalide");
        } else {
            this.mdp = mdp;
        }
        
        if (nom == null) {
            throw new IllegalArgumentException("Le nom de l'utilisateur ne peut pas être null");
        } else {
            this.nom = nom;
        }
        
        if (prenom == null) {
            throw new IllegalArgumentException("Le prénom de l'utilisateur ne peut pas être null");
        } else {
            this.prenom = prenom;
        }
        
        if (dateNaissance == null) {
            throw new IllegalArgumentException("La date de naissance de l'utilisateur ne peut pas être null");
        } else {
            this.dateNaissance = dateNaissance;
        }
        boolean verifMail = isValidEmail(email);
        if (verifMail == false) {
            throw new IllegalArgumentException("L'email de l'utilisateur ne peut pas être null");
        } else {
            this.email = email;
        }
        
        if (tel == null) {
            throw new IllegalArgumentException("Le téléphone de l'utilisateur ne peut pas être null");
        } else if (tel.length() != 10 || tel.charAt(0) != '0') {
            throw new IllegalArgumentException("Le téléphone de l'utilisateur doit avoir 10 chiffres");
        
        }else {
            this.tel = tel;
        }
        
        if (adresse == null) {
            throw new IllegalArgumentException("L'adresse de l'utilisateur ne peut pas être null");
        } else {
            this.adresse = adresse;
        }

    }

    /**
     * le guetter de l'id
     * @return l'id de l'utilisateur
     */
    public long getId() {
        return this.id;
    }

    /**
     * le guetter du mot de passe
     * @return le mot de passe de l'utilisateur
     */
    public String getMdp() {
        return this.mdp;
    }

    /**
     * le guetter du nom
     * @return le nom de l'utilisateur
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * le guetter du prénom
     * @return le prénom de l'utilisateur
     */
    public String getPrenom() {
        return this.prenom;
    }


    /**
     * le guetter de la date de naissance
     * @return la date de naissance de l'utilisateur
     */
    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    /**
     * le guetter de l'email
     * @return l'email de l'utilisateur
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * le guetter du tel
     * @return le tel de l'utilisateur
     */
    public String getTel() {
        return this.tel;
    }

    /**
     * le guetter de l'adresse
     * @return l'adresse de l'utilisateur
     */
    public String getAdresse() {
        return this.adresse;
    }

    /**
     * le setter de l'id
     * @param id l'id de l'utilisateur
     * @throws IllegalArgumentException si l'id est négatif
     */
    public void setId(long id) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("L'id de l'utilisateur ne peut pas être négatif");
        } else {
            this.id = id;
        }
    }

    /**
     * le setter du mot de passe
     * @param mdp le mot de passe de l'utilisateur
     * @throws IllegalArgumentException si le mot de passe est null
     */
    public void setMdp(String mdp) throws IllegalArgumentException {
        if (mdp == null || mdp.length() < 8 || mdp.length() > 20) {
            throw new IllegalArgumentException("Le mot de passe de l'utilisateur est invalide");
        } else {
            this.mdp = mdp;
        }
    }

    /**
     * le setter du nom
     * @param nom le nom de l'utilisateur
     * @throws IllegalArgumentException si le nom est null
     */
    public void setNom(String nom) throws IllegalArgumentException {
        if (nom == null) {
            throw new IllegalArgumentException("Le nom de l'utilisateur ne peut pas être null");
        } else {
            this.nom = nom;
        }
    }

    /**
     * le setter du prénom
     * @param prenom le prénom de l'utilisateur
     * @throws IllegalArgumentException si le prénom est null
     */
    public void setPrenom(String prenom) throws IllegalArgumentException {
        if (prenom == null) {
            throw new IllegalArgumentException("Le prénom de l'utilisateur ne peut pas être null");
        } else {
            this.prenom = prenom;
        }
    }

    /**
     * le setter de la date de naissance
     * @param dateNaissance la date de naissance de l'utilisateur
     * @throws IllegalArgumentException si la date de naissance est null
     */
    public void setDateNaissance(LocalDate dateNaissance) throws IllegalArgumentException {
        if (dateNaissance == null) {
            throw new IllegalArgumentException("La date de naissance de l'utilisateur ne peut pas être null");
        } else {
            this.dateNaissance = dateNaissance;
        }
    }

    /**
     * le setter de l'email
     * @param email l'email de l'utilisateur
     * @throws IllegalArgumentException si l'email est null
     */
    public void setEmail(String email) throws IllegalArgumentException {
        boolean verifMail = isValidEmail(email);
        if (verifMail == false) {
            throw new IllegalArgumentException("L'email de l'utilisateur ne peut pas être null");
        } else {
            this.email = email;
        }
    }

    /**
     * le setter du tel
     * @param tel le tel de l'utilisateur
     * @throws IllegalArgumentException si le tel est null
     */
    public void setTel(String tel) throws IllegalArgumentException {
        if (tel == null || tel.length() != 10 || !tel.matches("[0-9]+")) {
            throw new IllegalArgumentException("Le tel de l'utilisateur ne peut pas être null");
        } else {
            this.tel = tel;
        }
    }

    /**
     * le setter de l'adresse
     * @param adresse l'adresse de l'utilisateur
     * @throws IllegalArgumentException si l'adresse est null
     */
    public void setAdresse(String adresse) throws IllegalArgumentException {
        if (adresse == null) {
            throw new IllegalArgumentException("L'adresse de l'utilisateur ne peut pas être null");
        } else {
            this.adresse = adresse;
        }
    }

    /**
     * retourne une representation textuelle de l'utilisateur sous forme de chaine
     * @return une chaine representant l'utilisateur
     */
    public String toString() {
        String ret = "Fiche utilisateur :\n";
        ret += "ID : " + this.id + "\n";
        ret += "Nom : " + this.nom + "\n";
        ret += "Prenom : " + this.prenom + "\n";
        ret += "Date de naissance : " + this.dateNaissance + "\n";
        ret += "Email : " + this.email + "\n";
        ret += "Tel : " + this.tel + "\n";
        ret += "Adresse : " + this.adresse + "\n";
        return ret;
    }




    // Regex 
    private static final String EMAIL_REGEX =
    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    /**
     * Vérifie si l'email est valide
     * @param email l'email  valider
     * @return vrai si l'email est valide, faux sinon
     */
    public static boolean isValidEmail(String email) {
        boolean ret = true;
        if (email == null) {
            ret = false;
        }
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            ret = false;
        }
        return ret;
    }

}
