<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="Reset Password" />
  <jsp:param name="extraHead" value="/WEB-INF/includes/password-reset-styles.jsp" />
</jsp:include>

<main class="main-content">
  <div class="auth-container">
    <div class="auth-form-container">
      <h2><i class="fas fa-lock"></i> Reset Password</h2>

      <%
        // Display error message if any
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
      %>
      <div class="error-message">
        <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
      </div>
      <% } %>

      <p class="auth-info">Please enter your new password below.</p>

      <form action="reset-password" method="post">
        <input type="hidden" name="token" value="${token}">

        <div class="input-group">
          <i class="fas fa-lock"></i>
          <input type="password" name="password" id="password" placeholder="New Password" required>
        </div>

        <div class="input-group">
          <i class="fas fa-lock"></i>
          <input type="password" name="confirmPassword" placeholder="Confirm New Password" required>
        </div>

        <!-- Password strength meter removed -->

        <button type="submit" class="btn-login"><i class="fas fa-check-circle"></i> Reset Password</button>
      </form>

      <div class="form-footer">
        <p>Remember your password? <a href="login.jsp" class="login-link">Back to Login</a></p>
      </div>
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
