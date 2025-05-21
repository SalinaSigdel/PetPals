<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.snapgramfx.petpals.model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="Admin Dashboard" />
  <jsp:param name="activePage" value="admin" />
</jsp:include>
<%
  // Get stats from request attributes (set by AdminDashboardServlet)
  Integer totalPets = (Integer) request.getAttribute("totalPets");
  Integer availablePets = (Integer) request.getAttribute("availablePets");
  Integer pendingApplications = (Integer) request.getAttribute("pendingApplications");
  Integer totalAdoptions = (Integer) request.getAttribute("totalAdoptions");
  Integer totalUsers = (Integer) request.getAttribute("totalUsers");
  Integer adminUsers = (Integer) request.getAttribute("adminUsers");

  // If stats are null, redirect to the servlet
  if (totalPets == null || availablePets == null || pendingApplications == null ||
          totalAdoptions == null || totalUsers == null || adminUsers == null) {
    response.sendRedirect("admin-dashboard");
    return;
  }
%>

<div class="admin-dashboard">
  <div class="container">
    <div class="admin-header">
      <h1>Admin Dashboard</h1>
      <p>Welcome to the PetPals administration area. Here you can manage pets, users, and adoption applications.</p>
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
    <div class="alert alert-danger">
      <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
    </div>
    <% } %>

    <div class="dashboard-stats">
      <div class="stat-card">
        <i class="fas fa-paw"></i>
        <h4><%= totalPets %></h4>
        <p>Total Pets</p>
      </div>
      <div class="stat-card">
        <i class="fas fa-heart"></i>
        <h4><%= availablePets %></h4>
        <p>Available Pets</p>
      </div>
      <div class="stat-card">
        <i class="fas fa-clock"></i>
        <h4><%= pendingApplications %></h4>
        <p>Pending Applications</p>
      </div>
      <div class="stat-card">
        <i class="fas fa-home"></i>
        <h4><%= totalAdoptions %></h4>
        <p>Successful Adoptions</p>
      </div>
      <div class="stat-card">
        <i class="fas fa-users"></i>
        <h4><%= totalUsers %></h4>
        <p>Total Users</p>
      </div>
      <div class="stat-card">
        <i class="fas fa-user-shield"></i>
        <h4><%= adminUsers %></h4>
        <p>Admin Users</p>
      </div>
    </div>

    <div class="dashboard-section">
      <div class="section-header">
        <h3><i class="fas fa-tachometer-alt"></i> Quick Actions</h3>
      </div>
      <div class="quick-actions">
        <a href="admin-pets" class="quick-action-btn">
          <i class="fas fa-paw"></i>
          <span>Manage Pets</span>
        </a>
        <a href="admin-users" class="quick-action-btn">
          <i class="fas fa-users-cog"></i>
          <span>Manage Users</span>
        </a>
        <a href="admin-applications" class="quick-action-btn">
          <i class="fas fa-clipboard-list"></i>
          <span>View Applications</span>
        </a>
        <a href="add-pet" class="quick-action-btn">
          <i class="fas fa-plus-circle"></i>
          <span>Add New Pet</span>
        </a>
      </div>
    </div>
  </div>
</div>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    if (alerts.length > 0) {
      setTimeout(() => {
        alerts.forEach(alert => {
          alert.style.opacity = '0';
          setTimeout(() => {
            alert.style.display = 'none';
          }, 300);
        });
      }, 5000);
    }
  });
</script>

<jsp:include page="/WEB-INF/includes/admin-footer.jsp" />