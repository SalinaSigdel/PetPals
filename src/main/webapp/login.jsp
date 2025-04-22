<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login | PetPals</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<header>
    <div class="container">
        <h1><i class="fas fa-paw"></i> PetPals</h1>
        <nav>
            <a href="index.jsp"><i class="fas fa-home"></i> Home</a>
            <a href="adopt.jsp"><i class="fas fa-heart"></i> Adopt</a>
            <a href="login.jsp" class="active"><i class="fas fa-user"></i> Login</a>
            <a href="register.jsp"><i class="fas fa-user-plus"></i> Register</a>
            <a href="about.jsp"><i class="fas fa-info-circle"></i> About</a>
            <% if(session.getAttribute("username") != null) { %>
            <a href="userprofile.jsp"><i class="fas fa-user-circle"></i> Profile</a>
            <% } %>
            <% if(session.getAttribute("userRole") != null && session.getAttribute("userRole").equals("admin")) { %>
            <a href="admindashboard.jsp"><i class="fas fa-tachometer-alt"></i> Admin</a>
            <% } %>
        </nav>
    </div>
</header>

<div class="form-container">
    <h2><i class="fas fa-user-circle"></i> Welcome Back</h2>

    <%
        // Display error message if login failed
        String errorMessage = (String) session.getAttribute("loginError");
        if (errorMessage != null) {
    %>
    <div class="error-message">
        <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
    </div>
    <%
            // Clear the error message after displaying it
            session.removeAttribute("loginError");
        }
    %>

    <form action="LoginServlet" method="post">
        <div class="input-group">
            <i class="fas fa-user"></i>
            <input type="text" name="username" placeholder="Username" required>
        </div>
        <div class="input-group">
            <i class="fas fa-lock"></i>
            <input type="password" name="password" placeholder="Password" required>
        </div>
        <div class="form-options">
            <label>
                <input type="checkbox" name="remember"> Remember me
            </label>
            <a href="#" class="forgot-password">Forgot Password?</a>
        </div>
        <button type="submit" class="btn-login"><i class="fas fa-sign-in-alt"></i> Login</button>
    </form>
    <div class="form-footer">
        <p>New to PetPals? <a href="register.jsp" class="register-link">Create an account</a></p>
    </div>
</div>

<footer>
    <div class="container">
        <p>&copy; <%= new java.util.Date().getYear() + 1900 %> PetPals | Built with <i class="fas fa-heart"></i> for pets and people</p>
        <div class="social-links">
            <a href="#"><i class="fab fa-facebook"></i></a>
            <a href="#"><i class="fab fa-twitter"></i></a>
            <a href="#"><i class="fab fa-instagram"></i></a>
        </div>
    </div>
</footer>
</body>
</html>