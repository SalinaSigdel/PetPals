<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.snapgramfx.petpals.model.AdoptionApplication" %>
<%@ page import="com.snapgramfx.petpals.model.Pet" %>
<%@ page import="com.snapgramfx.petpals.service.PetService" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="My Applications" />
  <jsp:param name="activePage" value="my-applications" />
  <jsp:param name="extraHead" value="/WEB-INF/includes/my-applications-styles.jsp" />
</jsp:include>
<%
  // Check if user is logged in
  Object userIdObj = session.getAttribute("userId");
  String userRole = (String) session.getAttribute("userRole");
  boolean isLoggedIn = (userIdObj != null);
  boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

// Redirect if not logged in
  if (!isLoggedIn) {
    response.sendRedirect("login.jsp");
    return;
  }

// Get applications from request attribute
  List<AdoptionApplication> applications = (List<AdoptionApplication>) request.getAttribute("applications");
  PetService petService = new PetService();
%>

<div class="applications-container">
  <h2 class="page-title">My Adoption Applications</h2>

  <% if (request.getAttribute("successMessage") != null) { %>
  <div class="success-message">
    <i class="fas fa-check-circle"></i> <%= request.getAttribute("successMessage") %>
  </div>
  <% } %>

  <% if (applications == null || applications.isEmpty()) { %>
  <div class="no-applications">
    <i class="fas fa-folder-open"></i>
    <h3>You don't have any adoption applications yet</h3>
    <p>Browse our available pets and submit an application to adopt your new best friend!</p>
    <a href="adopt" class="btn-browse-pets"><i class="fas fa-heart"></i> Browse Pets</a>
  </div>
  <% } else { %>
  <% for (AdoptionApplication application : applications) {
    Pet pet = petService.getPetById(application.getPetId());
    if (pet == null) continue; // Skip if pet not found
  %>
  <div class="application-card">
    <div class="application-header">
      <div class="application-id">Application #<%= application.getApplicationId() %></div>
      <div class="application-status status-<%= application.getStatus().toLowerCase() %>">
        <%= application.getStatus().substring(0, 1).toUpperCase() + application.getStatus().substring(1) %>
      </div>
    </div>
    <div class="application-body">
      <img src="<%= pet.getImageUrl() %>" alt="<%= pet.getName() %>" class="pet-image">
      <div class="application-details">
        <h3 class="pet-name"><%= pet.getName() %></h3>
        <div class="application-date">
          <i class="far fa-calendar-alt"></i> Applied on: <%= new java.text.SimpleDateFormat("MMM d, yyyy").format(application.getCreatedAt()) %>
        </div>
        <div class="pet-info">
          <span><i class="fas fa-paw"></i> <%= pet.getType() %></span> •
          <span><i class="fas fa-birthday-cake"></i> <%= pet.getAge() %></span> •
          <span><i class="fas fa-venus-mars"></i> <%= pet.getGender() %></span>
        </div>
        <div class="application-actions">
          <a href="application-details?id=<%= application.getApplicationId() %>" class="btn-view-details">
            <i class="fas fa-eye"></i> View Details
          </a>
        </div>
      </div>
    </div>
  </div>
  <% } %>
  <% } %>

  <%
    // Add pagination if there are multiple pages
    if (request.getAttribute("totalPages") != null) {
      int currentPage = (Integer) request.getAttribute("currentPage");
      int totalPages = (Integer) request.getAttribute("totalPages");

      if (totalPages > 1) {
  %>
  <div class="pagination">
    <% if (currentPage > 1) { %>
    <a href="my-applications?page=<%= currentPage - 1 %>" class="page-btn"><i class="fas fa-chevron-left"></i></a>
    <% } else { %>
    <span class="page-btn disabled"><i class="fas fa-chevron-left"></i></span>
    <% } %>

    <%
      // Determine range of page numbers to display
      int startPage = Math.max(1, currentPage - 2);
      int endPage = Math.min(totalPages, currentPage + 2);

      // Ensure we always show at least 5 page numbers if available
      if (endPage - startPage + 1 < 5) {
        if (startPage == 1) {
          endPage = Math.min(5, totalPages);
        } else if (endPage == totalPages) {
          startPage = Math.max(1, totalPages - 4);
        }
      }

      // First page button if not in range
      if (startPage > 1) {
    %>
    <a href="my-applications?page=1" class="page-btn">1</a>
    <% if (startPage > 2) { %>
    <span class="page-ellipsis">...</span>
    <% } %>
    <% } %>

    <%
      // Page number buttons
      for (int i = startPage; i <= endPage; i++) {
    %>
    <a href="my-applications?page=<%= i %>" class="page-btn <%= i == currentPage ? "active" : "" %>"><%= i %></a>
    <% } %>

    <%
      // Last page button if not in range
      if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
    %>
    <span class="page-ellipsis">...</span>
    <% } %>
    <a href="my-applications?page=<%= totalPages %>" class="page-btn"><%= totalPages %></a>
    <% } %>

    <% if (currentPage < totalPages) { %>
    <a href="my-applications?page=<%= currentPage + 1 %>" class="page-btn"><i class="fas fa-chevron-right"></i></a>
    <% } else { %>
    <span class="page-btn disabled"><i class="fas fa-chevron-right"></i></span>
    <% } %>
  </div>
  <%
      }
    }
  %>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />