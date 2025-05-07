<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.snapgramfx.petpals.model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="Adoption Applications" />
  <jsp:param name="activePage" value="admin-applications" />
</jsp:include>

<div class="container">
  <div class="admin-container">
    <div class="admin-header">
      <h1><i class="fas fa-clipboard-list"></i> Adoption Applications</h1>
      <p>View and manage adoption applications.</p>

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
            <option value="pending" <%= "pending".equals(request.getAttribute("currentStatus")) ? "selected" : "" %>>Pending</option>
            <option value="approved" <%= "approved".equals(request.getAttribute("currentStatus")) ? "selected" : "" %>>Approved</option>
            <option value="rejected" <%= "rejected".equals(request.getAttribute("currentStatus")) ? "selected" : "" %>>Rejected</option>
            <option value="all" <%= "all".equals(request.getAttribute("currentStatus")) ? "selected" : "" %>>All</option>
          </select>
        </div>
      </div>

      <div class="admin-table-container">
        <table class="admin-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Pet Name</th>
              <th>Applicant</th>
              <th>Date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <%
            List<AdoptionApplication> applications = (List<AdoptionApplication>) request.getAttribute("applications");
            boolean hasApplications = applications != null && !applications.isEmpty();

            if (hasApplications) {
              for (AdoptionApplication app : applications) {
                int applicationId = app.getApplicationId();
                String petName = app.getPetName();
                String applicantName = app.getApplicantName();
                String applicationDate = new java.text.SimpleDateFormat("MMM d, yyyy").format(app.getCreatedAt());
                String status = app.getStatus();

                // Status CSS class
                String statusClass = "";
                if ("pending".equalsIgnoreCase(status)) {
                  statusClass = "pending";
                } else if ("approved".equalsIgnoreCase(status)) {
                  statusClass = "approved";
                } else if ("rejected".equalsIgnoreCase(status)) {
                  statusClass = "rejected";
                }
            %>
            <tr>
              <td><%= applicationId %></td>
              <td><%= petName %></td>
              <td><%= applicantName %></td>
              <td><%= applicationDate %></td>
              <td><span class="status <%= statusClass %>"><%= status %></span></td>
              <td class="action-group">
                <a href="application-details?id=<%= applicationId %>" class="btn-action btn-view" title="View Details">
                  <i class="fas fa-eye"></i>
                </a>
                <% if ("pending".equalsIgnoreCase(status)) { %>
                <form method="post" action="approve-application" style="display:inline;">
                  <input type="hidden" name="id" value="<%= applicationId %>">
                  <button type="submit" class="btn-action btn-edit" title="Approve Application">
                    <i class="fas fa-check"></i>
                  </button>
                </form>
                <form method="post" action="reject-application" style="display:inline;">
                  <input type="hidden" name="id" value="<%= applicationId %>">
                  <button type="submit" class="btn-action btn-delete" title="Reject Application">
                    <i class="fas fa-times"></i>
                  </button>
                </form>
                <% } %>
              </td>
            </tr>
            <%
              }
            }

            if (!hasApplications) {
            %>
            <tr>
              <td colspan="6">No applications found</td>
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
          <a href="admin-applications?status=<%= currentStatus %>&page=<%= currentPage - 1 %>" class="btn-page"><i class="fas fa-chevron-left"></i></a>
          <% } else { %>
          <span class="btn-page disabled"><i class="fas fa-chevron-left"></i></span>
          <% } %>

          <%
          int startPage = Math.max(1, currentPage - 2);
          int endPage = Math.min(totalPages, startPage + 4);

          for (int i = startPage; i <= endPage; i++) {
          %>
          <a href="admin-applications?status=<%= currentStatus %>&page=<%= i %>" class="btn-page <%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
          <% } %>

          <% if (currentPage < totalPages) { %>
          <a href="admin-applications?status=<%= currentStatus %>&page=<%= currentPage + 1 %>" class="btn-page"><i class="fas fa-chevron-right"></i></a>
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
    window.location.href = 'admin-applications?status=' + status;
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
