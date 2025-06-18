package modele.graphe.algorithme;

import modele.persistence.*;
import modele.graphe.modele.GrapheDAG;

/**
 * Contient les algorithmes pour la vérification du graphe de compétences
 * et pour l'affectation des secouristes, adaptés pour la SAE S2.02.
 */
public class DAGMethode {

    // =======================================================================
    // MÉTHODE 1 : VÉRIFICATION DU GRAPHE DE COMPÉTENCES (DAG)
    // =======================================================================

    /**
     * Vérifie si le graphe des compétences est un DAG (Graphe Orienté Acyclique).
     * Un cycle signifierait une dépendance logique impossible (ex: A requiert B et B requiert A).
     * @param grapheCompetences Le graphe des compétences à vérifier.
     * @return true si le graphe est bien un DAG, false s'il contient un cycle.
     */
    public boolean estUnDAG(GrapheDAG<Competence> grapheCompetences) {
        // // On récupère la représentation du graphe sous forme de matrice.
        boolean[][] matrice = grapheCompetences.getMatriceAdjacence();
        // // On récupère le nombre total de sommets (compétences).
        int nbSommets = grapheCompetences.getNombreSommets();
        
        // // On crée un tableau pour suivre l'état de chaque sommet pendant le parcours.
        // // 0 = non visité, 1 = en cours de visite (dans la pile de récursion), 2 = entièrement exploré.
        int[] etats = new int[nbSommets]; 

        // // On lance une recherche de cycle depuis chaque sommet du graphe.
        // // C'est nécessaire si le graphe n'est pas connexe (plusieurs groupes de compétences).
        for (int i = 0; i < nbSommets; i++) {
            // // Si on n'a pas encore visité ce sommet, on commence une nouvelle exploration.
            if (etats[i] == 0) {
                // // On appelle la fonction récursive qui fait le vrai travail de détection.
                // // Si elle trouve un cycle, on arrête tout et on retourne 'false'.
                if (detecterCycleUtil(i, etats, matrice, nbSommets)) {
                    return false; // // Cycle détecté ! Ce n'est pas un DAG.
                }
            }
        }
        // // Si on a exploré tous les sommets sans jamais trouver de cycle, le graphe est un DAG.
        return true; 
    }

    /**
     * Fonction utilitaire récursive (basée sur le parcours en profondeur DFS) pour détecter un cycle.
     */
    private boolean detecterCycleUtil(int sommet, int[] etats, boolean[][] matrice, int nbSommets) {
        // // On marque le sommet actuel comme "en cours de visite".
        etats[sommet] = 1;

        // // On regarde tous les autres sommets pour trouver ses "voisins" (compétences requises).
        for (int voisin = 0; voisin < nbSommets; voisin++) {
            // // S'il y a un arc de 'sommet' vers 'voisin'...
            if (matrice[sommet][voisin]) {
                // // Si on tombe sur un voisin qui est lui-même "en cours de visite"...
                if (etats[voisin] == 1) {
                    // // C'est la preuve qu'on est revenu sur nos pas : on a trouvé un cycle.
                    return true; 
                }
                // // Si le voisin n'a pas encore été exploré...
                if (etats[voisin] == 0) {
                    // // ... on lance la recherche récursivement depuis ce voisin.
                    // // Si l'appel récursif trouve un cycle, on propage le résultat 'true'.
                    if (detecterCycleUtil(voisin, etats, matrice, nbSommets)) {
                        return true;
                    }
                }
            }
        }

        // // Si on a exploré tous les voisins du sommet actuel sans trouver de cycle,
        // // on le marque comme "entièrement exploré".
        etats[sommet] = 2; 
        // // On retourne 'false' pour indiquer qu'aucun cycle n'a été trouvé à partir d'ici.
        return false;
    }
}
