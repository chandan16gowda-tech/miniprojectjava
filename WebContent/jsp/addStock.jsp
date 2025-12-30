<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Add Stock - MyStox</title>
        <link rel="stylesheet" href="../css/style.css">
        <script src="../js/validation.js"></script>
    </head>

    <body>
        <nav class="navbar">
            <a href="../index.jsp" class="brand" style="color: white; text-decoration: none;">MyStox 📈</a>
            <div class="nav-links">
                <a href="dashboard.jsp">Dashboard</a>
            </div>
        </nav>

        <div class="container">
            <div class="card auth-card">
                <h2 class="text-center mb-3">Add Investment</h2>
                <% String error=request.getParameter("error"); if(error !=null) { %>
                    <p class="error-msg text-center">
                        <%= error %>
                    </p>
                    <% } %>

                        <form action="../AddStockServlet" method="post" onsubmit="return validateAddStock()">
                            <div class="form-group">
                                <label>Stock Symbol</label>
                                <input type="text" name="symbol" class="form-control" placeholder="e.g. AAPL" required>
                            </div>
                            <div class="form-group">
                                <label>Company Name</label>
                                <input type="text" name="companyName" class="form-control" placeholder="e.g. Apple Inc."
                                    required>
                            </div>
                            <div class="form-group">
                                <label>Quantity</label>
                                <input type="number" id="quantity" name="quantity" class="form-control" min="1"
                                    required>
                            </div>
                            <div class="form-group">
                                <label>Buy Price ($)</label>
                                <input type="number" id="buyPrice" name="buyPrice" step="0.01" class="form-control"
                                    min="0.01" required>
                            </div>
                            <div style="display:flex; gap: 10px;">
                                <a href="dashboard.jsp" class="btn btn-secondary"
                                    style="flex:1; text-align:center;">Cancel</a>
                                <button type="submit" class="btn" style="flex:1">Add Data</button>
                            </div>
                        </form>
            </div>
        </div>
    </body>

    </html>