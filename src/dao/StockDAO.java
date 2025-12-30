package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Stock;
import util.DBConnection;

public class StockDAO {

    // Get stock by symbol or create if not exists
    public int getOrCreateStockId(String symbol, String companyName) {
        int existingId = getStockIdBySymbol(symbol);
        if (existingId != -1) {
            // Optional: Update company name if needed, but for now we trust the ID
            return existingId;
        }
        return createStock(symbol, companyName);
    }

    public int getStockIdBySymbol(String symbol) {
        String sql = "SELECT id FROM stocks WHERE symbol = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, symbol.toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int createStock(String symbol, String companyName) {
        String sql = "INSERT INTO stocks (symbol, company_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, symbol.toUpperCase());
            pstmt.setString(2, companyName);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
