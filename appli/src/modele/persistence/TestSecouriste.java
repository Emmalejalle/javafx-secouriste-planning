package modele.persistence;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;



public class TestSecouriste {
    
    /**
     * Secouristes utilisés pour les tests
     */
    private Secouriste u1, u2, u3, u4, u5;

    /**
     * Journees utilisées pour les tests
     */
    private Journee jContenu;

    private Competence  cPossede;

    /**
     * Tests the Secouriste constructor and toString method.
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
     * the constructor and toString with various cases.
     *
     * @param theAmnt   The amount to use for Secouriste.
     * @param theCurr   The currency to use for Secouriste.
     * @param attendu   The expected string representation.
     * @param casErreur Indicates if the test should result in an error.
     */
    public void testCasConstructeuretToString(long id, String mdp, String nom, String prenom, String dateNaissance, String email, String tel, String adresse, boolean erreur) {
        if(erreur) {
            System.out.println("Cas erreur : message d'erreur attendu");
        }

        System.out.println("Test Constructeur et toString");
        System.out.print(">");
        try {
            Secouriste res = new Secouriste(id , mdp, nom, prenom, dateNaissance, email, tel, adresse);
            if (erreur) {
                    System.out.println("Le test cas d'erreur a échoué");
            } else {
                System.out.println("Le test cas normal a réussi");
                String retStr = res.toString();
                System.out.println(retStr);
            }
        } catch (IllegalArgumentException e) {
            if (!erreur) {
                System.out.println("Le test cas normal a échoué");
            } else {
                System.out.println("Le test cas d'erreur a réussi");
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
     * @param u l'Secouriste dont on test l'id
     * @param id l'id attendu
     */
    public void testCasGetId(Secouriste u, int id) {
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
     * @param u     l'Secouriste dont on test le mot de passe
     * @param mdp   le mot de passe attendu
     */
    public void testCasGetMdp(Secouriste u, String mdp) {
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
     * @param u The Secouriste instance whose name is being tested.
     * @param nom The expected name of the Secouriste.
     */
    public void testCasGetNom(Secouriste u, String nom) {
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
     * @param u l'Secouriste dont on test le prénom
     * @param prenom le prénom attendu
     */
    public void testCasGetPrenom(Secouriste u, String prenom) {
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
     * @param u l'Secouriste dont on test la date de naissance
     * @param dateNaissance la date de naissance attendue
     */
    public void testCasGetDateNaissance(Secouriste u, String dateNaissance) {
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
     * @param u     l'Secouriste dont on test l'email
     * @param email l'email attendu
     */
    public void testCasGetEmail(Secouriste u, String email) {
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
     * @param u     l'Secouriste dont on test le telephone
     * @param tel   le telephone attendu
     */
    public void testCasGetTel(Secouriste u, String tel) {
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
     * @param u     l'Secouriste dont on test l'adresse
     * @param adresse l'adresse attendue
     */
    public void testCasGetAdresse(Secouriste u, String adresse) {
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
     * @param u     l'Secouriste dont on test la methode setId
     * @param id    l'id que l'on essaye de donner a l'Secouriste
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetId(Secouriste u, int id, boolean erreur) {
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
     * @param u     l'Secouriste dont on test la methode setMdp
     * @param mdp   le mot de passe que l'on essaye de donner a l'Secouriste
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
     * @param u     l'Secouriste dont on test la methode setMdp
     * @param mdp   le mot de passe que l'on essaye de donner a l'Secouriste
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetMdp(Secouriste u, String mdp, boolean erreur) {
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
     * @param u     l'Secouriste dont on test la methode setNom
     * @param nom   le nom que l'on essaye de donner a l'Secouriste
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetNom(Secouriste u, String nom, boolean erreur) {
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
     * @param u      l'Secouriste dont on test la methode setPrenom
     * @param prenom le prenom que l'on essaye de donner a l'Secouriste
     * @param erreur si vrai, on attend que la methode setId leve une exception
     */
    public void testCasSetPrenom(Secouriste u, String prenom, boolean erreur) {
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
     * @param u l'Secouriste a tester
     * @param dateNaissance la date de naissance a tester
     * @param erreur si un message d'erreur est attendu
     */
    public void testCasSetDateNaissance(Secouriste u, String dateNaissance, boolean erreur) {
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
     * @param u      l'Secouriste dont on test la methode setEmail
     * @param email  l'email que l'on essaye de donner a l'Secouriste
     * @param erreur si vrai, on attend que la methode setEmail leve une exception
     */
    public void testCasSetEmail(Secouriste u, String email, boolean erreur) {
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
     * @param u      l'Secouriste dont on test la methode setTel
     * @param tel    le telephone que l'on essaye de donner a l'Secouriste
     * @param erreur si vrai, on attend que la methode setTel leve une exception
     */
    public void testCasSetTel(Secouriste u, String tel, boolean erreur) {
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
     * @param u       l'Secouriste dont on test la methode setAdresse
     * @param adresse l'adresse que l'on essaye de donner a l'Secouriste
     * @param erreur  si vrai, on attend que la methode setAdresse leve une exception
     */
    public void testCasSetAdresse(Secouriste u, String adresse, boolean erreur) {
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
     * Tests la methode estDisponible
     */
    @Test 
    public void testEstDisponible() {
        System.out.println("======Test estDisponible======");
        System.out.println("Test cas normaux");
        Journee j1 = new Journee(1, 1, 1, 2023);
        System.out.println("");
        testCasEstDisponible(u1, j1, false);
        testCasEstDisponible(u1,this.jContenu,true);

        System.out.println("Test cas d'erreur");
        testCasEstDisponible(u1, null, false);
        
    }


    
    /**
     * Tests le cas d'erreur et de normal pour la methode estDisponible
     * @param u l'objet Secouriste a tester
     * @param j la Journee a tester
     * @param resAtt le resultat attendu
     * @param erreur si vrai, on attend que la methode leve une exception
     */
    public void testCasEstDisponible(Secouriste u, Journee j, boolean resAtt) {
        System.out.println("u.estDisponible("+j+") : "+resAtt);
        System.out.print(">");
        boolean res = u.estDisponible(j);
        if (res == resAtt) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }

    }

    /**
     * Tests la methode possedeCompetence
     */
    @Test
    public void testPossedeCompetence() {
        System.out.println("======Test possedeCompetence======");
        System.out.println("");
        System.out.println("Test cas normaux");
        Competence c1 = new Competence("Premier Secour","PS");
        testCasPossedeCompetence(u1, c1, false,false);
        testCasPossedeCompetence(u1, this.cPossede, true,false);
    
        System.out.println("Test cas erreur");
        testCasPossedeCompetence(u1, null,false,true);
    }

    /**
     * Tests le cas d'erreur et de normal pour la methode possedeCompetence
     * @param u l'objet Secouriste a tester
     * @param c la Competence a tester
     * @param resAtt le resultat attendu
     * @param erreur si vrai, on attend que la methode leve une exception
     */
    public void testCasPossedeCompetence(Secouriste u, Competence c, boolean resAtt, boolean erreur) {
        System.out.println("u.possedeCompetence("+c+") : "+resAtt);
        System.out.print(">");
        boolean res = u.possedeCompetence(c);
        if (res == resAtt) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }
    
    



    /**
     * Main method to run JUnit tests for the TestSecouriste class.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        
        JUnitCore.runClasses(TestSecouriste.class);
    
    }


    /**
     * Initializes the test environment before each test.
     */
    @Before
    public void instancier() {
        String dateNaissance = "01/01/2000";
        this.u1 = new Secouriste(14, "Joe1234567890", "Joseph", "joe", dateNaissance, "joe.Joseph@univ-lille.fr", "06-01-02-03-04", "12 rue de la paix");
        this.u2 = new Secouriste(17, "deefgerrgg", "Lorec", "Arthur", dateNaissance, "Lorec.Arthur@gmail.com", "06-01-02-03-04", "12 rue de la paix");
        this.u3 = new Secouriste(19, "veeveevev", "Deogen", "Nino", dateNaissance, "Deogen.Nino@gmail.fr", "06-01-02-03-04", "12 rue de la paix");
        this.u4 = new Secouriste(1, "veeezdftr", "Nomad", "Joseph", dateNaissance, "Nomad.Joseph@univ-ubs.fr", "06-01-02-03-04", "12 rue de la paix");
        this.u5 = null;
        Journee j1 = new Journee(12,3,4,2000);
        Journee j2 = new Journee(13,10,4,2000);
        this.jContenu = j1;
        ArrayList<Journee> disponibilites = new ArrayList<>();
        disponibilites.add(j1);
        disponibilites.add(j2);
        u1.setDisponibilites(disponibilites);

        Competence c1 = new Competence("Pilote","PIL");
        Competence c2 = new Competence("Private First Class","PFC");
        this.cPossede = c1;
        ArrayList<Competence> competences = new ArrayList<>();
        competences.add(c1);
        competences.add(c2);
        u1.setCompetences(competences);


    }
}