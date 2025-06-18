package modele.graphe.modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un graphe orienté simple en utilisant une matrice d'adjacence.
 * Cette classe fournit les fonctionnalités de base pour manipuler un graphe
 * et sert de fondation pour les algorithmes plus complexes (DAG, affectation...).
 * Les sommets sont représentés par des indices (de 0 à N-1).
 * * @author Emilien EMERIAU et Elie Tardy
 * @version 1.0
 */
public class Graphe {

    /**
     * La représentation interne du graphe.
     * matriceAdjacence[i][j] = 1 signifie qu'il y a un arc du sommet i vers le sommet j.
     * matriceAdjacence[i][j] = 0 signifie qu'il n'y a pas d'arc.
     */
    private int[][] matriceAdjacence;

    /**
     * Construit un objet Graphe à partir d'une matrice d'adjacence existante.
     * @param matrice La matrice d'adjacence (int[][]). Doit être une matrice carrée.
     * @throws IllegalArgumentException si la matrice n'est pas carrée.
     */
    public Graphe(int[][] matrice) {
        if (matrice == null || matrice.length == 0) {
            throw new IllegalArgumentException("La matrice ne peut pas être nulle ou vide.");
        }
        int nbLignes = matrice.length;
        for (int i = 0; i < nbLignes; i++) {
            if (matrice[i].length != nbLignes) {
                throw new IllegalArgumentException("La matrice d'adjacence doit être carrée.");
            }
        }
        this.matriceAdjacence = matrice;
    }

    /**
     * Retourne l'ordre du graphe (le nombre de sommets).
     * @return Le nombre de sommets.
     */
    public int getOrdre() {
        return this.matriceAdjacence.length;
    }

    /**
     * Retourne la taille du graphe (le nombre d'arcs).
     * @return Le nombre d'arcs.
     */
    public int getTaille() {
        int taille = 0;
        for (int i = 0; i < getOrdre(); i++) {
            for (int j = 0; j < getOrdre(); j++) {
                // Si la valeur est 1, c'est un arc, on incrémente le compteur.
                if (this.matriceAdjacence[i][j] == 1) {
                    taille++;
                }
            }
        }
        return taille;
    }
    
    /**
     * Retourne la liste des sommets du graphe (représentés par leurs indices).
     * @return Une liste d'entiers de 0 à N-1.
     */
    public List<Integer> getSommets() {
        List<Integer> sommets = new ArrayList<>();
        for (int i = 0; i < getOrdre(); i++) {
            sommets.add(i);
        }
        return sommets;
    }

    /**
     * Vérifie s'il existe un arc entre deux sommets.
     * @param sommet1 Le sommet de départ.
     * @param sommet2 Le sommet d'arrivée.
     * @return true si un arc existe de sommet1 vers sommet2, false sinon.
     */
    public boolean sontVoisins(int sommet1, int sommet2) {
        // On vérifie que les indices sont valides pour éviter les erreurs.
        if (sommet1 >= 0 && sommet1 < getOrdre() && sommet2 >= 0 && sommet2 < getOrdre()) {
            return this.matriceAdjacence[sommet1][sommet2] == 1;
        }
        return false;
    }

    /**
     * Retourne la liste des voisins (successeurs) d'un sommet donné.
     * @param sommet Le sommet dont on veut connaître les voisins.
     * @return Une liste d'entiers représentant les indices des sommets voisins.
     */
    public List<Integer> getVoisins(int sommet) {
        List<Integer> voisins = new ArrayList<>();
        if (sommet >= 0 && sommet < getOrdre()) {
            for (int j = 0; j < getOrdre(); j++) {
                if (this.matriceAdjacence[sommet][j] == 1) {
                    voisins.add(j);
                }
            }
        }
        return voisins;
    }

    /**
     * Affiche la matrice d'adjacence dans la console de manière lisible.
     */
    public void afficherMatrice() {
        System.out.println("--- Matrice d'adjacence ---");
        for (int i = 0; i < getOrdre(); i++) {
            for (int j = 0; j < getOrdre(); j++) {
                System.out.print(this.matriceAdjacence[i][j] + " ");
            }
            System.out.println(); // Saut de ligne après chaque ligne de la matrice
        }
        System.out.println("---------------------------");
    }
}
