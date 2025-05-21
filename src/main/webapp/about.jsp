<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.snapgramfx.petpals.service.StatisticsService" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="About Us" />
  <jsp:param name="activePage" value="about" />
</jsp:include>

<section class="about-hero">
  <div class="container">
    <h2>Our Mission</h2>
    <p>Creating loving homes for pets in need through ethical adoption practices</p>
  </div>
</section>

<section class="about-content">
  <div class="container">
    <div class="about-grid">
      <div class="about-card">
        <i class="fas fa-shield-alt"></i>
        <h3>Ethical Adoption</h3>
        <p>We ensure every adoption process is transparent, safe, and in the best interest of the animals.</p>
      </div>
      <div class="about-card">
        <i class="fas fa-handshake"></i>
        <h3>Verified Shelters</h3>
        <p>All our partner shelters are thoroughly vetted to maintain the highest standards of animal care.</p>
      </div>
      <div class="about-card">
        <i class="fas fa-heart"></i>
        <h3>Support System</h3>
        <p>We provide ongoing support to both adopters and shelters throughout the adoption journey.</p>
      </div>
    </div>

    <div class="about-story">
      <h3>Our Story</h3>
      <p>PetPals was founded with a simple mission: to make pet adoption accessible, transparent, and ethical. We believe that every pet deserves a loving home, and every adopter deserves a smooth, trustworthy adoption process.</p>
      <p>Since our inception, we've helped thousands of pets find their forever homes while maintaining the highest standards of animal welfare and adoption practices.</p>
    </div>

    <%
      // Get statistics from the service
      StatisticsService statisticsService = new StatisticsService();
      Object[] statistics = statisticsService.getAllStatistics();

      int adoptionCount = (int) statistics[0];
      double successRate = (double) statistics[1];
      int shelterCount = (int) statistics[2];
    %>

    <div class="about-stats">
      <div class="stat-card">
        <h4><%= adoptionCount %>+</h4>
        <p>Successful Adoptions</p>
      </div>
      <div class="stat-card">
        <h4><%= shelterCount %>+</h4>
        <p>Verified Shelters</p>
      </div>
      <div class="stat-card">
        <h4><%= String.format("%.0f", successRate) %>%</h4>
        <p>Adoption Success Rate</p>
      </div>
    </div>
  </div>
</section>

<jsp:include page="/WEB-INF/includes/footer.jsp" />