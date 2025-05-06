<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Adoption Form | PetPals</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="assets/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <style>
    .adoption-container {
      max-width: 600px;
      margin: 2rem auto;
      padding: 2rem;
      background: var(--white);
      border-radius: 10px;
      box-shadow: var(--shadow);
    }

    .form-title {
      color: #00a651;
      text-align: center;
      margin-bottom: 0.5rem;
    }

    .form-subtitle {
      text-align: center;
      color: #666;
      margin-bottom: 2rem;
    }

    .pet-preview {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 1rem;
      margin-bottom: 2rem;
      display: flex;
      align-items: center;
      gap: 1.5rem;
    }

    .pet-preview img {
      width: 120px;
      height: 120px;
      border-radius: 8px;
      object-fit: cover;
    }

    .pet-info h3 {
      color: #333;
      margin: 0 0 0.5rem 0;
    }

    .pet-info p {
      color: #666;
      margin: 0;
      font-size: 0.9rem;
      line-height: 1.4;
    }

    .pet-stats {
      display: flex;
      gap: 1rem;
      margin-top: 0.5rem;
      color: #666;
      font-size: 0.9rem;
    }

    .pet-description {
      margin-top: 1rem;
      color: #666;
      font-size: 0.9rem;
      line-height: 1.5;
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    .form-group label {
      display: block;
      color: #333;
      margin-bottom: 0.5rem;
    }

    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1rem;
      margin-bottom: 1rem;
    }

    input, textarea {
      width: 100%;
      padding: 0.8rem;
      border: 1px solid #ddd;
      border-radius: 6px;
      font-size: 1rem;
    }

    input:focus, textarea:focus {
      outline: none;
      border-color: #00a651;
    }

    textarea {
      min-height: 100px;
      resize: vertical;
    }

    .submit-btn {
      background: #00a651;
      color: white;
      width: 100%;
      padding: 1rem;
      border: none;
      border-radius: 6px;
      font-size: 1rem;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    .submit-btn:hover {
      background: #008c44;
    }

    .notification-box {
      margin-top: 1.5rem;
      padding: 1rem;
      background: #f8f9fa;
      border-left: 4px solid #00a651;
      border-radius: 4px;
    }

    .notification-box p {
      margin: 0;
      color: #666;
      font-size: 0.9rem;
    }

    .notification-box a {
      color: #00a651;
      text-decoration: none;
    }

    .notification-box a:hover {
      text-decoration: underline;
    }

    .success-message {
      background-color: rgba(0, 166, 81, 0.1);
      color: #00a651;
      padding: 15px;
      border-radius: 5px;
      margin-bottom: 20px;
      text-align: center;
    }

    .error-message {
      background-color: rgba(255, 0, 0, 0.1);
      color: #d9534f;
      padding: 15px;
      border-radius: 5px;
      margin-bottom: 20px;
      text-align: center;
    }

    @media (max-width: 600px) {
      .form-row {
        grid-template-columns: 1fr;
      }

      .pet-preview {
        flex-direction: column;
        text-align: center;
      }
    }
  </style>
</head>
<body>
<header>
  <div class="container">
    <h1><i class="fas fa-paw"></i> PetPals</h1>
    <nav>
      <a href="index.jsp"><i class="fas fa-home"></i> Home</a>
      <a href="adopt.jsp" class="active"><i class="fas fa-heart"></i> Adopt</a>
      <% if(session.getAttribute("username") == null) { %>
      <a href="login.jsp"><i class="fas fa-user"></i> Login</a>
      <a href="register.jsp"><i class="fas fa-user-plus"></i> Register</a>
      <% } else { %>
      <a href="LogoutServlet"><i class="fas fa-sign-out-alt"></i> Logout</a>
      <% } %>
      <a href="about.jsp"><i class="fas fa-info-circle"></i> About</a>
      <% if(session.getAttribute("username") != null) { %>
      <a href="userprofile.jsp"><i class="fas fa-user-circle"></i> Profile</a>
      <% } %>
      <% if(session.getAttribute("userRole") != null && session.getAttribute("userRole").equals("admin")) { %>
      <a href="admindashboard.jsp"><i class="fas fa-tachometer-alt"></i> Admin</a>
      <% } %>
    </nav>
  </div>
</header>

<div class="adoption-container">
  <h2 class="form-title">Quick Adoption Application</h2>
  <p class="form-subtitle">Please fill out this simple form to start the adoption process</p>

  <%
    // Display success or error message if available
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");

    if (successMessage != null) {
  %>
  <div class="success-message">
    <i class="fas fa-check-circle"></i> <%= successMessage %>
  </div>
  <% }

    if (errorMessage != null) {
  %>
  <div class="error-message">
    <i class="fas fa-exclamation-circle"></i> <%= errorMessage %>
  </div>
  <% }

    // Get the pet ID from the request
    String petIdStr = request.getParameter("petId");
    int petId = 0;

    // Pet details
    String petName = "Pet";
    String petBreed = "Unknown";
    double petAge = 0;
    String petGender = "Unknown";
    double petWeight = 0;
    String petDescription = "No description available.";
    String petImageUrl = "https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";

    // Check if petId is valid
    if (petIdStr != null && !petIdStr.trim().isEmpty()) {
      try {
        petId = Integer.parseInt(petIdStr);

        // Get pet details from database
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
          // Load JDBC driver
          Class.forName("com.mysql.jdbc.Driver");

          // Establish connection
          String url = "jdbc:mysql://localhost:3306/petpals_db";
          String username = "root";
          String password = "";
          conn = DriverManager.getConnection(url, username, password);

          // Prepare statement to get pet details
          pstmt = conn.prepareStatement("SELECT * FROM pets WHERE id = ?");
          pstmt.setInt(1, petId);

          // Execute query
          rs = pstmt.executeQuery();

          // Check if pet exists
          if (rs.next()) {
            petName = rs.getString("name");
            petBreed = rs.getString("breed");
            petAge = rs.getDouble("age");
            petGender = rs.getString("gender");
            petWeight = rs.getDouble("weight");
            petDescription = rs.getString("description");

            String imageUrl = rs.getString("image_url");
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
              petImageUrl = imageUrl;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          // Use default values if there's an error
        } finally {
          // Close resources
          try { if (rs != null) rs.close(); } catch (Exception e) { }
          try { if (pstmt != null) pstmt.close(); } catch (Exception e) { }
          try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
      } catch (NumberFormatException e) {
        // Invalid pet ID format
      }
    }
  %>

  <div class="pet-preview">
    <img id="pet-image" src="<%= petImageUrl %>" alt="<%= petName %>">
    <div class="pet-info">
      <h3 id="pet-name"><%= petName %></h3>
      <p id="pet-breed"><%= petBreed %></p>
      <div class="pet-stats">
        <span><i class="fas fa-clock"></i> <span id="pet-age"><%= petAge < 1 ? String.format("%.1f", petAge * 12) + " months" : String.format("%.1f", petAge) + " years" %></span></span>
        <span><i class="fas fa-venus-mars"></i> <span id="pet-gender"><%= petGender %></span></span>
        <span><i class="fas fa-weight"></i> <span id="pet-weight"><%= petWeight %> kg</span></span>
      </div>
      <p id="pet-description" class="pet-description"><%= petDescription %></p>
    </div>
  </div>

  <form id="adoption-form" action="AdoptionServlet" method="post">
    <input type="hidden" name="petId" value="<%= petId %>">
    <h3>Your Information</h3>
    <div class="form-row">
      <input type="text" name="fullName" placeholder="Full Name" required>
      <input type="email" name="email" placeholder="Email" required>
    </div>
    <div class="form-row">
      <input type="tel" name="phone" placeholder="Phone" required>
      <input type="text" name="location" placeholder="City & State" required>
    </div>
    <div class="form-group">
      <label>Tell us a bit about why you want to adopt this pet</label>
      <textarea name="reason" required></textarea>
    </div>
    <button type="submit" class="submit-btn">💖 Submit Application</button>
  </form>

  <div class="notification-box">
    <p>You'll be notified about your application status through:</p>
    <p><i class="fas fa-envelope"></i> Email notification</p>
    <p><i class="fas fa-user"></i> Updates in your <a href="userprofile.jsp">user profile</a></p>
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