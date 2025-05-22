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
          <div class="password-toggle-wrapper">
            <input type="password" name="password" id="password" placeholder="New Password" required>
            <button type="button" class="password-toggle" title="Show password">
              <i class="fas fa-eye"></i>
            </button>
          </div>
        </div>

        <div class="input-group">
          <i class="fas fa-lock"></i>
          <div class="password-toggle-wrapper">
            <input type="password" name="confirmPassword" placeholder="Confirm New Password" required>
            <button type="button" class="password-toggle" title="Show password">
              <i class="fas fa-eye"></i>
            </button>
          </div>
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
  // Direct password toggle functionality
  document.addEventListener('DOMContentLoaded', function() {
    console.log('Reset password page DOM loaded');
    const toggleBtns = document.querySelectorAll('.password-toggle');
    
    toggleBtns.forEach(toggleBtn => {
      const passwordField = toggleBtn.previousElementSibling;
      
      if (passwordField && passwordField.type === 'password') {
        toggleBtn.addEventListener('click', function(e) {
          // Prevent form submission
          e.preventDefault();
          e.stopPropagation();
          
          console.log('Toggle password clicked');
          
          if (passwordField.type === 'password') {
            passwordField.type = 'text';
            this.innerHTML = '<i class="fas fa-eye-slash"></i>';
            this.title = 'Hide password';
          } else {
            passwordField.type = 'password';
            this.innerHTML = '<i class="fas fa-eye"></i>';
            this.title = 'Show password';
          }
        });
      }
    });
  });
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
