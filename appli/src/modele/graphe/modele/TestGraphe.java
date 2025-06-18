package modele.graphe.modele; // Assurez-vous que le package correspond à votre structure

/**
 * Classe de test dédiée pour la classe Graphe.
 * Elle contient une méthode main pour instancier un Graphe
 * et vérifier le bon fonctionnement de toutes ses méthodes publiques.
 */
public class TestGraphe {

    /**
     * Méthode principale pour tester la classe Graphe.
     */
    public static void main(String[] args) {
        // --- Préparation des données de test ---
        // On définit un exemple de matrice d'adjacence pour un graphe à 4 sommets.
        // Ce graphe représente les relations : 0->1, 0->2, 1->2, 2->3
        int[][] matriceExemple = {
            {0, 1, 1, 0}, // Le sommet 0 a des arcs vers 1 et 2
            {0, 0, 1, 0}, // Le sommet 1 a un arc vers 2
            {0, 0, 0, 1}, // Le sommet 2 a un arc vers 3
            {0, 0, 0, 0}  // Le sommet 3 n'a pas d'arc sortant
        };

        // --- Création de l'objet à tester ---
        // On crée un nouvel objet Graphe avec cette matrice.
        Graphe monGraphe = new Graphe(matriceExemple);
        System.out.println(">>> Un objet Graphe a été créé avec succès.");

        // --- Lancement des tests ---
        System.out.println("\n--- Test des méthodes du Graphe ---");

        // Test 1: Affichage de la matrice
        System.out.println("\n1. Test de afficherMatrice():");
        monGraphe.afficherMatrice();

        // Test 2: Ordre et Taille
        System.out.println("\n2. Test de getOrdre() et getTaille():");
        System.out.println("   Ordre (nombre de sommets) : " + monGraphe.getOrdre()); // Doit afficher 4
        System.out.println("   Taille (nombre d'arcs) : " + monGraphe.getTaille());   // Doit afficher 4

        // Test 3: Liste des sommets
        System.out.println("\n3. Test de getSommets():");
        System.out.println("   Liste des sommets : " + monGraphe.getSommets()); // Doit afficher [0, 1, 2, 3]

        // Test 4: Voisinage direct
        System.out.println("\n4. Test de sontVoisins(s1, s2):");
        System.out.println("   Le sommet 0 est-il voisin du sommet 1 ? " + monGraphe.sontVoisins(0, 1)); // Doit afficher true
        System.out.println("   Le sommet 1 est-il voisin du sommet 0 ? " + monGraphe.sontVoisins(1, 0)); // Doit afficher false
        System.out.println("   Le sommet 1 est-il voisin du sommet 3 ? " + monGraphe.sontVoisins(1, 3)); // Doit afficher false

        // Test 5: Liste des successeurs
        System.out.println("\n5. Test de getVoisins(sommet):");
        System.out.println("   Voisins du sommet 0 : " + monGraphe.getVoisins(0)); // Doit afficher [1, 2]
        System.out.println("   Voisins du sommet 2 : " + monGraphe.getVoisins(2)); // Doit afficher [3]
        System.out.println("   Voisins du sommet 3 : " + monGraphe.getVoisins(3)); // Doit afficher []

        System.out.println("\n--- Fin des tests ---");
    }
}
