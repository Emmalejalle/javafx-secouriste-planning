package modele.graphe.modele;

import modele.persistence.Competence; // Important d'importer Competence
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un graphe orienté générique.
 * CETTE VERSION A ÉTÉ CORRIGÉE POUR ÊTRE PLUS FIABLE.
 * Elle n'utilise plus de HashMap pour éviter les problèmes liés
 * aux méthodes equals() et hashCode() des objets sommets.
 * 
 * @author Emilien EMERIAU
 * @version 1.0
 */
public class GrapheDAG<T> {

    private boolean[][] matriceAdjacence;
    private int nombreSommets;
    private ArrayList<T> indexVersSommet; // Permet de retrouver un objet à partir de son index

    /**
     * Constructeur de base. Crée un graphe avec des sommets mais SANS arcs.
     * @param sommets La liste des objets qui seront les sommets du graphe.
     */
    public GrapheDAG(List<T> sommets) {
        this.nombreSommets = sommets.size();
        this.matriceAdjacence = new boolean[nombreSommets][nombreSommets];
        this.indexVersSommet = new ArrayList<>(sommets);
    }

    /**
     * Méthode "FACTORY" : C'est la méthode à utiliser dans ton application !
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
        // // CORRECTION : On utilise indexOf au lieu d'une Map pour trouver l'index.
        // // C'est plus simple et ça marche même si equals/hashCode ne sont pas définis.
        int indexSource = this.indexVersSommet.indexOf(source);
        int indexDestination = this.indexVersSommet.indexOf(destination);

        // // On vérifie que les deux sommets existent bien dans notre graphe.
        if (indexSource != -1 && indexDestination != -1) {
            matriceAdjacence[indexSource][indexDestination] = true;
        }
    }

    // --- Getters ---
    public boolean[][] getMatriceAdjacence() {
        return this.matriceAdjacence;
    }

    /**
     * Retourne le nombre de sommets (de compétences) dans ce graphe.
     * @return Le nombre de sommets.
     */
    public int getNombreSommets() {
        return this.nombreSommets;
    }
}
