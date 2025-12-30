const API = {
    stocks: '/api/stocks',
    transaction: '/api/transaction',
    analytics: '/api/analytics',
    file: '/api/file'
};

async function post(url, data) {
    const res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });
    return await res.json();
}

async function get(url) {
    const res = await fetch(url);
    return await res.json();
}

function formatCurrency(num) { return '$' + parseFloat(num).toFixed(2); }

// --- MODULE 1: STOCK ---
async function loadMarket() {
    const stocks = await get(API.stocks);
    const tbody = document.getElementById('marketTable');
    const select = document.getElementById('tradeSymbol');

    tbody.innerHTML = '';
    select.innerHTML = '<option>Select Stock...</option>';

    stocks.forEach(s => {
        // Table Row
        tbody.innerHTML += `<tr><td>${s.symbol}</td><td>${s.name}</td><td>${formatCurrency(s.price)}</td></tr>`;
        // Select Option
        select.innerHTML += `<option value="${s.symbol}" data-price="${s.price}">${s.symbol} ($${s.price})</option>`;
    });
}

async function addStock() {
    const symbol = document.getElementById('newStockSymbol').value;
    const name = document.getElementById('newStockName').value;
    const price = document.getElementById('newStockPrice').value;

    if (!symbol || !price) return alert('Fill info');

    await post(API.stocks, { symbol, name, price });
    document.getElementById('newStockSymbol').value = '';
    document.getElementById('newStockName').value = '';
    document.getElementById('newStockPrice').value = '';
    loadMarket();
}

// --- MODULE 2: TRANSACTION ---
async function trade(type) {
    const select = document.getElementById('tradeSymbol');
    const symbol = select.value;
    const qty = document.getElementById('tradeQty').value;

    if (select.selectedIndex === 0 || !qty) return alert('Select stock and quantity');

    // Get current price from the selected option data attribute (simple approximation for UI)
    const price = select.options[select.selectedIndex].getAttribute('data-price');

    const res = await post(API.transaction, { type, symbol, qty, price });
    alert(res.message);
    if (res.status === 'success') {
        loadAnalytics(); // Refresh holdings
    }
}

// --- MODULE 3: ANALYTICS ---
async function loadAnalytics() {
    const data = await get(API.analytics);
    // data = { totalInvested, totalValue, totalPnL, items: [] }

    document.getElementById('totalInvested').innerText = formatCurrency(data.totalInvested);
    document.getElementById('currentValue').innerText = formatCurrency(data.totalValue);

    const pnlEl = document.getElementById('totalPnL');
    pnlEl.innerText = formatCurrency(data.totalPnL);
    pnlEl.className = 'stat-value ' + (data.totalPnL >= 0 ? 'positive' : 'negative');

    const tbody = document.getElementById('holdingsTable');
    tbody.innerHTML = '';
    data.items.forEach(item => {
        const pnlClass = item.pnl >= 0 ? 'positive' : 'negative';
        tbody.innerHTML += `
            <tr>
                <td>${item.symbol}</td>
                <td>${item.qty}</td>
                <td>${formatCurrency(item.avg)}</td>
                <td class="${pnlClass}">${formatCurrency(item.pnl)}</td>
            </tr>`;
    });

    // Draw Chart
    drawPieChart(data.items, data.totalValue);
}

function drawPieChart(items, totalValue) {
    const canvas = document.getElementById('portfolioChart');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    const legend = document.getElementById('chartLegend');

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    legend.innerHTML = '';

    if (items.length === 0 || totalValue === 0) {
        ctx.font = "14px Arial";
        ctx.fillStyle = "#fff";
        ctx.textAlign = "center";
        ctx.fillText("No data to display", canvas.width / 2, canvas.height / 2);
        return;
    }

    const colors = ['#3b82f6', '#22c55e', '#ef4444', '#f97316', '#a855f7', '#ec4899', '#eab308', '#6366f1'];
    let startAngle = 0;
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const radius = Math.min(centerX, centerY) - 20;

    items.forEach((item, index) => {
        // Calculate Slice
        const value = item.qty * item.curr; // Current Value approx
        // Note: item.val from backend is precise, we can use that if available, but let's recalculate or use property if mismatch.
        // Checking PortfolioAnalytics.java: "val":%.2f which corresponds to total value of that stock.
        // Wait, item from backend has "val" field? Yes.
        // Let's check `item` object in `loadAnalytics`... 
        // PortfolioAnalytics returns: symbol, name, qty, avg, curr, val, pnl

        const sliceVal = item.val;
        const sliceAngle = (sliceVal / totalValue) * 2 * Math.PI;
        const color = colors[index % colors.length];

        // Draw Slice
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, startAngle, startAngle + sliceAngle);
        ctx.closePath();
        ctx.fillStyle = color;
        ctx.fill();

        startAngle += sliceAngle;

        // Legend
        const percentage = ((sliceVal / totalValue) * 100).toFixed(1);
        legend.innerHTML += `
            <div class="legend-item">
                <span class="legend-color" style="background:${color}"></span>
                <span>${item.symbol} (${percentage}%)</span>
            </div>
        `;
    });
}

// --- MODULE 4: FILE ---
async function saveData() {
    const res = await post(API.file, {});
    document.getElementById('fileStatus').innerText = res.message;
    setTimeout(() => document.getElementById('fileStatus').innerText = '', 3000);
}

async function loadData() {
    const res = await get(API.file);
    alert("Loaded Data: " + JSON.stringify(res).substring(0, 50) + "...");
    // In a real app we would restore DB from this JSON, but here we just show it works
}

// Init
loadMarket();
loadAnalytics();
