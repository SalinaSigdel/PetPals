<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.snapgramfx.petpals.model.Pet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
    <jsp:param name="title" value="Adopt a Pet" />
    <jsp:param name="activePage" value="adopt" />
    <jsp:param name="extraHead" value="/WEB-INF/includes/adopt-styles.jsp" />
</jsp:include>

<section class="adopt-hero">
    <div class="container">
        <h2>Find Your Perfect Companion</h2>
        <p>Browse our selection of loving pets waiting for their forever homes</p>
    </div>
</section>

<section class="adopt-pets">
    <div class="container">
        <div class="filter-section">
            <form action="adopt" method="get" id="filterForm">
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
                // Get pets from request attribute
                List<Pet> pets = (List<Pet>) request.getAttribute("pets");
                boolean hasResults = (pets != null && !pets.isEmpty());

                if (hasResults) {
                    for (Pet pet : pets) {
                        String imageUrl = pet.getImageUrl();
                        if (imageUrl == null || imageUrl.trim().isEmpty()) {
                            String petType = pet.getType();
                            if("dog".equalsIgnoreCase(petType)) {
                                imageUrl = "https://images.unsplash.com/photo-1601979031925-424e53b6caaa?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                            } else if("cat".equalsIgnoreCase(petType)) {
                                imageUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                            } else if("bird".equalsIgnoreCase(petType)) {
                                imageUrl = "https://images.unsplash.com/photo-1552728089-57bdde30beb3?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                            } else if("rabbit".equalsIgnoreCase(petType)) {
                                imageUrl = "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                            } else {
                                imageUrl = "https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                            }
                        }
            %>
            <!-- Pet Card for <%= pet.getName() %> -->
            <div class="pet-card" data-pet-id="<%= pet.getPetId() %>">
                <div class="pet-image">
                    <img src="<%= imageUrl %>" alt="<%= pet.getBreed() %>" onerror="this.src='https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60'">
                    <% if(pet.getBadge() != null && !pet.getBadge().isEmpty()) { %>
                    <span class="pet-badge"><%= pet.getBadge() %></span>
                    <% } %>
                </div>
                <div class="pet-info">
                    <h3><%= pet.getName() %></h3>
                    <p class="pet-breed"><%= pet.getBreed() %></p>
                    <div class="pet-details">
                        <span><i class="fas fa-birthday-cake"></i> <%= pet.getFormattedAge() %></span>
                        <span><i class="fas fa-venus-mars"></i> <%= pet.getGender() %></span>
                        <span><i class="fas fa-weight"></i> <%= pet.getWeight() %> kg</span>
                    </div>
                    <div class="pet-description">
                        <p><%= pet.getDescription() %></p>
                    </div>
                    <div class="pet-actions">
                        <a href="adoption-form?petId=<%= pet.getPetId() %>" class="btn-adopt">Adopt Me</a>
                    </div>
                </div>
            </div>
            <%
                    }
                }

                // If no pets were found, display a message
                if(!hasResults) {
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

        <% if (request.getAttribute("totalPages") != null && (int)request.getAttribute("totalPages") > 0) { %>
        <div class="pagination">
            <%
                int currentPage = (int)request.getAttribute("currentPage");
                int totalPages = (int)request.getAttribute("totalPages");

                // Previous page button
                if (currentPage > 1) {
            %>
            <a href="adopt?page=<%= currentPage - 1 %><%= request.getParameter("search") != null ? "&search=" + request.getParameter("search") : "" %><%= request.getParameter("petType") != null ? "&petType=" + request.getParameter("petType") : "" %><%= request.getParameter("ageGroup") != null ? "&ageGroup=" + request.getParameter("ageGroup") : "" %>" class="page-btn"><i class="fas fa-chevron-left"></i></a>
            <% } else { %>
            <span class="page-btn disabled"><i class="fas fa-chevron-left"></i></span>
            <% } %>

            <%
                // Determine range of page numbers to display
                int startPage = Math.max(1, currentPage - 2);
                int endPage = Math.min(totalPages, currentPage + 2);

                // Ensure we always show at least 5 page numbers if available
                if (endPage - startPage + 1 < 5) {
                    if (startPage == 1) {
                        endPage = Math.min(5, totalPages);
                    } else if (endPage == totalPages) {
                        startPage = Math.max(1, totalPages - 4);
                    }
                }

                // First page button if not in range
                if (startPage > 1) {
            %>
            <a href="adopt?page=1<%= request.getParameter("search") != null ? "&search=" + request.getParameter("search") : "" %><%= request.getParameter("petType") != null ? "&petType=" + request.getParameter("petType") : "" %><%= request.getParameter("ageGroup") != null ? "&ageGroup=" + request.getParameter("ageGroup") : "" %>" class="page-btn">1</a>
            <% if (startPage > 2) { %>
            <span class="page-ellipsis">...</span>
            <% } %>
            <% } %>

            <%
                // Page number buttons
                for (int i = startPage; i <= endPage; i++) {
            %>
            <a href="adopt?page=<%= i %><%= request.getParameter("search") != null ? "&search=" + request.getParameter("search") : "" %><%= request.getParameter("petType") != null ? "&petType=" + request.getParameter("petType") : "" %><%= request.getParameter("ageGroup") != null ? "&ageGroup=" + request.getParameter("ageGroup") : "" %>" class="page-btn <%= i == currentPage ? "active" : "" %>"><%= i %></a>
            <% } %>

            <%
                // Last page button if not in range
                if (endPage < totalPages) {
                    if (endPage < totalPages - 1) {
            %>
            <span class="page-ellipsis">...</span>
            <% } %>
            <a href="adopt?page=<%= totalPages %><%= request.getParameter("search") != null ? "&search=" + request.getParameter("search") : "" %><%= request.getParameter("petType") != null ? "&petType=" + request.getParameter("petType") : "" %><%= request.getParameter("ageGroup") != null ? "&ageGroup=" + request.getParameter("ageGroup") : "" %>" class="page-btn"><%= totalPages %></a>
            <% } %>

            <%
                // Next page button
                if (currentPage < totalPages) {
            %>
            <a href="adopt?page=<%= currentPage + 1 %><%= request.getParameter("search") != null ? "&search=" + request.getParameter("search") : "" %><%= request.getParameter("petType") != null ? "&petType=" + request.getParameter("petType") : "" %><%= request.getParameter("ageGroup") != null ? "&ageGroup=" + request.getParameter("ageGroup") : "" %>" class="page-btn"><i class="fas fa-chevron-right"></i></a>
            <% } else { %>
            <span class="page-btn disabled"><i class="fas fa-chevron-right"></i></span>
            <% } %>
        </div>
        <% } %>
    </div>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Add event listener to search form
        const searchForm = document.getElementById('filterForm');
        const searchInput = searchForm.querySelector('input[name="search"]');

        searchForm.addEventListener('submit', function(e) {
            // Only submit if search has content or other filters are selected
            if (searchInput.value.trim() === '' &&
                (!searchForm.querySelector('select[name="petType"]').value &&
                    !searchForm.querySelector('select[name="ageGroup"]').value)) {
                e.preventDefault();
            }
        });
    });
</script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />