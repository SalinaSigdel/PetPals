<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} | PetPals</title>
    <link rel="stylesheet" href="styles.css">
    <link rel="stylesheet" href="css/custom.css">
    <link rel="stylesheet" href="css/responsive.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <!-- Additional head content -->
    <c:if test="${not empty param.extraHead}">
        <jsp:include page="${param.extraHead}" />
    </c:if>
</head>
<body>
    <%
    // Check if user is logged in
    Object userIdObj = session.getAttribute("userId");
    Object userRoleObj = session.getAttribute("userRole");
    String userRole = (userRoleObj != null) ? userRoleObj.toString() : null;
    boolean isLoggedIn = (userIdObj != null);
    boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

    // Get active page for navigation highlighting
    String activePage = request.getParameter("activePage");
    if (activePage == null) activePage = "";
    %>

    <header>
        <div class="container">
            <h1><i class="fas fa-paw"></i> PetPals</h1>
            <nav>
                <% if (isAdmin) { %>
                <!-- Admin Navigation -->
                <a href="admin-dashboard" class="<%= "admin".equals(activePage) ? "active" : "" %>"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
                <a href="admin-users" class="<%= "admin-users".equals(activePage) ? "active" : "" %>"><i class="fas fa-users-cog"></i> User Management</a>
                <a href="admin-pets" class="<%= "admin-pets".equals(activePage) ? "active" : "" %>"><i class="fas fa-paw"></i> Manage Pets</a>
                <a href="admin-applications" class="<%= "admin-applications".equals(activePage) ? "active" : "" %>"><i class="fas fa-clipboard-list"></i> Applications</a>
                <a href="admin-adoptions" class="<%= "admin-adoptions".equals(activePage) ? "active" : "" %>"><i class="fas fa-history"></i> Adoption History</a>
                <a href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
                <% } else { %>
                <!-- Regular User Navigation -->
                <a href="index.jsp" class="<%= "home".equals(activePage) ? "active" : "" %>"><i class="fas fa-home"></i> Home</a>
                <a href="adopt" class="<%= "adopt".equals(activePage) ? "active" : "" %>"><i class="fas fa-heart"></i> Adopt</a>
                <% if (!isLoggedIn) { %>
                <a href="login.jsp" class="<%= "login".equals(activePage) ? "active" : "" %>"><i class="fas fa-user"></i> Login</a>
                <a href="register.jsp" class="<%= "register".equals(activePage) ? "active" : "" %>"><i class="fas fa-user-plus"></i> Register</a>
                <% } else { %>
                <a href="profile" class="<%= "profile".equals(activePage) ? "active" : "" %>"><i class="fas fa-user-circle"></i> Profile</a>
                <a href="logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
                <% } %>
                <a href="about.jsp" class="<%= "about".equals(activePage) ? "active" : "" %>"><i class="fas fa-info-circle"></i> About</a>
                <% } %>
            </nav>
        </div>
    </header>

    <div class="content-wrapper">
