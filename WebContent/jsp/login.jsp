<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Login - MyStox</title>
        <link rel="stylesheet" href="../css/style.css">
    </head>

    <body>
        <nav class="navbar">
            <a href="../index.jsp" class="brand" style="color: white; text-decoration: none;">MyStox 📈</a>
            <div class="nav-links">
                <a href="register.jsp">Register</a>
            </div>
        </nav>

        <div class="container">
            <div class="card auth-card">
                <h2 class="text-center mb-3">Welcome Back</h2>
                <% String error=(String) request.getAttribute("error"); String success=request.getParameter("success");
                    if(error !=null) { %>
                    <p class="error-msg text-center">
                        <%= error %>
                    </p>
                    <% } %>
                        <% if(success !=null) { %>
                            <p class="success-msg text-center">
                                <%= success %>
                            </p>
                            <% } %>

                                <form action="../LoginServlet" method="post">
                                    <div class="form-group">
                                        <label>Email</label>
                                        <input type="email" name="email" class="form-control" required>
                                    </div>
                                    <div class="form-group">
                                        <label>Password</label>
                                        <input type="password" name="password" class="form-control" required>
                                    </div>
                                    <button type="submit" class="btn" style="width: 100%;">Login</button>
                                </form>
                                <p class="text-center" style="margin-top: 1rem;">
                                    New here? <a href="register.jsp">Create an account</a>
                                </p>
            </div>
        </div>
    </body>

    </html>