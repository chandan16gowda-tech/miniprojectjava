package module_stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import common.DBConnection;

public class StockPriceSimulator {

    private final Random random = new Random();

    /**
     * Simulates market changes by updating all stock prices
     * with a random percentage between -5% and +5%.
     */
    public void simulateMarket() {
        String fetchSql = "SELECT symbol, current_price FROM stocks";
        String updateSql = "UPDATE stocks SET current_price = ? WHERE symbol = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement fetchStmt = conn.prepareStatement(fetchSql);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // 1. Fetch current prices
            ResultSet rs = fetchStmt.executeQuery();

            while (rs.next()) {
                String symbol = rs.getString("symbol");
                double currentPrice = rs.getDouble("current_price");

                // 2. Calculate random change (-0.05 to +0.05)
                // random.nextDouble() gives 0.0 to 1.0
                // (random.nextDouble() * 0.1) gives 0.0 to 0.1
                // ( ... ) - 0.05 gives -0.05 to +0.05
                double changePercent = (random.nextDouble() * 0.1) - 0.05;

                double newPrice = currentPrice + (currentPrice * changePercent);

                // Ensure price doesn't drop below 0.01
                if (newPrice < 0.01)
                    newPrice = 0.01;

                // 3. Update Database
                updateStmt.setDouble(1, newPrice);
                updateStmt.setString(2, symbol);
                updateStmt.addBatch();
            }

            updateStmt.executeBatch();
            System.out.println("Market Simulated: Prices updated.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
