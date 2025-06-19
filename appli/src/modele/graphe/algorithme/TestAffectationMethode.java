package modele.graphe.algorithme; // Assure-toi que le package est correct

import java.util.Arrays;

/**
 * Classe de test pour les algorithmes d'affectation contenus dans AffectationMethode.
 * Permet de lancer les différents algorithmes sur une même matrice de test
 * et de comparer leurs résultats et leur performance.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class TestAffectationMethode {

    public static void main(String[] args) {
        // --- Préparation des données de test ---
        // On définit une matrice de test pour nos algorithmes.
        // 4 secouristes (lignes) et 5 postes (colonnes).
        // C'est un bon exemple car le glouton naïf ne trouvera pas la solution optimale.
        int[][] matriceTest = {
            // P0  P1  P2  P3  P4
            {  1,  1,  0,  0,  0 }, // Secouriste 0 (polyvalent sur P0, P1)
            {  1,  0,  0,  0,  0 }, // Secouriste 1 (spécialiste de P0)
            {  0,  0,  1,  1,  0 }, // Secouriste 2
            {  0,  0,  0,  0,  1 }, // Secouriste 3 (spécialiste de P4)
            {  0,  1,  1,  0,  0 }  // Secouriste 4 (spécialiste de P1 et P2)
        };

        // On instancie la classe qui contient nos algorithmes.
        AffectationMethode solveur = new AffectationMethode();

        System.out.println("Lancement des tests d'affectation sur la matrice :");
        afficherMatrice(matriceTest);

        // --- Test de l'algorithme glouton naïf ---
        System.out.println("\n--- 1. Lancement de l'algorithme GLOUTON NAÏF ---");
        // Cet algo va probablement affecter S0 à P0, bloquant S1, et donc ne faire que 3 affectations.
        int[] resultatNaif = solveur.resoudreAffectationGloutonneNaive(matriceTest);
        afficherResultat("Glouton Naïf", resultatNaif, AffectationMethode.cpt);

        System.out.println("\n---------------------------------------------------\n");

        // --- Test de l'algorithme glouton avec tri de matrice ---
        System.out.println("--- 2. Lancement de l'algorithme GLOUTON avec TRI DE MATRICE ---");
        // Cet algo devrait être plus malin : il traitera S1 et S3 (spécialistes) en premier,
        // ce qui devrait lui permettre de trouver la solution optimale de 4 affectations.
        int[] resultatTriMatrice = solveur.resoudreAffectationGloutonneTriMatrice(matriceTest);
        afficherResultat("Glouton avec Tri de Matrice", resultatTriMatrice, AffectationMethode.cpt);

        
        // --- Test de l'algorithme exhaustif (Optionnel, peut être très long) ---
        System.out.println("\n---------------------------------------------------\n");
        System.out.println("--- 3. Lancement de l'algorithme EXHAUSTIF (OPTIMAL GARANTI) ---");
        int[] resultatExhaustif = solveur.resoudreAffectationExhaustive(matriceTest);
        afficherResultat("Exhaustif (Optimal)", resultatExhaustif, AffectationMethode.cpt);
        
    }

    /**
     * Méthode utilitaire pour afficher proprement les résultats d'un algorithme.
     * @param nomAlgo Le nom de l'algorithme testé.
     * @param resultat Le tableau de couplage retourné par l'algorithme.
     * @param operations Le nombre d'opérations comptées.
     */
    public static void afficherResultat(String nomAlgo, int[] resultat, long operations) {
        System.out.println("Résultat pour l'algorithme : " + nomAlgo);
        if (resultat == null) {
            System.out.println("  Erreur : le résultat est null.");
            return;
        }
        int affectationsRealisees = 0;
        for (int poste = 0; poste < resultat.length; poste++) {
            if (resultat[poste] != -1) {
                System.out.println("  Poste " + poste + " -> Secouriste " + resultat[poste]);
                affectationsRealisees++;
            } else {
                System.out.println("  Poste " + poste + " -> Non affecté");
            }
        }
        System.out.println("\n  >> Nombre total d'affectations : " + affectationsRealisees);
        System.out.println("  >> Nombre d'opérations (complexité) : " + operations);
    }

    /**
     * Méthode utilitaire pour afficher la matrice de test.
     * 
     * @param matrice La matrice à afficher.
     */
    public static void afficherMatrice(int[][] matrice) {
        System.out.println("Matrice de test (Lignes=Secouristes, Colonnes=Postes):");
        for (int[] ligne : matrice) {
            System.out.println("  " + Arrays.toString(ligne));
        }
    }
}
