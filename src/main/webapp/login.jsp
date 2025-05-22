<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.snapgramfx.petpals.util.CookieUtil" %>

<%
  // Get redirect URL if any
  String redirectUrl = request.getParameter("redirect");
  // We'll handle success message later in the page
  
  // For debugging
  System.out.println("Success message parameter: " + request.getParameter("success"));
  
// Redirect if already logged in
  Object userIdObj = session.getAttribute("userId");
  if (userIdObj != null) {
    if (redirectUrl != null && !redirectUrl.isEmpty()) {
      response.sendRedirect(redirectUrl);
    } else {
      response.sendRedirect("index.jsp");
    }
    return;
  }

// Get remembered username for form pre-filling
  String rememberedUsername = CookieUtil.getCookieValue(request, "remember_username");
%>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="Login" />
  <jsp:param name="activePage" value="login" />
</jsp:include>

<main class="login-container">
  <div class="form-container">
    <h2><i class="fas fa-user-circle"></i> Welcome Back</h2>

    <%
      // Display error message if login failed
      String errorMessage = (String) request.getAttribute("errorMessage");
      if (errorMessage == null) {
        errorMessage = (String) session.getAttribute("loginError");
      }

      // Check for specific error types from query parameters
      String errorType = request.getParameter("error");
      if (errorType != null) {
        if (errorType.equals("unauthorized")) {
          errorMessage = "You need administrator privileges to access this page";
        } else if (errorType.equals("login_required")) {
          errorMessage = "Please log in to access this page";
        }
      }

      if (errorMessage != null) {
    %>
    <div class="error-message">
      <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
    </div>
    <%
        // Clear the error message after displaying it
        session.removeAttribute("loginError");
      }

      // Get success message from request attribute first, then from URL parameter
      String successMessage = (String) request.getAttribute("successMessage");
      if (successMessage == null) {
        successMessage = request.getParameter("success");
      }
      
      // Display success message if any
      if (successMessage != null) {
    %>
    <div class="success-message">
      <i class="fas fa-check-circle"></i> <%= successMessage %>
    </div>
    <%
      }
    %>

    <form action="login" method="post">
      <% if (redirectUrl != null && !redirectUrl.isEmpty()) { %>
      <input type="hidden" name="redirect" value="<%= redirectUrl %>">
      <% } %>
      <div class="input-group">
        <i class="fas fa-user"></i>
        <input type="text" name="username" placeholder="Username" required>
      </div>
      <div class="input-group">
        <i class="fas fa-lock"></i>
        <div class="password-toggle-wrapper">
          <input type="password" name="password" placeholder="Password" required>
          <button type="button" class="password-toggle" title="Show password" aria-label="Show password">
            <i class="fas fa-eye"></i>
          </button>
        </div>
      </div>
      <div class="form-options">
        <label>
          <input type="checkbox" name="remember" id="remember">
          <span>Remember me</span>
        </label>
        <a href="forgot-password" class="forgot-password">Forgot Password?</a>
      </div>

      <script>
        // Check if username is remembered
        const rememberedUsername = '<%= rememberedUsername != null ? rememberedUsername : "" %>';
        if (rememberedUsername) {
          document.querySelector('input[name="username"]').value = rememberedUsername;
          document.getElementById('remember').checked = true;
        }
        
        // Debug the success message parameter
        document.addEventListener('DOMContentLoaded', function() {
          const successParam = '<%= request.getParameter("success") %>';
          console.log("Success parameter:", successParam);
          if (successParam && successParam !== 'null') {
            console.log("Success message found:", successParam);
          }
          
          // Remove any password strength indicators that might be created
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
          const toggleBtn = document.querySelector('.password-toggle');
          const passwordField = document.querySelector('input[name="password"]');
          
          if (toggleBtn && passwordField) {
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
      </script>
      <button type="submit" class="btn-login"><i class="fas fa-sign-in-alt"></i> Login</button>
    </form>
    <div class="form-footer">
      <p>New to PetPals? <a href="register.jsp" class="register-link">Create an account</a></p>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/includes/footer.jsp" />