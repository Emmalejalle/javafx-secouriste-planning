package modele.persistence;

import java.beans.Transient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;



/**
 * Cette classe test les méthode de Admin
 * @author Tardy Elie
 * @version 1.0
 */
public class TestAdmin {

    /**
     * Les instances utilisées pour les tests
     */
    private Admin u1, u2, u3, u4, u5;

    /**
     * Test de la methode Admin ToString et du Constructeur de Admin
     */
    @Test
    public void testConstructeuretToString() {
        System.out.println("=======Test Constructeur et ToString=======");

        System.out.println("Test cas normaux");
        System.out.println("");

        String dateNaissance = "2000-01-01";
        testCasConstructeuretToString(14,"Joe1234567890","Joseph","joe",dateNaissance,"joe.Joseph@univ-lille.fr","06-01-02-03-04","12 rue de la paix", false);
        System.out.println("");
        System.out.println("");

        testCasConstructeuretToString(20,"michel1234567890","jean","michel",dateNaissance,"michel.michel@univ-lille.fr","06-01-02-53-04","62 rue de la paix", false);
        System.out.println("");
        System.out.println("");

        testCasConstructeuretToString(55,"Lolo1234","roro","lolo",dateNaissance,"lolo.lolo@univ-lille.fr","06-01-02-03-04","12 rue de la paix", false);
        System.out.println("");
        System.out.println("");

        System.out.println("Test cas erreur : ");
        System.out.println("");
        testCasConstructeuretToString(-5, "Lolo1234", "roro", "lolo", dateNaissance, "lolo.lolo@univ-lille.fr", "06-01-02-03-04", "12 rue de la paix", true);
        System.out.println("");
        System.out.println("");

        testCasConstructeuretToString(2, null,"roro", "lolo", dateNaissance, "lolo.lolo@univ-lille.fr", "06-01-02-03-04", "12 rue de la paix", true);
        System.out.println("");
        System.out.println("");

        testCasConstructeuretToString(2, "1","roro", "lolo", dateNaissance, "lolo.lolo@univ-lille.fr", "06-01-02-03-04", "12 rue de la paix", true);
        System.out.println("");
        System.out.println("");

        testCasConstructeuretToString(2, "eejbbhvbhvbhebehbvebhvebhvebhevbh","roro", "lolo", dateNaissance, "lolo.lolo@univ-lille.fr", "06-01-02-03-04", "12 rue de la paix", true);
        System.out.println("");
        System.out.println("");


        testCasConstructeuretToString(2, "elie1234","roro", "lolo", null, "lolo.lolo@univ-lille.fr", "06-01-02-03-04", "12 rue de la paix", true);
        System.out.println("");
        System.out.println("");

        testCasConstructeuretToString(2, "null1234","roro", "lolo", dateNaissance, "mail.pasbon", "06-01-02-03-04", "12 rue de la paix", true);
        System.out.println("");
        System.out.println("");

    }

    /**
     * le test cas de constructeur et toString
     *
     * @param id l'id de l'admin
     * @param mdp le mot de passe de l'admin
     * @param nom le nom de l'admin
     * @param prenom le prenom de l'admin
     * @param dateNaissance la date de naissance de l'admin
     * @param email l'email de l'admin
     * @param tel le telephone de l'admin
     * @param adresse l'adresse de l'admin
     * 
     */
    public void testCasConstructeuretToString(long id, String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse, boolean erreur) {
        if(erreur) {
            System.out.println("Cas erreur : message d'erreur attendu");
        }

        System.out.println("Test Constructeur et toString");
        System.out.print(">");
        try {
            Admin res = new Admin(id , mdp, nom, prenom, dateNaissance, email, tel, adresse);
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
     * @param u l'admin dont on test l'id
     * @param id l'id attendu
     */
    public void testCasGetId(Admin u, int id) {
        System.out.println("u.getId() : "+id);
        System.out.print(">");
        if (u.getId() == id) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Les tests de la methode getMdp.
     */

    @Test
    public void testGetMdp() {
        System.out.println("======Test getMdp======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetMdp(u1, "Joe1234567890");
        testCasGetMdp(u2, "deefgerrgg");
    }

    /**
     * Les tests cas de la methode testGetMdp
     * 
     * @param u     l'admin dont on test le mot de passe
     * @param mdp   le mot de passe attendu
     */
    public void testCasGetMdp(Admin u, String mdp) {
        System.out.println("u.getMdp() : "+mdp);
        System.out.print(">");
        if (u.getMdp().equals(mdp)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la methode getNom
     */
    @Test
    public void testGetNom() {
        System.out.println("======Test getNom======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetNom(u1, "Joseph");
        testCasGetNom(u2, "Lorec");
    }

    /**
     * Tests les cas de la methode getNom
     *
     * @param u The Admin instance whose name is being tested.
     * @param nom The expected name of the Admin.
     */
    public void testCasGetNom(Admin u, String nom) {
        System.out.println("u.getNom() : "+nom);
        System.out.print(">");
        if (u.getNom().equals(nom)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la methode getPrenom
     */
    @Test
    public void testGetPrenom() {
        System.out.println("======Test getPrenom======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetPrenom(u1, "joe");
        testCasGetPrenom(u2, "Arthur");
    }


    /**
     * tests les cas de la methode getPrenom
     *
     * @param u l'admin dont on test le prénom
     * @param prenom le prénom attendu
     */
    public void testCasGetPrenom(Admin u, String prenom) {
        System.out.println("u.getPrenom() : "+prenom);
        System.out.print(">");
        if (u.getPrenom().equals(prenom)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Tests la methode getDateNaissance
     */
    @Test
    public void testGetDateNaissance() {
        System.out.println("======Test getDateNaissance======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetDateNaissance(u1, "01/01/2000");
        testCasGetDateNaissance(u2, "01/01/2000");
    }

    /**
     * tests les cas de la methode getDateNaissance
     *
     * @param u l'admin dont on test la date de naissance
     * @param dateNaissance la date de naissance attendue
     */
    public void testCasGetDateNaissance(Admin u, String dateNaissance) {
        System.out.println("u.getDateNaissance() : "+dateNaissance);
        System.out.print(">");
        if (u.getDateNaissance().equals(dateNaissance)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la methode getEmail
     */
    @Test
    public void testGetEmail() {
        System.out.println("======Test getEmail======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetEmail(u1, "joe.Joseph@univ-lille.fr");
        testCasGetEmail(u2, "Lorec.Arthur@gmail.com");
    }

    /**
     * Tests les cas de la methode getEmail
     *
     * @param u     l'admin dont on test l'email
     * @param email l'email attendu
     */
    public void testCasGetEmail(Admin u, String email) {
        System.out.println("u.getEmail() : "+email);
        System.out.print(">");
        if (u.getEmail().equals(email)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la méthode getTel
     */
    @Test
    public void testGetTel() {
        System.out.println("======Test getTel======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetTel(u1, "06-01-02-03-04");
        testCasGetTel(u2, "06-01-02-03-04");
    }

    /**
     * Tests les cas de la methode getTel
     * 
     * @param u     l'admin dont on test le telephone
     * @param tel   le telephone attendu
     */
    public void testCasGetTel(Admin u, String tel) {
        System.out.println("u.getTel() : "+tel);
        System.out.print(">");
        if (u.getTel().equals(tel)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la methode getAdresse
     */
    @Test
    public void testGetAdresse() {
        System.out.println("======Test getAdresse======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetAdresse(u1, "12 rue de la paix");
        testCasGetAdresse(u2, "12 rue de la paix");
    }    

    /**
     * Tests les cas de la methode getAdresse
     * 
     * @param u     l'admin dont on test l'adresse
     * @param adresse l'adresse attendue
     */
    public void testCasGetAdresse(Admin u, String adresse) {
        System.out.println("u.getAdresse() : "+adresse);
        System.out.print(">");
        if (u.getAdresse().equals(adresse)) {
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
     * @param u     l'admin dont on test la methode setId
     * @param id    l'id que l'on essaye de donner a l'admin
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetId(Admin u, int id, boolean erreur) {
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
     * Tests la methode setMdp
     * 
     * @param u     l'admin dont on test la methode setMdp
     * @param mdp   le mot de passe que l'on essaye de donner a l'admin
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    @Test
    public void testSetMdp() {
        System.out.println("======Test setMdp======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetMdp(u1, "Joe1234567890",false);
        testCasSetMdp(u2, "deefgerrgg",false);

        System.out.println("Test cas erreur");
        testCasSetMdp(u1, null,true);
        testCasSetMdp(u2, "12",true);

    }


    /**
     * Tests la methode setMdp
     * 
     * @param u     l'admin dont on test la methode setMdp
     * @param mdp   le mot de passe que l'on essaye de donner a l'admin
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetMdp(Admin u, String mdp, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setMdp("+mdp+")");
        System.out.print(">");
        try {
            u.setMdp(mdp);
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
     * Test de la methode setNom
     */
    @Test
    public void testSetNom() {
        System.out.println("======Test setNom======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetNom(u1, "Joseph",false);
        testCasSetNom(u2, "Lorec",false);

        System.out.println("Test cas erreur");
        testCasSetNom(u1, null,true);
        testCasSetNom(u2, "",true);
    }

    /**
     * Test de la methode setNom
     * 
     * @param u     l'admin dont on test la methode setNom
     * @param nom   le nom que l'on essaye de donner a l'admin
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetNom(Admin u, String nom, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setNom("+nom+")");
        System.out.print(">");
        try {
            u.setNom(nom);
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
     * Tests la methode setPrenom
     */
    @Test
    public void testSetPrenom() {
        System.out.println("======Test setPrenom======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetPrenom(u1, "Joe",false);
        testCasSetPrenom(u2, "Arthur",false);

        System.out.println("Test cas erreur");
        testCasSetPrenom(u1, null,true);
        testCasSetPrenom(u2, "",true);
    }

    /**
     * Tests la methode setPrenom
     *
     * @param u      l'admin dont on test la methode setPrenom
     * @param prenom le prenom que l'on essaye de donner a l'admin
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetPrenom(Admin u, String prenom, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setPrenom("+prenom+")");
        System.out.print(">");
        try {
            u.setPrenom(prenom);
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
     * Tests la methode setDateNaissance
     */
    @Test
    public void TestSetDateNaissance() {
        System.out.println("======Test setDateNaissance======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetDateNaissance(u1, "01/01/2000",false);
        testCasSetDateNaissance(u2, "01/01/2000",false);

        System.out.println("Test cas erreur");
        testCasSetDateNaissance(u1, null,true);
    }


    /**
     * Tests le cas de setDateNaissance
     * @param u l'Admin a tester
     * @param dateNaissance la date de naissance a tester
     * @param erreur si un message d'erreur est attendu
     */
    public void testCasSetDateNaissance(Admin u, String dateNaissance, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setDateNaissance("+dateNaissance+")");
        System.out.print(">");
        try {
            u.setDateNaissance(dateNaissance);
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
     * Tests the setEmail method.
     */
    @Test
    public void testSetEmail() {
        System.out.println("======Test setEmail======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetEmail(u1, "joe.Joseph@univ-lille.fr",false);
        testCasSetEmail(u2, "Lorec.Arthur@gmail.com",false);

        System.out.println("Test cas erreur");
        testCasSetEmail(u1, null,true);
        testCasSetEmail(u2, "dezndj.cek,com",true);
    }


    /**
     * Tests le cas de setEmail
     * 
     * @param u      l'admin dont on test la methode setEmail
     * @param email  l'email que l'on essaye de donner a l'admin
     * @param erreur si vrai, on attend que la methode setEmail leve une exception
     */
    public void testCasSetEmail(Admin u, String email, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setEmail("+email+")");
        System.out.print(">");
        try {
            u.setEmail(email);
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
     * Tests la methode setTel
     */
    @Test
    public void testSetTel() {
        System.out.println("======Test setTel======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetTel(u1, "06-01-02-03-04",false);
        testCasSetTel(u2, "06-01-02-03-04",false);
    
        System.out.println("Test cas erreur");
        testCasSetTel(u1, null,true);
    }

    /**
     * Tests le cas de setTel
     * 
     * @param u      l'admin dont on test la methode setTel
     * @param tel    le telephone que l'on essaye de donner a l'admin
     * @param erreur si vrai, on attend que la methode setTel leve une exception
     */
    public void testCasSetTel(Admin u, String tel, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setTel("+tel+")");
        System.out.print(">");
        try {
            u.setTel(tel);
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
     * Tests la methode setAdresse
     */
    @Test
    public void testSetAdresse() {
        System.out.println("======Test setAdresse======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetAdresse(u1, "12 rue de la paix",false);
        testCasSetAdresse(u2, "12 rue de la paix",false);
    
        System.out.println("Test cas erreur");
        testCasSetAdresse(u1, null,true);
    }

    /**
     * Tests la methode setAdresse
     *
     * @param u       l'admin dont on test la methode setAdresse
     * @param adresse l'adresse que l'on essaye de donner a l'admin
     * @param erreur  si vrai, on attend que la methode setAdresse leve une exception
     */
    public void testCasSetAdresse(Admin u, String adresse, boolean erreur) {
        if(erreur){
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("u.setAdresse("+adresse+")");
        System.out.print(">");
        try {
            u.setAdresse(adresse);
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
        
        JUnitCore.runClasses(TestAdmin.class);
    
    }


    /**
     * Initializes the test environment before each test.
     */
    @Before
    public void instancier() {
        String dateNaissance = "01/01/2000";
        this.u1 = new Admin(14, "Joe1234567890", "Joseph", "joe", dateNaissance, "joe.Joseph@univ-lille.fr", "06-01-02-03-04", "12 rue de la paix");
        this.u2 = new Admin(17, "deefgerrgg", "Lorec", "Arthur", dateNaissance, "Lorec.Arthur@gmail.com", "06-01-02-03-04", "12 rue de la paix");
        this.u3 = new Admin(19, "veeveevev", "Deogen", "Nino", dateNaissance, "Deogen.Nino@gmail.fr", "06-01-02-03-04", "12 rue de la paix");
        this.u4 = new Admin(1, "veeezdftr", "Nomad", "Joseph", dateNaissance, "Nomad.Joseph@univ-ubs.fr", "06-01-02-03-04", "12 rue de la paix");
        this.u5 = null;
    }
}