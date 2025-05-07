<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${param.title} | PetPals Admin</title>
  <link rel="stylesheet" href="styles.css">
  <link rel="stylesheet" href="css/custom.css">
  <link rel="stylesheet" href="css/admin-styles.css">
  <link rel="stylesheet" href="css/admin-forms.css">
  <link rel="stylesheet" href="css/responsive.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <!-- Additional head content -->
  <c:if test="${not empty param.extraCSS}">
    <link rel="stylesheet" href="${param.extraCSS}">
  </c:if>
</head>
<body>
  <%
    // Check if user is logged in and is admin
    Object userIdObj = session.getAttribute("userId");
    Object userRoleObj = session.getAttribute("userRole");
    String userRole = (userRoleObj != null) ? userRoleObj.toString() : null;
    boolean isLoggedIn = (userIdObj != null);
    boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

    // Redirect if not admin
    if (!isAdmin) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get active page for navigation highlighting
    String activePage = request.getParameter("activePage");
    if (activePage == null) activePage = "";
    %>

<header class="admin-header-main">
  <div class="container">
    <div class="admin-brand">
      <h1><i class="fas fa-paw"></i> PetPals Admin</h1>
    </div>
    <nav class="admin-nav">
      <!-- Admin Navigation -->
      <a href="admin-dashboard" class="admin-nav-item <%= "admin".equals(activePage) ? "active" : "" %>">
        <i class="fas fa-tachometer-alt"></i>
        <span>Dashboard</span>
      </a>
      <a href="admin-users" class="admin-nav-item <%= "admin-users".equals(activePage) ? "active" : "" %>">
        <i class="fas fa-users-cog"></i>
        <span>User Management</span>
      </a>
      <a href="admin-pets" class="admin-nav-item <%= "admin-pets".equals(activePage) ? "active" : "" %>">
        <i class="fas fa-paw"></i>
        <span>Manage Pets</span>
      </a>
      <a href="admin-applications" class="admin-nav-item <%= "admin-applications".equals(activePage) ? "active" : "" %>">
        <i class="fas fa-clipboard-list"></i>
        <span>Applications</span>
      </a>
      <a href="admin-adoptions" class="admin-nav-item <%= "admin-adoptions".equals(activePage) ? "active" : "" %>">
        <i class="fas fa-history"></i>
        <span>Adoption History</span>
      </a>
      <a href="logout" class="admin-nav-item admin-logout">
        <i class="fas fa-sign-out-alt"></i>
        <span>Logout</span>
      </a>
    </nav>
  </div>
</header>

<div class="admin-content-wrapper">
