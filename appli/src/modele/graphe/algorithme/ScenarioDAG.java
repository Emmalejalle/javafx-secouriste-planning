package modele.graphe.algorithme; // Adapte le package si besoin

import modele.graphe.modele.GrapheDAG;
import modele.persistence.Competence; // Assure-toi que le chemin d'import est correct

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe de test pour la vérification de graphe orienté acyclique (DAG).
 * Ce scénario teste la classe DAGMethode pour la SAE S2.02.
 */
public class ScenarioDAG {

    public static void main(String[] args) {
        System.out.println("--- DÉBUT DU SCÉNARIO DE TEST POUR LA VÉRIFICATION DU DAG ---");

        // On instancie la classe qui contient notre algorithme.
        DAGMethode algo = new DAGMethode();

        // =======================================================================
        // TEST 1 : UN GRAPHE VALIDE (SANS CYCLE)
        // =======================================================================
        System.out.println("\n--- TEST 1 : Cas d'un graphe valide (doit être un DAG) ---");

        // 1. On crée les objets Competence qui seront les sommets de notre graphe.
        Competence pse1 = new Competence(1L, "PSE1", "PSE1");
        Competence pse2 = new Competence(2L, "PSE2", "PSE2");
        Competence ce = new Competence(3L, "Chef d'Équipe", "CE");
        Competence cp = new Competence(4L, "Chef de Poste", "CP");

        // 2. On crée une liste avec ces compétences pour construire le graphe.
        ArrayList<Competence> competencesValides = new ArrayList<>(Arrays.asList(pse1, pse2, ce, cp));
        
        // 3. On instancie notre classe GrapheDAG.
        GrapheDAG<Competence> grapheValide = new GrapheDAG<>(competencesValides);

        // 4. On ajoute les arcs pour représenter les prérequis.
        // La flèche A -> B signifie "A requiert B".
        grapheValide.ajouterArc(cp, ce);       // Chef de Poste requiert Chef d'Équipe
        grapheValide.ajouterArc(ce, pse2);     // Chef d'Équipe requiert PSE2
        grapheValide.ajouterArc(pse2, pse1);   // PSE2 requiert PSE1
        System.out.println("Graphe créé : CP -> CE -> PSE2 -> PSE1");

        // 5. On affiche le graphe pour visualiser la matrice d'adjacence.
        afficherGraphe(grapheValide);

        // 6. On lance l'algorithme et on affiche le résultat.
        boolean resultatTest1 = algo.estUnDAG(grapheValide);
        System.out.println("Résultat du test 'estUnDAG' : " + resultatTest1);

        // 7. On vérifie si le résultat est celui attendu.
        if (resultatTest1) {
            System.out.println("VALIDATION : Le test est correct, le graphe est bien détecté comme un DAG.");
        } else {
            System.out.println("ERREUR : Le test a échoué, ce graphe ne devrait pas avoir de cycle.");
        }


        // =======================================================================
        // TEST 2 : UN GRAPHE INVALIDE (AVEC UN CYCLE)
        // =======================================================================
        System.out.println("\n--- TEST 2 : Cas d'un graphe invalide (ne doit PAS être un DAG) ---");

        // 1. On crée de nouvelles compétences pour ce test.
        Competence compA = new Competence(10L, "Compétence A", "A");
        Competence compB = new Competence(11L, "Compétence B", "B");
        Competence compC = new Competence(12L, "Compétence C", "C");

        // 2. On crée la liste et le graphe.
        ArrayList<Competence> competencesInvalides = new ArrayList<>(Arrays.asList(compA, compB, compC));
        GrapheDAG<Competence> grapheInvalide = new GrapheDAG<>(competencesInvalides);

        // 4. On ajoute des arcs qui forment une boucle : A -> B -> C -> A
        grapheInvalide.ajouterArc(compA, compB); // A requiert B
        grapheInvalide.ajouterArc(compB, compC); // B requiert C
        grapheInvalide.ajouterArc(compC, compA); // C requiert A (ce qui crée le cycle)
        System.out.println("Graphe créé : A -> B -> C -> A");

        //5. On affiche le graphe pour visualiser la matrice d'adjacence.
        afficherGraphe(grapheInvalide);

        // 6. On lance l'algorithme.
        boolean resultatTest2 = algo.estUnDAG(grapheInvalide);
        System.out.println("Résultat du test 'estUnDAG' : " + resultatTest2);

        // 7. On vérifie le résultat.
        if (!resultatTest2) {
            System.out.println("VALIDATION : Le test est correct, le cycle a bien été détecté.");
        } else {
            System.out.println("ERREUR : Le test a échoué, l'algorithme aurait dû trouver un cycle.");
        }

        // =======================================================================
        // TEST 2 : UN GRAPHE INVALIDE CAS AVEC UN CYCL ENTRE 2 SOMMETS
        // =======================================================================

        System.out.println("\n--- TEST 3 : Cas d'un graphe invalide avec un cycle entre 2 sommets ---");

        // 1. On crée de nouvelles compétences pour ce test.
        Competence compD = new Competence(13L, "Compétence D", "D");
        Competence compE = new Competence(14L, "Compétence E", "E");    

        // 2. On crée la liste et le graphe.
        ArrayList<Competence> competencesInvalides2 = new ArrayList<>(Arrays.asList(compD, compE));
        GrapheDAG<Competence> grapheInvalide2 = new GrapheDAG<>(competencesInvalides2);

        // 4. On ajoute des arcs qui forment une boucle : D -> E -> D
        grapheInvalide2.ajouterArc(compD, compE); // D requiert E
        grapheInvalide2.ajouterArc(compE, compD); // E requiert D (ce qui crée le cycle)
        System.out.println("Graphe création : D -> E -> D");

        // 5. On affiche le graphe pour visualiser la matrice d'adjacence.
        afficherGraphe(grapheInvalide2);

        // 6. On lance l'algorithme.
        boolean resultatTest3 = algo.estUnDAG(grapheInvalide2);
        System.out.println("Résultat du test 'estUnDAG' : " + resultatTest3);

        // 7. On vérifie le résultat.
        if (!resultatTest3) {
            System.out.println("VALIDATION : Le test est correct, le cycle a bien été détecté.");
        } else {
            System.out.println("ERREUR : Le test a échoué, l'algorithme aurait dû trouver un cycle.");
        }
        
        System.out.println("\n--- FIN DU SCÉNARIO ---");
    }


    /**
     * Affiche le graphe sous forme de matrice d'adjacence.
     * @param graphe Le graphe à afficher.
     * @param <T> Le type des sommets du graphe.
     * @return La matrice d'adjacence représentant le graphe.
     */
    public static <T> boolean[][] afficherGraphe(GrapheDAG<T> graphe) {
        boolean[][] matrice = graphe.getMatriceAdjacence();
        System.out.println("Matrice d'adjacence du graphe :");
        for (boolean[] ligne : matrice) {
            for (boolean valeur : ligne) {
                System.out.print((valeur ? 1 : 0) + " ");
            }
            System.out.println();
        }
        return matrice;
    }
}
