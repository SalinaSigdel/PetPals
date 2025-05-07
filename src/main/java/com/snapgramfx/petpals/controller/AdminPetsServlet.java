package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.Pet;
import com.snapgramfx.petpals.service.PetService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling admin pets management
 */
@WebServlet(name = "adminPetsServlet", urlPatterns = {"/admin-pets"})
public class AdminPetsServlet extends HttpServlet {
    private final PetService petService;

    public AdminPetsServlet() {
        this.petService = new PetService();
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

        // Get all pets with pagination
        int page = 1;
        int pageSize = 10;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            }
        } catch (NumberFormatException e) {
            // Use default page value
        }

        List<Pet> pets = petService.getAllPets((page - 1) * pageSize, pageSize);
        int totalPetsCount = petService.getTotalPetsCount();
        int totalPages = (int) Math.ceil((double) totalPetsCount / pageSize);

        request.setAttribute("pets", pets);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Forward to the JSP
        request.getRequestDispatcher("/admin-pets.jsp").forward(request, response);
    }
}
