package controller;

import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.PortfolioDAO;
import dao.StockDAO;
import model.Portfolio;
import model.User;

@WebServlet("/AddStockServlet")
public class AddStockServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private StockDAO stockDAO = new StockDAO();
    private PortfolioDAO portfolioDAO = new PortfolioDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String symbol = request.getParameter("symbol");
        String companyName = request.getParameter("companyName");
        
        // Parse numbers with basic error handling
        int quantity = 0;
        double buyPrice = 0.0;
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
            buyPrice = Double.parseDouble(request.getParameter("buyPrice"));
        } catch (NumberFormatException e) {
            response.sendRedirect("jsp/addStock.jsp?error=Invalid quantity or price");
            return;
        }

        int stockId = stockDAO.getOrCreateStockId(symbol, companyName);
        if (stockId == -1) {
            response.sendRedirect("jsp/addStock.jsp?error=Database error creating stock");
            return;
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(user.getId());
        portfolio.setStockId(stockId);
        portfolio.setQuantity(quantity);
        portfolio.setBuyPrice(buyPrice);
        portfolio.setBuyDate(new Date(System.currentTimeMillis())); // Default to now

        if (portfolioDAO.addStockToPortfolio(portfolio)) {
            response.sendRedirect("jsp/dashboard.jsp?success=Stock Added Successfully");
        } else {
            response.sendRedirect("jsp/addStock.jsp?error=Failed to add stock");
        }
    }
}
