package module_transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import common.DBConnection;

public class TransactionManager {

    /**
     * Executes a BUY order.
     * 1. Checks if stock exists.
     * 2. Updates/Creates Portfolio entry.
     * 3. Logs Transaction.
     */
    public String buyStock(String symbol, int qty, double price) {
        if (qty <= 0)
            return "{\"status\":\"error\", \"message\":\"Invalid quantity\"}";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction Start

            // 1. Update Portfolio
            // Check if exists
            String checkSql = "SELECT quantity, avg_buy_price FROM portfolio WHERE symbol = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, symbol);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update existing
                int oldQty = rs.getInt("quantity");
                double oldAvg = rs.getDouble("avg_buy_price");
                int newQty = oldQty + qty;
                double newAvg = ((oldQty * oldAvg) + (qty * price)) / newQty;

                String updateSql = "UPDATE portfolio SET quantity = ?, avg_buy_price = ? WHERE symbol = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, newQty);
                updateStmt.setDouble(2, newAvg);
                updateStmt.setString(3, symbol);
                updateStmt.executeUpdate();
            } else {
                // Insert new
                String insertSql = "INSERT INTO portfolio (symbol, quantity, avg_buy_price) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, symbol);
                insertStmt.setInt(2, qty);
                insertStmt.setDouble(3, price);
                insertStmt.executeUpdate();
            }

            // 2. Log Transaction
            String logSql = "INSERT INTO transactions (symbol, type, quantity, price) VALUES (?, 'BUY', ?, ?)";
            PreparedStatement logStmt = conn.prepareStatement(logSql);
            logStmt.setString(1, symbol);
            logStmt.setInt(2, qty);
            logStmt.setDouble(3, price);
            logStmt.executeUpdate();

            conn.commit();
            return "{\"status\":\"success\", \"message\":\"Buy Successful\"}";

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * Executes a SELL order.
     * 1. Checks if user has enough quantity.
     * 2. Decreases Portfolio quantity (Delete if 0).
     * 3. Logs Transaction.
     */
    public String sellStock(String symbol, int qty, double price) {
        if (qty <= 0)
            return "{\"status\":\"error\", \"message\":\"Invalid quantity\"}";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Check Holdings
            String checkSql = "SELECT quantity FROM portfolio WHERE symbol = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, symbol);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next())
                return "{\"status\":\"error\", \"message\":\"Stock not owned\"}";
            int currentQty = rs.getInt("quantity");
            if (currentQty < qty)
                return "{\"status\":\"error\", \"message\":\"Insufficient quantity\"}";

            // 2. Update Portfolio
            if (currentQty == qty) {
                String delSql = "DELETE FROM portfolio WHERE symbol = ?";
                PreparedStatement delStmt = conn.prepareStatement(delSql);
                delStmt.setString(1, symbol);
                delStmt.executeUpdate();
            } else {
                String upSql = "UPDATE portfolio SET quantity = ? WHERE symbol = ?";
                PreparedStatement upStmt = conn.prepareStatement(upSql);
                upStmt.setInt(1, currentQty - qty);
                upStmt.setString(2, symbol);
                upStmt.executeUpdate();
            }

            // 3. Log
            String logSql = "INSERT INTO transactions (symbol, type, quantity, price) VALUES (?, 'SELL', ?, ?)";
            PreparedStatement logStmt = conn.prepareStatement(logSql);
            logStmt.setString(1, symbol);
            logStmt.setInt(2, qty);
            logStmt.setDouble(3, price);
            logStmt.executeUpdate();

            conn.commit();
            return "{\"status\":\"success\", \"message\":\"Sell Successful\"}";

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
