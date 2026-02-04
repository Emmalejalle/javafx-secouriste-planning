package modele.service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modele.DAO.CompetenceDAO;
import modele.graphe.algorithme.DAGMethode;
import modele.graphe.modele.GrapheDAG;
import modele.persistence.Competence;

/**
 * La classe CompetenceManagement gère les opérations liées aux compétences.
 * Elle permet de lister, créer, modifier et supprimer des compétences,
 * tout en s'assurant que les modifications respectent les contraintes de dépendances.
 */
public class CompetenceManagement {

    private CompetenceDAO competenceDAO;
    private DAGMethode dagMethode;

    public CompetenceManagement() {
        this.competenceDAO = new CompetenceDAO();
        this.dagMethode = new DAGMethode();
    }

    /**
     * Renvoie une liste de toutes les compétences en BDD.
     * @return Une liste de toutes les compétences.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public List<Competence> listerToutesLesCompetences() throws SQLException {
        return competenceDAO.findAll();
    }

    /**
     * Crée une nouvelle compétence en base de données.
     * 
     * @param intitule - l'intitulé de la compétence
     * @param abrev - l'abréviation de la compétence
     * @param prerequis - les compétences pré-requises
     * @return <code>true</code> si la compétence a été créée, <code>false</code> sinon.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public boolean creerCompetence(String intitule, String abrev, List<Competence> prerequis) throws SQLException {
        if (intitule == null || intitule.trim().isEmpty() || abrev == null || abrev.trim().isEmpty()) {
            throw new IllegalArgumentException("L'intitulé et l'abréviation ne peuvent pas être vides.");
        }

        Competence nouvelleCompetence = new Competence(intitule, abrev);
        nouvelleCompetence.setPrerequis(new ArrayList<>(prerequis));

        
        List<Competence> competencesFutures = new ArrayList<>(this.listerToutesLesCompetences());
        competencesFutures.add(nouvelleCompetence);

        
        GrapheDAG<Competence> grapheTest =  GrapheDAG.creerGrapheDeCompetences(competencesFutures);

       
        if (!dagMethode.estUnDAG(grapheTest)) {
            System.err.println("ERREUR: La création de cette compétence créerait un cycle de dépendances.");
            return false; 
        }
        

        
        competenceDAO.create(nouvelleCompetence);
        return true; 
    }

    /**
     * Modifie une compétence en base de données.
     * 
     * @param competence - la compétence à modifier
     * @param intitule - l'intitulé de la compétence
     * @param abrev - l'abréviation de la compétence
     * @param prerequis - les compétences pré-requises
     * @return <code>true</code> si la compétence a été modifiée, <code>false</code> sinon.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public boolean modifierCompetence(Competence competence, String intitule, String abrev, List<Competence> prerequis) throws SQLException {
        // on garde en memoire les anciens parametre de la competence si jamais l'update provoque un DAG
        String ancienIntitule = competence.getIntitule();
        String ancienAbrev = competence.getAbrevComp();
        ArrayList<Competence> ancienPrerequis = competence.getPrerequis();
    
        // On met à jour l'objet compétence en mémoire pour simuler le changement
        competence.setIntitule(intitule);
        competence.setAbrevComp(abrev); 
        competence.setPrerequis(new ArrayList<>(prerequis));
        competenceDAO.update(competence);


        // --- Début de la vérification du DAG ---
        // 1. On récupère toutes les compétences (dont celle qu'on vient de modifier en mémoire)
        List<Competence> competencesFutures = this.listerToutesLesCompetences();

        // 2. On construit le graphe de test
        GrapheDAG<Competence> grapheTest = GrapheDAG.creerGrapheDeCompetences(competencesFutures);

        // 3. On vérifie s'il y a un cycle
        if (!dagMethode.estUnDAG(grapheTest)) {
            // Si le graphe n'est pas un DAG, on annule les changements sur l'objet compétence
            competence.setIntitule(ancienIntitule);
            competence.setAbrevComp(ancienAbrev);
            competence.setPrerequis(ancienPrerequis);
            System.err.println("ERREUR: La modification de cette compétence créerait un cycle de dépendances.");
            // Important : on pourrait vouloir annuler les changements sur l'objet 'competence' ici,
            // mais comme on va rafraîchir les données, ce n'est pas critique.
            return false; // On refuse la modification
        }
        // --- Fin de la vérification ---

        return true;
    }

    /**
     * Supprime une compétence de la base de données.
     * 
     * @param competence - La compétence à supprimer.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public void supprimerCompetence(Competence competence) throws SQLException {
        competenceDAO.delete(competence);
    }

    /**
     * Filtre une liste de compétences en mémoire basé sur un texte de recherche.
     * La recherche est sensible à la casse.
     * 
     * @param liste - la liste de compétences à filtrer
     * @param recherche - le texte à rechercher
     * @return une liste de compétences dont le nom contient le texte de recherche
     */
    public List<Competence> filtrerCompetences(List<Competence> liste, String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return liste;
        }
        String rechercheMiniscule = recherche.toLowerCase();
        return liste.stream()
            .filter(c -> c.getIntitule().toLowerCase().contains(rechercheMiniscule))
            .collect(Collectors.toList());
    }

    /**
     * Cherche une compétence par son intitulé.
     * La recherche est sensible à la casse.
     * 
     * @param intitule - l'intitulé de la compétence à chercher
     * @return la compétence trouvée, ou null si aucune compétence n'est trouvée avec cet intitulé.
     * @throws SQLException En cas d'erreur d'accès à la base de données.
     */
    public Competence findByIntitule(String intitule) throws SQLException {
        return listerToutesLesCompetences().stream()
            .filter(c -> c.getIntitule().equals(intitule))
            .findFirst()
            .orElse(null);
    }
}