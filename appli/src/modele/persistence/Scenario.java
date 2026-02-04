package modele.persistence;
import modele.persistence.*;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.Map;

/**
 * Classe du scénario
 * @author Tardy Elie
 * @version 1.0
 */
public class Scenario {

    /**
     * La méthode où se déroule le scénario
     */
    public static void main(String[] args) {
        System.out.println("Début de la simulation du diagramme de séquence 'Usc_GererAffectation'");

        // 1. Création des objets nécessaires pour la simulation
        // Simuler un administrateur
        Admin administrateur = new Admin(1, "password123", "Dupont", "Jean", "01/01/1980", "jean.dupont@example.com", "0612345678", "123 Rue de la Paix");
        System.out.println("\n[Admin] Administrateur créé : " + administrateur.getPrenom() + " " + administrateur.getNom());

        // Simuler une compétence existante
        Competence competenceSecouriste = new Competence(101, "Premiers Secours", "PS");
        System.out.println("[Competence] Compétence créée : " + competenceSecouriste.getIntitule());

        // Simuler un secouriste
        Secouriste secouriste = new Secouriste(201, "secouristeMDP", "Martin", "Sophie", "05/05/1990", "sophie.martin@example.com", "0698765432", "456 Avenue des Roses");
        // Ajouter la compétence au secouriste
        secouriste.setCompetences(new ArrayList<>(Arrays.asList(competenceSecouriste)));
        System.out.println("[Secouriste] Secouriste créé : " + secouriste.getPrenom() + " " + secouriste.getNom());
        System.out.println("[Secouriste] Compétences de Sophie : " + secouriste.getCompetences().get(0).getIntitule());

        // Simuler une journée
        Journee journeeDPS = new Journee(301, 15, 8, 2025);
        System.out.println("[Journee] Journée du DPS : " + journeeDPS.getJourDeLaSemaine() + " " + journeeDPS.toString());

        // Rendre le secouriste disponible pour cette journée
        secouriste.setDisponibilites(new ArrayList<>(Arrays.asList(journeeDPS)));
        System.out.println("[Secouriste] Sophie est disponible le : " + secouriste.getDisponibilites().get(0).toString());


        // Simuler un site
        Site siteDPS = new Site(401, "Stade Principal", 2.3522f, 48.8566f);
        System.out.println("[Site] Site du DPS : " + siteDPS.getNom());

        // Simuler un sport
        Sport sportDPS = new Sport(501, "Football");
        System.out.println("[Sport] Sport du DPS : " + sportDPS.getNom());

        // Simuler un DPS
        DPS dps = new DPS(601, 12, 16, siteDPS, sportDPS, journeeDPS);

        // Ajouter un besoin pour la compétence "Premiers Secours"
        dps.getBesoins().put(competenceSecouriste, 1);
        System.out.println("[DPS] DPS créé : " + dps.toString());
        System.out.println("[DPS] Besoin en compétence pour le DPS : " + dps.getBesoins().keySet().iterator().next().getIntitule() + " : " + dps.getBesoins().get(competenceSecouriste));


        // L'administrateur demande la liste des DPS
        System.out.println("\n[Admin] -> [Affection] : Demander la liste des DPS disponibles.");
        ArrayList<DPS> listeDPS = new ArrayList<>(Arrays.asList(dps));
        System.out.println("[Affection] Retourne la liste des DPS au Admin.");

        // L'administrateur sélectionne un DPS
        DPS dpsSelectionne = listeDPS.get(0);
        System.out.println("[Admin] -> [Affection] : Sélectionne le DPS : " + dpsSelectionne.getId());

        // L'administrateur demande les besoins en personnel pour le DPS sélectionné
        System.out.println("[Admin] -> [DPS] : getBesoins()");
        // On récupère directement les besoins du DPS créé
        for (Map.Entry<Competence, Integer> entry : dpsSelectionne.getBesoins().entrySet()) {
            System.out.println("[DPS] Retourne les besoins : \n \t - " + entry.getKey().getIntitule() + "\n \t - Quantité : " + entry.getValue());
        }

        // L'administrateur demande la liste des compétences
        System.out.println("\n[Admin] -> [Competence] : Demander la liste des compétences.");
        ArrayList<Competence> toutesLesCompetences = new ArrayList<>(Arrays.asList(competenceSecouriste));
        System.out.println("[Competence] Retourne la liste des compétences");

        // L'administrateur choisit une compétence requise 
        Competence competenceRequise = competenceSecouriste;
        System.out.println("[Admin] -> [Affectation] : Choisit la compétence : " + competenceRequise.getIntitule());


        // L'administrateur demande la liste des secouristes disponibles pour le DPS et la compétence
        System.out.println("[Admin] -> [Secouriste] : getListeSecouristes(DPS, Competence)");

        // regarde si le secouriste est disponible et possede la competence
        ArrayList<Secouriste> secouristesDisponibles = new ArrayList<>();
        if (secouriste.aCompetence(competenceRequise) && secouriste.estDisponible(dpsSelectionne.getJournee())) {
            secouristesDisponibles.add(secouriste);
        }
        System.out.println("[Secouriste] Retourne la liste des secouristes disponibles.");

        if (secouristesDisponibles.isEmpty()) {
            System.out.println("   (Aucun secouriste disponible trouvé pour ces critères)");
        } else {
            System.out.println("   Secouriste(s) disponible(s) : " + secouristesDisponibles.get(0).getPrenom() + " " + secouristesDisponibles.get(0).getNom());
        }

        // L'administrateur sélectionne un secouriste
        Secouriste secouristeSelectionne = null;
        Affectation nouvelleAffectation = null;
        if (!secouristesDisponibles.isEmpty()) {
            secouristeSelectionne = secouristesDisponibles.get(0);
            System.out.println("[Admin] -> [Affection] : Sélectionne le secouriste : " + secouristeSelectionne.getPrenom() + " " + secouristeSelectionne.getNom());

            //L'administrateur crée l'affectation
            System.out.println("[Admin] -> [Affectation] : creerAffectation(secouriste, dps, competence)");
            nouvelleAffectation = new Affectation(secouristeSelectionne, dpsSelectionne, competenceRequise);
            System.out.println("[Affectation] Affectation créée : " + nouvelleAffectation.toString());

            // L'affectation est vérifiée
            System.out.println("[Affectation] -> [Secouriste] : estDisponible(Journee)");
            System.out.println("[Affectation] -> [Secouriste] : possedeCompetence(Competence)");
            System.out.println("[Affectation] Vérifie la validité de l'affectation...");
            if (nouvelleAffectation.estValide()) {
                System.out.println("[Affectation] : L'affectation est valide !");
                System.out.println("[Affection] : Demande la sauvegarde de l'affectation .");
                System.out.println("[Admin] : Affectation enregistrée avec succès.");
            } else {
                System.out.println("[Affectation] : L'affectation n'est PAS valide (echec de la vérification) !");
                System.out.println("[Admin] : Impossible d'enregistrer l'affectation.");
            }
        } else {
            System.out.println("[Admin] : Impossible de créer une affectation car aucun secouriste n'est disponible pour le DPS et la compétence.");
        }


        // Le secouriste demande la competence qu'il doit remplir et le site sur lequel il est affecté
        System.out.println("\n[Secouriste] -> [Affectation] : Demande le site sur lequel il est affecté");
        System.out.println("[Secouriste] Retourne le site sur lequel il est affecté : " + nouvelleAffectation.getDps().getSite().getNom());
        System.out.println("[Secouriste] -> [Affectation] : Demande la competence qu'il doit remplir");
        System.out.println("[Secouriste] Retourne la competence qu'il doit remplir : " + competenceSecouriste.getIntitule());


        // Fin du scénario
        System.out.println("\nFin de la simulation.");
    }
}