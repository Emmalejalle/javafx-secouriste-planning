package modele.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import modele.DAO.CompetenceDAO;
import modele.persistence.Competence;

public class CompetenceManagement {

    private CompetenceDAO competenceDAO;

    public CompetenceManagement() {
        this.competenceDAO = new CompetenceDAO();
    }

    public List<Competence> listerToutesLesCompetences() throws SQLException {
        return competenceDAO.findAll();
    }

    public void creerCompetence(String intitule, String abrev, List<Competence> prerequis) throws SQLException {
        if (intitule == null || intitule.trim().isEmpty() || abrev == null || abrev.trim().isEmpty()) {
            throw new IllegalArgumentException("L'intitulé et l'abréviation ne peuvent pas être vides.");
        }
        Competence nouvelleCompetence = new Competence(intitule, abrev);
        nouvelleCompetence.setPrerequis(new ArrayList<>(prerequis));
        competenceDAO.create(nouvelleCompetence);
    }

    public void modifierCompetence(Competence competence, String intitule, String abrev, List<Competence> prerequis) throws SQLException {
        competence.setIntitule(intitule);
        competence.setAbrevComp(abrev);
        competence.setPrerequis(new ArrayList<>(prerequis));
        competenceDAO.update(competence);
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