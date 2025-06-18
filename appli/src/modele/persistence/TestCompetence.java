package modele.persistence;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.JUnitCore;

/**
 * Cette classe test les méthodes de Competence
 * @author Tardy Elie
 * @version 1.0
 */
public class TestCompetence {

    /**
     * Les instances utilisées pour les tests
     */
    private Competence c1, c2, c3, c4;

    /**
     * Initialise l'environnement de test avant chaque test.
     */
    @Before
    public void instancier() {
        this.c1 = new Competence(1, "Premier Secour", "PS");
        this.c2 = new Competence(2, "Secourisme Avancé", "SA");
        this.c3 = new Competence("Certification CQP", "CQP");
        // c3 a des prérequis
        ArrayList<Competence> prerequisC3 = new ArrayList<>();
        prerequisC3.add(c1);
        c3.setPrerequis(prerequisC3);
        this.c4 = null;
    }

    /**
     * Test de la methode Competence Constructeur et ToString
     */
    @Test
    public void testConstructeuretToString() {
        System.out.println("=======Test Constructeur et ToString=======");

        System.out.println("Test cas normaux (avec ID)");
        System.out.println("");
        testCasConstructeuretToString(10, "Gestion des Risques", "GR", false, true);
        System.out.println("");
        testCasConstructeuretToString(11, "Communication", "COM", false, true);
        System.out.println("");

        System.out.println("Test cas normaux (sans ID)");
        System.out.println("");
        testCasConstructeuretToString(-1, "Réanimation", "REA", false, false); // -1 car le constructeur sans ID met l'ID à -1
        System.out.println("");

        System.out.println("Test cas erreur : ");
        System.out.println("");
        testCasConstructeuretToString(5, null, "TST", true, true);
        System.out.println("");
        testCasConstructeuretToString(5, "A", "TST", true, true);
        System.out.println("");
        testCasConstructeuretToString(5, "Titre de compétence très très très long qui dépasse les quarante caractères", "TST", true, true);
        System.out.println("");
        testCasConstructeuretToString(5, "Test", "TSTLNG", true, true);
        System.out.println("");
        testCasConstructeuretToString(-2, "Valid", "VAL", true, true);
        System.out.println("");
    }

    /**
     * Méthode de test pour le constructeur et toString de Competence.
     *
     * @param idComp      l'id de la competence
     * @param intitule    l'intitule de la competence
     * @param abrevComp   l'abreviation de la competence
     * @param erreur      indique si le test doit générer une erreur
     * @param avecId      indique si le constructeur avec ID est utilisé
     */
    public void testCasConstructeuretToString(long idComp, String intitule, String abrevComp, boolean erreur, boolean avecId) {
        if (erreur) {
            System.out.println("Cas erreur : message d'erreur attendu");
        }

        System.out.println("Test Constructeur et toString");
        System.out.print(">");
        try {
            Competence res;
            if (avecId) {
                res = new Competence(idComp, intitule, abrevComp);
            } else {
                res = new Competence(intitule, abrevComp);
            }

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
     * Test de la methode getIdComp
     */
    @Test
    public void testGetIdComp() {
        System.out.println("======Test getIdComp======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetIdComp(c1, 1);
        testCasGetIdComp(c2, 2);
        testCasGetIdComp(c3, -1); // ID mis à -1 par le constructeur sans ID
    }

    /**
     * Tests les cas de la methode getIdComp
     *
     * @param c      la Competence dont on test l'id
     * @param idComp l'id attendu
     */
    public void testCasGetIdComp(Competence c, long idComp) {
        System.out.println("c.getIdComp() : " + idComp);
        System.out.print(">");
        if (c.getIdComp() == idComp) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la methode getIntitule
     */
    @Test
    public void testGetIntitule() {
        System.out.println("======Test getIntitule======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetIntitule(c1, "Premier Secour");
        testCasGetIntitule(c2, "Secourisme Avancé");
    }

    /**
     * Tests les cas de la methode getIntitule
     *
     * @param c        la Competence dont on test l'intitulé
     * @param intitule l'intitulé attendu
     */
    public void testCasGetIntitule(Competence c, String intitule) {
        System.out.println("c.getIntitule() : " + intitule);
        System.out.print(">");
        if (c.getIntitule().equals(intitule)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la methode getAbrevComp
     */
    @Test
    public void testGetAbrevComp() {
        System.out.println("======Test getAbrevComp======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetAbrevComp(c1, "PS");
        testCasGetAbrevComp(c2, "SA");
    }

    /**
     * Tests les cas de la methode getAbrevComp
     *
     * @param c         la Competence dont on test l'abréviation
     * @param abrevComp l'abréviation attendue
     */
    public void testCasGetAbrevComp(Competence c, String abrevComp) {
        System.out.println("c.getAbrevComp() : " + abrevComp);
        System.out.print(">");
        if (c.getAbrevComp().equals(abrevComp)) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Tests la methode setIdComp
     */
    @Test
    public void testSetIdComp() {
        System.out.println("======Test setIdComp======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetIdComp(c1, 10, false);
        System.out.println("");
        System.out.println("Test cas erreur");
        testCasSetIdComp(c1, -1, true);
    }

    /**
     * Tests les cas de la methode setIdComp
     *
     * @param c      la Competence dont on test la methode setIdComp
     * @param idComp l'id que l'on essaye de donner a la competence
     * @param erreur si vrai, on attend que la methode leve une exception
     */
    public void testCasSetIdComp(Competence c, long idComp, boolean erreur) {
        if (erreur) {
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("c.setIdComp(" + idComp + ")");
        System.out.print(">");
        try {
            c.setIdComp(idComp);
            if (erreur) {
                System.out.println("ERREUR DU PROGRAMME");
            } else {
                System.out.println("OK");
            }
        } catch (IllegalArgumentException e) {
            if (erreur) {
                System.out.println("OK");
                System.out.println("Message d'erreur : " + e.getMessage());
            } else {
                System.out.println("ERREUR DU PROGRAMME");
            }
        }
    }

    /**
     * Tests la methode setIntitule
     */
    @Test
    public void testSetIntitule() {
        System.out.println("======Test setIntitule======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetIntitule(c1, "Nouvel Intitulé", false);
        System.out.println("");
        System.out.println("Test cas erreur");
        testCasSetIntitule(c1, null, true);
        testCasSetIntitule(c1, "A", true);
        testCasSetIntitule(c1, "Un Intitulé Très Très Très Long Qui Dépasse Les Quarante Caractères Pour Tester La Limite", true);
    }

    /**
     * Tests les cas de la methode setIntitule
     *
     * @param c        la Competence dont on test la methode setIntitule
     * @param intitule l'intitulé que l'on essaye de donner a la competence
     * @param erreur   si vrai, on attend que la methode leve une exception
     */
    public void testCasSetIntitule(Competence c, String intitule, boolean erreur) {
        if (erreur) {
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("c.setIntitule(" + intitule + ")");
        System.out.print(">");
        try {
            c.setIntitule(intitule);
            if (erreur) {
                System.out.println("ERREUR DU PROGRAMME");
            } else {
                System.out.println("OK");
            }
        } catch (IllegalArgumentException e) {
            if (erreur) {
                System.out.println("OK");
                System.out.println("Message d'erreur : " + e.getMessage());
            } else {
                System.out.println("ERREUR DU PROGRAMME");
            }
        }
    }

    /**
     * Tests la methode setAbrevComp
     */
    @Test
    public void testSetAbrevComp() {
        System.out.println("======Test setAbrevComp======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasSetAbrevComp(c1, "Nouv", false);
        System.out.println("");
        System.out.println("Test cas erreur");
        testCasSetAbrevComp(c1, null, true);
        testCasSetAbrevComp(c1, "LONGABREV", true);
    }

    /**
     * Tests les cas de la methode setAbrevComp
     *
     * @param c         la Competence dont on test la methode setAbrevComp
     * @param abrevComp l'abréviation que l'on essaye de donner a la competence
     * @param erreur    si vrai, on attend que la methode leve une exception
     */
    public void testCasSetAbrevComp(Competence c, String abrevComp, boolean erreur) {
        if (erreur) {
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("c.setAbrevComp(" + abrevComp + ")");
        System.out.print(">");
        try {
            c.setAbrevComp(abrevComp);
            if (erreur) {
                System.out.println("ERREUR DU PROGRAMME");
            } else {
                System.out.println("OK");
            }
        } catch (IllegalArgumentException e) {
            if (erreur) {
                System.out.println("OK");
                System.out.println("Message d'erreur : " + e.getMessage());
            } else {
                System.out.println("ERREUR DU PROGRAMME");
            }
        }
    }

    /**
     * Test de la méthode estPrerequis
     */
    @Test
    public void testEstPrerequis() {
        System.out.println("======Test estPrerequis======");
        System.out.println("Test cas normaux");
        testCasEstPrerequis(c3, c1, true); // c1 est un prérequis de c3
        testCasEstPrerequis(c3, c2, false); // c2 n'est pas un prérequis de c3
        System.out.println("");
        System.out.println("Test cas erreur");
        testCasEstPrerequis(c1, null, false); // Gérer le cas de null (ne devrait pas lever d'exception mais retourner false)
    }

    /**
     * Tests les cas de la methode estPrerequis
     *
     * @param c                 la Competence dont on test la methode estPrerequis
     * @param autreCompetence   la Competence à vérifier si elle est un prérequis
     * @param expectedResult    le résultat attendu (true si prérequis, false sinon)
     */
    public void testCasEstPrerequis(Competence c, Competence autreCompetence, boolean expectedResult) {
        System.out.println("c.estPrerequis(" + autreCompetence + ") : " + expectedResult);
        System.out.print(">");
        boolean result = c.estPrerequis(autreCompetence);
        if (result == expectedResult) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }


    /**
     * Test de la methode getPrerequis
     */
    @Test
    public void testGetPrerequis() {
        System.out.println("======Test getPrerequis======");
        System.out.println("");
        System.out.println("Test cas normaux");
        testCasGetPrerequis(c3, 1); // c3 a 1 prérequis
        testCasGetPrerequis(c1, 0); // c1 n'a pas de prérequis
    }

    /**
     * Tests les cas de la methode getPrerequis
     *
     * @param c              la Competence dont on test les prérequis
     * @param expectedSize   la taille attendue de la liste des prérequis
     */
    public void testCasGetPrerequis(Competence c, int expectedSize) {
        System.out.println("c.getPrerequis().size() : " + expectedSize);
        System.out.print(">");
        if (c.getPrerequis().size() == expectedSize) {
            System.out.println("OK");
        } else {
            System.out.println("ERREUR DU PROGRAMME");
        }
    }

    /**
     * Test de la methode setPrerequis
     */
    @Test
    public void testSetPrerequis() {
        System.out.println("======Test setPrerequis======");
        System.out.println("");
        System.out.println("Test cas normaux");
        ArrayList<Competence> newPrerequis = new ArrayList<>();
        newPrerequis.add(c2);
        testCasSetPrerequis(c1, newPrerequis, false);
        System.out.println("");
        System.out.println("Test cas erreur");
        testCasSetPrerequis(c1, null, false); // setPrerequis(null) ne devrait pas lever d'exception, mais la liste devrait être null ou vide selon l'implémentation. Ici, on s'attend à ce que cela ne plante pas.
    }

    /**
     * Tests les cas de la methode setPrerequis
     *
     * @param c             la Competence dont on test la methode setPrerequis
     * @param prerequis     la liste de prérequis que l'on essaye de donner à la competence
     * @param erreur        si vrai, on attend que la methode leve une exception
     */
    public void testCasSetPrerequis(Competence c, ArrayList<Competence> prerequis, boolean erreur) {
        if (erreur) {
            System.out.println("Message d'erreur attendu");
        }
        System.out.println("c.setPrerequis(" + prerequis + ")");
        System.out.print(">");
        try {
            c.setPrerequis(prerequis);
            if (erreur) {
                System.out.println("ERREUR DU PROGRAMME");
            } else {
                System.out.println("OK");
                if (prerequis == null) {
                    assertNull(c.getPrerequis()); // Assuming setPrerequis(null) sets the internal list to null
                } else {
                    assertEquals(prerequis.size(), c.getPrerequis().size());
                    assertTrue(c.getPrerequis().containsAll(prerequis));
                }
            }
        } catch (IllegalArgumentException e) {
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
        JUnitCore.runClasses(TestCompetence.class);
    }
}