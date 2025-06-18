package modele.service;

import modele.DAO.CompetenceDAO;
import modele.persistence.Competence;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.stream.Stream; 

/**
 * Couche de service pour la gestion des compétences.
 * Contient la logique métier et communique avec le DAO.
 */
public class CompetenceManagement {

    private CompetenceDAO competenceDAO;

    public CompetenceManagement() {
        this.competenceDAO = new CompetenceDAO();
    }

    /**
     * Récupère la liste de toutes les compétences.
     * @return La liste complète des compétences.
     * @throws SQLException
     */
    public List<Competence> listerToutesLesCompetences() throws SQLException {
        // Pour l'instant, appelle simplement le DAO.
        // On pourrait ajouter ici de la logique (ex: mise en cache).
        return competenceDAO.findAll();
    }

    /**
     * Crée une nouvelle compétence après validation.
     * @param intitule Le nom de la nouvelle compétence.
     * @param abrev L'abréviation.
     * @param prerequis La liste des compétences prérequises.
     * @return true si la création a réussi, false sinon.
     */
    public boolean creerCompetence(String intitule, String abrev, List<Competence> prerequis) throws SQLException {
        // Ici, on pourrait ajouter de la logique métier.
        // Par exemple, vérifier si une compétence avec le même intitulé n'existe pas déjà.
        Competence nouvelleCompetence = new Competence(intitule, abrev);
        if (prerequis != null) {
            nouvelleCompetence.setPrerequis(new ArrayList<>(prerequis));
        }

        competenceDAO.create(nouvelleCompetence);
        return true;
    }

    /**
     * Met à jour une compétence existante.
     * @param competence L'objet compétence avec les nouvelles informations.
     * @param nouvelIntitule Le nouveau nom.
     * @param nouvelleAbrev La nouvelle abréviation.
     * @param nouveauxPrerequis La nouvelle liste de prérequis.
     * @return true si la mise à jour a réussi.
     */
    public boolean modifierCompetence(Competence competence, String nouvelIntitule, String nouvelleAbrev, List<Competence> nouveauxPrerequis) throws SQLException {
        competence.setIntitule(nouvelIntitule);
        competence.setAbrevComp(nouvelleAbrev);
        competence.setPrerequis(new ArrayList<>(nouveauxPrerequis));

        competenceDAO.update(competence);
        return true;
    }

    /**
     * Supprime une compétence.
     * @param competence La compétence à supprimer.
     * @return true si la suppression a réussi.
     */
    public boolean supprimerCompetence(Competence competence) throws SQLException {
        competenceDAO.delete(competence);
        return true;
    }

    /**
     * Filtre une liste de compétences en mémoire.
     * @param liste La liste complète à filtrer.
     * @param recherche Le texte à rechercher.
     * @return Une nouvelle liste contenant les résultats filtrés.
     */
    public List<Competence> filtrerCompetences(List<Competence> liste, String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return liste;
        }
        return liste.stream()
            .filter(c -> c.getIntitule().toLowerCase().contains(recherche.toLowerCase()))
            .collect(Collectors.toList());
    }
}