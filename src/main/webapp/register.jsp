<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  // Redirect if already logged in
  Object userIdObj = session.getAttribute("userId");
  if (userIdObj != null) {
    response.sendRedirect("index.jsp");
    return;
  }
%>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="Register" />
  <jsp:param name="activePage" value="register" />
</jsp:include>

<main class="login-container">
  <div class="form-container">
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

    <form action="register" method="post">
      <div class="input-group">
        <i class="fas fa-user"></i>
        <input type="text" name="fullName" placeholder="Full Name" required>
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
        <div class="password-toggle-wrapper">
          <input type="password" id="password" name="password" placeholder="Password" required>
          <button type="button" class="password-toggle" title="Show password" aria-label="Show password">
            <i class="fas fa-eye"></i>
          </button>
        </div>
      </div>

      <div class="input-group">
        <i class="fas fa-lock"></i>
        <div class="password-toggle-wrapper">
          <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required>
          <button type="button" class="password-toggle" title="Show password" aria-label="Show password">
            <i class="fas fa-eye"></i>
          </button>
        </div>
      </div>

      <div class="input-group">
        <%--          <i class="fas fa-user-tag"></i>--%>
        <select name="role" required hidden>
          <%--            <option value="" disabled selected>I am a...</option>--%>
          <option value="adopter">Pet Adopter</option>
          <option value="admin">Admin</option>
        </select>
      </div>

      <div class="terms">
        <label>
          <input type="checkbox" name="terms" required>
          <span>I agree to the <a href="#" class="terms-link">Terms and Conditions</a></span>
        </label>
      </div>

      <button type="submit" class="btn-register"><i class="fas fa-user-plus"></i> Register</button>
    </form>

    <div class="form-footer">
      <p>Already have an account? <a href="login.jsp" class="login-link">Login</a></p>
    </div>
  </div>
</main>

<script>
  // Remove any password strength indicators that might be created
  document.addEventListener('DOMContentLoaded', function() {
    // Function to remove password strength indicators
    function removePasswordStrength() {
      const strengthIndicators = document.querySelectorAll('.password-strength');
      strengthIndicators.forEach(indicator => {
        indicator.remove();
      });
    }

    // Remove on page load
    removePasswordStrength();

    // Also remove when typing in password fields
    const passwordFields = document.querySelectorAll('input[type="password"]');
    passwordFields.forEach(field => {
      field.addEventListener('input', removePasswordStrength);
    });
    
    // Add password toggle functionality
    const toggleBtns = document.querySelectorAll('.password-toggle');
    
    toggleBtns.forEach(toggleBtn => {
      const passwordField = toggleBtn.previousElementSibling;
      
      if (passwordField && passwordField.type === 'password') {
        toggleBtn.addEventListener('click', function(e) {
          // Prevent form submission
          e.preventDefault();
          e.stopPropagation();

          if (passwordField.type === 'password') {
            passwordField.type = 'text';
            this.innerHTML = '<i class="fas fa-eye-slash"></i>';
            this.title = 'Hide password';
            this.setAttribute('aria-label', 'Hide password');
          } else {
            passwordField.type = 'password';
            this.innerHTML = '<i class="fas fa-eye"></i>';
            this.title = 'Show password';
            this.setAttribute('aria-label', 'Show password');
          }
        });
      }
    });
  });
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />