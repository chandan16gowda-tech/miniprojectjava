package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Portfolio;
import util.DBConnection;

public class PortfolioDAO {

    public List<Portfolio> getUserPortfolio(int userId) {
        List<Portfolio> list = new ArrayList<>();
        String sql = "SELECT p.*, s.symbol, s.company_name FROM portfolio p " +
                "JOIN stocks s ON p.stock_id = s.id " +
                "WHERE p.user_id = ? ORDER BY p.buy_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Portfolio p = new Portfolio();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setStockId(rs.getInt("stock_id"));
                    p.setQuantity(rs.getInt("quantity"));
                    p.setBuyPrice(rs.getDouble("buy_price"));
                    p.setBuyDate(rs.getDate("buy_date"));
                    p.setStockSymbol(rs.getString("symbol"));
                    p.setCompanyName(rs.getString("company_name"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addStockToPortfolio(Portfolio portfolio) {
        String sql = "INSERT INTO portfolio (user_id, stock_id, quantity, buy_price, buy_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, portfolio.getUserId());
            pstmt.setInt(2, portfolio.getStockId());
            pstmt.setInt(3, portfolio.getQuantity());
            pstmt.setDouble(4, portfolio.getBuyPrice());
            pstmt.setDate(5, portfolio.getBuyDate());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePortfolio(int portfolioId, int userId, int quantity, double buyPrice) {
        String sql = "UPDATE portfolio SET quantity = ?, buy_price = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setDouble(2, buyPrice);
            pstmt.setInt(3, portfolioId);
            pstmt.setInt(4, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePortfolioItem(int portfolioId, int userId) {
        String sql = "DELETE FROM portfolio WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, portfolioId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
