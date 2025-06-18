package modele.graphe.algorithme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import modele.persistence.Secouriste;

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

    // =======================================================================
    // ALGORITHME 2 : GLOUTON NAÏF
    // =======================================================================
    /**
     * Approche gloutonne simple : affecte le premier secouriste compatible trouvé pour chaque poste.
     * @param matriceAdjacence La matrice de compatibilité.
     * @return Un tableau int[] représentant le couplage.
     */
    public int[] resoudreAffectationGloutonneNaive(int[][] matriceAdjacence) {
        AffectationMethode.cpt = 0;
        if (matriceAdjacence == null || matriceAdjacence.length == 0) return new int[0];
        
        int nbSecouristes = matriceAdjacence.length;
        int nbPostes = matriceAdjacence[0].length;

        int[] couplageResultat = new int[nbPostes];
        Arrays.fill(couplageResultat, -1);
        boolean[] secouristeEstPris = new boolean[nbSecouristes];

        for (int posteIndex = 0; posteIndex < nbPostes; posteIndex++) {
            for (int secouristeIndex = 0; secouristeIndex < nbSecouristes; secouristeIndex++) {
                AffectationMethode.cpt++;
                if (matriceAdjacence[secouristeIndex][posteIndex] == 1 && !secouristeEstPris[secouristeIndex]) {
                    couplageResultat[posteIndex] = secouristeIndex;
                    secouristeEstPris[secouristeIndex] = true;
                    break;
                }
            }
        }
        return couplageResultat;
    }

    // =======================================================================
    // NOUVEL ALGORITHME 3 : GLOUTON AVEC TRI DE MATRICE
    // =======================================================================
    /**
     * Approche gloutonne qui réorganise la matrice avant l'affectation.
     * 1. Trie les lignes (secouristes) pour traiter les plus spécialisés en premier.
     * 2. Trie les colonnes (postes) pour traiter les plus difficiles à pourvoir en premier.
     * @param matriceAdjacence La matrice de compatibilité originale.
     * @return Un tableau int[] représentant le couplage.
     */
    public int[] resoudreAffectationGloutonneTriMatrice(int[][] matriceAdjacence) {
        AffectationMethode.cpt = 0;
        if (matriceAdjacence == null || matriceAdjacence.length == 0) {
            return new int[0];
        }
        
        int nbLignes = matriceAdjacence.length;
        int nbColonnes = matriceAdjacence[0].length;

        // --- Étape 1 : Tri des lignes (Secouristes) ---

        // // On crée un tableau pour garder la trace des indices originaux des lignes.
        Integer[] mappingLignes = IntStream.range(0, nbLignes).boxed().toArray(Integer[]::new);
        
        // // On calcule la somme de chaque ligne (nombre de compétences par secouriste).
        int[] sommeLignes = new int[nbLignes];
        for (int i = 0; i < nbLignes; i++) {
            sommeLignes[i] = Arrays.stream(matriceAdjacence[i]).sum();
            AffectationMethode.cpt++;
        }
        
        // // On trie le tableau des indices en se basant sur la somme des lignes correspondantes.
        Arrays.sort(mappingLignes, Comparator.comparingInt(i -> sommeLignes[i]));
        AffectationMethode.cpt++;

        // // On crée une nouvelle matrice avec les lignes dans le bon ordre.
        int[][] matriceLignesTriees = new int[nbLignes][nbColonnes];
        for (int i = 0; i < nbLignes; i++) {
            matriceLignesTriees[i] = matriceAdjacence[mappingLignes[i]];
        }

        // --- Étape 2 : Tri des colonnes (Postes) ---

        // // On crée un tableau pour garder la trace des indices originaux des colonnes.
        Integer[] mappingColonnes = IntStream.range(0, nbColonnes).boxed().toArray(Integer[]::new);

        // // On calcule la somme de chaque colonne de la matrice DÉJÀ triée par ligne.
        int[] sommeColonnes = new int[nbColonnes];
        for (int j = 0; j < nbColonnes; j++) {
            for (int i = 0; i < nbLignes; i++) {
                sommeColonnes[j] += matriceLignesTriees[i][j];
            }
            AffectationMethode.cpt++;
        }
        
        // // On trie le tableau des indices de colonnes en se basant sur leur somme.
        Arrays.sort(mappingColonnes, Comparator.comparingInt(j -> sommeColonnes[j]));
        AffectationMethode.cpt++;

        // --- Étape 3 : Affectation gloutonne sur la matrice "virtuellement" triée ---
        
        int[] couplageFinal = new int[nbColonnes];
        Arrays.fill(couplageFinal, -1);
        boolean[] secouristeEstPris = new boolean[nbLignes];

        // // On parcourt les colonnes dans leur ordre de difficulté (grâce à mappingColonnes).
        for (int j_trie : mappingColonnes) { // j_trie est l'index original de la colonne à traiter
            // // On parcourt les lignes dans leur ordre de spécialisation (grâce à mappingLignes).
            for (int i_trie : mappingLignes) { // i_trie est l'index original du secouriste à tester
                AffectationMethode.cpt++;
                // // On vérifie si le secouriste 'i_trie' est compatible avec le poste 'j_trie' ET s'il est libre.
                if (matriceAdjacence[i_trie][j_trie] == 1 && !secouristeEstPris[i_trie]) {
                    // // On fait l'affectation dans le tableau final, en utilisant les indices ORIGINAUX.
                    couplageFinal[j_trie] = i_trie;
                    // // Ce secouriste est maintenant pris.
                    secouristeEstPris[i_trie] = true;
                    // // On arrête de chercher pour ce poste et on passe au suivant.
                    break;
                }
            }
        }
        
        return couplageFinal;
    }
    
}

