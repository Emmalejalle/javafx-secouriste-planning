package modele.persistence;

import java.beans.Transient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;



/**
 * Cette classe test les méthode de DPS
 * @author Tardy Elie
 * @version 1.0
 */
public class TestDPS {

    /**
     * Les instances utilisées pour les tests
     */
    private DPS u1, u2;

    /**
     * Test de la methode DPS ToString et du Constructeur de DPS
     */
    @Test
    public void testConstructeuretToString() {
        System.out.println("=======Test Constructeur et ToString=======");

        System.out.println("Test cas normaux");
        System.out.println("");
        Site site1 = new Site(5,"Lille", 50f, 50f);
        Sport sport1 = new Sport(12,"Football");
        Journee journee1 = new Journee(10,7,4,2005);
        testCasConstructeuretToString(2, 300, 700, site1, sport1, journee1, false);



    }

    /**
     * le test cas de constructeur et toString
     *
     * @param id l'id de l'DPS
     * @param mdp le mot de passe de l'DPS
     * @param nom le nom de l'DPS
     * @param prenom le prenom de l'DPS
     * @param dateNaissance la date de naissance de l'DPS
     * @param email l'email de l'DPS
     * @param tel le telephone de l'DPS
     * @param adresse l'adresse de l'DPS
     * 
     */
    public void testCasConstructeuretToString(long id,int horaireDepart, int horaireFin, Site site, Sport sport, Journee journee, boolean erreur) {
        if(erreur) {
            System.out.println("Cas erreur : message d'erreur attendu");
        }

        System.out.println("Test Constructeur et toString");
        System.out.print(">");
        try {
            DPS res = new DPS(id , horaireDepart, horaireFin, site, sport, journee);
            if (erreur) {
                    System.out.println("ERREUR DU PROGRAMME");
            } else {
                System.out.println("OK");
                String retStr = res.toString();
                System.out.println(retStr);
            }
        } catch (IllegalArgumentException e) {
            if (!erreur) {
                System.out.println("ERREUR DU PROGRAMME");
            } else {
                System.out.println("OK");
                System.out.println("Le message d'erreur est : " + e.getMessage());

            }
        }
    }



    /**
     * Le test de la méthode getId
     */
    @Test
    public void testGetId() {

        System.out.println("======Test getId======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetId(u1, 14);
        testCasGetId(u2,17);
    }

    /**
     * Le test cas de getId
     * 
     * @param u l'DPS dont on test l'id
     * @param id l'id attendu
     */
    public void testCasGetId(DPS u, long id) {
        System.out.println("u.getId() : "+id);
        System.out.print(">");
        if (u.getId() == id) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Tests la methode setId
     */
    @Test
    public void testSetId() {
        System.out.println("======Test setId======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetId(u1, 2,false);
        testCasSetId(u2, 7,false);
        System.out.println("");
        System.out.println("Test cas erreur");
        testCasSetId(u1, -1,true);
    }

    /**
     * Test de la methode setId
     * 
     * @param u     l'DPS dont on test la methode setId
     * @param id    l'id que l'on essaye de donner a l'DPS
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetId(DPS u, int id, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setId("+id+")");
        System.out.print(">");
        try {
            u.setId(id);
            if(erreur){
                System.out.println("ERREUR DU PROGRAMME");
            } else {
                System.out.println("OK");
            }
        } catch (Exception e) {
            if (erreur) {
                System.out.println("OK");
                System.out.println("Message d'erreur : " + e.getMessage());
            } else {
                System.out.println("ERREUR DU PROGRAMME");
            }
        }
    }



    /**
     * La methode main pour lancer les tests
     *
     * @param args Les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        
        JUnitCore.runClasses(TestDPS.class);
    
    }


    /**
     * Initializes the test environment before each test.
     */
    @Before
    public void instancier() {
        Site site = new Site(12,"Lorec", 300, 500);
        Sport sport = new Sport(15, "Tennis");
        Journee journee = new Journee(13,1, 1, 2020);
        this.u1 = new DPS(14, 300,500,site,sport,journee);
        this.u2 = new DPS(17,500,600,site,sport,journee);
    }
}