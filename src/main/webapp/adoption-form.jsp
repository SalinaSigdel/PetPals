<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="Quick Adoption Application" />
  <jsp:param name="activePage" value="adopt" />
  <jsp:param name="extraHead" value="/WEB-INF/includes/adoption-form-styles.jsp" />
</jsp:include>


    <div class="adoption-container">
        <h2 class="form-title">Quick Adoption Application</h2>
        <p class="form-subtitle">Please fill out this simple form to start the adoption process</p>

        <%
        // Check if user is logged in
        HttpSession userSession = request.getSession(false);
        boolean isLoggedIn = (userSession != null && userSession.getAttribute("userId") != null);

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

        // Show login message if user is not logged in
        if (!isLoggedIn) {
        %>
            <div class="error-message">
                <i class="fas fa-exclamation-circle"></i> You must be logged in to submit an adoption application.
                <a href="login.jsp?redirect=adoption-form.jsp?petId=<%= request.getParameter("petId") %>">Login here</a>
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
                    // Use DatabaseUtil to get connection
                    conn = com.snapgramfx.petpals.util.DatabaseUtil.getConnection();

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

        <form id="adoption-form" action="AdoptionServlet" method="post" <%= !isLoggedIn ? "onsubmit='return false;'" : "" %>>
            <input type="hidden" name="petId" value="<%= petId %>">
            <h3>Your Information</h3>
            <div class="form-row">
                <input type="text" name="fullName" placeholder="Full Name" required <%= !isLoggedIn ? "disabled" : "" %>>
                <input type="email" name="email" placeholder="Email" required <%= !isLoggedIn ? "disabled" : "" %>>
            </div>
            <div class="form-row">
                <input type="tel" name="phone" placeholder="Phone" required <%= !isLoggedIn ? "disabled" : "" %>>
                <input type="text" name="location" placeholder="City & State" required <%= !isLoggedIn ? "disabled" : "" %>>
            </div>
            <div class="form-group">
                <label>Tell us a bit about why you want to adopt this pet</label>
                <textarea name="reason" required <%= !isLoggedIn ? "disabled" : "" %>></textarea>
            </div>
            <button type="submit" class="submit-btn" <%= !isLoggedIn ? "disabled" : "" %>>💖 Submit Application</button>
        </form>

        <div class="notification-box">
            <p>You'll be notified about your application status through:</p>
            <p><i class="fas fa-envelope"></i> Email notification</p>
            <p><i class="fas fa-user"></i> Updates in your <a href="profile">user profile</a></p>
        </div>
    </div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />