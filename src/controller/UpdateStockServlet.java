package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.PortfolioDAO;
import model.User;

@WebServlet("/UpdateStockServlet")
public class UpdateStockServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PortfolioDAO portfolioDAO = new PortfolioDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");

        try {
            int portfolioId = Integer.parseInt(request.getParameter("id"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double buyPrice = Double.parseDouble(request.getParameter("buyPrice"));

            if (portfolioDAO.updatePortfolio(portfolioId, user.getId(), quantity, buyPrice)) {
                response.sendRedirect("jsp/dashboard.jsp?success=Stock Updated");
            } else {
                response.sendRedirect("jsp/dashboard.jsp?error=Update Failed");
            }
        } catch (Exception e) {
            response.sendRedirect("jsp/dashboard.jsp?error=Invalid Input");
        }
    }
}
