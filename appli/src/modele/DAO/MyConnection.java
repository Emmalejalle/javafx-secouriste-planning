package modele.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La classe MyConnection permet de se connecter à la base de données.
 * Elle utilise le singleton pour ne pas créer plusieurs connexions.
 * @author EMERIAU Emilien
 * @version 1.0
 */
public class MyConnection {
    private static final String URL ="jdbc:mysql://localhost:3306/sae_secours"; //a la place du de localhost on met le nom du serv sur la vm
    private static final String LOGIN = "admin";
    private static final String PWD = "mdp_admin";
    private Connection conn = null;
    private static MyConnection myConnection = new MyConnection();

    private MyConnection(){};

    Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }
        
        if (conn == null || conn.isClosed()) {

        }
        try {
            conn = DriverManager.getConnection(URL, LOGIN, PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static MyConnection getMyConnection() {
        if (myConnection == null) {
            myConnection = new MyConnection();
        }
        return myConnection;
    }

    public static void main(String[] args) {
        try {
            MyConnection myConnection = new MyConnection();
            Connection connection = myConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
