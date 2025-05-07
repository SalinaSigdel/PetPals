package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.AdoptionApplication;
import com.snapgramfx.petpals.service.AdoptionApplicationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling admin adoption applications
 */
@WebServlet(name = "adminApplicationsServlet", urlPatterns = {"/admin-applications"})
public class AdminApplicationsServlet extends HttpServlet {
    private final AdoptionApplicationService applicationService;

    public AdminApplicationsServlet() {
        this.applicationService = new AdoptionApplicationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

        if (!isAdmin) {
            response.sendRedirect("login?error=unauthorized");
            return;
        }

        // Statistics removed - only shown on dashboard

        // Get applications with pagination
        int page = 1;
        int pageSize = 10;
        String status = request.getParameter("status");

        // Default to pending if no status specified
        if (status == null || status.isEmpty()) {
            status = "pending";
        }

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            // Use default page value
        }

        List<AdoptionApplication> applications;
        int totalApplicationsCount;

        if ("all".equals(status)) {
            applications = applicationService.getAllApplications((page - 1) * pageSize, pageSize);
            totalApplicationsCount = applicationService.getTotalApplicationsCount();
        } else {
            applications = applicationService.getApplicationsByStatus(status, (page - 1) * pageSize, pageSize);
            totalApplicationsCount = applicationService.getApplicationsCountByStatus(status);
        }

        int totalPages = (int) Math.ceil((double) totalApplicationsCount / pageSize);

        request.setAttribute("applications", applications);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentStatus", status);

        // Forward to the JSP
        request.getRequestDispatcher("/admin-applications.jsp").forward(request, response);
    }
}
