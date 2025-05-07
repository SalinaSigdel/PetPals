package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.service.AdoptionApplicationService;
import com.snapgramfx.petpals.service.AdoptionService;
import com.snapgramfx.petpals.service.PetService;
import com.snapgramfx.petpals.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Controller for handling admin dashboard data
 */
@WebServlet(name = "adminDashboardServlet", urlPatterns = {"/admin-dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    private final PetService petService;
    private final AdoptionApplicationService applicationService;
    private final AdoptionService adoptionService;
    private final UserService userService;

    public AdminDashboardServlet() {
        this.petService = new PetService();
        this.applicationService = new AdoptionApplicationService();
        this.adoptionService = new AdoptionService();
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

        if (!isAdmin) {
            response.sendRedirect("login.jsp?error=unauthorized");
            return;
        }

        // Get dashboard statistics
        Map<String, Integer> stats = getDashboardStats();
        request.setAttribute("totalPets", stats.get("totalPets"));
        request.setAttribute("availablePets", stats.get("availablePets"));
        request.setAttribute("pendingApplications", stats.get("pendingApplications"));
        request.setAttribute("totalAdoptions", stats.get("totalAdoptions"));
        request.setAttribute("totalUsers", stats.get("totalUsers"));
        request.setAttribute("adminUsers", stats.get("adminUsers"));

        // No need to fetch detailed data since we're only showing statistics

        // Forward to the JSP
        request.getRequestDispatcher("/admindashboard.jsp").forward(request, response);
    }

    /**
     * Get dashboard statistics
     * @return Map containing dashboard statistics
     */
    private Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();

        // Get total pets count
        int totalPets = petService.getTotalPetsCount();
        stats.put("totalPets", totalPets);

        // Get available pets count
        int availablePets = petService.getAvailablePetsCount();
        stats.put("availablePets", availablePets);

        // Get pending applications count
        int pendingApplications = applicationService.getPendingApplicationsCount();
        stats.put("pendingApplications", pendingApplications);

        // Get total adoptions count
        int totalAdoptions = adoptionService.getCompletedAdoptionsCount();
        stats.put("totalAdoptions", totalAdoptions);

        // Get total users count
        int totalUsers = userService.getAllUsers().size();
        stats.put("totalUsers", totalUsers);

        // Get admin users count
        int adminUsers = userService.getUsersByRole("admin").size();
        stats.put("adminUsers", adminUsers);

        return stats;
    }
}
