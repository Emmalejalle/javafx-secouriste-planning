package modele.graphe.algorithme;

import java.util.Random;


/**
 * Cette classe contient les tests de performance pour les algorithmes d'affectation
 * @author Tardy Elie
 * @version 1.0
 */
    public class TestPerformanceAffectationMethode {

    /**
     * Méthode principale qui lance l'ensemble des tests d'efficacité.
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        System.out.println("Lancement de la suite de tests de performance pour les algorithmes de AffectationMethode...");

        // --- Tests pour l'algorithme Glouton Naïf ---
        testGloutonNaif();

        // --- Tests pour l'algorithme Glouton avec Tri de Matrice ---
        testGloutonTriMatrice();

        // --- Tests pour l'algorithme Exhaustif ---
        testExhaustif();

        System.out.println("\nFin de la suite de tests.");
    }

    // ==========================================================================================
    // SECTION 1 : ALGORITHME GLOUTON NAÏF
    // ==========================================================================================
    private static void testGloutonNaif() {
        System.out.println("\n\n=== Test de Performance : resoudreAffectationGloutonneNaive() ===");
        System.out.println("Complexité attendue : O(N^2) pour une matrice N x N.");

        AffectationMethode solveur = new AffectationMethode();
        int n = 500; // Taille de départ pour la matrice carrée

        for (int i = 0; i < 5; i++) {
            int[][] matrice = genererMatriceAleatoire(n, n);

            long t1 = System.nanoTime();
            solveur.resoudreAffectationGloutonneNaive(matrice);
            long t2 = System.nanoTime();
            
            long operations = AffectationMethode.cpt;
            long duree = t2 - t1;

            System.out.println("\nPour N = " + n);
            System.out.println("Opérations (cpt) = " + operations);
            System.out.println("Temps d'exécution = " + duree + " ns");
            // Le rapport cpt / N^2 doit être à peu près constant.
            // Il est attendu autour de 0.5 car la boucle interne s'arrête dès qu'une affectation est trouvée.
            System.out.printf("Rapport (cpt / N^2) = %.4f\n", (double) operations / ((double) n * n));

            n *= 2; // On double la taille pour la prochaine itération.
        }
    }

    // ==========================================================================================
    // SECTION 2 : ALGORITHME GLOUTON AVEC TRI
    // ==========================================================================================
    private static void testGloutonTriMatrice() {
        System.out.println("\n\n=== Test de Performance : resoudreAffectationGloutonneTriMatrice() ===");
        System.out.println("Complexité attendue : O(N^2), dominée par les calculs de somme et l'affectation.");
        
        AffectationMethode solveur = new AffectationMethode();
        int n = 500; // Taille de départ

        for (int i = 0; i < 5; i++) {
            int[][] matrice = genererMatriceAleatoire(n, n);

            long t1 = System.nanoTime();
            solveur.resoudreAffectationGloutonneTriMatrice(matrice);
            long t2 = System.nanoTime();

            long operations = AffectationMethode.cpt;
            long duree = t2 - t1;

            System.out.println("\nPour N = " + n);
            System.out.println("Opérations (cpt) = " + operations);
            System.out.println("Temps d'exécution = " + duree + " ns");
            // Le rapport cpt / N^2 doit être constant, mais plus élevé que pour le glouton naïf
            // car il inclut le calcul des sommes des lignes et des colonnes.
            System.out.printf("Rapport (cpt / N^2) = %.4f\n", (double) operations / ((double) n * n));

            n *= 2;
        }
    }

    // ==========================================================================================
    // SECTION 3 : ALGORITHME EXHAUSTIF (BACKTRACKING)
    // ==========================================================================================
    private static void testExhaustif() {
        System.out.println("\n\n=== Test de Performance : resoudreAffectationExhaustive() ===");
        System.out.println("Complexité attendue : Exponentielle (de l'ordre de O(n!))");

        AffectationMethode solveur = new AffectationMethode();

        // ATTENTION : La complexité explose. Ne pas utiliser de N > 12.
        for (int n = 7; n <= 12; n++) {
            int[][] matrice = genererMatriceAleatoire(n, n);

            long t1 = System.nanoTime();
            solveur.resoudreAffectationExhaustive(matrice);
            long t2 = System.nanoTime();
            
            long operations = AffectationMethode.cpt;
            long duree = t2 - t1;
            long nFactoriel = factorielle(n);

            System.out.println("\nPour N = " + n);
            System.out.println("Opérations (cpt) = " + operations);
            System.out.println("n! (pour comparaison) = " + nFactoriel);
            System.out.println("Temps d'exécution = " + duree + " ns (" + String.format("%.4f", duree / 1_000_000_000.0) + " s)");

            // On vérifie que le rapport cpt / n! est à peu près constant.
            // Cela montre que la croissance est bien de type factorielle/exponentielle.
            System.out.printf("Rapport (cpt / n!) = %.4f\n", (double) operations / nFactoriel);
        }
    }

    // ==========================================================================================
    // OUTILS
    // ==========================================================================================

    /**
     * Génère une matrice carrée de taille N x N avec des 0 et des 1 aléatoires.
     * @param n Le nombre de lignes et de colonnes.
     * @return La matrice générée.
     */
    private static int[][] genererMatriceAleatoire(int lignes, int colonnes) {
        int[][] matrix = new int[lignes][colonnes];
        Random rand = new Random();
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                // Probabilité de 50% d'avoir une compatibilité (1)
                matrix[i][j] = rand.nextInt(2);
            }
        }
        return matrix;
    }

    /**
     * Calcule la factorielle de n.
     * @param n L'entier.
     * @return n!
     */
    private static long factorielle(int n) {
        long res = 1;
        for (int i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
    }
}