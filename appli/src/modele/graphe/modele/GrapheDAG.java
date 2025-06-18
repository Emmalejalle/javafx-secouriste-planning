package modele.graphe.modele;

import modele.persistence.Competence; // Important d'importer Competence
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente un graphe orienté générique.
 * Cette version a été corrigée pour inclure une méthode de construction
 * qui peuple automatiquement la matrice d'adjacence pour les compétences.
 */
public class GrapheDAG<T> {

    private boolean[][] matriceAdjacence;
    private int nombreSommets;
    private ArrayList<T> indexVersSommet;
    private Map<T, Integer> sommetVersIndex;

    /**
     * Constructeur de base. Crée un graphe avec des sommets mais SANS arcs.
     * @param sommets La liste des objets qui seront les sommets du graphe.
     */
    public GrapheDAG(List<T> sommets) {
        this.nombreSommets = sommets.size();
        this.matriceAdjacence = new boolean[nombreSommets][nombreSommets];
        this.indexVersSommet = new ArrayList<>(sommets);
        this.sommetVersIndex = new HashMap<>();
        
        for (int i = 0; i < nombreSommets; i++) {
            this.sommetVersIndex.put(sommets.get(i), i);
        }
    }

    /**
     * MÉTHODE "FACTORY" : C'est la méthode à utiliser dans ton application !
     * Elle crée un graphe de compétences et peuple automatiquement la matrice
     * en lisant les prérequis de chaque objet Competence.
     * @param competences La liste complète des compétences.
     * @return Un objet GrapheDAG prêt à être utilisé par les algorithmes.
     */
    public static GrapheDAG<Competence> creerGrapheDeCompetences(List<Competence> competences) {
        // // Étape 1 : On crée un graphe de base. Sa matrice est vide pour l'instant.
        GrapheDAG<Competence> graphe = new GrapheDAG<>(competences);

        // // Étape 2 (LA PLUS IMPORTANTE) : On remplit la matrice.
        // // On parcourt chaque compétence de la liste.
        for (Competence source : competences) {
            // // On regarde la liste de ses prérequis.
            if (source.getPrerequis() != null) {
                for (Competence destination : source.getPrerequis()) {
                    // // Pour chaque prérequis trouvé, on ajoute un arc dans le graphe.
                    // // C'est ici que les 'true' sont ajoutés à la matrice.
                    graphe.ajouterArc(source, destination);
                }
            }
        }
        // // Le graphe est maintenant complet et prêt pour la détection de cycle.
        return graphe;
    }

    /**
     * Ajoute un arc (une flèche) entre deux sommets.
     */
    public void ajouterArc(T source, T destination) {
        Integer indexSource = sommetVersIndex.get(source);
        Integer indexDestination = sommetVersIndex.get(destination);

        if (indexSource != null && indexDestination != null) {
            matriceAdjacence[indexSource][indexDestination] = true;
        }
    }

    // --- Getters ---
    public boolean[][] getMatriceAdjacence() {
        return this.matriceAdjacence;
    }

    public int getNombreSommets() {
        return this.nombreSommets;
    }
}
