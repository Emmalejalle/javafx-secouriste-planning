import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class AffectationMethode {
    public static void main(String[] args) {

        Map<String, List<String>> grapheCompetences = new HashMap<>();

        grapheCompetences.put("PSE1", Arrays.asList("PSE2", "SSA"));
        grapheCompetences.put("PSE2", Arrays.asList("CE", "VPSP"));
        grapheCompetences.put("SSA", new ArrayList<>());      // Pas de compétence dépendante
        grapheCompetences.put("VPSP", new ArrayList<>());     // Pas de compétence dépendante
        grapheCompetences.put("CE", Arrays.asList("CP"));
        grapheCompetences.put("CP", Arrays.asList("CO"));
        grapheCompetences.put("CO", new ArrayList<>());       // Terminale
        grapheCompetences.put("PBF", Arrays.asList("PBC"));
        grapheCompetences.put("PBC", new ArrayList<>());      // Terminale

        System.out.println("tEST DAG : devrait l'être");
        System.out.println("Est DAG : " + estDAG(grapheCompetences));

        Map<String, List<String>> grapheNonDAG = new HashMap<>();

        grapheNonDAG.put("PSE1", Arrays.asList("PSE2", "SSA"));
        grapheNonDAG.put("PSE2", Arrays.asList("CE", "VPSP"));
        grapheNonDAG.put("SSA", new ArrayList<>());
        grapheNonDAG.put("VPSP", new ArrayList<>());
        grapheNonDAG.put("CE", Arrays.asList("CP"));
        grapheNonDAG.put("CP", Arrays.asList("CO"));
        grapheNonDAG.put("CO", Arrays.asList("PSE1")); // Cycle ici !
        grapheNonDAG.put("PBF", Arrays.asList("PBC"));
        grapheNonDAG.put("PBC", new ArrayList<>());

        System.out.println("tEST DAG : ne devrait pas l'être");
        System.out.println("Est DAG : " + estDAG(grapheNonDAG));

        System.out.println("AffectationMethode");
        
        // on incremente une matrice d'adjacence 8 par 8 avec des 0 et des 1 aleatoirements qui reste logique par quart de matrice
        int[][] adj = new int[8][8];

        AffectationMethode adjacence = new AffectationMethode();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                adj[i][j] = (int)(Math.random() * 2);
            }
        }

        System.out.println("Matrice d'adjacence :");
        afficherMatrice(adj);

        HashMap<Integer, Integer> map = adjacence.algoGloutonNaif(adj);

        System.out.println("Affectation par Algo Glouton Naif :");
        System.out.println(map.toString());

        HashMap<String, String> affectation = adjacence.affecter(adj);

        System.out.println("Affectation par Algo Glouton  2.0 :");
        System.out.println(affectation.toString());
    }

    public static boolean estDAG(Map<String, List<String>> graphe) {
        Set<String> visites = new HashSet<>();
        Set<String> enCours = new HashSet<>();

        for (String competence : graphe.keySet()) {
            if (!visites.contains(competence)) {
                if (contientCycle(competence, graphe, visites, enCours)) {
                    return false; // cycle détecté
                }
            }
        }
        return true; // aucun cycle détecté
    }

    private static boolean contientCycle(String noeud, Map<String, List<String>> graphe, Set<String> visites, Set<String> enCours) {
        visites.add(noeud);
        enCours.add(noeud);

        List<String> voisins = graphe.getOrDefault(noeud, new ArrayList<>());

        for (String voisin : voisins) {
            if (!visites.contains(voisin)) {
                if (contientCycle(voisin, graphe, visites, enCours)) {
                    return true;
                }
            } else if (enCours.contains(voisin)) {
                return true; // cycle trouvé
            }
        }

        enCours.remove(noeud);
        return false;
    }

    /**
     * Glouton Naif algorithm.
     * 
     * il assigne un secouriste a un dps en fonction d'une matrice d'adjacence.
     * Il prend un secouriste et cherche le dps le plus proche.
     * Si il n'y en a plus alors il n'est pas assigné
     * 
     * @param adj the matrix of adjacency
     * @return a HashMap containing the assignments
     */
    public HashMap<Integer, Integer> algoGloutonNaif(int[][] adj) {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < (adj.length/2) ; i++) {

            int j = adj.length/2;
            boolean assigne = false;
            
            while(!assigne && j < adj.length) {
                if (adj[i][j] == 1) {
                    if(!(map.containsValue(j-3))) {
                        map.put(i + 1, j-3);
                        assigne = true;
                    }
                }
                j++;
            }
        }
        return map;
    }


    public HashMap<String,String> affecter(int[][] tabAffect) {
        int total = tabAffect.length;
        int n = total / 2; 
        int[] degreSortant = new int[n]; 
        int[] degreEntrant = new int[n]; 
        HashMap<String, String> resultMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            for (int j = n; j < total; j++) { 
                if (tabAffect[i][j] == 1) {
                    degreSortant[i]++;
                    degreEntrant[j - n]++;
                }
            }
        }

        boolean[] tacheAffectee = new boolean[n];
        boolean[] secouristeAffecte = new boolean[n];

        for (int count = 0; count < n; count++) {
            int minDegTache = Integer.MAX_VALUE;
            int minTache = -1;

            for (int i = 0; i < n; i++) {
                if (!tacheAffectee[i] && degreSortant[i] < minDegTache && degreSortant[i] > 0) {
                    minDegTache = degreSortant[i];
                    minTache = i;
                }
            }

            int minDegSecouriste = Integer.MAX_VALUE;
            int minSecouriste = -1;

            if (minTache != -1) {
                for (int j = 0; j < n; j++) {
                    if (!secouristeAffecte[j] && tabAffect[minTache][j + n] == 1 && degreEntrant[j] < minDegSecouriste) {
                        minDegSecouriste = degreEntrant[j];
                        minSecouriste = j;
                    }
                }
            }

            if (minTache != -1 && minSecouriste != -1) {
                resultMap.put("Tache " + (minTache + 1), "Secouriste " + (minSecouriste + 1));
                tacheAffectee[minTache] = true;
                secouristeAffecte[minSecouriste] = true;
            }
            /** 
            String affichage = "";
            for (int i = 0; i < n; i++) {
                affichage += "Tache " + (i + 1) + " : " + degreSortant[i] + "  ";
            }
            System.out.println(affichage);
            affichage = "";
            for (int i = 0; i < n; i++) {
                affichage += "Secouriste " + (i + 1) + " : " + degreEntrant[i] + "  ";
            }
            System.out.println(affichage);
            */
        }

        return resultMap;

    }

    public static void afficherMatrice(int[][] tableau) {
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                System.out.print(tableau[i][j] + " ");
            }
            System.out.println();
        }
    }
}