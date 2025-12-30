import java.sql.Connection;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import common.DBConnection;

public class SetupDB {
    public static void main(String[] args) {
        System.out.println("Initializing Database for 4-Module Architecture...");

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Read the SQL file content directly to ensure sync
            String sql = new String(Files.readAllBytes(Paths.get("database.sql")));

            // Simple split by semicolon to execute statements (basic runner)
            // Note: This is fragile for complex SQL but works for this simple script
            String[] statements = sql.split(";");
            for (String s : statements) {
                if (!s.trim().isEmpty()) {
                    stmt.execute(s.trim());
                }
            }

            System.out.println("Database reset and initialized successfully!");

        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
