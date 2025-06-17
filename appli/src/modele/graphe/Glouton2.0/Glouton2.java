import java.util.HashMap;

/**
 * Cette classe est la classe d'affectation en version glouton 2
 * @author Tardy Elie
 * @version 1.0
 */
public class Glouton2 {

    /**
     * Le HashMap contenant l'affectation
     */
    private HashMap<String, String> affectation = new HashMap<String,String>();

    /**
     * le constructeur
     */
    public Glouton2() {
        this.affectation.put("tache", "secouriste");
    }

    /**
     * Cette fonction effectue l'affectation en utilisant l'algorithme glouton 2
     * @param tabAffect le tableau de l'affectation
     * @return le HashMap contenant l'affectation
     */
    public HashMap<String, String> affecter(int[][] tabAffect) throws IllegalArgumentException {
        
        // On vérifie la forme du tableau
        if (tabAffect == null || tabAffect.length == 0 || tabAffect.length % 2 != 0) {
            throw new IllegalArgumentException("Le tableau n'a pas la bonne forme");
        } else {
            this.affectation.clear(); 
            int total = tabAffect.length;
            int n = total / 2; 
            int[] degreSortant = new int[n]; 
            int[] degreEntrant = new int[n]; 

            // Calcul des degrés
            for (int i = 0; i < n; i++) {
                for (int j = n; j < total; j++) { 
                    if (tabAffect[i][j] == 1) {
                        degreSortant[i]++;
                        degreEntrant[j - n]++;
                    }
                }
            }


            boolean[] tacheAffectee = new boolean[n];
            boolean[] secouristeAffecte = new boolean[n];
            // Affectation
            for (int count = 0; count < n; count++) {
                int minDegTache = Integer.MAX_VALUE;
                int minTache = -1;

                for (int i = 0; i < n; i++) {
                    if (!tacheAffectee[i] && degreSortant[i] < minDegTache && degreSortant[i] > 0) {
                        minDegTache = degreSortant[i];
                        minTache = i;
                    }
                }

                int minDegSecouriste = Integer.MAX_VALUE;
                int minSecouriste = -1;
                // Selection du secouriste
                if (minTache != -1) {
                    for (int j = 0; j < n; j++) {
                        if (!secouristeAffecte[j] && tabAffect[minTache][j + n] == 1 && degreEntrant[j] < minDegSecouriste) {
                            minDegSecouriste = degreEntrant[j];
                            minSecouriste = j;
                        }
                    }
                }
                // Affectation
                if (minTache != -1 && minSecouriste != -1) {
                    this.affectation.put("Tache " + (minTache + 1), "Secouriste " + (minSecouriste + 1));
                    tacheAffectee[minTache] = true;
                    secouristeAffecte[minSecouriste] = true;
                }
            }
        }

        return this.affectation;
    }



}