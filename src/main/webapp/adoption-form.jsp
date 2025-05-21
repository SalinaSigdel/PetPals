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

        // Get the pet from the request attribute
        com.snapgramfx.petpals.model.Pet pet = (com.snapgramfx.petpals.model.Pet) request.getAttribute("pet");
        int petId = 0;

        // Default pet details
        String petName = "Pet";
        String petBreed = "Unknown";
        String petAge = "0";
        String petGender = "Unknown";
        String petWeight = "0";
        String petDescription = "No description available.";
        String petImageUrl = "https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";

        // If pet object exists, use its details
        if (pet != null) {
            petId = pet.getPetId();
            petName = pet.getName();
            petBreed = pet.getBreed();
            petAge = pet.getAge();
            petGender = pet.getGender();
            petWeight = pet.getWeight();
            petDescription = pet.getDescription() != null ? pet.getDescription() : "No description available.";

            // Handle image URL based on pet type if no image URL is provided
            if (pet.getImageUrl() != null && !pet.getImageUrl().trim().isEmpty() && !pet.getImageUrl().equals("None")) {
                petImageUrl = pet.getImageUrl();
            } else {
                // Set default image based on pet type
                String petType = pet.getType();
                if ("Dog".equalsIgnoreCase(petType)) {
                    petImageUrl = "https://images.unsplash.com/photo-1601979031925-424e53b6caaa?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                } else if ("Cat".equalsIgnoreCase(petType)) {
                    petImageUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                } else if ("Bird".equalsIgnoreCase(petType)) {
                    petImageUrl = "https://images.unsplash.com/photo-1552728089-57bdde30beb3?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                } else if ("Rabbit".equalsIgnoreCase(petType)) {
                    petImageUrl = "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                }
            }
        } else {
            // Fallback to parameter if pet attribute is not set
            String petIdStr = request.getParameter("petId");
            if (petIdStr != null && !petIdStr.trim().isEmpty()) {
                try {
                    petId = Integer.parseInt(petIdStr);
                } catch (NumberFormatException e) {
                    // Invalid pet ID format
                }
            }
        }
    %>

    <div class="pet-preview">
        <img id="pet-image" src="<%= petImageUrl %>" alt="<%= petName %>">
        <div class="pet-info">
            <h3 id="pet-name"><%= petName %></h3>
            <p id="pet-breed"><%= petBreed %></p>
            <div class="pet-stats">
                <span><i class="fas fa-clock"></i> <span id="pet-age"><%= petAge %></span></span>
                <span><i class="fas fa-venus-mars"></i> <span id="pet-gender"><%= petGender %></span></span>
                <span><i class="fas fa-weight"></i> <span id="pet-weight"><%= petWeight %></span></span>
            </div>
            <p id="pet-description" class="pet-description"><%= petDescription %></p>
        </div>
    </div>

    <form id="adoption-form" action="adoption-form" method="post" <%= !isLoggedIn ? "onsubmit='return false;'" : "" %>>
        <input type="hidden" name="petId" value="<%= petId %>">
        <h3>Your Information</h3>
        <div class="form-row">
            <input type="text" name="applicantName" placeholder="Full Name" required <%= !isLoggedIn ? "disabled" : "" %>>
            <input type="email" name="email" placeholder="Email" required <%= !isLoggedIn ? "disabled" : "" %>>
        </div>
        <div class="form-row">
            <input type="tel" name="phone" placeholder="Phone" required <%= !isLoggedIn ? "disabled" : "" %>>
            <input type="text" name="cityState" placeholder="City & State" required <%= !isLoggedIn ? "disabled" : "" %>>
        </div>
        <div class="form-group">
            <label>Tell us a bit about why you want to adopt this pet</label>
            <textarea name="reasonForAdoption" required <%= !isLoggedIn ? "disabled" : "" %>></textarea>
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