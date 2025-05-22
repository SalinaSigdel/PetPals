<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="Add New Pet" />
  <jsp:param name="activePage" value="admin-pets" />
</jsp:include>

<style>
  .form-container {
    max-width: 700px;
    margin: 2rem auto;
    padding: 2rem;
    background: white;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }

  .form-title {
    color: var(--primary-color);
    margin-bottom: 1.5rem;
    text-align: center;
  }

  .form-group {
    margin-bottom: 1.5rem;
  }

  .form-group label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: 500;
  }

  .form-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
  }

  input, select, textarea {
    width: 100%;
    padding: 0.8rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1rem;
  }

  input:focus, select:focus, textarea:focus {
    outline: none;
    border-color: var(--primary-color);
  }

  textarea {
    min-height: 120px;
    resize: vertical;
  }

  .btn-container {
    display: flex;
    justify-content: space-between;
    margin-top: 2rem;
  }

  .btn {
    padding: 0.8rem 1.5rem;
    border-radius: 5px;
    font-size: 1rem;
    cursor: pointer;
    border: none;
    transition: background-color 0.3s;
  }

  .btn-primary {
    background-color: var(--primary-color);
    color: white;
  }

  .btn-primary:hover {
    background-color: #00814a;
  }

  .btn-secondary {
    background-color: #6c757d;
    color: white;
  }

  .btn-secondary:hover {
    background-color: #5a6268;
  }

  .alert {
    padding: 0.75rem 1.25rem;
    border-radius: 6px;
    margin-bottom: 1.5rem;
    border: 1px solid transparent;
  }

  .alert-success {
    background-color: #d4edda;
    border-color: #c3e6cb;
    color: #155724;
  }

  .alert-danger {
    background-color: #f8d7da;
    border-color: #f5c6cb;
    color: #721c24;
  }

  @media (max-width: 768px) {
    .form-row {
      grid-template-columns: 1fr;
    }
  }
</style>

<%
  // Check if user is logged in and is an admin
  Object userIdObj = session.getAttribute("userId");
  String userRole = (String) session.getAttribute("userRole");
  boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

// If not admin, redirect to login
  if (!isAdmin) {
    response.sendRedirect("login.jsp?error=unauthorized");
    return;
  }
%>

<div class="form-container">
  <h2 class="form-title"><i class="fas fa-plus-circle"></i> Add New Pet</h2>

  <%
    // Display success/error message if any
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

  <form action="add-pet" method="post" enctype="multipart/form-data">
    <div class="form-row">
      <div class="form-group">
        <label for="name">Pet Name *</label>
        <input type="text" id="name" name="name" required>
      </div>
      <div class="form-group">
        <label for="type">Pet Type *</label>
        <select id="type" name="type" required>
          <option value="">Select Type</option>
          <option value="dog">Dog</option>
          <option value="cat">Cat</option>
          <option value="bird">Bird</option>
          <option value="rabbit">Rabbit</option>
          <option value="other">Other</option>
        </select>
      </div>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label for="breed">Breed *</label>
        <input type="text" id="breed" name="breed" required>
      </div>
      <div class="form-group">
        <label for="age">Age (in years) *</label>
        <input type="number" id="age" name="age" step="0.1" min="0.1" required>
      </div>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label for="gender">Gender *</label>
        <select id="gender" name="gender" required>
          <option value="">Select Gender</option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
        </select>
      </div>
      <div class="form-group">
        <label for="weight">Weight (kg) *</label>
        <input type="number" id="weight" name="weight" step="0.1" min="0.1" required>
      </div>
    </div>

    <div class="form-group">
      <label for="description">Description *</label>
      <textarea id="description" name="description" required></textarea>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label for="petImage">Pet Image</label>
        <input type="file" id="petImage" name="petImage" accept="image/*">
        <small class="form-text text-muted">Upload an image of the pet (JPG, PNG, GIF). Max size: 5MB</small>
      </div>
      <div class="form-group">
        <label for="badge">Badge</label>
        <select id="badge" name="badge">
          <option value="">All Ages</option>
          <option value="Puppy/Kitten">Puppy/Kitten</option>
          <option value="Young">Young</option>
          <option value="Adult">Adult</option>
          <option value="Senior">Senior</option>
        </select>
      </div>
    </div>

    <div class="form-group">
      <label for="isAvailable">Availability Status *</label>
      <select id="isAvailable" name="isAvailable" required>
        <option value="true">Available for Adoption</option>
        <option value="false">Not Available</option>
      </select>
    </div>

    <div class="btn-container">
      <a href="admin-pets" class="btn btn-secondary">Cancel</a>
      <button type="submit" class="btn btn-primary">Add Pet</button>
    </div>
  </form>
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