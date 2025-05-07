<<<<<<< HEAD
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.snapgramfx.petpals.service.StatisticsService" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
    <jsp:param name="title" value="Ethical Pet Adoption" />
    <jsp:param name="activePage" value="home" />
</jsp:include>

<section class="hero">
    <div class="container">
        <div class="hero-content">
            <h2>Find Your New Best Friend</h2>
            <p>Adopt a pet from a verified shelter and give them a forever home. Every pet deserves love and care.</p>
            <div class="hero-buttons">
                <a href="adopt" class="btn btn-primary"><i class="fas fa-heart"></i> Start Adoption</a>
            </div>

            <%
                // Get statistics from the service
                StatisticsService statisticsService = new StatisticsService();
                Object[] statistics = statisticsService.getAllStatistics();

                int petsAdopted = (int) statistics[0];
                double successRate = (double) statistics[1];
                int shelterCount = (int) statistics[2];
            %>

            <div class="hero-stats">
                <div class="stat">
                    <span class="number"><%= petsAdopted %>+</span>
                    <span class="label">Pets Adopted</span>
                </div>
                <div class="stat">
                    <span class="number"><%= String.format("%.0f", successRate) %>%</span>
                    <span class="label">Success Rate</span>
                </div>
                <div class="stat">
                    <span class="number"><%= shelterCount %>+</span>
                    <span class="label">Shelters</span>
                </div>
            </div>
        </div>
        <div class="hero-image">
            <img src="https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80" alt="Happy Pet">
        </div>
    </div>
</section>

<section class="features">
    <div class="container">
        <h3>Why Choose PetPals?</h3>
        <div class="features-grid">
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-shield-alt"></i>
                </div>
                <h4>Verified Shelters</h4>
                <p>All our partner shelters are thoroughly vetted and verified for quality care.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-heart"></i>
                </div>
                <h4>Ethical Process</h4>
                <p>Transparent adoption process ensuring the best match for both pet and owner.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-file-alt"></i>
                </div>
                <h4>Simple Forms</h4>
                <p>Streamlined adoption process with easy-to-fill forms and quick approvals.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="fas fa-lock"></i>
                </div>
                <h4>Secure System</h4>
                <p>Your data is protected with our advanced security measures.</p>
            </div>
        </div>
    </div>
</section>

<section class="quick-links">
    <div class="container">
        <h3>Get Started</h3>
        <div class="link-grid">
            <a href="adopt" class="link-card">
                <div class="card-icon">
                    <i class="fas fa-heart"></i>
                </div>
                <h4>Adopt</h4>
                <p>Start your adoption journey</p>
                <span class="arrow"><i class="fas fa-arrow-right"></i></span>
            </a>
            <a href="about.jsp" class="link-card">
                <div class="card-icon">
                    <i class="fas fa-info-circle"></i>
                </div>
                <h4>About Us</h4>
                <p>Learn more about our mission</p>
                <span class="arrow"><i class="fas fa-arrow-right"></i></span>
            </a>
            <a href="register.jsp" class="link-card">
                <div class="card-icon">
                    <i class="fas fa-user-plus"></i>
                </div>
                <h4>Register</h4>
                <p>Create your account</p>
                <span class="arrow"><i class="fas fa-arrow-right"></i></span>
            </a>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/includes/footer.jsp" />
=======
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>
</body>
</html>
>>>>>>> 18bc66830136299081d1f005996ee176be0d0a61
