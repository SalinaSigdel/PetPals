<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.snapgramfx.petpals.model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="User Management" />
  <jsp:param name="activePage" value="admin-users" />
  <jsp:param name="extraCSS" value="css/admin-user-management.css" />
</jsp:include>

<%
  // Get users from request attributes (set by AdminUserManagementServlet)
  List<User> users = (List<User>) request.getAttribute("users");
  boolean hasUsers = users != null && !users.isEmpty();
%>

<div class="admin-dashboard">
  <div class="container">
    <div class="user-management-container">
      <div class="user-management-header">
        <h2>User Management</h2>
      </div>

      <p class="user-management-description">View and manage all users registered on the PetPals platform.</p>

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

      <div class="user-management-toolbar">
        <div class="filter-container">
          <span class="filter-label">Filter by Role:</span>
          <select id="roleFilter" class="filter-select" onchange="changeRole(this.value)">
            <option value="all" <%= "all".equals(request.getAttribute("currentRole")) ? "selected" : "" %>>All Roles</option>
            <option value="admin" <%= "admin".equals(request.getAttribute("currentRole")) ? "selected" : "" %>>Admin</option>
            <option value="adopter" <%= "adopter".equals(request.getAttribute("currentRole")) ? "selected" : "" %>>Adopter</option>
            <option value="volunteer" <%= "volunteer".equals(request.getAttribute("currentRole")) ? "selected" : "" %>>Volunteer</option>
          </select>
        </div>

        <div class="search-container">
          <i class="fas fa-search"></i>
          <input type="text" id="searchInput" placeholder="Search users..." onkeyup="searchUsers()">
        </div>

        <button class="btn-add-user" onclick="location.href='register.jsp'">
          <i class="fas fa-plus"></i> Add New User
        </button>
      </div>

      <% if (hasUsers) { %>
      <table class="users-table" id="usersTable">
        <thead>
        <tr>
          <th class="id-column">ID</th>
          <th>Username</th>
          <th>Full Name</th>
          <th>Email</th>
          <th>Role</th>
          <th>Created At</th>
          <th class="actions-column">Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for (User user : users) {
          int userId = user.getUserId();
          String username = user.getUsername();
          String fullName = user.getFullName();
          String email = user.getEmail();
          String role = user.getRole();
          java.sql.Timestamp createdAt = user.getCreatedAt();

          // Format date
          String formattedDate = (createdAt != null) ? createdAt.toString().substring(0, 10) : "N/A";
        %>
        <tr>
          <td class="id-column"><%= userId %></td>
          <td><%= username %></td>
          <td><%= fullName %></td>
          <td><%= email %></td>
          <td>
                  <span class="role-badge <%= role.toLowerCase() %>">
                    <%= role.toUpperCase() %>
                  </span>
          </td>
          <td><%= formattedDate %></td>
          <td class="actions-column">
            <div class="action-buttons">
              <button class="btn-action btn-edit tooltip" data-tooltip="Edit User" onclick="location.href='admin-edit-user?id=<%= userId %>'">
                <i class="fas fa-edit"></i>
              </button>
              <% if (!username.equals("admin")) { %>
              <form method="post" action="admin-delete-user" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this user?');">
                <input type="hidden" name="id" value="<%= userId %>">
                <button type="submit" class="btn-action btn-delete tooltip" data-tooltip="Delete User">
                  <i class="fas fa-trash"></i>
                </button>
              </form>
              <% } %>
            </div>
          </td>
        </tr>
        <% } %>
        </tbody>
      </table>

      <%
        // Add pagination if there are multiple pages
        if (request.getAttribute("totalPages") != null) {
          int currentPage = (Integer) request.getAttribute("currentPage");
          int totalPages = (Integer) request.getAttribute("totalPages");
          String currentRole = (String) request.getAttribute("currentRole");

          if (totalPages > 1) {
      %>
      <div class="pagination">
        <% if (currentPage > 1) { %>
        <button onclick="location.href='admin-users?page=<%= currentPage - 1 %>&role=<%= currentRole %>'">
          <i class="fas fa-chevron-left"></i> Previous
        </button>
        <% } %>

        <%
          // Show limited page numbers with ellipsis
          int startPage = Math.max(1, currentPage - 2);
          int endPage = Math.min(totalPages, currentPage + 2);

          if (startPage > 1) { %>
        <button onclick="location.href='admin-users?page=1&role=<%= currentRole %>'">1</button>
        <% if (startPage > 2) { %><span>...</span><% } %>
        <% } %>

        <% for (int i = startPage; i <= endPage; i++) { %>
        <button class="<%= i == currentPage ? "active" : "" %>"
                onclick="location.href='admin-users?page=<%= i %>&role=<%= currentRole %>'">
          <%= i %>
        </button>
        <% } %>

        <% if (endPage < totalPages) { %>
        <% if (endPage < totalPages - 1) { %><span>...</span><% } %>
        <button onclick="location.href='admin-users?page=<%= totalPages %>&role=<%= currentRole %>'">
          <%= totalPages %>
        </button>
        <% } %>

        <% if (currentPage < totalPages) { %>
        <button onclick="location.href='admin-users?page=<%= currentPage + 1 %>&role=<%= currentRole %>'">
          Next <i class="fas fa-chevron-right"></i>
        </button>
        <% } %>
      </div>
      <% }
      } %>

      <% } else { %>
      <div class="empty-state">
        <i class="fas fa-users-slash"></i>
        <p>No users found matching your criteria.</p>
        <button class="btn-add-user" onclick="location.href='register.jsp'">
          <i class="fas fa-plus"></i> Add New User
        </button>
      </div>
      <% } %>
    </div>
  </div>
</div>
</div>

<script>
  function changeRole(role) {
    window.location.href = 'admin-users?role=' + role;
  }

  function searchUsers() {
    const input = document.getElementById('searchInput');
    const filter = input.value.toUpperCase();
    const table = document.getElementById('usersTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 1; i < rows.length; i++) {
      let found = false;
      const cells = rows[i].getElementsByTagName('td');

      for (let j = 0; j < cells.length - 1; j++) {
        const cell = cells[j];
        if (cell) {
          const textValue = cell.textContent || cell.innerText;
          if (textValue.toUpperCase().indexOf(filter) > -1) {
            found = true;
            break;
          }
        }
      }

      rows[i].style.display = found ? '' : 'none';
    }
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
