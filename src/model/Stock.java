package model;

import java.io.Serializable;

public class Stock implements Serializable {
    private int id;
    private String symbol;
    private String companyName;

    public Stock() {}

    public Stock(String symbol, String companyName) {
        this.symbol = symbol;
        this.companyName = companyName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
