<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register | PetPals</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .simple-register {
            max-width: 500px;
            margin: 2rem auto;
            background: var(--white);
            padding: 2rem;
            border-radius: 10px;
            box-shadow: var(--shadow);
        }

        .simple-register h2 {
            text-align: center;
            color: var(--primary-color);
            margin-bottom: 1.5rem;
            font-size: 1.8rem;
        }

        .input-group {
            margin-bottom: 1.5rem;
            position: relative;
        }

        .input-group i {
            position: absolute;
            left: 1rem;
            top: 50%;
            transform: translateY(-50%);
            color: var(--primary-color);
        }

        .input-group input,
        .input-group select {
            width: 100%;
            padding: 0.8rem 1rem 0.8rem 2.5rem;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1rem;
        }

        .input-group input:focus,
        .input-group select:focus {
            border-color: var(--primary-color);
            outline: none;
        }

        .terms {
            margin: 1.5rem 0;
        }

        .terms label {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.9rem;
            color: #666;
        }

        .terms-link {
            color: var(--primary-color);
            text-decoration: none;
        }

        .terms-link:hover {
            text-decoration: underline;
        }

        .btn-register {
            width: 100%;
            padding: 0.8rem;
            background-color: var(--primary-color);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1rem;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

        .btn-register:hover {
            background-color: var(--secondary-color);
        }

        .form-footer {
            text-align: center;
            margin-top: 1.5rem;
            font-size: 0.9rem;
        }

        .login-link {
            color: var(--primary-color);
            text-decoration: none;
        }

        .login-link:hover {
            text-decoration: underline;
        }

        .error-message {
            background-color: rgba(255, 0, 0, 0.1);
            color: #d9534f;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 15px;
            text-align: center;
        }
    </style>
</head>
<body>
<header>
    <div class="container">
        <h1><i class="fas fa-paw"></i> PetPals</h1>
        <nav>
            <a href="index.jsp"><i class="fas fa-home"></i> Home</a>
            <a href="adopt.jsp"><i class="fas fa-heart"></i> Adopt</a>
            <a href="login.jsp"><i class="fas fa-user"></i> Login</a>
            <a href="register.jsp" class="active"><i class="fas fa-user-plus"></i> Register</a>
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

<div class="simple-register">
    <h2><i class="fas fa-user-plus"></i> Create Account</h2>

    <%
        // Display error message if registration failed
        String errorMessage = (String) request.getAttribute("registerError");
        if (errorMessage != null) {
    %>
    <div class="error-message">
        <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
    </div>
    <%
        }
    %>

    <form action="RegisterServlet" method="post">
        <div class="input-group">
            <i class="fas fa-user"></i>
            <input type="text" name="name" placeholder="Full Name" required>
        </div>

        <div class="input-group">
            <i class="fas fa-envelope"></i>
            <input type="email" name="email" placeholder="Email Address" required>
        </div>

        <div class="input-group">
            <i class="fas fa-id-card"></i>
            <input type="text" name="username" placeholder="Username" required>
        </div>

        <div class="input-group">
            <i class="fas fa-lock"></i>
            <input type="password" name="password" placeholder="Password" required>
        </div>

        <div class="input-group">
            <i class="fas fa-user-tag"></i>
            <select name="role" required>
                <option value="" disabled selected>I am a...</option>
                <option value="adopter">Pet Adopter</option>
                <option value="shelter">Admin</option>
            </select>
        </div>

        <div class="terms">
            <label>
                <input type="checkbox" name="terms" required>
                I agree to the <a href="#" class="terms-link">Terms and Conditions</a>
            </label>
        </div>

        <button type="submit" class="btn-register"><i class="fas fa-user-plus"></i> Register</button>
    </form>

    <div class="form-footer">
        <p>Already have an account? <a href="login.jsp" class="login-link">Login</a></p>
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