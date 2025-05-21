<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.snapgramfx.petpals.model.Pet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="All Pets" />
  <jsp:param name="activePage" value="adopt" />
  <jsp:param name="extraHead" value="/WEB-INF/includes/pet-list-styles.jsp" />
</jsp:include>


<div class="pet-list-header">
  <div class="container">
    <h2>Meet Our Wonderful Pets</h2>
    <p>Browse all our adorable pets available for adoption. Each one is waiting for a loving forever home.</p>
  </div>
</div>

<div class="container pet-list-container">
  <div class="filter-section">
    <form action="pets" method="get" id="filterForm">
      <div class="search-bar">
        <i class="fas fa-search"></i>
        <input type="text" name="search" placeholder="Search pets..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
      </div>
      <div class="filters">
        <select class="filter-select" name="petType" onchange="document.getElementById('filterForm').submit()">
          <option value="" <%= request.getParameter("petType") == null ? "selected" : "" %>>All Types</option>
          <option value="dog" <%= "dog".equals(request.getParameter("petType")) ? "selected" : "" %>>Dogs</option>
          <option value="cat" <%= "cat".equals(request.getParameter("petType")) ? "selected" : "" %>>Cats</option>
          <option value="bird" <%= "bird".equals(request.getParameter("petType")) ? "selected" : "" %>>Birds</option>
          <option value="rabbit" <%= "rabbit".equals(request.getParameter("petType")) ? "selected" : "" %>>Rabbits</option>
        </select>
        <select class="filter-select" name="ageGroup" onchange="document.getElementById('filterForm').submit()">
          <option value="" <%= request.getParameter("ageGroup") == null ? "selected" : "" %>>All Ages</option>
          <option value="puppy" <%= "puppy".equals(request.getParameter("ageGroup")) ? "selected" : "" %>>Puppy/Kitten</option>
          <option value="young" <%= "young".equals(request.getParameter("ageGroup")) ? "selected" : "" %>>Young</option>
          <option value="adult" <%= "adult".equals(request.getParameter("ageGroup")) ? "selected" : "" %>>Adult</option>
          <option value="senior" <%= "senior".equals(request.getParameter("ageGroup")) ? "selected" : "" %>>Senior</option>
        </select>
      </div>
    </form>
  </div>

  <div class="pet-grid">
    <%
      // Get pets from request attributes
      List<Pet> pets = (List<Pet>) request.getAttribute("pets");
      Integer currentPage = (Integer) request.getAttribute("currentPage");
      Integer totalPages = (Integer) request.getAttribute("totalPages");
      Integer totalRecords = (Integer) request.getAttribute("totalRecords");

      if (pets != null && !pets.isEmpty()) {
        for (Pet pet : pets) {
          String imageUrl = pet.getImageUrl();
          String petType = pet.getType();

          // If no image URL is provided, use a default based on pet type
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
    <!-- Pet Card for <%= pet.getName() %> -->
    <div class="pet-card" data-pet-id="<%= pet.getPetId() %>">
      <div class="pet-image">
        <img src="<%= imageUrl %>" alt="<%= pet.getBreed() %>">
        <% if (pet.getStatus() != null && !pet.getStatus().equals("available")) { %>
        <span class="pet-badge"><%= pet.getStatus() %></span>
        <% } %>
      </div>
      <div class="pet-info">
        <h3><%= pet.getName() %></h3>
        <p class="pet-breed"><%= pet.getBreed() %></p>
        <div class="pet-details">
          <%
            String ageStr = pet.getAge();
            double age = 0;
            try {
              age = Double.parseDouble(ageStr);
            } catch (Exception e) {
              // If age is not a number, just use the string
              ageStr = pet.getAge();
            }
          %>
          <span><i class="fas fa-birthday-cake"></i> <%= ageStr %></span>
          <span><i class="fas fa-venus-mars"></i> <%= pet.getGender() %></span>
          <span><i class="fas fa-weight"></i> <%= pet.getWeight() %></span>
        </div>
        <div class="pet-description">
          <p><%= pet.getDescription().length() > 150 ? pet.getDescription().substring(0, 150) + "..." : pet.getDescription() %></p>
        </div>
        <div class="pet-actions">
          <a href="pet-details?id=<%= pet.getPetId() %>" class="btn-adopt">View Details</a>
        </div>
      </div>
    </div>
    <%
      }
    } else {
    %>
    <div class="no-results">
      <i class="fas fa-search"></i>
      <h3>No pets found</h3>
      <p>Try changing your search criteria or check back later for new pets.</p>
    </div>
    <%
      }
    %>
  </div>

  <%
    // Use totalPages from request attribute
    if (totalPages == null) {
      totalPages = 1;
    }

    if (totalPages > 1) {
  %>
  <div class="pagination">
    <%
      // Get current page
      int currentPage = 1;
      String pageParam = request.getParameter("page");
      if (pageParam != null) {
        try {
          currentPage = Integer.parseInt(pageParam);
          if (currentPage < 1) currentPage = 1;
          if (currentPage > totalPages) currentPage = totalPages;
        } catch (NumberFormatException e) {
          // Invalid page parameter, use default
        }
      }

      // Build query string with existing parameters except page
      StringBuilder queryString = new StringBuilder();
      if (request.getParameter("search") != null && !request.getParameter("search").trim().isEmpty()) {
        queryString.append("search=").append(request.getParameter("search")).append("&");
      }
      if (request.getParameter("petType") != null && !request.getParameter("petType").trim().isEmpty()) {
        queryString.append("petType=").append(request.getParameter("petType")).append("&");
      }
      if (request.getParameter("ageGroup") != null && !request.getParameter("ageGroup").trim().isEmpty()) {
        queryString.append("ageGroup=").append(request.getParameter("ageGroup")).append("&");
      }

      // Previous page button
      if (currentPage > 1) {
    %>
    <a href="pets?<%= queryString.toString() %>page=<%= currentPage - 1 %>" class="page-btn"><i class="fas fa-chevron-left"></i></a>
    <%
    } else {
    %>
    <span class="page-btn" style="opacity: 0.5; cursor: not-allowed;"><i class="fas fa-chevron-left"></i></span>
    <%
      }

      // Page number buttons
      int startPage = Math.max(1, currentPage - 2);
      int endPage = Math.min(totalPages, startPage + 4);

      if (endPage - startPage < 4 && totalPages > 4) {
        startPage = Math.max(1, totalPages - 4);
      }

      for (int i = startPage; i <= endPage; i++) {
    %>
    <a href="pets?<%= queryString.toString() %>page=<%= i %>" class="page-btn <%= i == currentPage ? "active" : "" %>"><%= i %></a>
    <%
      }

      // Next page button
      if (currentPage < totalPages) {
    %>
    <a href="pets?<%= queryString.toString() %>page=<%= currentPage + 1 %>" class="page-btn"><i class="fas fa-chevron-right"></i></a>
    <%
    } else {
    %>
    <span class="page-btn" style="opacity: 0.5; cursor: not-allowed;"><i class="fas fa-chevron-right"></i></span>
    <%
      }
    %>
  </div>
  <%
    }
  %>
</div>

<jsp:include page="/WEB-INF/includes/footer.jsp" />