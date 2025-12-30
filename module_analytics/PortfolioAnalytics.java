package module_analytics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import common.DBConnection;

public class PortfolioAnalytics {

    /**
     * Returns the full portfolio with analytics (Current Value, PnL) combined.
     */
    public String getPortfolioAnalytics() {
        // We join Portfolio with Stocks to get real-time price
        String sql = "SELECT p.symbol, p.quantity, p.avg_buy_price, s.name, s.current_price " +
                "FROM portfolio p JOIN stocks s ON p.symbol = s.symbol";

        List<String> rows = new ArrayList<>();
        double totalInvested = 0;
        double totalValue = 0;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");
                int qty = rs.getInt("quantity");
                double avgPrice = rs.getDouble("avg_buy_price");
                double currPrice = rs.getDouble("current_price");

                double invested = qty * avgPrice;
                double value = qty * currPrice;
                double pnl = value - invested;

                totalInvested += invested;
                totalValue += value;

                rows.add(String.format(
                        "{\"symbol\":\"%s\", \"name\":\"%s\", \"qty\":%d, \"avg\":%.2f, \"curr\":%.2f, \"val\":%.2f, \"pnl\":%.2f}",
                        symbol, name, qty, avgPrice, currPrice, value, pnl));
            }

            // Append totals as a metadata object at end or just handling in specific route?
            // For simplicity, let's just return the list of items. Frontend can sum them up
            // too
            // OR we can return a wrapped object. Let's return wrapped object.

            double totalPnL = totalValue - totalInvested;
            String itemsJson = "[" + String.join(",", rows) + "]";

            return String.format("{\"totalInvested\":%.2f, \"totalValue\":%.2f, \"totalPnL\":%.2f, \"items\":%s}",
                    totalInvested, totalValue, totalPnL, itemsJson);

        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
