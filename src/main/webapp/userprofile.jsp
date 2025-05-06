<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Profile | PetPals</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .profile-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 1rem;
        }

        .profile-header {
            margin-bottom: 1.5rem;
        }

        .profile-header h1 {
            color: var(--primary-color);
            margin-bottom: 0.5rem;
            font-size: 2rem;
        }

        .profile-header p {
            color: var(--grey);
            margin: 0;
        }

        .profile-card {
            background: var(--white);
            border-radius: 10px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            box-shadow: var(--shadow);
        }

        .profile-card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .profile-card-header h2 {
            margin: 0;
            font-size: 1.5rem;
            color: var(--dark);
        }

        .btn {
            padding: 0.5rem 1rem;
            border-radius: 5px;
            font-size: 0.9rem;
            cursor: pointer;
            border: none;
            transition: all 0.3s;
        }

        .btn-outline {
            background: transparent;
            border: 1px solid #ddd;
            color: var(--dark);
        }

        .btn-outline:hover {
            background: #f5f5f5;
        }

        .btn-danger {
            background: #e74c3c;
            color: white;
        }

        .btn-danger:hover {
            background: #c0392b;
        }

        .profile-info-item {
            margin-bottom: 1.25rem;
        }

        .profile-info-item:last-child {
            margin-bottom: 0;
        }

        .profile-info-label {
            display: block;
            font-size: 0.9rem;
            color: var(--grey);
            margin-bottom: 0.25rem;
        }

        .profile-info-value {
            font-size: 1rem;
            color: var(--dark);
        }

        .member-since {
            font-size: 0.9rem;
            color: var(--grey);
            margin-bottom: 1.5rem;
        }

        .danger-zone {
            color: #e74c3c;
        }

        .danger-description {
            margin: 1rem 0;
            font-size: 0.9rem;
            color: var(--grey);
        }

        /* Notification Styles */
        .notification-badge {
            background-color: var(--primary-color);
            color: white;
            border-radius: 50%;
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 0.8rem;
            font-weight: bold;
        }

        .notifications-container {
            margin-bottom: 1.5rem;
        }

        .notification-item {
            display: flex;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
            position: relative;
            background-color: #f8f9fa;
            border-left: 4px solid #ddd;
        }

        .notification-success {
            border-left-color: #28a745;
            background-color: #f0fff4;
        }

        .notification-pending {
            border-left-color: #ffc107;
            background-color: #fffbeb;
        }

        .notification-rejected {
            border-left-color: #dc3545;
            background-color: #fff5f5;
        }

        .notification-icon {
            margin-right: 1rem;
            font-size: 1.5rem;
            color: #aaa;
        }

        .notification-success .notification-icon {
            color: #28a745;
        }

        .notification-pending .notification-icon {
            color: #ffc107;
        }

        .notification-rejected .notification-icon {
            color: #dc3545;
        }

        .notification-content {
            flex: 1;
        }

        .notification-content h4 {
            margin: 0 0 0.5rem 0;
            font-size: 1.1rem;
        }

        .notification-content p {
            margin: 0;
            color: #666;
            line-height: 1.4;
        }

        .notification-time {
            display: block;
            font-size: 0.8rem;
            color: #999;
            margin-top: 0.5rem;
        }

        .notification-dismiss {
            background: none;
            border: none;
            cursor: pointer;
            position: absolute;
            top: 0.5rem;
            right: 0.5rem;
            font-size: 0.9rem;
            color: #aaa;
        }

        .notification-dismiss:hover {
            color: #666;
        }

        .profile-tabs {
            display: flex;
            border-bottom: 1px solid #ddd;
            margin-bottom: 1.5rem;
        }

        .profile-tab {
            padding: 0.75rem 1.5rem;
            cursor: pointer;
            border-bottom: 3px solid transparent;
            font-weight: 500;
        }

        .profile-tab.active {
            border-bottom-color: var(--primary-color);
            color: var(--primary-color);
        }

        .profile-tab-content {
            display: none;
        }

        .profile-tab-content.active {
            display: block;
        }

        /* Adoption Application Styles */
        .application-status {
            display: inline-block;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.8rem;
            font-weight: 500;
            text-transform: uppercase;
        }

        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }

        .status-approved {
            background-color: #d4edda;
            color: #155724;
        }

        .status-rejected {
            background-color: #f8d7da;
            color: #721c24;
        }

        .adoption-list {
            list-style: none;
            padding: 0;
        }

        .adoption-item {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1rem;
            position: relative;
        }

        .adoption-item:last-child {
            margin-bottom: 0;
        }

        .adoption-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .adoption-pet-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .adoption-pet-image {
            width: 60px;
            height: 60px;
            border-radius: 5px;
            object-fit: cover;
        }

        .adoption-pet-name {
            font-weight: 600;
            color: var(--dark);
            margin: 0;
        }

        .adoption-pet-breed {
            font-size: 0.9rem;
            color: var(--grey);
            margin: 0;
        }

        .adoption-date {
            font-size: 0.8rem;
            color: var(--grey);
            margin-top: 0.5rem;
        }
    </style>
</head>
<body>
<%
    // Check if user is logged in
    String userId = (String) session.getAttribute("userId");
    boolean isLoggedIn = (userId != null && !userId.isEmpty());

    // User information
    String userName = "User";
    String userEmail = "";
    String userPhone = "";
    String userAddress = "";
    String userRole = "adopter";
    String memberSince = "January 1, 2023";

    // Notification count
    int notificationCount = 0;

    // If user is logged in, get their information from the database
    if (isLoggedIn) {
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

            // Get user information
            pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userName = rs.getString("name");
                userEmail = rs.getString("email");
                userPhone = rs.getString("phone") != null ? rs.getString("phone") : "";
                userAddress = rs.getString("address") != null ? rs.getString("address") : "";
                userRole = rs.getString("role");
                memberSince = new java.text.SimpleDateFormat("MMMM d, yyyy").format(rs.getDate("created_at"));
            }

            // Count unread notifications
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();

            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0");
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                notificationCount = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (Exception e) { }
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) { }
            try { if (conn != null) conn.close(); } catch (Exception e) { }
        }
    }
%>

<header>
    <div class="container">
        <h1><i class="fas fa-paw"></i> PetPals</h1>
        <nav>
            <a href="index.jsp"><i class="fas fa-home"></i> Home</a>
            <a href="adopt.jsp"><i class="fas fa-heart"></i> Adopt</a>
            <% if(session.getAttribute("username") == null) { %>
            <a href="login.jsp"><i class="fas fa-user"></i> Login</a>
            <a href="register.jsp"><i class="fas fa-user-plus"></i> Register</a>
            <% } else { %>
            <a href="LogoutServlet"><i class="fas fa-sign-out-alt"></i> Logout</a>
            <% } %>
            <a href="about.jsp"><i class="fas fa-info-circle"></i> About</a>
            <% if(session.getAttribute("username") != null) { %>
            <a href="userprofile.jsp" class="active"><i class="fas fa-user-circle"></i> Profile</a>
            <% } %>
            <% if(session.getAttribute("userRole") != null && session.getAttribute("userRole").equals("admin")) { %>
            <a href="admindashboard.jsp"><i class="fas fa-tachometer-alt"></i> Admin</a>
            <% } %>
        </nav>
    </div>
</header>

<div class="profile-container">
    <% if (!isLoggedIn) { %>
    <div class="profile-card">
        <div class="profile-card-header">
            <h2>Please Log In</h2>
        </div>
        <p>You need to be logged in to view your profile. Please <a href="login.jsp">log in</a> or <a href="register.jsp">create an account</a>.</p>
    </div>
    <% } else { %>
    <div class="profile-header">
        <h1>Welcome back, <%= userName %></h1>
        <p class="member-since">Member since <%= memberSince %></p>
    </div>

    <div class="profile-tabs">
        <div class="profile-tab active" data-tab="profile">Profile Information</div>
        <div class="profile-tab" data-tab="notifications">
            Notifications
            <% if (notificationCount > 0) { %>
            <span class="notification-badge"><%= notificationCount %></span>
            <% } %>
        </div>
        <div class="profile-tab" data-tab="adoptions">My Adoptions</div>
    </div>

    <div class="profile-tab-content active" id="profile-tab">
        <div class="profile-card">
            <div class="profile-card-header">
                <h2>Personal Information</h2>
                <button class="btn btn-outline" id="editProfileBtn">Edit Profile</button>
            </div>

            <div class="profile-info">
                <div class="profile-info-item">
                    <span class="profile-info-label">Full Name</span>
                    <span class="profile-info-value"><%= userName %></span>
                </div>
                <div class="profile-info-item">
                    <span class="profile-info-label">Email Address</span>
                    <span class="profile-info-value"><%= userEmail %></span>
                </div>
                <div class="profile-info-item">
                    <span class="profile-info-label">Phone Number</span>
                    <span class="profile-info-value"><%= userPhone.isEmpty() ? "Not provided" : userPhone %></span>
                </div>
                <div class="profile-info-item">
                    <span class="profile-info-label">Address</span>
                    <span class="profile-info-value"><%= userAddress.isEmpty() ? "Not provided" : userAddress %></span>
                </div>
            </div>
        </div>

        <div class="profile-card">
            <div class="profile-card-header">
                <h2 class="danger-zone">Danger Zone</h2>
            </div>
            <p class="danger-description">Once you delete your account, there is no going back. Please be certain.</p>
            <button class="btn btn-danger" id="deleteAccountBtn">Delete My Account</button>
        </div>
    </div>

    <div class="profile-tab-content" id="notifications-tab">
        <div class="profile-card">
            <div class="profile-card-header">
                <h2>Your Notifications</h2>
                <button class="btn btn-outline" id="markAllReadBtn">Mark All as Read</button>
            </div>

            <div class="notifications-container">
                <%
                    // Get user notifications from database
                    if (isLoggedIn) {
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

                            // Get notifications
                            pstmt = conn.prepareStatement(
                                    "SELECT n.*, p.name as pet_name, p.breed as pet_breed " +
                                            "FROM notifications n " +
                                            "LEFT JOIN adoptions a ON n.adoption_id = a.id " +
                                            "LEFT JOIN pets p ON a.pet_id = p.id " +
                                            "WHERE n.user_id = ? " +
                                            "ORDER BY n.created_at DESC " +
                                            "LIMIT 10"
                            );
                            pstmt.setString(1, userId);
                            rs = pstmt.executeQuery();

                            boolean hasNotifications = false;

                            while (rs.next()) {
                                hasNotifications = true;
                                String notificationType = rs.getString("type");
                                String notificationMessage = rs.getString("message");
                                boolean isRead = rs.getBoolean("is_read");
                                String createdAt = new java.text.SimpleDateFormat("MMM d, yyyy 'at' h:mm a").format(rs.getTimestamp("created_at"));
                                String petName = rs.getString("pet_name");

                                // Determine notification style based on type
                                String notificationClass = "notification-item";
                                String iconClass = "fas fa-bell";

                                if ("adoption_approved".equals(notificationType)) {
                                    notificationClass += " notification-success";
                                    iconClass = "fas fa-check-circle";
                                } else if ("adoption_rejected".equals(notificationType)) {
                                    notificationClass += " notification-rejected";
                                    iconClass = "fas fa-times-circle";
                                } else if ("adoption_pending".equals(notificationType)) {
                                    notificationClass += " notification-pending";
                                    iconClass = "fas fa-clock";
                                }
                %>
                <div class="<%= notificationClass %>" data-notification-id="<%= rs.getString("id") %>">
                    <div class="notification-icon">
                        <i class="<%= iconClass %>"></i>
                    </div>
                    <div class="notification-content">
                        <h4><%= petName != null ? "Update about " + petName : "Notification" %></h4>
                        <p><%= notificationMessage %></p>
                        <span class="notification-time"><%= createdAt %></span>
                    </div>
                    <button class="notification-dismiss" data-notification-id="<%= rs.getString("id") %>">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
                <%
                    }

                    if (!hasNotifications) {
                %>
                <p>You don't have any notifications yet.</p>
                <%
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // Close resources
                            try { if (rs != null) rs.close(); } catch (Exception e) { }
                            try { if (pstmt != null) pstmt.close(); } catch (Exception e) { }
                            try { if (conn != null) conn.close(); } catch (Exception e) { }
                        }
                    }
                %>
            </div>
        </div>
    </div>

    <div class="profile-tab-content" id="adoptions-tab">
        <div class="profile-card">
            <div class="profile-card-header">
                <h2>Your Adoption Applications</h2>
            </div>

            <div class="adoption-container">
                <%
                    // Get user's adoption applications from database
                    if (isLoggedIn) {
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

                            // Get adoption applications
                            pstmt = conn.prepareStatement(
                                    "SELECT a.*, p.name as pet_name, p.breed as pet_breed, p.image_url as pet_image " +
                                            "FROM adoptions a " +
                                            "JOIN pets p ON a.pet_id = p.id " +
                                            "WHERE a.user_id = ? " +
                                            "ORDER BY a.application_date DESC"
                            );
                            pstmt.setString(1, userId);
                            rs = pstmt.executeQuery();

                            boolean hasAdoptions = false;
                %>
                <ul class="adoption-list">
                    <%
                        while (rs.next()) {
                            hasAdoptions = true;
                            String status = rs.getString("status");
                            String petName = rs.getString("pet_name");
                            String petBreed = rs.getString("pet_breed");
                            String petImage = rs.getString("pet_image");
                            String applicationDate = new java.text.SimpleDateFormat("MMM d, yyyy").format(rs.getDate("application_date"));

                            // Default image if none provided
                            if (petImage == null || petImage.trim().isEmpty()) {
                                petImage = "https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";
                            }

                            // Status CSS class
                            String statusClass = "";
                            if ("pending".equalsIgnoreCase(status)) {
                                statusClass = "status-pending";
                            } else if ("approved".equalsIgnoreCase(status)) {
                                statusClass = "status-approved";
                            } else if ("rejected".equalsIgnoreCase(status)) {
                                statusClass = "status-rejected";
                            }
                    %>
                    <li class="adoption-item">
                        <div class="adoption-header">
                            <div class="adoption-pet-info">
                                <img src="<%= petImage %>" alt="<%= petName %>" class="adoption-pet-image">
                                <div>
                                    <h4 class="adoption-pet-name"><%= petName %></h4>
                                    <p class="adoption-pet-breed"><%= petBreed %></p>
                                </div>
                            </div>
                            <span class="application-status <%= statusClass %>"><%= status %></span>
                        </div>
                        <div class="adoption-details">
                            <p class="adoption-date">Applied on <%= applicationDate %></p>
                            <% if ("pending".equalsIgnoreCase(status)) { %>
                            <p>Your application is being reviewed. We'll notify you once there's an update.</p>
                            <% } else if ("approved".equalsIgnoreCase(status)) { %>
                            <p>Congratulations! Your application has been approved. Please check your email for next steps.</p>
                            <% } else if ("rejected".equalsIgnoreCase(status)) { %>
                            <p>We're sorry, but your application wasn't approved. Please contact us for more information.</p>
                            <% } %>
                        </div>
                    </li>
                    <%
                        }

                        if (!hasAdoptions) {
                    %>
                    <p>You haven't submitted any adoption applications yet. <a href="adopt.jsp">Find a pet</a> to start the adoption process.</p>
                    <%
                        }
                    %>
                </ul>
                <%
                } catch (Exception e) {
                    e.printStackTrace();
                %>
                <p>There was an error loading your adoption applications. Please try again later.</p>
                <%
                        } finally {
                            // Close resources
                            try { if (rs != null) rs.close(); } catch (Exception e) { }
                            try { if (pstmt != null) pstmt.close(); } catch (Exception e) { }
                            try { if (conn != null) conn.close(); } catch (Exception e) { }
                        }
                    }
                %>
            </div>
        </div>
    </div>
    <% } %>
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

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Tab functionality
        const tabs = document.querySelectorAll('.profile-tab');
        const tabContents = document.querySelectorAll('.profile-tab-content');

        tabs.forEach(tab => {
            tab.addEventListener('click', function() {
                // Remove active class from all tabs and contents
                tabs.forEach(t => t.classList.remove('active'));
                tabContents.forEach(c => c.classList.remove('active'));

                // Add active class to clicked tab and corresponding content
                this.classList.add('active');
                const tabName = this.getAttribute('data-tab');
                document.getElementById(tabName + '-tab').classList.add('active');
            });
        });

        // Edit profile button
        const editProfileBtn = document.getElementById('editProfileBtn');
        if (editProfileBtn) {
            editProfileBtn.addEventListener('click', function() {
                // Redirect to profile edit page
                window.location.href = 'edit-profile.jsp';
            });
        }

        // Delete account button
        const deleteAccountBtn = document.getElementById('deleteAccountBtn');
        if (deleteAccountBtn) {
            deleteAccountBtn.addEventListener('click', function() {
                if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
                    // Submit form to delete account
                    const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = 'DeleteAccountServlet';
                    document.body.appendChild(form);
                    form.submit();
                }
            });
        }

        // Mark all notifications as read
        const markAllReadBtn = document.getElementById('markAllReadBtn');
        if (markAllReadBtn) {
            markAllReadBtn.addEventListener('click', function() {
                // Submit form to mark all notifications as read
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'MarkNotificationsReadServlet';
                document.body.appendChild(form);
                form.submit();
            });
        }

        // Dismiss individual notifications
        const dismissBtns = document.querySelectorAll('.notification-dismiss');
        dismissBtns.forEach(btn => {
            btn.addEventListener('click', function() {
                const notificationId = this.getAttribute('data-notification-id');
                const notificationElement = document.querySelector('.notification-item[data-notification-id="' + notificationId + '"]');

                // Remove the notification visually
                if (notificationElement) {
                    notificationElement.style.opacity = '0';
                    setTimeout(() => {
                        notificationElement.style.display = 'none';
                    }, 300);
                }

                // Send request to mark notification as read
                const xhr = new XMLHttpRequest();
                xhr.open('POST', 'DismissNotificationServlet');
                xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xhr.send('notificationId=' + notificationId);
            });
        });
    });
</script>
</body>
</html>