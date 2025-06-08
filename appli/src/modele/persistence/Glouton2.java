import java.util.HashMap;

public class Glouton2 {

    private HashMap<String, String> affectation = new HashMap<>();


    public void affecter(int[][] tabAffect) {
        int total = tabAffect.length;
        int n = total / 2; 
        int[] degreSortant = new int[n]; 
        int[] degreEntrant = new int[n]; 

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

            if (minTache != -1) {
                for (int j = 0; j < n; j++) {
                    if (!secouristeAffecte[j] && tabAffect[minTache][j + n] == 1 && degreEntrant[j] < minDegSecouriste) {
                        minDegSecouriste = degreEntrant[j];
                        minSecouriste = j;
                    }
                }
            }

            if (minTache != -1 && minSecouriste != -1) {
                affectation.put("Tache " + (minTache + 1), "Secouriste " + (minSecouriste + 1));
                tacheAffectee[minTache] = true;
                secouristeAffecte[minSecouriste] = true;
            }

            String affichage = "";
            for (int i = 0; i < n; i++) {
                affichage += "Tache " + (i + 1) + " : " + degreSortant[i] + "  ";
            }
            System.out.println(affichage);
            affichage = "";
            for (int i = 0; i < n; i++) {
                affichage += "Secouriste " + (i + 1) + " : " + degreEntrant[i] + "  ";
            }
            System.out.println(affichage);

        }



    }
}
