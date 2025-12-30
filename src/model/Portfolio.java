package model;

import java.io.Serializable;
import java.sql.Date;

public class Portfolio implements Serializable {
    private int id;
    private int userId;
    private int stockId;
    private int quantity;
    private double buyPrice;
    private Date buyDate;
    
    // Joined data for display
    private String stockSymbol;
    private String companyName;

    public Portfolio() {}

    public Portfolio(int userId, int stockId, int quantity, double buyPrice, Date buyDate) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.buyDate = buyDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getStockId() { return stockId; }
    public void setStockId(int stockId) { this.stockId = stockId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getBuyPrice() { return buyPrice; }
    public void setBuyPrice(double buyPrice) { this.buyPrice = buyPrice; }

    public Date getBuyDate() { return buyDate; }
    public void setBuyDate(Date buyDate) { this.buyDate = buyDate; }

    public String getStockSymbol() { return stockSymbol; }
    public void setStockSymbol(String stockSymbol) { this.stockSymbol = stockSymbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    // Analytics helpers
    public double getTotalInvestment() {
        return quantity * buyPrice;
    }
}
