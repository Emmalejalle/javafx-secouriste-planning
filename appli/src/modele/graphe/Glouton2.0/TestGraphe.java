import java.util.HashMap;
import java.util.Map;

public class TestGraphe {

    /**
     * Le main du programme.
     * @param args les arguments du programme (non utilise)
     */
    public static void main(String[] args) {
        testGlouton2();
    }


    /**
     * Test de la classe Glouton2
     */
    public static void testGlouton2(){
        System.out.println("======Test Glouton2======");
        System.out.println("------Test Visuel------");
        System.out.println("Test cas normaux");
        int [][]tabAffect1 = {
            {0,0,0,1},
            {0,0,1,0},
            {0,0,0,0},
            {0,0,0,0}
        };

        testCasGloton2(tabAffect1,2,false);

        int [][]tabAffect2 = {
            {0,0,0,0,1,1},
            {0,0,0,0,1,1},
            {0,0,0,1,0,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0}
        };

        testCasGloton2(tabAffect2,3,false);

        System.out.println("Test Cas limites");
        testCasGloton2(tabAffect1,1,false);

        int [][]tabAffect3 = {
            {0,0,0,0,1,1},
            {0,0,0,0,1,1},
            {0,0,0,0,1,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0}
        };

        testCasGloton2(tabAffect3,2,false);

        System.out.println("Test Cas erreurs");
        testCasGloton2(null,-1,true);

        int [][]tabAffect4 = {};

        testCasGloton2(tabAffect4,-1,true);

        int [][]tabAffect5 = {
            {0,0,0,0,1},
            {0,0,0,0,1},
            {0,0,0,0,1},
            {0,0,0,0,0},
            {0,0,0,0,0},
            {0,0,0,0,0}
        };

        testCasGloton2(tabAffect5,-1,true);

    }


    /**
     * Effectue un test des cas sur la methode affecter de la classe Glouton2.
     * @param tabAffect le tableau de l'affectation
     * @param nbSecouristes le nombre de secouristes attendus
     * @param erreur si vrai, le test est considere comme erreur
     */
    public static void testCasGloton2(int[][] tabAffect, int nbSecouristes, boolean erreur){
        if(erreur){
            System.out.println("Cas erreur message attendu");
        }
        System.out.println("Affecter(tabAffect), length attendu = "+nbSecouristes);
        System.out.print(">");
        try{
            HashMap<String,String> res = new Glouton2().affecter(tabAffect);
            if (res.size() == nbSecouristes){
                System.out.println("OK");
            }else{
                System.out.println("ERREUR");
            }
        } catch (Exception e) {
            if(erreur){
                System.out.println("OK");
                System.out.println("Message d'erreur :"+e.getMessage());
            }else{
                System.out.println("ERREUR");
            }
        }
        System.out.println();
    }

    
}