<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Adopt a Pet | PetPals</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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

<section class="adopt-hero">
    <div class="container">
        <h2>Find Your Perfect Companion</h2>
        <p>Browse our selection of loving pets waiting for their forever homes</p>
    </div>
</section>

<section class="adopt-pets">
    <div class="container">
        <div class="filter-section">
            <form action="adopt.jsp" method="get" id="filterForm">
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

                    // Build the query based on filters
                    StringBuilder queryBuilder = new StringBuilder();
                    queryBuilder.append("SELECT * FROM pets WHERE is_available = 1");

                    // Add search condition if provided
                    if(request.getParameter("search") != null && !request.getParameter("search").trim().isEmpty()) {
                        queryBuilder.append(" AND (name LIKE ? OR breed LIKE ?)");
                    }

                    // Add pet type filter if provided
                    if(request.getParameter("petType") != null && !request.getParameter("petType").trim().isEmpty()) {
                        queryBuilder.append(" AND pet_type = ?");
                    }

                    // Add age group filter if provided
                    if(request.getParameter("ageGroup") != null && !request.getParameter("ageGroup").trim().isEmpty()) {
                        String ageGroup = request.getParameter("ageGroup");
                        if(ageGroup.equals("puppy")) {
                            queryBuilder.append(" AND age < 1");
                        } else if(ageGroup.equals("young")) {
                            queryBuilder.append(" AND age >= 1 AND age < 3");
                        } else if(ageGroup.equals("adult")) {
                            queryBuilder.append(" AND age >= 3 AND age < 8");
                        } else if(ageGroup.equals("senior")) {
                            queryBuilder.append(" AND age >= 8");
                        }
                    }

                    // Add pagination limits
                    queryBuilder.append(" LIMIT 6");

                    // Prepare statement with filters
                    pstmt = conn.prepareStatement(queryBuilder.toString());

                    int paramIndex = 1;

                    // Set search parameter if provided
                    if(request.getParameter("search") != null && !request.getParameter("search").trim().isEmpty()) {
                        String searchTerm = "%" + request.getParameter("search") + "%";
                        pstmt.setString(paramIndex++, searchTerm);
                        pstmt.setString(paramIndex++, searchTerm);
                    }

                    // Set pet type parameter if provided
                    if(request.getParameter("petType") != null && !request.getParameter("petType").trim().isEmpty()) {
                        pstmt.setString(paramIndex++, request.getParameter("petType"));
                    }

                    // Execute query
                    rs = pstmt.executeQuery();

                    // Check if we have results
                    boolean hasResults = false;

                    // Display pets from database
                    while(rs.next()) {
                        hasResults = true;
                        int petId = rs.getInt("id");
                        String name = rs.getString("name");
                        String breed = rs.getString("breed");
                        String petType = rs.getString("pet_type");
                        double age = rs.getDouble("age");
                        String gender = rs.getString("gender");
                        double weight = rs.getDouble("weight");
                        String description = rs.getString("description");
                        String imageUrl = rs.getString("image_url");
                        String badge = rs.getString("badge");

                        // If no image URL is provided, use a default based on pet type
                        if(imageUrl == null || imageUrl.trim().isEmpty()) {
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
            <!-- Pet Card for <%= name %> -->
            <div class="pet-card" data-pet-id="<%= petId %>">
                <div class="pet-image">
                    <img src="<%= imageUrl %>" alt="<%= breed %>">
                    <% if(badge != null && !badge.isEmpty()) { %>
                    <span class="pet-badge"><%= badge %></span>
                    <% } %>
                </div>
                <div class="pet-info">
                    <h3><%= name %></h3>
                    <p class="pet-breed"><%= breed %></p>
                    <div class="pet-details">
                        <span><i class="fas fa-birthday-cake"></i> <%= age < 1 ? String.format("%.1f", age * 12) + " months" : String.format("%.1f", age) + " years" %></span>
                        <span><i class="fas fa-venus-mars"></i> <%= gender %></span>
                        <span><i class="fas fa-weight"></i> <%= weight %> kg</span>
                    </div>
                    <div class="pet-description">
                        <p><%= description %></p>
                    </div>
                    <div class="pet-actions">
                        <a href="adoptionfrom.jsp?petId=<%= petId %>" class="btn-adopt">Adopt Me</a>
                    </div>
                </div>
            </div>
            <%
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

            } catch(Exception e) {
                e.printStackTrace();
                // If there's an error, display sample pet data
            %>
            <!-- Dog 1 -->
            <div class="pet-card">
                <div class="pet-image">
                    <img src="https://images.unsplash.com/photo-1601979031925-424e53b6caaa?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" alt="Golden Retriever">
                    <span class="pet-badge">New</span>
                </div>
                <div class="pet-info">
                    <h3>Max</h3>
                    <p class="pet-breed">Golden Retriever</p>
                    <div class="pet-details">
                        <span><i class="fas fa-birthday-cake"></i> 2 years</span>
                        <span><i class="fas fa-venus-mars"></i> Male</span>
                        <span><i class="fas fa-weight"></i> 30 kg</span>
                    </div>
                    <div class="pet-description">
                        <p>Max is a friendly and energetic Golden Retriever who loves playing fetch and going for long walks. He's great with kids and other pets.</p>
                    </div>
                    <div class="pet-actions">
                        <a href="adoptionfrom.jsp?petId=1" class="btn-adopt">Adopt Me</a>
                    </div>
                </div>
            </div>

            <!-- Cat 1 -->
            <div class="pet-card">
                <div class="pet-image">
                    <img src="https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" alt="British Shorthair">
                    <span class="pet-badge">Popular</span>
                </div>
                <div class="pet-info">
                    <h3>Luna</h3>
                    <p class="pet-breed">British Shorthair</p>
                    <div class="pet-details">
                        <span><i class="fas fa-birthday-cake"></i> 1 year</span>
                        <span><i class="fas fa-venus-mars"></i> Female</span>
                        <span><i class="fas fa-weight"></i> 4 kg</span>
                    </div>
                    <div class="pet-description">
                        <p>Luna is a calm and affectionate British Shorthair who enjoys lounging around and being pampered. She's perfect for a quiet home.</p>
                    </div>
                    <div class="pet-actions">
                        <a href="adoptionfrom.jsp?petId=2" class="btn-adopt">Adopt Me</a>
                    </div>
                </div>
            </div>
            <%
                } finally {
                    // Close resources
                    try { if(rs != null) rs.close(); } catch(Exception e) { }
                    try { if(pstmt != null) pstmt.close(); } catch(Exception e) { }
                    try { if(conn != null) conn.close(); } catch(Exception e) { }
                }
            %>
        </div>

        <div class="pagination">
            <button class="page-btn"><i class="fas fa-chevron-left"></i></button>
            <button class="page-btn active">1</button>
            <button class="page-btn">2</button>
            <button class="page-btn">3</button>
            <button class="page-btn"><i class="fas fa-chevron-right"></i></button>
        </div>
    </div>
</section>

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

<script>
    // JavaScript for pet filtering and pagination if needed
    document.addEventListener('DOMContentLoaded', function() {
        // You can add client-side filtering and pagination here if needed
    });
</script>
</body>
</html>