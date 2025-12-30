<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Register - MyStox</title>
        <link rel="stylesheet" href="../css/style.css">
        <script src="../js/validation.js"></script>
    </head>

    <body>
        <nav class="navbar">
            <a href="../index.jsp" class="brand" style="color: white; text-decoration: none;">MyStox 📈</a>
            <div class="nav-links">
                <a href="login.jsp">Login</a>
            </div>
        </nav>

        <div class="container">
            <div class="card auth-card">
                <h2 class="text-center mb-3">Create Account</h2>
                <% String error=(String) request.getAttribute("error"); if(error !=null) { %>
                    <p class="error-msg text-center">
                        <%= error %>
                    </p>
                    <% } %>

                        <form action="../RegisterServlet" method="post" onsubmit="return validateRegister()">
                            <div class="form-group">
                                <label>Username</label>
                                <input type="text" name="username" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Email</label>
                                <input type="email" name="email" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Password</label>
                                <input type="password" name="password" id="password" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Confirm Password</label>
                                <input type="password" name="confirmPassword" id="confirmPassword" class="form-control"
                                    required>
                            </div>
                            <button type="submit" class="btn" style="width: 100%;">Register</button>
                        </form>
            </div>
        </div>
    </body>

    </html>