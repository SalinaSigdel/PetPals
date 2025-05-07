<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.snapgramfx.petpals.model.Pet" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Pet Details | PetPals</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="styles.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <style>
    .pet-details-container {
      max-width: 1000px;
      margin: 2rem auto;
      padding: 1rem;
    }

    .pet-details-card {
      background: white;
      border-radius: 10px;
      box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
      overflow: hidden;
      margin-bottom: 2rem;
    }

    .pet-header {
      position: relative;
      height: 300px;
      overflow: hidden;
    }

    .pet-header img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .pet-status {
      position: absolute;
      top: 1rem;
      right: 1rem;
      padding: 0.5rem 1rem;
      border-radius: 20px;
      font-size: 0.9rem;
      font-weight: 500;
      background: rgba(255, 255, 255, 0.9);
    }

    .status-available {
      color: #28a745;
    }

    .status-adopted {
      color: #dc3545;
    }

    .status-pending {
      color: #ffc107;
    }

    .pet-content {
      padding: 2rem;
    }

    .pet-name {
      font-size: 2rem;
      margin: 0 0 0.5rem 0;
      color: #333;
    }

    .pet-stats {
      display: flex;
      flex-wrap: wrap;
      gap: 1.5rem;
      margin-bottom: 1.5rem;
    }

    .pet-stat {
      display: flex;
      align-items: center;
      color: #666;
    }

    .pet-stat i {
      margin-right: 0.5rem;
      color: var(--primary-color);
    }

    .pet-description {
      color: #333;
      line-height: 1.6;
      margin-bottom: 2rem;
    }

    .pet-actions {
      display: flex;
      gap: 1rem;
      margin-top: 1rem;
    }

    .btn-adopt {
      display: inline-block;
      padding: 0.8rem 1.5rem;
      background: var(--primary-color);
      color: white;
      border-radius: 5px;
      text-decoration: none;
      font-weight: 500;
      transition: background 0.3s;
    }

    .btn-adopt:hover {
      background: var(--primary-dark);
    }

    .btn-back {
      display: inline-block;
      padding: 0.6rem 1.2rem;
      background: #6c757d;
      color: white;
      border-radius: 5px;
      text-decoration: none;
      margin-bottom: 1.5rem;
      transition: background 0.3s;
    }

    .btn-back:hover {
      background: #5a6268;
    }

    .pet-details-section {
      margin-bottom: 2rem;
    }

    .section-title {
      font-size: 1.3rem;
      color: #333;
      margin-bottom: 1rem;
      padding-bottom: 0.5rem;
      border-bottom: 1px solid #eee;
    }

    .pet-details-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 1.5rem;
    }

    .detail-item {
      margin-bottom: 1rem;
    }

    .detail-label {
      font-size: 0.9rem;
      color: #666;
      margin-bottom: 0.3rem;
    }

    .detail-value {
      font-size: 1.1rem;
      color: #333;
    }

    @media (max-width: 768px) {
      .pet-details-grid {
        grid-template-columns: 1fr;
      }
    }
  </style>
</head>
<body>
  <%
  // Check if user is logged in
  Object userIdObj = session.getAttribute("userId");
  String userRole = (String) session.getAttribute("userRole");
  boolean isLoggedIn = (userIdObj != null);
  boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

  // Get pet from request attribute
  Pet pet = (Pet) request.getAttribute("pet");

  // Redirect if pet not found
  if (pet == null) {
    response.sendRedirect("pets");
    return;
  }
  %>

  <header>
    <div class="container">
      <h1><i class="fas fa-paw"></i> PetPals</h1>
      <nav>
        <a href="index.jsp"><i class="fas fa-home"></i> Home</a>
        <a href="adopt.jsp" class="active"><i class="fas fa-heart"></i> Adopt</a>
        <% if (!isLoggedIn) { %>
        <a href="login.jsp"><i class="fas fa-user"></i> Login</a>
        <a href="register.jsp"><i class="fas fa-user-plus"></i> Register</a>
        <% } else { %>
        <a href="profile"><i class="fas fa-user-circle"></i> Profile</a>
        <a href="my-applications"><i class="fas fa-file-alt"></i> My Applications</a>
        <a href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
        <% } %>
        <a href="about.jsp"><i class="fas fa-info-circle"></i> About</a>
        <% if (isAdmin) { %>
        <a href="admindashboard.jsp"><i class="fas fa-tachometer-alt"></i> Admin</a>
        <% } %>
      </nav>
    </div>
  </header>

  <div class="pet-details-container">
    <a href="pets" class="btn-back">
      <i class="fas fa-arrow-left"></i> Back to Pets
    </a>

    <div class="pet-details-card">
      <div class="pet-header">
        <img src="<%= pet.getImageUrl() != null ? pet.getImageUrl() : "" %>" alt="<%= pet.getName() %>" onerror="this.src='https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60'">
        <div class="pet-status status-<%= pet.getStatus().toLowerCase() %>">
          <%= pet.getStatus().substring(0, 1).toUpperCase() + pet.getStatus().substring(1) %>
        </div>
      </div>

      <div class="pet-content">
        <h2 class="pet-name"><%= pet.getName() %></h2>

        <div class="pet-stats">
          <div class="pet-stat">
            <i class="fas fa-paw"></i>
            <span><%= pet.getType() %></span>
          </div>
          <% if (pet.getBreed() != null && !pet.getBreed().isEmpty()) { %>
          <div class="pet-stat">
            <i class="fas fa-dog"></i>
            <span><%= pet.getBreed() %></span>
          </div>
          <% } %>
          <div class="pet-stat">
            <i class="fas fa-birthday-cake"></i>
            <span><%= pet.getAge() %></span>
          </div>
          <div class="pet-stat">
            <i class="fas fa-venus-mars"></i>
            <span><%= pet.getGender() %></span>
          </div>
          <% if (pet.getWeight() != null && !pet.getWeight().isEmpty()) { %>
          <div class="pet-stat">
            <i class="fas fa-weight"></i>
            <span><%= pet.getWeight() %></span>
          </div>
          <% } %>
        </div>

        <div class="pet-details-section">
          <h3 class="section-title">About <%= pet.getName() %></h3>
          <div class="pet-description">
            <%= pet.getDescription() %>
          </div>
        </div>

        <div class="pet-details-section">
          <h3 class="section-title">Details</h3>
          <div class="pet-details-grid">
            <div class="detail-item">
              <div class="detail-label">Type</div>
              <div class="detail-value"><%= pet.getType() %></div>
            </div>
            <% if (pet.getBreed() != null && !pet.getBreed().isEmpty()) { %>
            <div class="detail-item">
              <div class="detail-label">Breed</div>
              <div class="detail-value"><%= pet.getBreed() %></div>
            </div>
            <% } %>
            <div class="detail-item">
              <div class="detail-label">Age</div>
              <div class="detail-value"><%= pet.getAge() %></div>
            </div>
            <div class="detail-item">
              <div class="detail-label">Gender</div>
              <div class="detail-value"><%= pet.getGender() %></div>
            </div>
            <% if (pet.getWeight() != null && !pet.getWeight().isEmpty()) { %>
            <div class="detail-item">
              <div class="detail-label">Weight</div>
              <div class="detail-value"><%= pet.getWeight() %></div>
            </div>
            <% } %>
            <div class="detail-item">
              <div class="detail-label">Status</div>
              <div class="detail-value"><%= pet.getStatus().substring(0, 1).toUpperCase() + pet.getStatus().substring(1) %></div>
            </div>
          </div>
        </div>

        <% if ("available".equals(pet.getStatus())) { %>
        <form action="apply" method="post" class="pet-actions">
          <input type="hidden" name="petId" value="<%= pet.getPetId() %>">
          <% if (isLoggedIn) { %>
          <button type="submit" class="btn-adopt">
            <i class="fas fa-heart"></i> Apply to Adopt
          </button>
          <% } else { %>
          <a href="login.jsp" class="btn-adopt">
            <i class="fas fa-user"></i> Login to Adopt
          </a>
          <% } %>
        </form>
        <% } %>
      </div>
    </div>
  </div>

  <footer>
    <div class="container">
      <p>&copy; <%= new java.util.Date().getYear() + 1900 %> PetPals | Built with <i class="fas fa-heart"></i> for pets and people</p>
      <div class="social-links">
        <a href="#"><i class="fab fa-facebook"></i></a>
        <a href="#"><i class="fab fa-twitter"></i></a>
        <a href="#"><i class="fab fa-instagram"></i></a>
      </div>
    </div>
  </footer>
</body>
</html>