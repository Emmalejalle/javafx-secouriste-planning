package modele.service;

import modele.DAO.AffectationDAO;
import modele.DAO.DpsDAO;
import modele.DAO.JourneeDAO;
import modele.DAO.UserDAO;
import modele.graphe.algorithme.AffectationMethode;
import modele.persistence.Affectation;
import modele.persistence.Competence;
import modele.persistence.DPS;
import modele.persistence.Journee;
import modele.persistence.Secouriste;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe de gestion des affectations.
 * Cette classe est responsable de la logique métier liée aux affectations.
 * Elle peut inclure des méthodes pour créer, modifier, supprimer ou lister des affectations.
 * elle contient également des méthodes pour interagir avec la base de données ou d'autres services.
 * elle contient également des méthodes qui font l'affectation auto pour une journée présente.
 * 
 *  @author Emilien EMERIAU
 *  @version 1.0
 */
public class AffectationMngt {

    private final AffectationDAO affectationDAO = new AffectationDAO();
    private final UserDAO userDAO = new UserDAO();
    private final DpsDAO dpsDAO = new DpsDAO();
    private final JourneeDAO journeeDAO = new JourneeDAO();

    /**
     * Crée une affectation dans la base de données.
     */
    public int createAffectation(Competence competence, Secouriste secouriste , DPS dps) throws SQLException {
        Affectation affectation = new Affectation(secouriste, dps, competence);
        return affectationDAO.create(affectation);
    }

    /**
     * Supprime une affectation dans la base de données.
     */
    public int deleteAffectation(Competence competence, Secouriste secouriste , DPS dps) throws SQLException {
         Affectation affectation = new Affectation(secouriste, dps, competence);
         return affectationDAO.delete(affectation);
    }

    /**
     * Supprime toutes les affectations pour une journée donnée.
     */
    public int deleteAllAffectationJournee(int jour, int mois, int annee) throws SQLException {
         Journee j = journeeDAO.findByDate(jour, mois, annee);
         if (j == null) {
            return 0;
         }
         return affectationDAO.deleteAllAffectationsForJournee(j.getId());
    }

    /**
     * Effectue l'affectation automatique gloutonne pour une journée et crée les affectations en base..
     * @return la liste des affectations créées
     */
    public List<Affectation> affectationAutoGloutonnePourJournee(int jour, int mois, int annee) throws SQLException {
        // --- Étape 1 : Récupération des données depuis les DAO ---
        System.out.println("--- Début de l'affectation automatique ---");
        
        Journee journee = journeeDAO.findByDate(jour, mois, annee);
        if (journee == null) {
            System.err.println("ERREUR : Aucune journée trouvée pour la date " + jour + "/" + mois + "/" + annee);
            return new ArrayList<>();
        }
        System.out.println("Journée sélectionnée : " + journee.toString() + " (ID: " + journee.getId() + ")");

        ArrayList<Secouriste> secouristes = userDAO.findAvailableSecouristesForJournee(journee.getId());
        ArrayList<DPS> dpsList = dpsDAO.findAllByJournee(journee.getId());

        System.out.println("Trouvé : " + secouristes.size() + " secouristes disponibles.");
        System.out.println("Trouvé : " + dpsList.size() + " DPS pour cette journée.");

        // --- Étape 2 : Construction de la liste des postes à pourvoir ---
        ArrayList<DPS> dpsBesoinList = new ArrayList<>();
        ArrayList<Competence> compBesoinList = new ArrayList<>();
        for (DPS dps : dpsList) {
            for (Map.Entry<Competence, Integer> entry : dps.getBesoins().entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    dpsBesoinList.add(dps);
                    compBesoinList.add(entry.getKey());
                }
            }
        }
        System.out.println("Total de postes à pourvoir : " + dpsBesoinList.size());

        if (secouristes.isEmpty() || dpsBesoinList.isEmpty()) {
            System.out.println("Aucun secouriste disponible ou aucun poste à pourvoir. Fin de l'affectation.");
            return new ArrayList<>();
        }

        // --- Étape 3 : Construction de la matrice de compatibilité ---
        System.out.println("\nConstruction de la matrice de compatibilité...");
        int[][] matrice = new int[secouristes.size()][dpsBesoinList.size()];
        for (int i = 0; i < secouristes.size(); i++) {
            Secouriste secouriste = secouristes.get(i);
            for (int j = 0; j < dpsBesoinList.size(); j++) {
                Competence competenceRequise = compBesoinList.get(j);
                
                // // CORRECTION CRUCIALE : On compare les IDs des compétences manuellement ici.
                // // C'est la seule façon sûre de vérifier la compatibilité.
                boolean aLaCompetence = false;
                for (Competence c : secouriste.getCompetences()) {
                    if (c.getIdComp() == competenceRequise.getIdComp()) {
                        aLaCompetence = true;
                        break; // Inutile de chercher plus loin
                    }
                }

                if (aLaCompetence) {
                    matrice[i][j] = 1; 
                } else {
                    matrice[i][j] = 0;
                }
            }
        }
        
        System.out.println("Matrice construite (Lignes=Secouristes, Colonnes=Postes) :");
        for (int[] row : matrice) {
            System.out.println(Arrays.toString(row));
        }

        // --- Étape 4 : Appel de l'algorithme d'affectation ---
        System.out.println("\nLancement de l'algorithme 'Glouton avec Tri de Matrice'...");
        AffectationMethode algo = new AffectationMethode();
        // NOTE: Assure-toi que la bonne méthode d'algo est appelée ici
        int[] couplage = algo.resoudreAffectationGloutonneTriMatrice(matrice); 

        System.out.println("Résultat du couplage (poste -> secouriste) : " + Arrays.toString(couplage));
        
        // --- Étape 5 : Création des affectations en base de données ---
        System.out.println("Création des affectations en base de données...");
        List<Affectation> affectationsCreees = new ArrayList<>();
        for (int j = 0; j < couplage.length; j++) {
            int i = couplage[j];
            if (i != -1) { 
                Affectation aff = new Affectation(
                        secouristes.get(i),
                        dpsBesoinList.get(j),
                        compBesoinList.get(j)
                );
                affectationDAO.create(aff);
                affectationsCreees.add(aff);
            }
        }
        System.out.println(affectationsCreees.size() + " affectations ont été créées avec succès !");
        System.out.println("--- Fin de l'affectation automatique ---");
        
        return affectationsCreees;
    }

     /**
     * Effectue l'affectation automatique gloutonne pour une journée et crée les affectations en base.
     * @return la liste des affectations créées
     */
    public List<Affectation> affectationAutoNaivePourJournee(int jour, int mois, int annee) throws SQLException {
        // --- Étape 1 : Récupération des données depuis les DAO ---
        System.out.println("--- Début de l'affectation automatique ---");
        
        Journee journee = journeeDAO.findByDate(jour, mois, annee);
        if (journee == null) {
            System.err.println("ERREUR : Aucune journée trouvée pour la date " + jour + "/" + mois + "/" + annee);
            return new ArrayList<>();
        }
        System.out.println("Journée sélectionnée : " + journee.toString() + " (ID: " + journee.getId() + ")");

        ArrayList<Secouriste> secouristes = userDAO.findAvailableSecouristesForJournee(journee.getId());
        ArrayList<DPS> dpsList = dpsDAO.findAllByJournee(journee.getId());

        System.out.println("Trouvé : " + secouristes.size() + " secouristes disponibles.");
        System.out.println("Trouvé : " + dpsList.size() + " DPS pour cette journée.");

        // --- Étape 2 : Construction de la liste des postes à pourvoir ---
        ArrayList<DPS> dpsBesoinList = new ArrayList<>();
        ArrayList<Competence> compBesoinList = new ArrayList<>();
        for (DPS dps : dpsList) {
            for (Map.Entry<Competence, Integer> entry : dps.getBesoins().entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    dpsBesoinList.add(dps);
                    compBesoinList.add(entry.getKey());
                }
            }
        }
        System.out.println("Total de postes à pourvoir : " + dpsBesoinList.size());

        if (secouristes.isEmpty() || dpsBesoinList.isEmpty()) {
            System.out.println("Aucun secouriste disponible ou aucun poste à pourvoir. Fin de l'affectation.");
            return new ArrayList<>();
        }

        // --- Étape 3 : Construction de la matrice de compatibilité ---
        System.out.println("\nConstruction de la matrice de compatibilité...");
        int[][] matrice = new int[secouristes.size()][dpsBesoinList.size()];
        for (int i = 0; i < secouristes.size(); i++) {
            Secouriste secouriste = secouristes.get(i);
            for (int j = 0; j < dpsBesoinList.size(); j++) {
                Competence competenceRequise = compBesoinList.get(j);
                
                // // CORRECTION CRUCIALE : On compare les IDs des compétences manuellement ici.
                // // C'est la seule façon sûre de vérifier la compatibilité.
                boolean aLaCompetence = false;
                for (Competence c : secouriste.getCompetences()) {
                    if (c.getIdComp() == competenceRequise.getIdComp()) {
                        aLaCompetence = true;
                        break; // Inutile de chercher plus loin
                    }
                }

                if (aLaCompetence) {
                    matrice[i][j] = 1; 
                } else {
                    matrice[i][j] = 0;
                }
            }
        }
        
        System.out.println("Matrice construite (Lignes=Secouristes, Colonnes=Postes) :");
        for (int[] row : matrice) {
            System.out.println(Arrays.toString(row));
        }

        // --- Étape 4 : Appel de l'algorithme d'affectation ---
        System.out.println("\nLancement de l'algorithme 'Glouton avec Tri de Matrice'...");
        AffectationMethode algo = new AffectationMethode();
        // NOTE: Assure-toi que la bonne méthode d'algo est appelée ici
        int[] couplage = algo.resoudreAffectationGloutonneNaive(matrice); 

        System.out.println("Résultat du couplage (poste -> secouriste) : " + Arrays.toString(couplage));
        
        // --- Étape 5 : Création des affectations en base de données ---
        System.out.println("Création des affectations en base de données...");
        List<Affectation> affectationsCreees = new ArrayList<>();
        for (int j = 0; j < couplage.length; j++) {
            int i = couplage[j];
            if (i != -1) { 
                Affectation aff = new Affectation(
                        secouristes.get(i),
                        dpsBesoinList.get(j),
                        compBesoinList.get(j)
                );
                affectationDAO.create(aff);
                affectationsCreees.add(aff);
            }
        }
        System.out.println(affectationsCreees.size() + " affectations ont été créées avec succès !");
        System.out.println("--- Fin de l'affectation automatique ---");
        
        return affectationsCreees;
    }

    /**
     * Effectue l'affectation automatique gloutonne pour une journée et crée les affectations en base.
     * CETTE VERSION CONTIENT LA CORRECTION POUR LE PROBLÈME DE LA MATRICE DE COMPATIBILITÉ.
     * @return la liste des affectations créées
     */
    public List<Affectation> affectationAutoExhaustivePourJournee(int jour, int mois, int annee) throws SQLException {
        // --- Étape 1 : Récupération des données depuis les DAO ---
        System.out.println("--- Début de l'affectation automatique ---");
        
        Journee journee = journeeDAO.findByDate(jour, mois, annee);
        if (journee == null) {
            System.err.println("ERREUR : Aucune journée trouvée pour la date " + jour + "/" + mois + "/" + annee);
            return new ArrayList<>();
        }
        System.out.println("Journée sélectionnée : " + journee.toString() + " (ID: " + journee.getId() + ")");

        ArrayList<Secouriste> secouristes = userDAO.findAvailableSecouristesForJournee(journee.getId());
        ArrayList<DPS> dpsList = dpsDAO.findAllByJournee(journee.getId());

        System.out.println("Trouvé : " + secouristes.size() + " secouristes disponibles.");
        System.out.println("Trouvé : " + dpsList.size() + " DPS pour cette journée.");

        // --- Étape 2 : Construction de la liste des postes à pourvoir ---
        ArrayList<DPS> dpsBesoinList = new ArrayList<>();
        ArrayList<Competence> compBesoinList = new ArrayList<>();
        for (DPS dps : dpsList) {
            for (Map.Entry<Competence, Integer> entry : dps.getBesoins().entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    dpsBesoinList.add(dps);
                    compBesoinList.add(entry.getKey());
                }
            }
        }
        System.out.println("Total de postes à pourvoir : " + dpsBesoinList.size());

        if (secouristes.isEmpty() || dpsBesoinList.isEmpty()) {
            System.out.println("Aucun secouriste disponible ou aucun poste à pourvoir. Fin de l'affectation.");
            return new ArrayList<>();
        }

        // --- Étape 3 : Construction de la matrice de compatibilité ---
        System.out.println("\nConstruction de la matrice de compatibilité...");
        int[][] matrice = new int[secouristes.size()][dpsBesoinList.size()];
        for (int i = 0; i < secouristes.size(); i++) {
            Secouriste secouriste = secouristes.get(i);
            for (int j = 0; j < dpsBesoinList.size(); j++) {
                Competence competenceRequise = compBesoinList.get(j);
                
                // // CORRECTION CRUCIALE : On compare les IDs des compétences manuellement ici.
                // // C'est la seule façon sûre de vérifier la compatibilité.
                boolean aLaCompetence = false;
                for (Competence c : secouriste.getCompetences()) {
                    if (c.getIdComp() == competenceRequise.getIdComp()) {
                        aLaCompetence = true;
                        break; // Inutile de chercher plus loin
                    }
                }

                if (aLaCompetence) {
                    matrice[i][j] = 1; 
                } else {
                    matrice[i][j] = 0;
                }
            }
        }
        
        System.out.println("Matrice construite (Lignes=Secouristes, Colonnes=Postes) :");
        for (int[] row : matrice) {
            System.out.println(Arrays.toString(row));
        }

        // --- Étape 4 : Appel de l'algorithme d'affectation ---
        System.out.println("\nLancement de l'algorithme 'Glouton avec Tri de Matrice'...");
        AffectationMethode algo = new AffectationMethode();
        // NOTE: Assure-toi que la bonne méthode d'algo est appelée ici
        int[] couplage = algo.resoudreAffectationExhaustive(matrice); 

        System.out.println("Résultat du couplage (poste -> secouriste) : " + Arrays.toString(couplage));
        
        // --- Étape 5 : Création des affectations en base de données ---
        System.out.println("Création des affectations en base de données...");
        List<Affectation> affectationsCreees = new ArrayList<>();
        for (int j = 0; j < couplage.length; j++) {
            int i = couplage[j];
            if (i != -1) { 
                Affectation aff = new Affectation(
                        secouristes.get(i),
                        dpsBesoinList.get(j),
                        compBesoinList.get(j)
                );
                affectationDAO.create(aff);
                affectationsCreees.add(aff);
            }
        }
        System.out.println(affectationsCreees.size() + " affectations ont été créées avec succès !");
        System.out.println("--- Fin de l'affectation automatique ---");
        
        return affectationsCreees;
    }

    /**
     * Exporte les affectations d'une journée au format CSV.
     * @param jour Le jour
     * @param mois Le mois
     * @param annee L'année
     * @param file Le fichier de destination
     * @throws SQLException
     * @throws IOException
     */
    public void exportAffectationsJourneeToCSV(int jour, int mois, int annee, File file) throws SQLException, IOException {
        Journee j = journeeDAO.findByDate(jour, mois, annee);
        if (j == null) throw new IllegalArgumentException("Journée non trouvée");
        List<Affectation> affectations = affectationDAO.findAffectationsForJournee(j.getId());

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Nom;Prénom;Compétence;DPS;Heure début;Heure fin\n");
            for (Affectation aff : affectations) {
                writer.write(
                    aff.getSecouriste().getNom() + ";" +
                    aff.getSecouriste().getPrenom() + ";" +
                    aff.getCompetenceRemplie().getIntitule() + ";" +
                    aff.getDps().getId() + ";" +
                    aff.getDps().getHoraireDepart() + ";" +
                    aff.getDps().getHoraireFin() + "\n"
                );
            }
        }
    }
}
