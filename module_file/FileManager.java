package module_file;

import java.io.FileWriter;
import java.io.IOException;
import module_analytics.PortfolioAnalytics; // Dependency for getting data to save

// Simple FileManager that saves the Analytics Report
public class FileManager {

    public String savePortfolioToFile() {
        PortfolioAnalytics analytics = new PortfolioAnalytics();
        String json = analytics.getPortfolioAnalytics();

        try (FileWriter writer = new FileWriter("portfolio_backup.json")) {
            writer.write(json);
            return "{\"status\":\"success\", \"message\":\"Saved to portfolio_backup.json\"}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}";
        }
    }

    // Load is tricky because we'd need to parse JSON back into DB.
    // For this simple assignment, "Load" might just mean reading the file content
    // to display.
    public String loadPortfolioFromFile() {
        try {
            String content = new String(
                    java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("portfolio_backup.json")));
            return content;
        } catch (IOException e) {
            return "{\"status\":\"error\", \"message\":\"File not found\"}";
        }
    }
}
