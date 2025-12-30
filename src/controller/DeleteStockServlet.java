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

@WebServlet("/DeleteStockServlet")
public class DeleteStockServlet extends HttpServlet {
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
            if (portfolioDAO.deletePortfolioItem(portfolioId, user.getId())) {
                response.sendRedirect("jsp/dashboard.jsp?success=Stock Removed");
            } else {
                response.sendRedirect("jsp/dashboard.jsp?error=Deletion Failed");
            }
        } catch (Exception e) {
            response.sendRedirect("jsp/dashboard.jsp?error=Invalid ID");
        }
    }
}
