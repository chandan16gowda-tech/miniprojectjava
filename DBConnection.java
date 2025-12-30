import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres"; // CHANGE DB NAME IF NEEDED
    private static final String USER = "postgres"; // CHANGE USER IF NEEDED
    private static final String PASSWORD = "Nithin@7483"; // CHANGE PASSWORD IF NEEDED

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("PostgreSQL Driver not found! Make sure to add postgresql-x.x.x.jar to classpath.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
