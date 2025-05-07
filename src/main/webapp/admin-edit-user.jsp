<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.snapgramfx.petpals.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="Edit User" />
  <jsp:param name="activePage" value="admin-users" />
</jsp:include>

<%
  // Get user from request attributes (set by AdminUserManagementServlet)
  User user = (User) request.getAttribute("user");

// If user is null, redirect to users list
  if (user == null) {
    response.sendRedirect("admin-users");
    return;
  }
%>

<div class="admin-dashboard">
  <div class="container">
    <div class="admin-header">
      <h1>Edit User</h1>
      <p>Update user information and permissions.</p>
    </div>

    <%
      // Display success/error messages if any
      String successMessage = (String) request.getAttribute("successMessage");
      String errorMessage = (String) request.getAttribute("errorMessage");

      if (successMessage != null) {
    %>
    <div class="alert alert-success">
      <i class="fas fa-check-circle"></i> <%= successMessage %>
    </div>
    <% }

      if (errorMessage != null) {
    %>
    <div class="alert alert-error">
      <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
    </div>
    <% } %>

    <form action="admin-update-user" method="post">
      <input type="hidden" name="userId" value="<%= user.getUserId() %>">

      <div class="admin-form-container">
        <div class="admin-form-section">
          <h3>Basic Information</h3>
          <div class="admin-form-row">
            <div class="admin-form-group">
              <label for="username">Username</label>
              <input type="text" id="username" name="username" value="<%= user.getUsername() %>" required>
            </div>
            <div class="admin-form-group">
              <label for="email">Email</label>
              <input type="email" id="email" name="email" value="<%= user.getEmail() %>" required>
            </div>
          </div>
          <div class="admin-form-group">
            <label for="fullName">Full Name</label>
            <input type="text" id="fullName" name="fullName" value="<%= user.getFullName() %>" required>
          </div>
        </div>

        <div class="admin-form-section">
          <h3>Role & Permissions</h3>
          <div class="admin-form-group">
            <label for="role">User Role</label>
            <select id="role" name="role" required>
              <option value="adopter" <%= "adopter".equals(user.getRole()) ? "selected" : "" %>>Adopter</option>
              <option value="admin" <%= "admin".equals(user.getRole()) ? "selected" : "" %>>Administrator</option>
            </select>
          </div>
          <div class="admin-form-check">
            <input type="checkbox" id="emailNotifications" name="emailNotifications" <%= user.isEmailNotificationsEnabled() ? "checked" : "" %>>
            <label for="emailNotifications">Enable Email Notifications</label>
          </div>
        </div>

        <div class="admin-form-section">
          <h3>Change Password</h3>
          <p class="admin-form-note">Leave blank to keep the current password.</p>
          <div class="admin-form-row">
            <div class="admin-form-group">
              <label for="newPassword">New Password</label>
              <input type="password" id="newPassword" name="newPassword">
            </div>
            <div class="admin-form-group">
              <label for="confirmPassword">Confirm Password</label>
              <input type="password" id="confirmPassword" name="confirmPassword">
            </div>
          </div>
        </div>

        <div class="admin-btn-container">
          <a href="admin-users" class="admin-btn-secondary">Cancel</a>
          <button type="submit" class="admin-btn-primary">Save Changes</button>
        </div>
      </div>
    </form>
  </div>
</div>

<jsp:include page="/WEB-INF/includes/admin-footer.jsp" />
