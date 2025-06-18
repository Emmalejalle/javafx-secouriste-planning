package modele.graphe.algorithme;

import java.util.Arrays;

/**
 * Contient les algorithmes pour l'affectation des secouristes.
 * Cette classe implémente une approche exhaustive (backtracking) pour trouver
 * le couplage de cardinalité maximale dans un graphe biparti.
 */
public class AffectationMethode {

    /**
     * Compteur d'opérations pour évaluer la complexité et comparer les algorithmes.
     * Il est public et static pour être facilement accessible et réinitialisable.
     */
    public static long cpt = 0;

    // Attributs privés pour stocker la meilleure solution trouvée pendant la récursion.
    private int[] meilleurCouplage;
    private int tailleMeilleurCouplage;

    /**
     * Méthode principale qui lance l'algorithme d'affectation exhaustif.
     * @param matriceAdjacence La matrice représentant le graphe biparti.
     * Les lignes sont les secouristes, les colonnes sont les postes.
     * matrice[i][j] = 1 si le secouriste i est compatible avec le poste j.
     * @return Un tableau d'entiers représentant le couplage optimal.
     * resultat[j] = i signifie que le poste j est affecté au secouriste i.
     * resultat[j] = -1 si le poste j n'est pas pourvu.
     */
    public int[] resoudreAffectationExhaustive(int[][] matriceAdjacence) {
        // Vérification que la matrice n'est pas vide.
        if (matriceAdjacence == null || matriceAdjacence.length == 0) {
            return new int[0];
        }

        int nbSecouristes = matriceAdjacence.length;
        int nbPostes = matriceAdjacence[0].length;

        // Réinitialisation des variables pour une nouvelle exécution.
        AffectationMethode.cpt = 0;
        this.tailleMeilleurCouplage = 0;
        this.meilleurCouplage = new int[nbPostes];
        Arrays.fill(this.meilleurCouplage, -1);

        // Tableaux de travail pour la récursion.
        int[] couplageActuel = new int[nbPostes];
        Arrays.fill(couplageActuel, -1);
        boolean[] secouristeEstPris = new boolean[nbSecouristes];

        // On lance l'algorithme récursif en commençant par le premier poste (index 0).
        backtrack(0, 0, couplageActuel, secouristeEstPris, matriceAdjacence);

        // Une fois toutes les combinaisons explorées, on retourne la meilleure trouvée.
        return this.meilleurCouplage;
    }

    /**
     * Le cœur de l'algorithme : une fonction récursive qui explore toutes les combinaisons.
     * @param posteIndex L'index du poste que l'on essaie d'affecter.
     * @param tailleActuelle Le nombre d'affectations déjà réalisées dans la branche actuelle.
     * @param couplageActuel L'état des affectations dans la branche actuelle.
     * @param secouristeEstPris Un tableau pour savoir quels secouristes sont déjà affectés.
     * @param matriceAdjacence La matrice de compatibilité.
     */
    private void backtrack(int posteIndex, int tailleActuelle, int[] couplageActuel, boolean[] secouristeEstPris, int[][] matriceAdjacence) {
        // À chaque appel, on considère une décision, donc on incrémente le compteur.
        AffectationMethode.cpt++;

        // Cas de base : si on a considéré tous les postes.
        if (posteIndex == matriceAdjacence[0].length) {
            // On a atteint la fin d'une branche, on a donc une solution complète.
            // On vérifie si cette solution est meilleure que la meilleure enregistrée.
            if (tailleActuelle > this.tailleMeilleurCouplage) {
                this.tailleMeilleurCouplage = tailleActuelle;
                // On copie la solution actuelle comme étant la nouvelle meilleure.
                this.meilleurCouplage = Arrays.copyOf(couplageActuel, couplageActuel.length);
            }
            return; // On arrête cette branche de la récursion.
        }

        // --- Exploration récursive ---

        // Choix N°1 : On décide de NE PAS pourvoir le poste actuel (posteIndex).
        // On passe directement au poste suivant sans rien changer.
        backtrack(posteIndex + 1, tailleActuelle, couplageActuel, secouristeEstPris, matriceAdjacence);

        // Choix N°2 : On essaie d'affecter le poste actuel à CHAQUE secouriste compatible.
        for (int secouristeIndex = 0; secouristeIndex < matriceAdjacence.length; secouristeIndex++) {
            // On vérifie si le secouriste 'i' est compatible avec le poste 'j' ET s'il est libre.
            if (matriceAdjacence[secouristeIndex][posteIndex] == 1 && !secouristeEstPris[secouristeIndex]) {
                
                // --- On fait l'affectation ---
                secouristeEstPris[secouristeIndex] = true; // Ce secouriste n'est plus libre.
                couplageActuel[posteIndex] = secouristeIndex; // On assigne le secouriste au poste.

                // --- On continue l'exploration ---
                // On appelle la fonction pour le poste suivant, avec la nouvelle affectation.
                backtrack(posteIndex + 1, tailleActuelle + 1, couplageActuel, secouristeEstPris, matriceAdjacence);

                // --- On annule l'affectation (c'est le "backtrack") ---
                // C'est l'étape la plus importante. On revient en arrière pour pouvoir explorer
                // les autres possibilités (ex: affecter un autre secouriste à ce poste).
                secouristeEstPris[secouristeIndex] = false;
                couplageActuel[posteIndex] = -1; 
            }
        }
    }

}