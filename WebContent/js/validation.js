function validateRegister() {
    let password = document.getElementById("password").value;
    let confirm = document.getElementById("confirmPassword").value;
    if (password !== confirm) {
        alert("Passwords do not match!");
        return false;
    }
    if (password.length < 6) {
        alert("Password must be at least 6 characters long.");
        return false;
    }
    return true;
}

function validateAddStock() {
    let qty = document.getElementById("quantity").value;
    let price = document.getElementById("buyPrice").value;
    
    if (qty <= 0) {
        alert("Quantity must be greater than 0");
        return false;
    }
    if (price <= 0) {
        alert("Price must be greater than 0");
        return false;
    }
    return true;
}
