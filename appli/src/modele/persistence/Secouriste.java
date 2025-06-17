package modele.persistence;

import java.util.ArrayList;

/**
 * Représente un utilisateur de type secouriste.
 * Hérite de User et contiendra ses compétences et disponibilités.
 * @author Elie Tardy et Emilien EMERIAU
 * @version 2.1
 */
public class Secouriste extends User {
    
    /**
     * Liste des compétences du secouriste.
     * Cette liste sera chargée par le UserDAO.
     */
    private ArrayList<Competence> competences;

    /**
     * Liste des journées où le secouriste est disponible.
     * Cette liste sera chargée par le UserDAO.
     */
    private ArrayList<Journee> disponibilites;
    
    /**
     * Constructeur de la classe Secouriste.
     */
    public Secouriste(long id, String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse) {
        // Appelle le constructeur de la classe parente
        super(id, mdp, nom, prenom, dateNaissance, email, tel, adresse);
        
        // Initialise toujours les listes pour éviter les erreurs
        this.competences = new ArrayList<>();
        this.disponibilites = new ArrayList<>();
    }

    /**
     * Constructeur de la classe Secouriste sans ID.
     * Utilisé pour créer un nouveau secouriste avant l'insertion en base de données.
     */
    public Secouriste(String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse) {
        // Appelle le constructeur de la classe parente sans ID
        super(mdp, nom, prenom, dateNaissance, email, tel, adresse);
        
        // Initialise toujours les listes pour éviter les erreurs
        this.competences = new ArrayList<>();
        this.disponibilites = new ArrayList<>();
    }

    // --- Getters et Setters pour les listes (utilisés par le DAO) ---

    public ArrayList<Competence> getCompetences() {
        return this.competences;
    }

    public void setCompetences(ArrayList<Competence> competences) {
        this.competences = competences;
    }

    public ArrayList<Journee> getDisponibilites() {
        return this.disponibilites;
    }

    public void setDisponibilites(ArrayList<Journee> disponibilites) {
        this.disponibilites = disponibilites;
    }

    // --- Méthodes de traitement spécifiques au secouriste ---

    /**
     * Vérifie si le secouriste est disponible pour une journée donnée.
     * @param journee la journée à vérifier.
     * @return true si le secouriste est disponible ce jour-là, false sinon.
     */
    public boolean estDisponible(Journee journee) {
        if (journee == null) {
            return false;
        }
        return this.disponibilites.contains(journee);
    }

    /**
     * Vérifie si le secouriste possède une compétence spécifique.
     * @param competence la compétence à vérifier.
     * @return true si le secouriste possède la compétence, false sinon.
     */
    public boolean possedeCompetence(Competence competence) {
        if (competence == null) {
            return false;
        }
        return this.competences.contains(competence);
    }
}