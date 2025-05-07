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
        <input type="password" id="password" name="password" placeholder="Password" required>
      </div>

      <div class="input-group">
        <i class="fas fa-lock"></i>
        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required>
      </div>

      <div class="input-group">
        <i class="fas fa-user-tag"></i>
        <select name="role" required>
          <option value="" disabled selected>I am a...</option>
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
  });
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />