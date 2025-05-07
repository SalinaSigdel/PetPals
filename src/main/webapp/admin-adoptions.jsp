<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.snapgramfx.petpals.model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="Adoption History" />
  <jsp:param name="activePage" value="admin-adoptions" />
</jsp:include>

<div class="container">
  <div class="admin-container">
    <div class="admin-header">
      <h1><i class="fas fa-history"></i> Adoption History</h1>
      <p>View and manage completed adoptions.</p>

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
      <div class="admin-filters">
        <div class="filter-group">
          <label>Status:</label>
          <select id="statusFilter" onchange="changeStatus(this.value)">
            <option value="completed" <%= "completed".equals(request.getAttribute("currentStatus")) ? "selected" : "" %>>Completed</option>
            <option value="returned" <%= "returned".equals(request.getAttribute("currentStatus")) ? "selected" : "" %>>Returned</option>
            <option value="all" <%= "all".equals(request.getAttribute("currentStatus")) ? "selected" : "" %>>All</option>
          </select>
        </div>
      </div>

      <div class="admin-table-container">
        <table class="admin-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Pet</th>
              <th>Adopter</th>
              <th>Date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <%
            List<Adoption> adoptions = (List<Adoption>) request.getAttribute("adoptions");
            boolean hasAdoptions = adoptions != null && !adoptions.isEmpty();

            if (hasAdoptions) {
              for (Adoption adoption : adoptions) {
                int adoptionId = adoption.getAdoptionId();
                String petName = adoption.getPetName();
                String adopterName = adoption.getUserName();
                String adoptionDate = new java.text.SimpleDateFormat("MMM d, yyyy").format(adoption.getAdoptionDate());
                String status = adoption.getStatus();

                // Status CSS class
                String statusClass = "";
                if ("pending".equalsIgnoreCase(status)) {
                  statusClass = "pending";
                } else if ("completed".equalsIgnoreCase(status)) {
                  statusClass = "approved";
                } else if ("returned".equalsIgnoreCase(status)) {
                  statusClass = "rejected";
                }
            %>
            <tr>
              <td><%= adoptionId %></td>
              <td><%= petName %></td>
              <td><%= adopterName %></td>
              <td><%= adoptionDate %></td>
              <td><span class="status <%= statusClass %>"><%= status %></span></td>
              <td class="action-group">
                <a href="adoption-details?id=<%= adoptionId %>" class="btn-action btn-view" title="View Details">
                  <i class="fas fa-eye"></i>
                </a>
                <% if ("completed".equalsIgnoreCase(status)) { %>
                <form method="post" action="return-pet" style="display:inline;" onsubmit="return confirm('Are you sure you want to mark this pet as returned?');">
                  <input type="hidden" name="id" value="<%= adoptionId %>">
                  <button type="submit" class="btn-action btn-delete" title="Mark as Returned">
                    <i class="fas fa-undo"></i>
                  </button>
                </form>
                <% } %>
              </td>
            </tr>
            <%
              }
            }

            if (!hasAdoptions) {
            %>
            <tr>
              <td colspan="6">No adoption history found</td>
            </tr>
            <%
            }
            %>
          </tbody>
        </table>

        <%
        int currentPage = (Integer) request.getAttribute("currentPage");
        int totalPages = (Integer) request.getAttribute("totalPages");
        String currentStatus = (String) request.getAttribute("currentStatus");

        if (totalPages > 1) {
        %>
        <div class="pagination">
          <% if (currentPage > 1) { %>
          <a href="admin-adoptions?status=<%= currentStatus %>&page=<%= currentPage - 1 %>" class="btn-page"><i class="fas fa-chevron-left"></i></a>
          <% } else { %>
          <span class="btn-page disabled"><i class="fas fa-chevron-left"></i></span>
          <% } %>

          <%
          int startPage = Math.max(1, currentPage - 2);
          int endPage = Math.min(totalPages, startPage + 4);

          for (int i = startPage; i <= endPage; i++) {
          %>
          <a href="admin-adoptions?status=<%= currentStatus %>&page=<%= i %>" class="btn-page <%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
          <% } %>

          <% if (currentPage < totalPages) { %>
          <a href="admin-adoptions?status=<%= currentStatus %>&page=<%= currentPage + 1 %>" class="btn-page"><i class="fas fa-chevron-right"></i></a>
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
  function changeStatus(status) {
    window.location.href = 'admin-adoptions?status=' + status;
  }

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
