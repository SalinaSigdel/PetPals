package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.Adoption;
import com.snapgramfx.petpals.service.AdoptionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling admin adoption history
 */
@WebServlet(name = "adminAdoptionsServlet", urlPatterns = {"/admin-adoptions"})
public class AdminAdoptionsServlet extends HttpServlet {
    private final AdoptionService adoptionService;

    public AdminAdoptionsServlet() {
        this.adoptionService = new AdoptionService();
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

        // Get adoptions with pagination
        int page = 1;
        int pageSize = 10;
        String status = request.getParameter("status");

        // Default to all if no status specified
        if (status == null || status.isEmpty()) {
            status = "all";
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

        List<Adoption> adoptions;
        int totalAdoptionsCount;

        if ("all".equals(status)) {
            adoptions = adoptionService.getAllAdoptions((page - 1) * pageSize, pageSize);
            totalAdoptionsCount = adoptionService.getTotalAdoptionsCount();
        } else {
            adoptions = adoptionService.getAdoptionsByStatus(status, (page - 1) * pageSize, pageSize);
            totalAdoptionsCount = adoptionService.getAdoptionsCountByStatus(status);
        }

        int totalPages = (int) Math.ceil((double) totalAdoptionsCount / pageSize);

        request.setAttribute("adoptions", adoptions);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentStatus", status);

        // Forward to the JSP
        request.getRequestDispatcher("/admin-adoptions.jsp").forward(request, response);
    }
}
