package modele.persistence;

/**
 * La classe Sport représente une discipline sportive.
 * C'est un objet de persistance simple (POJO).
 * La logique de récupération des DPS ou des sites associés est gérée par les DAOs.
 * 
 * @author Emilien EMERIAU et Elie Tardy
 * @version 1.0
 */
public class Sport {

    /**
     * Le code unique du sport (ex: "SKI-ALP").
     */
    private long code;

    /**
     * Le nom complet du sport (ex: "Ski Alpin").
     */
    private String nom;

    /**
     * Constructeur de la classe Sport.
     * @param code Le code unique du sport.
     * @param nom Le nom complet du sport.
     */
    public Sport(long code, String nom) {
        // On appelle les setters qui contiennent la logique de validation.
        if ( code == 0 || code < 0) {
            throw new IllegalArgumentException("Le code du sport ne peut pas être négatif ou zéro.");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du sport ne peut pas être nul ou vide.");
        }
        if (nom.length() > 50) {
            throw new IllegalArgumentException("Le nom du sport ne peut pas dépasser 50 caractères.");
        }
        this.code = code;
        this.setNom(nom);
    }

    /**
     * Constructeur sans code
     * @param nom Le nom complet du sport.
     * @return Un objet Sport avec un code par défaut -1
     */
    public Sport(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du sport ne peut pas être nul ou vide.");
        }
        if (nom.length() > 50) {
            throw new IllegalArgumentException("Le nom du sport ne peut pas dépasser 50 caractères.");
        }
        this.code = -1; // Code par défaut
        this.nom = nom;
    }

    // --- Getters ---
    public long getCode() {
        return this.code;
    }

    public String getNom() {
        return this.nom;
    }

    // --- Setters avec validation ---
    public final void setCode(long code) {
        if (code == 0 || code < 0) {
            throw new IllegalArgumentException("Le code du sport ne peut pas etre null ou zéro.");
        }
        this.code = code;
    }

    public final void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du sport ne peut pas être nul ou vide.");
        }
        if (nom.length() > 50) {
            throw new IllegalArgumentException("Le nom du sport ne peut pas dépasser 50 caractères.");
        }
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Sport{" +
                "code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }
}
