package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.UserDAO;
import model.User;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || email == null || password == null || !password.equals(confirmPassword)) {
            request.setAttribute("error", "Invalid Input or Passwords do not match");
            request.getRequestDispatcher("jsp/register.jsp").forward(request, response);
            return;
        }

        User user = new User(username, email, password);
        if (userDAO.registerUser(user)) {
            response.sendRedirect("jsp/login.jsp?success=Registration Successful");
        } else {
            request.setAttribute("error", "Registration Failed. Email or Username might already exist.");
            request.getRequestDispatcher("jsp/register.jsp").forward(request, response);
        }
    }
}
