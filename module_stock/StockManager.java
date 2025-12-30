package module_stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import common.DBConnection;

public class StockManager {

    /**
     * Adds a new stock to the market list.
     */
    public boolean addStock(String symbol, String name, double price) {
        String sql = "INSERT INTO stocks (symbol, name, current_price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, symbol.toUpperCase());
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);

            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the market price of a stock.
     */
    public boolean updatePrice(String symbol, double newPrice) {
        String sql = "UPDATE stocks SET current_price = ? WHERE symbol = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, symbol.toUpperCase());

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all available stocks and their prices.
     * Returns JSON string.
     */
    public String getAllStocks() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM stocks ORDER BY symbol";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(String.format("{\"symbol\":\"%s\", \"name\":\"%s\", \"price\":%.2f}",
                        rs.getString("symbol"), rs.getString("name"), rs.getDouble("current_price")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[" + String.join(",", list) + "]";
    }
}
