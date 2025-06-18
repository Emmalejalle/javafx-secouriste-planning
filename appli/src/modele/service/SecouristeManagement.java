package modele.service;

import modele.DAO.UserDAO;
import modele.persistence.Competence;
import modele.persistence.Secouriste;
import modele.persistence.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Couche de service pour la gestion des Secouristes.
 * Contient la logique métier (validation, etc.) et communique avec le UserDAO.
 */
public class SecouristeManagement {

    private UserDAO userDAO;

    public SecouristeManagement() {
        this.userDAO = new UserDAO();
    }

    /**
     * Récupère la liste de tous les utilisateurs qui sont des secouristes.
     * @return Une liste d'objets Secouriste.
     * @throws SQLException
     */
    public List<Secouriste> listerTousLesSecouristes() throws SQLException {
        // On récupère tous les utilisateurs et on ne garde que ceux qui sont une instance de Secouriste.
        return userDAO.findAll().stream()
            .filter(user -> user instanceof Secouriste)
            .map(user -> (Secouriste) user)
            .collect(Collectors.toList());
    }

    /**
     * Crée un nouveau secouriste dans la base de données.
     */
    public void creerSecouriste(String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse, List<Competence> competences) throws SQLException {
        // La validation des données est déjà faite dans le constructeur de Secouriste, c'est parfait.
        Secouriste nouveauSecouriste = new Secouriste(0, mdp, nom, prenom, dateNaissance, email, tel, adresse);
        nouveauSecouriste.setCompetences(new ArrayList<>(competences));
        
        userDAO.create(nouveauSecouriste);
    }

    /**
     * Met à jour un secouriste existant.
     */
    public void modifierSecouriste(Secouriste secouriste, String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse, List<Competence> competences) throws SQLException {
        // On met à jour les champs de l'objet existant.
        // Les setters contiennent la logique de validation.
        secouriste.setMdp(mdp);
        secouriste.setNom(nom);
        secouriste.setPrenom(prenom);
        secouriste.setDateNaissance(dateNaissance);
        secouriste.setEmail(email);
        secouriste.setTel(tel);
        secouriste.setAdresse(adresse);
        secouriste.setCompetences(new ArrayList<>(competences));
        
        userDAO.update(secouriste);
    }

    /**
     * Supprime un secouriste de la base de données.
     */
    public void supprimerSecouriste(Secouriste secouriste) throws SQLException {
        userDAO.delete(secouriste);
    }
    
    /**
     * Filtre une liste de secouristes en mémoire basé sur un texte de recherche.
     */
    public List<Secouriste> filtrerSecouristes(List<Secouriste> liste, String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return liste;
        }
        String rechercheMiniscule = recherche.toLowerCase();
        return liste.stream()
            .filter(s -> s.getNom().toLowerCase().contains(rechercheMiniscule) || s.getPrenom().toLowerCase().contains(rechercheMiniscule))
            .collect(Collectors.toList());
    }
}