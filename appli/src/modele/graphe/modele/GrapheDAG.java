package modele.graphe.modele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente un graphe orienté générique, pouvant contenir n'importe quel type de sommet.
 * Utilise une matrice d'adjacence en interne pour la compatibilité avec les algorithmes existants.
 * @param <T> Le type des objets qui représentent les sommets (ex: Competence, Secouriste...).
 */
public class GrapheDAG<T> {

    private boolean[][] matriceAdjacence;
    private int nombreSommets;
    private ArrayList<T> indexVersSommet; // Permet de retrouver un objet à partir de son index
    private Map<T, Integer> sommetVersIndex; // Permet de retrouver l'index d'un objet

    /**
     * Construit un graphe à partir d'une liste de sommets.
     * @param sommets La liste des objets qui seront les sommets du graphe.
     */
    public GrapheDAG(List<T> sommets) {
        this.nombreSommets = sommets.size();
        this.matriceAdjacence = new boolean[nombreSommets][nombreSommets];
        this.indexVersSommet = new ArrayList<>(sommets);
        this.sommetVersIndex = new HashMap<>();
        
        // On remplit la map pour pouvoir trouver l'index de chaque sommet rapidement
        for (int i = 0; i < nombreSommets; i++) {
            this.sommetVersIndex.put(sommets.get(i), i);
        }
    }

    /**
     * Ajoute un arc (une flèche) entre deux sommets.
     * @param source Le sommet de départ.
     * @param destination Le sommet d'arrivée.
     */
    public void ajouterArc(T source, T destination) {
        Integer indexSource = sommetVersIndex.get(source);
        Integer indexDestination = sommetVersIndex.get(destination);

        if (indexSource != null && indexDestination != null) {
            matriceAdjacence[indexSource][indexDestination] = true;
        }
    }

    // --- Getters utiles pour les algorithmes ---

    /**
     * Retourne la matrice d'adjacence du graphe.
     * @return un tableau 2D de booléens.
     */
    public boolean[][] getMatriceAdjacence() {
        return this.matriceAdjacence;
    }

    public void setMatriceAdjacence(boolean[][] nouvelleMatrice) {
        this.matriceAdjacence = nouvelleMatrice;
        // On met à jour le nombre de sommets en se basant sur la taille de la nouvelle matrice.
        // Note: cette approche suppose que les listes de sommets ne sont plus utilisées par cet objet Graphe,
        // car il a été créé avec une liste vide.
        this.nombreSommets = (nouvelleMatrice != null && nouvelleMatrice.length > 0) ? nouvelleMatrice[0].length : 0;
    }

    /**
     * Retourne le nombre de sommets dans le graphe.
     * @return le nombre de sommets.
     */
    public int getNombreSommets() {
        return this.nombreSommets;
    }

    /**
     * Retourne le sommet (l'objet) correspondant à un index donné.
     * @param index L'index dans la matrice.
     * @return L'objet de type T.
     */
    public T getSommet(int index) {
        return indexVersSommet.get(index);
    }
}
