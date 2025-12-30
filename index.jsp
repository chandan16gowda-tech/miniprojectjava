<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Stock Portfolio Tracker</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="brand">MyStox 📈</div>
        <div class="nav-links">
            <a href="jsp/login.jsp">Login</a>
            <a href="jsp/register.jsp" class="btn btn-sm">Register</a>
        </div>
    </nav>

    <div class="container text-center" style="padding-top: 5rem;">
        <h1 style="font-size: 3rem; margin-bottom: 1rem;">Track Your Financial Journey</h1>
        <p style="font-size: 1.2rem; color: #666; margin-bottom: 2rem;">
            A simple, colorful, and powerful way to manage your stock investments.
            Realtime profits, analytics, and more.
        </p>
        <a href="jsp/register.jsp" class="btn" style="padding: 1rem 2rem; font-size: 1.2rem;">Get Started Free</a>
        
        <div style="margin-top: 4rem; display: flex; justify-content: center; gap: 2rem;">
            <div class="card" style="width: 300px;">
                <h3>📊 Visual Analytics</h3>
                <p>Track gains and losses instantly.</p>
            </div>
            <div class="card" style="width: 300px;">
                <h3>🔒 Secure</h3>
                <p>Your data is encrypted and safe.</p>
            </div>
        </div>
    </div>

    <footer>
        &copy; 2024 Mini Stock Portfolio Tracker. Built with Java & Postgres.
    </footer>
</body>
</html>
