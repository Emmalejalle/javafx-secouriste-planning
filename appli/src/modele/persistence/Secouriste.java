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

    /**
     * Retourne la liste des compétences du secouriste.
     * @return Une ArrayList contenant les compétences.
     */
    public ArrayList<Competence> getCompetences() {
        return this.competences;
    }

    /**
     * Fixe la liste des compétences du secouriste.
     * @param competences la liste des compétences à fixer.
     */
    public void setCompetences(ArrayList<Competence> competences) {
        this.competences = competences;
    }

    /**
     * Retourne la liste des disponibilités du secouriste.
     * @return Une ArrayList contenant les disponibilités (objets Journee).
     */
    public ArrayList<Journee> getDisponibilites() {
        return this.disponibilites;
    }

    /**
     * Fixe la liste des disponibilités du secouriste.
     * @param disponibilites la liste des disponibilités à fixer.
     */
    public void setDisponibilites(ArrayList<Journee> disponibilites) {
        this.disponibilites = disponibilites;
    }

    /**
     * Verifie si le secouriste à une compétence
     * @param competence
     * @return  true si le secouriste possède la compétence, false sinon.
     */
    public boolean aCompetence(Competence competence) {
        if (competence == null) {
            return false;
        }
        //comparaison des id des compétences
        for (Competence c : this.competences) {
            if (c.getIdComp() == competence.getIdComp()) {
                return true;
            }
        }
        return this.competences.contains(competence);
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
}