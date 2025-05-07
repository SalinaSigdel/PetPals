<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="Forgot Password" />
  <jsp:param name="extraHead" value="/WEB-INF/includes/password-reset-styles.jsp" />
</jsp:include>

<main class="main-content">
  <div class="auth-container">
    <div class="auth-form-container">
      <h2><i class="fas fa-key"></i> Forgot Password</h2>

      <%
      // Display error message if any
      String errorMessage = (String) request.getAttribute("errorMessage");
      if (errorMessage != null) {
      %>
        <div class="error-message">
          <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
        </div>
      <% }

      // Display success message if any
      String successMessage = (String) request.getAttribute("successMessage");
      if (successMessage != null) {
      %>
        <div class="success-message">
          <i class="fas fa-check-circle"></i> <%= successMessage %>
        </div>

        <%
        // Display reset link for demonstration purposes
        String resetLink = (String) request.getAttribute("resetLink");
        if (resetLink != null) {
        %>
          <div class="demo-info">
            <p><strong>Demo Only:</strong> In a real application, an email would be sent with the reset link.</p>
            <p>For demonstration purposes, here is your reset link:</p>
            <a href="<%= resetLink %>" class="reset-link"><%= resetLink %></a>
          </div>
        <% } %>
      <% } else { %>

      <p class="auth-info">Enter your email address below and we'll send you a link to reset your password.</p>

      <form action="forgot-password" method="post">
        <div class="input-group">
          <i class="fas fa-envelope"></i>
          <input type="email" name="email" placeholder="Email Address" required>
        </div>

        <button type="submit" class="btn-login"><i class="fas fa-paper-plane"></i> Send Reset Link</button>
      </form>

      <% } %>

      <div class="form-footer">
        <p>Remember your password? <a href="login.jsp" class="login-link">Back to Login</a></p>
      </div>
    </div>
  </div>
</main>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
