<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.snapgramfx.petpals.model.AdoptionApplication" %>
<%@ page import="com.snapgramfx.petpals.model.Pet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="Application Details" />
  <jsp:param name="activePage" value="my-applications" />
  <jsp:param name="extraHead" value="/WEB-INF/includes/application-details-styles.jsp" />
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

// Get application and pet from request attributes
  AdoptionApplication appData = (AdoptionApplication) request.getAttribute("application");
  Pet pet = (Pet) request.getAttribute("pet");

// Redirect if application not found
  if (appData == null || pet == null) {
    if (isAdmin) {
      response.sendRedirect("admin-dashboard");
    } else {
      response.sendRedirect("my-applications");
    }
    return;
  }

// Check if user is authorized to view this application
  if (!isAdmin && appData.getUserId() != ((Integer) userIdObj).intValue()) {
    response.sendRedirect("my-applications");
    return;
  }
%>

<div class="application-details-container">
  <a href="<%= isAdmin ? "admin-dashboard" : "my-applications" %>" class="btn-back">
    <i class="fas fa-arrow-left"></i> Back to <%= isAdmin ? "Dashboard" : "My Applications" %>
  </a>

  <h2 class="page-title">Application Details</h2>

  <div class="application-card">
    <div class="application-header">
      <div class="application-id">Application #<%= appData.getApplicationId() %></div>
      <div class="application-status status-<%= appData.getStatus().toLowerCase() %>">
        <%= appData.getStatus().substring(0, 1).toUpperCase() + appData.getStatus().substring(1) %>
      </div>
    </div>

    <div class="application-content">
      <div class="application-section">
        <h3 class="section-title">Pet Information</h3>
        <%
          // Handle image URL
          String petImageUrl = "https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
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
        %>
        <div class="pet-info-card">
          <img src="<%= petImageUrl %>" alt="<%= pet.getName() %>" class="pet-image">
          <div class="pet-details">
            <h3 class="pet-name"><%= pet.getName() %></h3>
            <div class="pet-stats">
              <div class="pet-stat">
                <i class="fas fa-paw"></i>
                <span><%= pet.getType() %></span>
              </div>
              <div class="pet-stat">
                <i class="fas fa-birthday-cake"></i>
                <span><%= pet.getAge() %></span>
              </div>
              <div class="pet-stat">
                <i class="fas fa-venus-mars"></i>
                <span><%= pet.getGender() %></span>
              </div>
              <div class="pet-stat">
                <i class="fas fa-weight"></i>
                <span><%= pet.getWeight() %></span>
              </div>
            </div>
            <div class="pet-description">
              <%= pet.getDescription() %>
            </div>
          </div>
        </div>
      </div>

      <div class="application-section">
        <h3 class="section-title">Applicant Information</h3>
        <div class="info-grid">
          <div class="info-item">
            <div class="info-label">Full Name</div>
            <div class="info-value"><%= appData.getApplicantName() %></div>
          </div>
          <div class="info-item">
            <div class="info-label">Email</div>
            <div class="info-value"><%= appData.getEmail() %></div>
          </div>
          <div class="info-item">
            <div class="info-label">Phone</div>
            <div class="info-value"><%= appData.getPhone() %></div>
          </div>
          <div class="info-item">
            <div class="info-label">City/State</div>
            <div class="info-value"><%= appData.getCityState() %></div>
          </div>
          <div class="info-item">
            <div class="info-label">Application Date</div>
            <div class="info-value"><%= new java.text.SimpleDateFormat("MMM d, yyyy").format(appData.getCreatedAt()) %></div>
          </div>
        </div>

        <div class="reason-box">
          <div class="info-label">Reason for Adoption</div>
          <div class="reason-text">
            <%= appData.getReasonForAdoption() %>
          </div>
        </div>

        <% if ("rejected".equals(appData.getStatus()) && appData.getRejectionReason() != null && !appData.getRejectionReason().isEmpty()) { %>
        <div class="rejection-reason">
          <h4><i class="fas fa-exclamation-circle"></i> Application Rejected</h4>
          <div class="rejection-text">
            <%= appData.getRejectionReason() %>
          </div>
        </div>
        <% } %>
      </div>

      <% if (isAdmin && "pending".equals(appData.getStatus())) { %>
      <div class="application-section">
        <h3 class="section-title">Admin Actions</h3>
        <div class="admin-actions">
          <form action="approve-application" method="post">
            <input type="hidden" name="id" value="<%= appData.getApplicationId() %>">
            <button type="submit" class="btn-approve">
              <i class="fas fa-check-circle"></i> Approve Application
            </button>
          </form>

          <button type="button" class="btn-reject" onclick="showRejectionForm()">
            <i class="fas fa-times-circle"></i> Reject Application
          </button>
        </div>

        <div id="rejectionForm" class="rejection-form">
          <form action="reject-application" method="post">
            <input type="hidden" name="id" value="<%= appData.getApplicationId() %>">
            <textarea name="rejectionReason" rows="4" placeholder="Please provide a reason for rejection..." required></textarea>
            <button type="submit" class="btn-reject">
              <i class="fas fa-times-circle"></i> Confirm Rejection
            </button>
          </form>
        </div>
      </div>
      <% } %>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />

<script>
  function showRejectionForm() {
    document.getElementById('rejectionForm').style.display = 'block';
  }
</script>