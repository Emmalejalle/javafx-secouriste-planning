package modele.service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modele.DAO.CompetenceDAO;
import modele.graphe.algorithme.DAGMethode;
import modele.graphe.modele.GrapheDAG;
import modele.persistence.Competence;


public class CompetenceManagement {

    private CompetenceDAO competenceDAO;
    private DAGMethode dagMethode;

    public CompetenceManagement() {
        this.competenceDAO = new CompetenceDAO();
        this.dagMethode = new DAGMethode();
    }

    public List<Competence> listerToutesLesCompetences() throws SQLException {
        return competenceDAO.findAll();
    }

    public boolean creerCompetence(String intitule, String abrev, List<Competence> prerequis) throws SQLException {
        if (intitule == null || intitule.trim().isEmpty() || abrev == null || abrev.trim().isEmpty()) {
            throw new IllegalArgumentException("L'intitulé et l'abréviation ne peuvent pas être vides.");
        }

        Competence nouvelleCompetence = new Competence(intitule, abrev);
        nouvelleCompetence.setPrerequis(new ArrayList<>(prerequis));

        
        List<Competence> competencesFutures = new ArrayList<>(this.listerToutesLesCompetences());
        competencesFutures.add(nouvelleCompetence);

        
        GrapheDAG<Competence> grapheTest = new GrapheDAG<>(competencesFutures);
        for (Competence c : competencesFutures) {
            for (Competence p : c.getPrerequis()) {
                grapheTest.ajouterArc(p, c); 
            }
        }

       
        if (!dagMethode.estUnDAG(grapheTest)) {
            System.err.println("ERREUR: La création de cette compétence créerait un cycle de dépendances.");
            return false; 
        }
        

        
        competenceDAO.create(nouvelleCompetence);
        return true; 
    }

        

    public boolean modifierCompetence(Competence competence, String intitule, String abrev, List<Competence> prerequis) throws SQLException {
        // On met à jour l'objet compétence en mémoire pour simuler le changement
        competence.setIntitule(intitule);
        competence.setAbrevComp(abrev);
        competence.setPrerequis(new ArrayList<>(prerequis));

        // --- Début de la vérification du DAG ---
        // 1. On récupère toutes les compétences (dont celle qu'on vient de modifier en mémoire)
        List<Competence> competencesFutures = this.listerToutesLesCompetences();

        // 2. On construit le graphe de test
        GrapheDAG<Competence> grapheTest = new GrapheDAG<>(competencesFutures);
        for (Competence c : competencesFutures) {
            for (Competence p : c.getPrerequis()) {
                grapheTest.ajouterArc(p, c);
            }
        }

        // 3. On vérifie s'il y a un cycle
        if (!dagMethode.estUnDAG(grapheTest)) {
            System.err.println("ERREUR: La modification de cette compétence créerait un cycle de dépendances.");
            // Important : on pourrait vouloir annuler les changements sur l'objet 'competence' ici,
            // mais comme on va rafraîchir les données, ce n'est pas critique.
            return false; // On refuse la modification
        }
        // --- Fin de la vérification ---

        // Si tout est bon, on met à jour la compétence dans la BDD
        competenceDAO.update(competence);
        return true;
    }

    public void supprimerCompetence(Competence competence) throws SQLException {
        competenceDAO.delete(competence);
    }

    public List<Competence> filtrerCompetences(List<Competence> liste, String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return liste;
        }
        String rechercheMiniscule = recherche.toLowerCase();
        return liste.stream()
            .filter(c -> c.getIntitule().toLowerCase().contains(rechercheMiniscule))
            .collect(Collectors.toList());
    }

    public Competence findByIntitule(String intitule) throws SQLException {
        return listerToutesLesCompetences().stream()
            .filter(c -> c.getIntitule().equals(intitule))
            .findFirst()
            .orElse(null);
    }
}