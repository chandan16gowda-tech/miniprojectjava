import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Importing Modules
import module_stock.StockManager;
import module_stock.StockPriceSimulator; // Imported
import module_transaction.TransactionManager;
import module_analytics.PortfolioAnalytics;
import module_file.FileManager;

public class Main {
    private static final int PORT = 8000;

    // Instantiate Modules
    private static final StockManager stockManager = new StockManager();
    private static final StockPriceSimulator stockSimulator = new StockPriceSimulator(); // New Simulator
    private static final TransactionManager transactionManager = new TransactionManager();
    private static final PortfolioAnalytics portfolioAnalytics = new PortfolioAnalytics();
    private static final FileManager fileManager = new FileManager();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Static Files
        server.createContext("/", new StaticFileHandler());

        // --- Module 1: Stock Handling ---
        server.createContext("/api/stocks", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    String response = stockManager.getAllStocks();
                    sendResponse(exchange, 200, response);
                } else if ("POST".equals(exchange.getRequestMethod())) {
                    Map<String, String> data = parseJson(readBody(exchange));
                    stockManager.addStock(data.get("symbol"), data.get("name"), Double.parseDouble(data.get("price")));
                    sendResponse(exchange, 200, "{\"status\":\"success\"}");
                }
            }
        });

        // --- Module 2: Transaction Handling ---
        server.createContext("/api/transaction", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    Map<String, String> data = parseJson(readBody(exchange));
                    String type = data.get("type"); // BUY or SELL
                    String symbol = data.get("symbol");
                    int qty = Integer.parseInt(data.get("qty"));
                    double price = Double.parseDouble(data.get("price"));

                    String result;
                    if ("BUY".equalsIgnoreCase(type)) {
                        result = transactionManager.buyStock(symbol, qty, price);
                    } else {
                        result = transactionManager.sellStock(symbol, qty, price);
                    }
                    sendResponse(exchange, 200, result);
                }
            }
        });

        // --- Module 3: Analytics ---
        server.createContext("/api/analytics", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    // TRIGGER SIMULATION ON REFRESH
                    stockSimulator.simulateMarket();

                    String json = portfolioAnalytics.getPortfolioAnalytics();
                    sendResponse(exchange, 200, json);
                }
            }
        });

        // --- Module 4: File Handling ---
        server.createContext("/api/file", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    // Save
                    String res = fileManager.savePortfolioToFile();
                    sendResponse(exchange, 200, res);
                } else if ("GET".equals(exchange.getRequestMethod())) {
                    // Load
                    String res = fileManager.loadPortfolioFromFile();
                    sendResponse(exchange, 200, res);
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Mini Stock Portfolio Tracker (4-Module Architecture) Started on Port " + PORT);
    }

    // --- Helpers ---
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String readBody(HttpExchange exchange) {
        return new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));
    }

    private static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] parts = pair.split(":");
                if (parts.length >= 2) {
                    // Primitive cleaning of quotes
                    String key = parts[0].trim().replace("\"", "");
                    String val = parts[1].trim().replace("\"", "");
                    map.put(key, val);
                }
            }
        }
        return map;
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/"))
                path = "/dashboard.html"; // Default to dashboard directly
            if (path.equals("/index.html") || path.equals("/login.html"))
                path = "/dashboard.html"; // Redirect old pages

            File file = new File("web" + path);
            if (file.exists() && !file.isDirectory()) {
                String contentType = "text/html";
                if (path.endsWith(".css"))
                    contentType = "text/css";
                if (path.endsWith(".js"))
                    contentType = "application/javascript";
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, file.length());
                try (OutputStream os = exchange.getResponseBody();
                        FileInputStream fs = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fs.read(buffer)) != -1)
                        os.write(buffer, 0, len);
                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody().close();
            }
        }
    }
}
