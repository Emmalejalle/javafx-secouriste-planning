package modele.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modele.persistence.User;

/**
 * La classe MyConnection permet de se connecter à la base de données.
 * Elle utilise le singleton pour ne pas créer plusieurs connexions.
 * @author EMERIAU Emilien
 * @version 1.0
 */
public class MyConnection {
    private static final String URL ="jdbc:mysql://localhost:3306/sae_secours"; // URL de la base de données, à la place du de localhost on met le nom du serv sur la vm
    private static final String LOGIN = "admin"; // login de la base de données
    private static final String PWD = "mdp_admin"; // mot de passe de la base de données
    private Connection conn = null; // La connexion à la base de données, initialement nulle.
    private static MyConnection myConnection = new MyConnection(); // Le singleton

    /**
     * Constructeur privé pour empêcher l'instanciation directe.
     * Utilisez getMyConnection() pour obtenir l'instance unique.
     */
    private MyConnection(){};

    /**
     * Obtient une connexion à la base de données.
     * 
     * Si la connexion actuelle est nulle ou fermée, une nouvelle connexion sera établie.
     * Assure que le pilote JDBC est chargé avant d'établir la connexion.
     * 
     * @return L'objet Connection à la base de données.
     * @throws SQLException Si une erreur survient lors de l'établissement de la connexion.
     */
    Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
        
        if (conn == null || conn.isClosed()) {
            try {
                conn = DriverManager.getConnection(URL, LOGIN, PWD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return conn;
    }

    /**
     * Méthode statique pour obtenir l'instance unique de MyConnection.
     * 
     * @return L'instance unique de MyConnection.
     */
    public static MyConnection getMyConnection() {
        if (myConnection == null) {
            myConnection = new MyConnection();
        }
        return myConnection;
    }

    /**
     * Méthode principale pour tester la connexion à la base de données.
     * Elle exécute une requête simple pour récupérer les utilisateurs et affiche leurs informations.
     * 
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        try {
            MyConnection myConnection = new MyConnection();
            Connection connection = myConnection.getConnection();

            Statement st = connection.createStatement ();
            ResultSet rs = st.executeQuery("SELECT * FROM User");
            while (rs.next()) {
                String nom = rs.getString(1);
                String pwd = rs.getString(2);
                System.out.println("Login: " + nom + ", Password: " + pwd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
