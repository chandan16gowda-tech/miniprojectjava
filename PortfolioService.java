import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PortfolioService {

    public List<String> getPortfolio(int userId) {
        List<String> portfolio = new ArrayList<>();
        String sql = "SELECT * FROM portfolio WHERE user_id = ? ORDER BY stock_symbol";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // Building JSON manually to avoid dependencies
            while (rs.next()) {
                int id = rs.getInt("id");
                String symbol = rs.getString("stock_symbol");
                String name = rs.getString("stock_name");
                int qty = rs.getInt("quantity");
                double avgPrice = rs.getDouble("avg_buy_price");
                double currentPrice = rs.getDouble("current_price");

                double totalInvested = qty * avgPrice;
                double currentValue = qty * currentPrice;
                double pnl = currentValue - totalInvested;
                double pnlPercent = totalInvested > 0 ? (pnl / totalInvested) * 100 : 0;

                String json = String.format(
                        "{\"id\": %d, \"symbol\": \"%s\", \"name\": \"%s\", \"quantity\": %d, \"avgPrice\": %.2f, \"currentPrice\": %.2f, \"totalInvested\": %.2f, \"currentValue\": %.2f, \"pnl\": %.2f, \"pnlPercent\": %.2f}",
                        id, symbol, name, qty, avgPrice, currentPrice, totalInvested, currentValue, pnl, pnlPercent);
                portfolio.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return portfolio;
    }

    public boolean addStock(int userId, String symbol, String name, int quantity, double price) {
        String sql = "INSERT INTO portfolio (user_id, stock_symbol, stock_name, quantity, avg_buy_price, current_price) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, symbol.toUpperCase());
            pstmt.setString(3, name);
            pstmt.setInt(4, quantity);
            pstmt.setDouble(5, price);
            pstmt.setDouble(6, price); // Initially current price = buy price

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStock(int id, int userId, int quantity, double currentPrice) {
        // Simple update: We update quantity and current market price.
        // In a real app, updating quantity might change avg_buy_price if buying more,
        // but for simplicity we'll just allow editing the current perceived value or
        // holdings.

        String sql = "UPDATE portfolio SET quantity = ?, current_price = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setDouble(2, currentPrice);
            pstmt.setInt(3, id);
            pstmt.setInt(4, userId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStock(int id, int userId) {
        String sql = "DELETE FROM portfolio WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
