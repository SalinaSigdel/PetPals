<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.snapgramfx.petpals.model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="Manage Pets" />
  <jsp:param name="activePage" value="admin-pets" />
</jsp:include>

<div class="container">
  <div class="admin-container">
    <div class="admin-header">
      <h1><i class="fas fa-paw"></i> Manage Pets</h1>
      <p>View and manage all pets in the system.</p>

      <% if (request.getParameter("success") != null) { %>
      <div class="alert alert-success">
        <i class="fas fa-check-circle"></i> <%= request.getParameter("success") %>
      </div>
      <% } %>

      <% if (request.getParameter("error") != null) { %>
      <div class="alert alert-error">
        <i class="fas fa-exclamation-circle"></i> <%= request.getParameter("error") %>
      </div>
      <% } %>
    </div>

    <div class="admin-content">
      <div class="admin-actions">
        <a href="add-pet" class="btn-primary">
          <i class="fas fa-plus"></i> Add New Pet
        </a>
      </div>

      <div class="admin-table-container">
        <table class="admin-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Image</th>
            <th>Name</th>
            <th>Type</th>
            <th>Breed</th>
            <th>Age</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <%
            List<Pet> pets = (List<Pet>) request.getAttribute("pets");
            boolean hasPets = pets != null && !pets.isEmpty();

            if (hasPets) {
              for (Pet pet : pets) {
                int petId = pet.getPetId();
                String petName = pet.getName();
                String petType = pet.getType();
                String petBreed = pet.getBreed();
                String petAge = pet.getAge();
                String status = pet.getStatus();
                boolean isAvailable = "available".equals(status);
                String imageUrl = pet.getImageUrl();

                // Default image if none provided
                if (imageUrl == null || imageUrl.trim().isEmpty()) {
                  if ("dog".equalsIgnoreCase(petType)) {
                    imageUrl = "https://images.unsplash.com/photo-1601979031925-424e53b6caaa?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                  } else if ("cat".equalsIgnoreCase(petType)) {
                    imageUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                  } else if ("bird".equalsIgnoreCase(petType)) {
                    imageUrl = "https://images.unsplash.com/photo-1552728089-57bdde30beb3?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                  } else if ("rabbit".equalsIgnoreCase(petType)) {
                    imageUrl = "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                  } else {
                    imageUrl = "https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                  }
                }
          %>
          <tr>
            <td><%= petId %></td>
            <td>
              <img src="<%= imageUrl %>" alt="<%= petName %>">
            </td>
            <td><%= petName %></td>
            <td><%= petType %></td>
            <td><%= petBreed %></td>
            <td><%= petAge %></td>
            <td>
                <span class="status <%= isAvailable ? "available" : "adopted" %>">
                  <%= isAvailable ? "Available" : "Adopted" %>
                </span>
            </td>
            <td class="action-group">
              <a href="edit-pet?id=<%= petId %>" class="btn-action btn-edit" title="Edit Pet">
                <i class="fas fa-edit"></i>
              </a>
              <form method="post" action="delete-pet" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this pet?');">
                <input type="hidden" name="id" value="<%= petId %>">
                <button type="submit" class="btn-action btn-delete" title="Delete Pet">
                  <i class="fas fa-trash"></i>
                </button>
              </form>
            </td>
          </tr>
          <%
              }
            }

            if (!hasPets) {
          %>
          <tr>
            <td colspan="8">No pets found</td>
          </tr>
          <%
            }
          %>
          </tbody>
        </table>

        <%
          int currentPage = (Integer) request.getAttribute("currentPage");
          int totalPages = (Integer) request.getAttribute("totalPages");

          if (totalPages > 1) {
        %>
        <div class="pagination">
          <% if (currentPage > 1) { %>
          <a href="admin-pets?page=<%= currentPage - 1 %>" class="btn-page"><i class="fas fa-chevron-left"></i></a>
          <% } else { %>
          <span class="btn-page disabled"><i class="fas fa-chevron-left"></i></span>
          <% } %>

          <%
            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(totalPages, startPage + 4);

            for (int i = startPage; i <= endPage; i++) {
          %>
          <a href="admin-pets?page=<%= i %>" class="btn-page <%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
          <% } %>

          <% if (currentPage < totalPages) { %>
          <a href="admin-pets?page=<%= currentPage + 1 %>" class="btn-page"><i class="fas fa-chevron-right"></i></a>
          <% } else { %>
          <span class="btn-page disabled"><i class="fas fa-chevron-right"></i></span>
          <% } %>
        </div>
        <% } %>
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
